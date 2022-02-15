package org.comppress.customnewsapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PublisherDto {
    private String name;
    private Long id;
    private String lang;
    @JsonProperty(value = "urlToImage")
    private String urlToImage;
}
