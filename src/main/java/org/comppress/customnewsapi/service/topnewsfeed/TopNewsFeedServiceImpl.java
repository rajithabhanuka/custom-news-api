package org.comppress.customnewsapi.service.topnewsfeed;

import lombok.extern.slf4j.Slf4j;
import org.comppress.customnewsapi.entity.Article;
import org.comppress.customnewsapi.entity.TopNewsFeed;
import org.comppress.customnewsapi.repository.ArticleRepository;
import org.comppress.customnewsapi.repository.TopNewsFeedRepository;
import org.comppress.customnewsapi.service.article.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TopNewsFeedServiceImpl implements TopNewsFeedService {

    private final ArticleService articleService;
    private final TopNewsFeedRepository topNewsFeedRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public TopNewsFeedServiceImpl(ArticleService articleService, TopNewsFeedRepository topNewsFeedRepository, ArticleRepository articleRepository) {
        this.articleService = articleService;
        this.topNewsFeedRepository = topNewsFeedRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public void checkTopNewsFeeds() {
        // Get All Top News and Build a list
        List<TopNewsFeed> listTopNewsFeeds = topNewsFeedRepository.findAll();
        // Fetch TopNews
        for(TopNewsFeed topNewsFeed:listTopNewsFeeds){
            List<Article> listTopNewsArticles = articleService.fetchArticlesFromTopNewsFeed(topNewsFeed);
            if(listTopNewsArticles == null) continue;
            log.info("Fetching {} News from {}",listTopNewsArticles.size(),topNewsFeed.getUrl());
            // Check if there is an entry in the db
            for(Article article:listTopNewsArticles){
                Optional<Article> articleOptional = articleRepository.findByUrlAndIsTopNewsFalse(article.getUrl());
                if(articleOptional.isPresent()){
                    // If yes give them a Flag
                    articleOptional.get().setTopNews(true);
                    articleRepository.save(articleOptional.get());
                    log.info("Set Flag for Top News for article with id {}",articleOptional.get().getId());
                }
            }
        }
    }
}