package ru.zrv.newspagespr.newspage.service;

import org.springframework.stereotype.Service;
import ru.zrv.newspagespr.newspage.dao.TagsDao;

import java.sql.SQLException;
import java.util.Map;

@Service
public class TagsService {

    private final TagsDao tagsDao;

    public TagsService(TagsDao tagsDao) {
        this.tagsDao = tagsDao;
    }

    public Map<String, Integer> getTagsMap() throws SQLException {
        Map<String, Integer> tagsMap;
        tagsMap = tagsDao.getTagsMap();
        return tagsMap;
    }
}
