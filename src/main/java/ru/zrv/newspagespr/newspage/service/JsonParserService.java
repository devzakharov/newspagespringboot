package ru.zrv.newspagespr.newspage.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.zrv.newspagespr.newspage.domian.ArticlePart;
import ru.zrv.newspagespr.newspage.domian.ArticlePartHashSet;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JsonParserService {

    @Value("${application.json-parser.limit-per-request}")
    private int limitPerRequest;
    @Value("${application.json-parser.offset}")
    private int offset;
    @Value("${application.json-parser.articles-count-limit}")
    private int articlesCountLimit;

    Set<ArticlePart> partArticleSet = new HashSet<>();

    public void fillDataObject() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.registerModule(new JSR310Module());

        while (partArticleSet.size() < articlesCountLimit) {
            ArticlePartHashSet addedPreviewArticles = mapper.readValue(getRequestLink(), ArticlePartHashSet.class);
            partArticleSet.addAll(addedPreviewArticles.getArticlePartHashSet());
            offset += limitPerRequest;
        }
    }

    public Map<String, ArticlePart> getDataObject() {

        return partArticleSet.stream().collect(Collectors.toMap(ArticlePart::getId, e -> e));
    }

    private URL getRequestLink() throws MalformedURLException {

        return new URL(String.format("https://www.rbc.ru/v10/search/ajax/?offset=%d&limit=%d", offset, limitPerRequest));
    }
}
