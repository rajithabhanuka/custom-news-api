package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;



public interface ArticleRepository extends JpaRepository<Article,Long>, JpaSpecificationExecutor<Article> {

}
