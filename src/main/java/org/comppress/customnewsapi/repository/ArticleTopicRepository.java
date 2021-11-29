package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.ArticleTopic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleTopicRepository extends JpaRepository<ArticleTopic,Long> {

    List<ArticleTopic> findByArticleIdAndTopicId(Long articleId, Long topicId);
}
