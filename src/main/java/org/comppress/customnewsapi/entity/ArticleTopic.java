package org.comppress.customnewsapi.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Data
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"articleId", "topicId"})
})
public class ArticleTopic extends AbstractEntity{
    private Long articleId;
    private Long topicId;
}
