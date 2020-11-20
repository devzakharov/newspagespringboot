package ru.zrv.newspagespr.newspage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.zrv.newspagespr.newspage.exception.AppException;
import ru.zrv.newspagespr.newspage.exception.ErrorType;
import ru.zrv.newspagespr.newspage.service.TagsService;

import java.sql.SQLException;
import java.util.Map;

@RestController
public class TagsController {

    private final TagsService tagsService;

    public TagsController(TagsService tagsService) {
        this.tagsService = tagsService;
    }

    @GetMapping("/api/v1/tags")
    Map<String, Integer> getTags(@RequestParam int getalltags) throws SQLException {
        if (getalltags != 1) throw AppException.of(ErrorType.BAD_TAGS_REQUEST);
        return tagsService.getTagsMap();
    }
}
