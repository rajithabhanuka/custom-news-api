package org.comppress.customnewsapi.controller;

import org.comppress.customnewsapi.dto.TwitterArticleDto;
import org.comppress.customnewsapi.service.twitter.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import twitter4j.TwitterException;

@RestController
@RequestMapping(value = "/twitter")
public class TwitterController {

    private final TwitterService twitterService;

    @Autowired
    public TwitterController(TwitterService twitterService) {
        this.twitterService = twitterService;
    }

    @GetMapping
    public ResponseEntity<TwitterArticleDto> getTwitterArticle(@RequestParam(value = "articleId") Long articleId) throws TwitterException {
        return twitterService.getTwitterArticle(articleId);
    }
}
