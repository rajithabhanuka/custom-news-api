package org.comppress.customnewsapi.service.article;

import org.comppress.customnewsapi.dto.GenericPage;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ArticleService {

    ResponseEntity<GenericPage> getArticles(int page, int size,
                                            String title, String category,
                                            String publisherNewsPaper, String lang,
                                            String fromDate, String toDate);

    // TODO improve Method
    void fetchArticlesWithRome();

    void updateArticlePayWall(int pageSize);

    ResponseEntity<GenericPage> getRatedArticles(int page, int size,
                                                 String title, String category,
                                                 String publisherNewsPaper, String lang,
                                                 String fromDate, String toDate);

    ResponseEntity<GenericPage> getArticlesNotRated(int page, int size, Long category,
                                                                List<Long> listPublisherIds, String lang,
                                                                String fromDate, String toDate);
}
