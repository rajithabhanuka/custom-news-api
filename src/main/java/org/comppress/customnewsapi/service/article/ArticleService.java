package org.comppress.customnewsapi.service.article;

import org.comppress.customnewsapi.dto.GenericPage;
import org.springframework.http.ResponseEntity;

public interface ArticleService {

    ResponseEntity<GenericPage> getArticles(int page, int size,
                                            String title, String category,
                                            String publisherNewsPaper, String lang,
                                            String fromDate, String toDate);

    void fetchArticlesWithRome();


    ResponseEntity<GenericPage> getRatedArticles(int page, int size,
                                                 String title, String category,
                                                 String publisherNewsPaper, String lang,
                                                 String fromDate, String toDate);
}
