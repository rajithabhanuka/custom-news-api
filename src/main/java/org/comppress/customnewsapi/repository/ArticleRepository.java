package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.Article;
import org.comppress.customnewsapi.utils.StaticSQLQueries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query(value = "SELECT * FROM article ORDER BY RAND() LIMIT :numberArticles ", nativeQuery = true)
    List<Article> retrieveRandomArticles(@Param("numberArticles") Integer numberArticles);

    @Query(value = "SELECT * FROM article JOIN rss_feed rf on rf.id = article.rss_feed_id WHERE rf.category_id = :categoryId ", nativeQuery = true)
    List<Article> retrieveArticlesByCategoryId(@Param("categoryId") Long categoryId);

    boolean existsById(Long id);

    Optional<Article> findByGuid(String guid);

    @Query(value = "SELECT * FROM article at JOIN rss_feed rf on rf.id = at.rss_feed_id " +
            "JOIN publisher p on p.id = rf.publisher_id " +
            "JOIN category c on c.id = rf.category_id WHERE " +
            "(:category is null or :category = '' or c.name LIKE %:category%) AND " +
            "(:publisherName is null or :publisherName = '' or p.name LIKE %:publisherName%) AND " +
            "(:title is null or :title = '' or at.title LIKE %:title%) AND " +
            "(:language is null or :language = '' or rf.lang LIKE :language) AND " +
            "at.published_at BETWEEN IFNULL(:fromDate, '1900-01-01 00:00:00') AND " +
            "IFNULL(:toDate,now()) ",
            countQuery = "SELECT count(*) FROM article at JOIN rss_feed rf on rf.id = at.rss_feed_id " +
                    "JOIN publisher p on p.id = rf.publisher_id " +
                    "JOIN category c on c.id = rf.category_id WHERE " +
                    "(:category is null or :category = '' or c.name LIKE %:category%) AND " +
                    "(:publisherName is null or :publisherName = '' or p.name LIKE %:publisherName%) AND " +
                    "(:title is null or :title = '' or at.title LIKE %:title%) AND " +
                    "(:language is null or :language = '' or rf.lang LIKE :language) AND " +
                    "at.published_at BETWEEN IFNULL(:fromDate, '1900-01-01 00:00:00') AND " +
                    "IFNULL(:toDate,now())", nativeQuery = true)
    Page<Article> retrieveByCategoryOrPublisherName(@Param("category") String category,
                                                    @Param("publisherName") String publisherName,
                                                    @Param("title") String title,
                                                    @Param("language") String language,
                                                    @Param("fromDate") LocalDateTime fromDate,
                                                    @Param("toDate") LocalDateTime toDate,
                                                    Pageable pageable);

    @Query(value = "select t.article_id, a.author, a.title, a.description, a.url, a.url_to_image, a.guid, a.published_at, a.content, a.count_ratings, t.average_rating_criteria_1, t.average_rating_criteria_2, t.average_rating_criteria_3, sum(t.average_rating_criteria_1 + t.average_rating_criteria_2 + t.average_rating_criteria_3)/" +
            "(CASE WHEN  t.average_rating_criteria_1 IS NULL THEN 0 ELSE 1 END + CASE WHEN t.average_rating_criteria_2 IS NULL THEN 0 ELSE 1 END + CASE WHEN t.average_rating_criteria_3 IS NULL THEN 0 ELSE 1 END) AS total_average_rating " +
            "from (SELECT distinct r.article_id, " +
            "(select avg(r1.rating) from rating r1 where r1.article_id = r.article_id AND r1.criteria_id=1) as average_rating_criteria_1, " +
            "(select avg(r1.rating) from rating r1 where r1.article_id = r.article_id AND r1.criteria_id=2) as average_rating_criteria_2, " +
            "(select avg(r1.rating) from rating r1 where r1.article_id = r.article_id AND r1.criteria_id=3) as average_rating_criteria_3 " +
            "FROM rating r group by r.article_id) as t INNER JOIN article a ON a.id= t.article_id INNER JOIN rss_feed rf ON rf.id = a.rss_feed_id INNER JOIN category c ON c.id = rf.category_id INNER JOIN publisher p ON p.id = rf.publisher_id " +
            "WHERE(:category is null or :category = '' or c.name LIKE %:category%) AND " +
            "(:publisherName is null or :publisherName = '' or p.name LIKE %:publisherName%) AND " +
            "(:title is null or :title = '' or a.title LIKE %:title%) AND " +
            "(:language is null or :language = '' or rf.lang LIKE :language) AND " +
            "a.published_at BETWEEN IFNULL(:fromDate, '1900-01-01 00:00:00') AND IFNULL(:toDate,now()) " +
            "group by t.article_id order by total_average_rating DESC;", nativeQuery = true)
    List<CustomRatedArticle> retrieveAllRatedArticlesInDescOrder(
            @Param("category") String category,
            @Param("publisherName") String publisherName,
            @Param("title") String title,
            @Param("language") String language,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);



    // TODO Look for clean solution here
    interface CustomRatedArticle {
        Long getArticle_id();
        String getAuthor();
        String getTitle();
        String getDescription();
        String getUrl();
        String getUrl_to_image();
        String getGuid();
        String getPublished_at();
        String getContent();
        Integer getCount_ratings();
        Double getAverage_rating_criteria_1();
        Double getAverage_rating_criteria_2();
        Double getAverage_rating_criteria_3();
        Double getTotal_average_rating();
    }


    @Query(value = "select t.article_id, a.author, a.title, a.description, a.url, a.url_to_image, a.guid, a.published_at,\n" +
            "       a.content, a.count_ratings, t.average_rating_criteria_1, t.average_rating_criteria_2,\n" +
            "       t.average_rating_criteria_3, sum(t.average_rating_criteria_1  +t.average_rating_criteria_2+  t.average_rating_criteria_3)/\n" +
            "                                    (CASE WHEN  t.average_rating_criteria_1 IS NULL THEN 0 ELSE 1 END +\n" +
            "                                     CASE WHEN t.average_rating_criteria_2 IS NULL THEN 0 ELSE 1 END +\n" +
            "                                     CASE WHEN t.average_rating_criteria_3 IS NULL THEN 0 ELSE 1 END) AS total_average_rating\n" +
            "from (SELECT distinct r.article_id,\n" +
            "                      (select avg(r1.rating) from rating r1 where r1.article_id = r.article_id AND r1.criteria_id=1) as average_rating_criteria_1,\n" +
            "                      (select avg(r1.rating) from rating r1 where r1.article_id = r.article_id AND r1.criteria_id=2) as average_rating_criteria_2,\n" +
            "                      (select avg(r1.rating) from rating r1 where r1.article_id = r.article_id AND r1.criteria_id=3) as average_rating_criteria_3\n" +
            "      FROM rating r group by r.article_id) as t INNER JOIN article a ON a.id= t.article_id INNER JOIN rss_feed rf ON rf.id = a.rss_feed_id INNER JOIN category c ON c.id = rf.category_id INNER JOIN publisher p ON p.id = rf.publisher_id\n" +
            "WHERE ( rf.lang = :lang ) AND " +
            "c.id in :categoryIds  AND " +
            "p.id in :publisherIds AND " +
            "a.published_at BETWEEN IFNULL(:fromDate, '1900-01-01 00:00:00') AND IFNULL(:toDate,now()) " +
            "group by t.article_id order by total_average_rating DESC"
            ,countQuery = "select count(*) " +
            "from (SELECT distinct r.article_id,\n" +
            "                      (select avg(r1.rating) from rating r1 where r1.article_id = r.article_id AND r1.criteria_id=1) as average_rating_criteria_1,\n" +
            "                      (select avg(r1.rating) from rating r1 where r1.article_id = r.article_id AND r1.criteria_id=2) as average_rating_criteria_2,\n" +
            "                      (select avg(r1.rating) from rating r1 where r1.article_id = r.article_id AND r1.criteria_id=3) as average_rating_criteria_3\n" +
            "      FROM rating r group by r.article_id) as t INNER JOIN article a ON a.id= t.article_id INNER JOIN rss_feed rf ON rf.id = a.rss_feed_id INNER JOIN category c ON c.id = rf.category_id INNER JOIN publisher p ON p.id = rf.publisher_id\n" +
            "WHERE ( rf.lang = :lang ) AND " +
            "c.id in :categoryIds  AND " +
            "p.id in :publisherIds AND " +
            "a.published_at BETWEEN IFNULL(:fromDate, '1900-01-01 00:00:00') AND IFNULL(:toDate,now()) " +
            "group by t.article_id"
            ,nativeQuery = true)
    Page<CustomRatedArticle> retrieveArticlesByCategoryIdsAndPublisherIdsAndLanguage(
            @Param("categoryIds") List<Long> categoryIds,
            @Param("publisherIds") List<Long> publisherIds,
            @Param("lang") String lang,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable);


    @Query(value = "select t.article_id, a.author, a.title, a.description, a.url, a.url_to_image, a.guid, a.published_at,\n" +
            "       a.content, a.count_ratings, t.average_rating_criteria_1, t.average_rating_criteria_2,\n" +
            "       t.average_rating_criteria_3, sum(t.average_rating_criteria_1  +t.average_rating_criteria_2+  t.average_rating_criteria_3)/\n" +
            "                                    (CASE WHEN  t.average_rating_criteria_1 IS NULL THEN 0 ELSE 1 END +\n" +
            "                                     CASE WHEN t.average_rating_criteria_2 IS NULL THEN 0 ELSE 1 END +\n" +
            "                                     CASE WHEN t.average_rating_criteria_3 IS NULL THEN 0 ELSE 1 END) AS total_average_rating\n" +
            "from (SELECT distinct r.article_id,\n" +
            "                      (select avg(r1.rating) from rating r1 where r1.article_id = r.article_id AND r1.criteria_id=1) as average_rating_criteria_1,\n" +
            "                      (select avg(r1.rating) from rating r1 where r1.article_id = r.article_id AND r1.criteria_id=2) as average_rating_criteria_2,\n" +
            "                      (select avg(r1.rating) from rating r1 where r1.article_id = r.article_id AND r1.criteria_id=3) as average_rating_criteria_3\n" +
            "      FROM rating r group by r.article_id) as t INNER JOIN article a ON a.id= t.article_id INNER JOIN rss_feed rf ON rf.id = a.rss_feed_id INNER JOIN category c ON c.id = rf.category_id INNER JOIN publisher p ON p.id = rf.publisher_id\n" +
            "WHERE ( rf.lang = :lang ) AND " +
            "c.id = :categoryId AND " +
            "p.id in :publisherIds AND " +
            "a.published_at BETWEEN IFNULL(:fromDate, '1900-01-01 00:00:00') AND IFNULL(:toDate,now()) " +
            "group by t.article_id order by total_average_rating DESC LIMIT 1"
            ,nativeQuery = true)
    CustomRatedArticle retrieveArticlesByCategoryIdsAndPublisherIdsAndLanguageAndLimit(
            @Param("categoryId") Long categoryId,
            @Param("publisherIds") List<Long> publisherIds,
            @Param("lang") String lang,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );


    @Query(value = "SELECT * FROM article WHERE (:articleId is null or id = :articleId)",nativeQuery = true)
    List<Article> customTestQuery(@Param("articleId") Long articleId);


}
