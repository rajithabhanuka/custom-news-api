package org.comppress.customnewsapi.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
public class TopNewsFeed extends AbstractEntity{

    @Column(unique = true, nullable = false)
    private String url;
    private Long publisherId;
    private String lang;

}
