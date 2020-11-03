package ru.zrv.newspagespr.newspage.domian;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Photo {

    @JsonProperty("url")
    private String url;

    @Override
    public String toString() {
        return "{\"url\":\"" + url + '\"' + '}';
    }
}
