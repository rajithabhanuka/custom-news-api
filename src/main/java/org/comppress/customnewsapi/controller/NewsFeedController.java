package org.comppress.customnewsapi.controller;

import org.comppress.customnewsapi.service.article.ArticleService;
import org.comppress.customnewsapi.service.article.ArticleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/newsFeed")
public class NewsFeedController {

    private final ArticleService articleService;

    @Autowired
    public NewsFeedController(ArticleService articleService) {
        this.articleService = articleService;
    }
    @Value("${scheduler.enabled}")
    private boolean enabled;

    @GetMapping
    public ResponseEntity<String> startFeed() {
        if (enabled == false) {
            articleService.fetchArticlesWithRome();
        }
        return ResponseEntity.ok().body("Fetched News");
    }
}
