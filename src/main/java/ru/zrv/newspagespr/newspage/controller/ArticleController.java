package ru.zrv.newspagespr.newspage.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.zrv.newspagespr.newspage.domian.Article;
import ru.zrv.newspagespr.newspage.exception.AppException;
import ru.zrv.newspagespr.newspage.exception.ErrorType;
import ru.zrv.newspagespr.newspage.service.ArticleService;

import java.sql.SQLException;

@RestController
public class ArticleController {

    private final ArticleService articleService;
    private final ObjectMapper objectMapper;

    public ArticleController(ArticleService articleService, ObjectMapper objectMapper) {
        this.articleService = articleService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/api/v1/article")
    @ResponseStatus(HttpStatus.OK)
    public String getArticleByID(@RequestParam String id) throws JsonProcessingException, SQLException {
            Article article = articleService.getArticle(id);
            if (article == null) throw AppException.of(ErrorType.ARTICLE_NOT_FOUND_BY_ID, id);
            return objectMapper.writeValueAsString(article);
    }
}
