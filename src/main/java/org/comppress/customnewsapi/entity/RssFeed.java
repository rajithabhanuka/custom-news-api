package org.comppress.customnewsapi.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@Entity
public class RssFeed extends AbstractEntity{

    private String category;
    private String urlRssFeed;
    @ManyToOne
    private Publisher publisher;
}
