package ru.zrv.newspagespr.newspage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    @RequestMapping("/register")
    String register() {
        return null;
    }
}
