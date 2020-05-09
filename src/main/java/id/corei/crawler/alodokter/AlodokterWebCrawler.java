package id.corei.crawler.alodokter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import id.corei.crawler.alodokter.model.DrugItem;
import id.corei.crawler.model.Drug;
import id.corei.crawler.service.DrugService;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

public class AlodokterWebCrawler {
    private final AlodokterConfig config;
    private ArrayList<DrugItem> drugItems;
    Logger logger = LoggerFactory.getLogger(getClass());
    private DrugService drugService;

    public AlodokterWebCrawler(AlodokterConfig alodokterConfig, DrugService drugService) {
        this.drugItems = new ArrayList<>();
        this.config = alodokterConfig;
        this.drugService = drugService;
    }

    /**
     * Get all drug links from initial URL
     */
    public void getAllLinks() {
        try {
            //get html page
            CloseableHttpClient httpClient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(30000)
                    .setConnectTimeout(10000)
                    .build();

            HttpGet httpGet = new HttpGet(this.config.getCatalogUrl());
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //proceed alodokter page obat-a-z
                Document document = Jsoup.parse(EntityUtils.toString(entity));
                Element element = document.selectFirst("search-a-z-2");

                //logger.info("elements : {}", element.attr("search-results"));
                Type listType = new TypeToken<ArrayList<DrugItem>>(){}.getType();
                drugItems = new Gson().fromJson(element.attr("search-results"), listType);
                logger.info("Got all links, {} page found", drugItems.size());
            }

        } catch (IOException e) {
            System.err.println("For '" + this.config.getCatalogUrl() + "': " + e.getMessage());
        }
    }

    /**
     * Read and parse all drug links, save to database
     */
    public void proceedAllLinks() {
        for (DrugItem item : drugItems) {
            String url = config.getBaseUrl()+item.getPermalink();
            proceedSingleLink(item.getPermalink(), url);
            insertDb(item);
        }
    }

    /**
     * Proceed single link
     * @param drugName
     * @param permalink
     * @param url
     * @return
     */
    private void proceedSingleLink(String permalink, String url) {
        try {
            logger.info("Proceed link ===> {}", url);
            String filename = "data/"+permalink+".html";
            File file = new File(filename);
            if (file.exists()) {
                logger.info("Already processed, skip...");
                return;
            }
            CloseableHttpClient httpClient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(30000)
                    .setConnectTimeout(10000)
                    .build();

            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                FileWriter writer = new FileWriter(filename);
                String html = EntityUtils.toString(entity);
                Document doc = Jsoup.parse(html);
                Element el = doc.selectFirst("div.post-content");
                if (el != null) {
                    writer.write(el.html());
                }
                writer.close();
            }
            //give interval between loop
            Thread.sleep(1000);
            logger.info("sleep for 1 seconds");

        } catch (IOException | InterruptedException e) {
            System.err.println("For '" + url + "': " + e.getMessage());
        }
    }

    /**
     * Save to database
     * @param item
     */
    private void insertDb(DrugItem item) {
        logger.info("drug == {}", new Gson().toJson(item));
        try {
            String desc = new String(Files.readAllBytes(Paths.get("data/"+item.getPermalink()+".html")));
            Date now = new Date();
            Drug drug = new Drug();
            drug.setName(item.getPostTitle());
            drug.setDescription(desc);
            drug.setLastRetrieved(now);
            drug.setCreatedAt(now);
            drug.setCreatedBy("init_process");
            drug.setPermalink(this.config.getBaseUrl()+item.getPermalink());
            drug.setSource("Alodokter");
            //check if the data already saved before
            if (drugService.getByPermalink(drug.getPermalink()) == null) {
                drugService.save(drug);
            } else {
                logger.info("The drug already exists in database, skip..");
            }
        }  catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
