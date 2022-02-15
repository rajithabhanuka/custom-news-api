package org.comppress.customnewsapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserPreferenceDto {

    @JsonProperty("categories")
    private List<CustomCategoryDto> categoryDtoList;

}
