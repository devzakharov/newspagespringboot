package ru.zrv.newspagespr.newspage.domian;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class Article {

    private String id;
    
    private String description;
    
    private String newsKeywords;

    private String image;

    private String articleHtml;

    private String frontUrl;
    
    private String title;
    
    private Photo photo;

    private String project;
    
    private String category;

    private String opinionAuthors;
    
    private String anons;
    
    private Timestamp publishDate;

    private Timestamp parsedDate = Timestamp.valueOf(LocalDateTime.now());

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
