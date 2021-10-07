package org.comppress.customnewsapi.dto;

import lombok.Data;
import org.comppress.customnewsapi.entity.RssFeed;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
public class ArticleDto {

    private String author;

    private String title;

    private String description;

    private String url;

    private String urlToImage;

    private String guid;

    private LocalDateTime publishedAt;

    private String content;


}
