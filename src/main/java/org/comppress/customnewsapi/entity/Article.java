package org.comppress.customnewsapi.entity;

import lombok.Data;
import org.comppress.customnewsapi.dto.ArticleDto;
import org.springframework.beans.BeanUtils;

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
    private String description;
    @Column(unique = true)
    private String url;
    private String urlToImage; // done
    @Column(unique = true)
    private String guid; // done
    private LocalDateTime publishedAt;
    private String content; // done
    @ManyToOne
    private RssFeed rssFeed;

    public ArticleDto toDto(){
        ArticleDto articleDto = new ArticleDto();
        BeanUtils.copyProperties(this, articleDto);
        return articleDto;
    }
}
