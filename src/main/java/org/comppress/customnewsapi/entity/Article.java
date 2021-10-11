package org.comppress.customnewsapi.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
public class Article extends AbstractEntity{

    private String author;
    private String title;
    @Column(length = 65536 * 64)
    private String description;
    @Column(unique = true, columnDefinition = "TEXT")
    private String url;
    @Column(columnDefinition = "TEXT")
    private String urlToImage;
    @Column(unique = true, columnDefinition = "TEXT")
    private String guid;
    private LocalDateTime publishedAt;
    @Column(length = 65536 * 64)
    private String content;
    private Long rssFeedId;
}
