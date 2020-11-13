package ru.zrv.newspagespr.newspage.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ErrorType {

    ARTICLE_NOT_FOUND_BY_ID(NOT_FOUND, "Article not found by ID: id=%s"),
    ARTICLES_NOT_FOUND(NOT_FOUND, "Статей по вашему запросу не найдено!"),
    SUGGESTIONS_NOT_FOUND(NOT_FOUND, "Suggestions not found");

    private final HttpStatus httpStatus;
    private final String message;

}
