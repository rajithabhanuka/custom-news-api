package org.comppress.customnewsapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwitterTweet extends AbstractEntity{

    private Long articleId;
    private Long twitterId;
    @Column(columnDefinition = "integer default 0", insertable = false)
    private Integer replyCount = 0;
    private String twitterArticleUrl;
}