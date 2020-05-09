package id.corei.crawler.alodokter;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AlodokterConfig {

    private final String baseUrl = "https://www.alodokter.com/";

    @Value("${crawler.alodokter.url}")
    private String catalogUrl;
}
