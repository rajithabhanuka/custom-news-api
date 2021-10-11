package org.comppress.customnewsapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class RssFeed extends AbstractEntity{

    @Column(unique = true)
    private String url;
    private Long publisherId;
    private Long categoryId;
    private String lang;

}
