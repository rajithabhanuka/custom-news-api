package org.comppress.customnewsapi.scheduler;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.comppress.customnewsapi.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Slf4j
public class NewsFeedScheduler {

    @Value("${scheduler.enabled}")
    private boolean enabled;

    private final ArticleService articleService;

    @Autowired
    public NewsFeedScheduler(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Scheduled(fixedDelayString = "${scheduler.triggeringIntervalMilliSeconds}",
            initialDelayString = "${scheduler.initialDelayIntervalMilliSeconds}")
    @SchedulerLock(name = "newsFeedingScheduler")
    public void saveNewsFeed(){
        log.info("Scheduler feeding data!");

        if (enabled){
            articleService.fetchArticles();
        }

    }

}
