package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating,Long> {

    @Query(value = "SELECT * From rating rt " +
            "JOIN article a on a.id = rt.article_id " +
            "JOIN user u on rt.user_id " +
            "WHERE u.name LIKE :username AND a.id LIKE :articleId ;",nativeQuery = true)
    Rating retrieveByUserNameAndArticleId(@Param("username") String username, @Param("articleId") String articleId);

}
