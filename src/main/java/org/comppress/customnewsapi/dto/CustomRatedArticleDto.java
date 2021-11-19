package org.comppress.customnewsapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CustomRatedArticleDto {

    private String content;
    private String url;
    private String title;
    @JsonProperty(value = "article_id")
    private String article_id;
    @JsonProperty(value = "url_to_image")
    private String url_to_image;
    @JsonProperty(value = "published_at")
    private String published_at;
    @JsonProperty(value = "count_ratings")
    private Integer count_ratings;
    @JsonProperty(value = "average_criteria_1")
    private Double average_rating_criteria_1;
    @JsonProperty(value = "average_criteria_2")
    private Double average_rating_criteria_2;
    @JsonProperty(value = "average_criteria_3")
    private Double average_rating_criteria_3;
    @JsonProperty(value = "total_average")
    private Double total_average_rating;
}
