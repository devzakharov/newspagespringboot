package ru.zrv.newspagespr.newspage.domian;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


import java.util.HashSet;

@Data
public class ArticlePartHashSet {

    @JsonProperty("items")
    private HashSet<ArticlePart> articlePartHashSet;
}
