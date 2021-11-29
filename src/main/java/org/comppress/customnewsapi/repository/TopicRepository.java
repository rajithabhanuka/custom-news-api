package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic,Long> {

    Topic findByName(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM topic t JOIN article_topic a on t.id = a.topic_id WHERE a.article_id = :articleId")
    List<Topic> retrieveByArticleId(@Param("articleId") Long articleId);
}
