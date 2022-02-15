package org.comppress.customnewsapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CriteriaRatingDto {
    @JsonProperty(value = "criteria_id")
    private Long criteriaId;
    private Integer rating;
}
