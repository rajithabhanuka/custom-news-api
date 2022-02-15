package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.TwitterTweet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwitterRepository extends JpaRepository<TwitterTweet,Long> {

    TwitterTweet findByArticleId(Long articleId);

}
