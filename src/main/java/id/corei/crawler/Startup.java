package id.corei.crawler;

import id.corei.crawler.alodokter.AlodokterConfig;
import id.corei.crawler.alodokter.AlodokterWebCrawler;
import id.corei.crawler.service.DrugService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Startup {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    AlodokterConfig alodokterConfig;

    @Autowired
    DrugService drugService;

    @PostConstruct
    public void initCrawler() {
        logger.info("=========== Init crawler Alodokter ===============");
        AlodokterWebCrawler crawler1 = new AlodokterWebCrawler(alodokterConfig, drugService);
        crawler1.getAllLinks();
        crawler1.proceedAllLinks();
        logger.info("=========== Finished crawler Alodokter ===============");
    }
}
