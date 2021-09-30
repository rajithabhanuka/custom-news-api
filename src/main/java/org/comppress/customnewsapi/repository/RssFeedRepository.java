package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.RssFeed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RssFeedRepository extends JpaRepository<RssFeed,Long> {
}
