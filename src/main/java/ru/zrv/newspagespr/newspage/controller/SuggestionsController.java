package ru.zrv.newspagespr.newspage.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SuggestionsController {

    @RequestMapping("/suggestions")
    String getSuggestions() {
        return "Suggestions";
    }
}
