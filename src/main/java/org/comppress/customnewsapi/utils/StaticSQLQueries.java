package org.comppress.customnewsapi.utils;

public class StaticSQLQueries {

    public final static String CALCULATE_RATING = "select t.article_id, a.author, a.title, a.description, a.url, a.url_to_image, a.guid, a.published_at,\n" +
            "       a.content, a.count_ratings, t.average_rating_criteria_1, t.average_rating_criteria_2,\n" +
            "       t.average_rating_criteria_3, sum(t.average_rating_criteria_1  +t.average_rating_criteria_2+  t.average_rating_criteria_3)/\n" +
            "            (CASE WHEN  t.average_rating_criteria_1 IS NULL THEN 0 ELSE 1 END +\n" +
            "            CASE WHEN t.average_rating_criteria_2 IS NULL THEN 0 ELSE 1 END +\n" +
            "            CASE WHEN t.average_rating_criteria_3 IS NULL THEN 0 ELSE 1 END) AS total_average_rating\n" +
            "            from (SELECT distinct r.article_id,\n" +
            "            (select avg(r1.rating) from rating r1 where r1.article_id = r.article_id AND r1.criteria_id=1) as average_rating_criteria_1,\n" +
            "            (select avg(r1.rating) from rating r1 where r1.article_id = r.article_id AND r1.criteria_id=2) as average_rating_criteria_2,\n" +
            "            (select avg(r1.rating) from rating r1 where r1.article_id = r.article_id AND r1.criteria_id=3) as average_rating_criteria_3";

}
