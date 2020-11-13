package ru.zrv.newspagespr.newspage.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.zrv.newspagespr.newspage.dao.TagsDao;
import ru.zrv.newspagespr.newspage.exception.AppException;
import ru.zrv.newspagespr.newspage.exception.ErrorType;

import java.sql.SQLException;
import java.util.Set;

@RestController
public class SuggestionsController {

    private final TagsDao tagsDao;

    public SuggestionsController(TagsDao tagsDao) {
        this.tagsDao = tagsDao;
    }

    @RequestMapping(value = "/api/v1/suggestions", produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public Set<String> getSuggestions(@RequestParam String inputvalue) throws SQLException {
        Set<String> suggestionsSet = tagsDao.getSuggestions(inputvalue);
        if (suggestionsSet == null) throw AppException.of(ErrorType.SUGGESTIONS_NOT_FOUND);
        return suggestionsSet;
    }
}
