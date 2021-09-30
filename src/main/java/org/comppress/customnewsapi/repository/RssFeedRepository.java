package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.RssFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RssFeedRepository extends JpaRepository<RssFeed,Long> {

    Optional<RssFeed> findByUrlRssFeed(String url);

}
