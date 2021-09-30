package org.comppress.customnewsapi.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@Entity
public class Article extends AbstractEntity{

    @Column(unique = true)
    private String url;
    private String title;
    private String imageUrl;
    private String description;
    private String category;
    @ManyToOne
    private RssFeed rssFeed;
    @ManyToOne
    private Publisher publisher;

}
