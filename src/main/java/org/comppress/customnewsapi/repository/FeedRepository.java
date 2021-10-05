package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed,Long> {

    Boolean existsByUrl(String url);

}
