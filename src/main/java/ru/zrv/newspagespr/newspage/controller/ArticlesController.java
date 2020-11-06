package ru.zrv.newspagespr.newspage.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class ArticlesController {

    @GetMapping("/api/v1/articles")
    String getArticles(
            @RequestParam Integer limit,
            @RequestParam Integer offset,
            @RequestParam String tags,
            @RequestParam String fromDate,
            @RequestParam String toDate,
            @RequestParam String search) {
        return limit + " " + offset + " " + tags + " " + fromDate + " " + toDate + " " + search;
    }
}
