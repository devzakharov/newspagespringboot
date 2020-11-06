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

    //Брат, [03.11.20 17:00]
    //Я делаю абстрактную ошибку которая наследуется от рантайм эксепшин
    //
    //Брат, [03.11.20 17:01]
    //И пишу @ControllerAdvice отдельный
    //
    //Брат, [03.11.20 17:01]
    //Который ловит все эксепшины, либо ловит абстрактный класс ошибки
    //
    //Брат, [03.11.20 17:01]
    //В нем лежит статус и сообщение
    //
    //Брат, [03.11.20 17:01]
    //И засовываю в респонс
    //
    //Брат, [03.11.20 17:02]
    //В наследнике тебе как раз можно написать сообщение и указать хттпстатус
    //
    //Брат, [03.11.20 17:02]
    //И всё автоматически будет работаьь
    @GetMapping("/api/v1/article")
    @ResponseStatus(HttpStatus.OK)
    public String getArticleByID(@RequestParam String id) throws JsonProcessingException, SQLException {
            Article article = articleService.getArticle(id);
            if (article == null) throw AppException.of(ErrorType.ARTICLE_NOT_FOUND_BY_ID, id);
            return objectMapper.writeValueAsString(article);
    }
}
