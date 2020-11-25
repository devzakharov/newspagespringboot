package ru.zrv.newspagespr.newspage.controller;

import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/api/v1/suggestions")
    public Set<String> getSuggestions(@RequestParam String inputvalue) throws SQLException {
        Set<String> suggestionsSet = tagsDao.getSuggestions(inputvalue);
        if (suggestionsSet == null) throw AppException.of(ErrorType.SUGGESTIONS_NOT_FOUND);
        return suggestionsSet;
    }
}
