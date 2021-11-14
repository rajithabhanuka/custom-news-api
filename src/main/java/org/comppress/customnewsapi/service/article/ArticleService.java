package org.comppress.customnewsapi.service.article;

import org.comppress.customnewsapi.dto.GenericPage;
import org.springframework.http.ResponseEntity;

public interface ArticleService {

    ResponseEntity<GenericPage> getArticles(int page, int size,
                                            String title, String category,
                                            String publisherNewsPaper,
                                            String fromDate, String toDate);

    // TODO ? void fetchArticles();

    ResponseEntity<GenericPage> getRatedArticles(int page, int size);

    ResponseEntity<GenericPage> getRatedArticles(int page, int size,
                                                 String title, String category,
                                                 String publisherNewsPaper,
                                                 String fromDate, String toDate);
}
