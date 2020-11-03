package ru.zrv.newspagespr.newspage.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import ru.zrv.newspagespr.newspage.dao.ArticleDao;
import ru.zrv.newspagespr.newspage.domian.Article;
import ru.zrv.newspagespr.newspage.domian.ArticlePart;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class HtmlParserService {

    private final ArticleDao articleDao;
    private final Set<Article> articleSet = new HashSet<>();

    public HtmlParserService(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    // TODO add concurrency multithreading
    public void fillDataObject(Map<String, ArticlePart> previewArticleMap) {

        previewArticleMap.forEach((previewArticleId, previewArticle) -> {
            try {
                parseLink(previewArticle);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void parseLink(ArticlePart articlePart) throws IOException {

        Article article = new Article();
        Document doc = Jsoup.connect(articlePart.getFrontUrl()).get();

        article.setId(doc.select(".js-rbcslider-slide").attr("data-id"));
        article.setDescription(doc.select("div.js-rbcslider > div > meta").attr("content"));
        article.setNewsKeywords(doc.select("div.js-rbcslider > div > meta:nth-child(2)").attr("content"));
        article.setImage(doc.select("div.js-rbcslider > div > link:nth-child(7)").attr("href"));
        article.setArticleHtml(doc.select(".article__text").html());

        article.setFrontUrl(articlePart.getFrontUrl());
        article.setTitle(articlePart.getTitle());
        article.setPhoto(articlePart.getPhoto());
        article.setProject(articlePart.getProject());
        article.setCategory(articlePart.getCategory());
        article.setOpinionAuthors(articlePart.getOpinionAuthors());
        article.setAnons(articlePart.getAnons());
        article.setPublishDate(articlePart.getPublishDate());

        this.articleSet.add(article);
    }

    public Set<Article> getArticleSet() {

        return articleSet;
    }

    public void writeArticlesToDb () throws SQLException {

        articleDao.save(this.articleSet);
    }
}
