package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.TopNewsFeed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopNewsFeedRepository extends JpaRepository<TopNewsFeed,Long> {

    TopNewsFeed findByUrl(String url);
    Boolean existsByUrl(String url);

}
