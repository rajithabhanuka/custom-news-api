package org.comppress.customnewsapi.dto;

import lombok.Data;
import org.comppress.customnewsapi.repository.ArticleRepository;

import java.util.List;

@Data
public class CustomCategoryDto {

    private String name;
    private Long id;
    private List<ArticleRepository.CustomRatedArticle> articleDtoList;

}
