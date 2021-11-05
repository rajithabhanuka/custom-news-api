package org.comppress.customnewsapi.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ArticleDto {

    private Long id;
    private String author;

    private String title;

    private String description;

    private String url;

    private String urlToImage;

    private String guid;

    private LocalDateTime publishedAt;

    private String content;

    private Integer countRatings;


}