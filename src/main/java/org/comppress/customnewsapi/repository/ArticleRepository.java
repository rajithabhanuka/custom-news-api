package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,Long> {

}
