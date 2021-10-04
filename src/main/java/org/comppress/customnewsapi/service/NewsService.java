package org.comppress.customnewsapi.service;

import org.springframework.http.ResponseEntity;

public interface NewsService {

    ResponseEntity<String> getNews();
}
