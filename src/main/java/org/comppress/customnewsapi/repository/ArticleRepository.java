package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.Article;
import org.comppress.customnewsapi.entity.RssFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article,Long>, JpaSpecificationExecutor<Article> {

    List<Article> findByRssFeedId(Long rssFeedId);
    Optional<Article> findByGuid(String guid);

}
