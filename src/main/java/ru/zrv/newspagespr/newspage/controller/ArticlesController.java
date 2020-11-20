package ru.zrv.newspagespr.newspage.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.zrv.newspagespr.newspage.domian.Article;
import ru.zrv.newspagespr.newspage.exception.AppException;
import ru.zrv.newspagespr.newspage.exception.ErrorType;
import ru.zrv.newspagespr.newspage.service.ArticlesService;

import java.sql.SQLException;
import java.util.List;

@RestController
public class ArticlesController {

    private final ArticlesService articlesService;

    public ArticlesController(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }

    @GetMapping("/api/v1/articles")
    public List<Article> getArticles(
            @RequestParam Integer limit,
            @RequestParam Integer offset,
            @RequestParam String tags,
            @RequestParam String fromDate,
            @RequestParam String toDate,
            @RequestParam String search) throws SQLException {

        List<Article> articlesList = articlesService
                .getArticlesList(limit, offset, tags, fromDate,toDate, search);

        if (articlesList == null) throw AppException.of(ErrorType.ARTICLES_NOT_FOUND);

        return articlesList;
    }
}
