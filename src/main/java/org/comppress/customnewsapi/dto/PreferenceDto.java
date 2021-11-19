package org.comppress.customnewsapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PreferenceDto {
    @JsonProperty(value = "category_ids")
    private String listOfCategoryIds;
    @JsonProperty(value = "publisher_ids")
    private String listOfPublisherIds;
}
