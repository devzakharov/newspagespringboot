package ru.zrv.newspagespr.newspage.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ErrorType {

    ARTICLE_NOT_FOUND_BY_ID(NOT_FOUND, "Article not found by ID: id=%s");

    private final HttpStatus httpStatus;
    private final String message;

}
