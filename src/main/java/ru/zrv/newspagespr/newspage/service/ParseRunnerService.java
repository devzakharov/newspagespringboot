package ru.zrv.newspagespr.newspage.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class ParseRunnerService {

    private final JsonParserService jsonParserService;
    private final HtmlParserService htmlParserService;

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driver-class-name}")
    private String driver;


    public ParseRunnerService(JsonParserService jsonParserService, HtmlParserService htmlParserService) {
        this.jsonParserService = jsonParserService;
        this.htmlParserService = htmlParserService;
    }

    @SneakyThrows
    @Scheduled(fixedRate = 1800000) // 30 mins
    public void Run() {
        jsonParserService.fillDataObject();
        htmlParserService.fillDataObject(jsonParserService.getDataObject());
        htmlParserService.writeArticlesToDb();
    }
}
