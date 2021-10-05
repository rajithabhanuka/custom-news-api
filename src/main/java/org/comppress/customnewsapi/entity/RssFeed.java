package org.comppress.customnewsapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RssFeed extends AbstractEntity{

    @Column(unique = true)
    private String url;
    @ManyToOne
    private Source source;
    private String category;

}
