package ru.zrv.newspagespr.newspage.domian;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class Article {

    @NotNull
    private String id;

    @NotNull
    private String description;

    @NotNull
    private String newsKeywords;

    @NotNull
    private String image;

    @NotNull
    private String articleHtml;

    @NotNull
    private String frontUrl;

    @NotNull
    private String title;

    @NotNull
    private Photo photo;

    @NotNull
    private String project;

    @NotNull
    private String category;

    @NotNull
    private String opinionAuthors;

    @NotNull
    private String anons;

    @NotNull
    private Timestamp publishDate;

    @NotNull
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
