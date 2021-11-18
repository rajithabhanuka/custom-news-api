package org.comppress.customnewsapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserPreferenceDto {

    private List<CustomCategoryDto> categoryDtoList;

}
