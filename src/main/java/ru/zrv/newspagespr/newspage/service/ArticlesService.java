package ru.zrv.newspagespr.newspage.service;

import org.springframework.stereotype.Service;
import ru.zrv.newspagespr.newspage.dao.ArticleDao;
import ru.zrv.newspagespr.newspage.domian.Article;

import java.sql.SQLException;
import java.util.List;

@Service
public class ArticlesService {

    private final ArticleDao articleDao;

    public ArticlesService(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    public List<Article> getArticlesList(
            Integer limit,
            Integer offset,
            String tags,
            String fromDate,
            String toDate,
            String searchQuery
    ) throws SQLException {
        return articleDao.getFilteredResult(limit, offset, tags, fromDate, toDate, searchQuery);
    }
}
