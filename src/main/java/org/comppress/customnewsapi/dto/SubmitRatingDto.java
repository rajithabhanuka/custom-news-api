package org.comppress.customnewsapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SubmitRatingDto {
    @JsonProperty(value = "article_id")
    private Long articleId;
    private List<CriteriaRatingDto> ratings;
}
