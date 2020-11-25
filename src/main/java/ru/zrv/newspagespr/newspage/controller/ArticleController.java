package ru.zrv.newspagespr.newspage.controller;

import org.springframework.web.bind.annotation.*;
import ru.zrv.newspagespr.newspage.domian.Article;
import ru.zrv.newspagespr.newspage.exception.AppException;
import ru.zrv.newspagespr.newspage.exception.ErrorType;
import ru.zrv.newspagespr.newspage.service.ArticleService;

import java.sql.SQLException;

@RestController
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/api/v1/article")
    public Article getArticleByID(@RequestParam String id) throws SQLException {
            Article article = articleService.getArticle(id);
            if (article == null) throw AppException.of(ErrorType.ARTICLE_NOT_FOUND_BY_ID, id);
            return article;
    }
}
