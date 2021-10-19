package org.comppress.customnewsapi.service;

import org.comppress.customnewsapi.dto.GenericPage;
import org.springframework.http.ResponseEntity;

public interface ArticleService {

    ResponseEntity<GenericPage> getArticles(int page, int size,
                                            String title, String category,
                                            String publisherNewsPaper,
                                            String fromDate, String toDate);

    void fetchArticles();

}
