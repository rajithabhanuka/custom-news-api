package org.comppress.customnewsapi.dto;

import lombok.Data;

@Data
public class CustomCategoryDto {

    private String name;
    private Long id;
    private RatingArticleDto article;

}
