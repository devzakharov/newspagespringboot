package ru.zrv.newspagespr.newspage.service;

import org.springframework.stereotype.Service;
import ru.zrv.newspagespr.newspage.dao.ArticleDao;
import ru.zrv.newspagespr.newspage.domian.Article;

import java.sql.SQLException;

@Service
public class ArticleService {

    ArticleDao articleDao;

    public ArticleService(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    public Article getArticle(String id) throws SQLException {

        if(articleDao.get(id).isPresent()) {
            return articleDao.get(id).get();
        } else {
            throw new RuntimeException();
        }
    }
}
