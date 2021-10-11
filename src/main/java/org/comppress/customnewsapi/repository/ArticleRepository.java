package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.Article;
import org.comppress.customnewsapi.entity.RssFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article,Long> {

    List<Article> findByRssFeed(RssFeed rssFeed);
    Optional<Article> findByGuid(String guid);

}
