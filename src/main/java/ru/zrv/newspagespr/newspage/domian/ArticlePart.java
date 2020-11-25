package ru.zrv.newspagespr.newspage.domian;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ArticlePart {

    @JsonProperty("id")
    private String id;

    @JsonProperty("fronturl")
    private String frontUrl;

    @JsonProperty("publish_date")
    private Timestamp publishDate;

    @JsonProperty("title")
    private String title;

    @JsonProperty("photo")
    private Photo photo;

    @JsonProperty("project")
    private String project;

    @JsonProperty("category")
    private String category;

    @JsonProperty("opinion_authors")
    private String opinionAuthors;

    @JsonProperty("anons")
    private String anons;

    @Override
    public String toString() {
        return "PreviewArticle{" +
                "id='" + id + '\'' +
                ", frontUrl='" + frontUrl + '\'' +
                ", publishDate=" + publishDate +
                ", title='" + title + '\'' +
                ", photo=" + photo +
                ", project='" + project + '\'' +
                ", category='" + category + '\'' +
                ", opinionAuthors='" + opinionAuthors + '\'' +
                ", anons='" + anons + '\'' +
                '}';
    }
}
