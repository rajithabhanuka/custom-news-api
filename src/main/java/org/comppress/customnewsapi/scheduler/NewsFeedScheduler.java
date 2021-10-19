package org.comppress.customnewsapi.scheduler;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.comppress.customnewsapi.service.ArticleServiceImpl;
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

    private final ArticleServiceImpl articleServiceImpl;

    @Autowired
    public NewsFeedScheduler(ArticleServiceImpl articleServiceImpl) {
        this.articleServiceImpl = articleServiceImpl;
    }

    @Scheduled(fixedDelayString = "${scheduler.triggeringIntervalMilliSeconds}",
            initialDelayString = "${scheduler.initialDelayIntervalMilliSeconds}")
    @SchedulerLock(name = "newsFeedingScheduler")
    public void saveNewsFeed(){
        if(enabled){
            log.info("Scheduler feeding data!");
            articleServiceImpl.fetchArticlesWithRome();
        }
    }

}
