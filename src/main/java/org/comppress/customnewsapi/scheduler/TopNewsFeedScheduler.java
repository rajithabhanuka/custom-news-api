package org.comppress.customnewsapi.scheduler;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.comppress.customnewsapi.service.topnewsfeed.TopNewsFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Slf4j
public class TopNewsFeedScheduler {

    @Value("${scheduler.top-news-feeds.enabled}")
    private boolean enabled;


    private final TopNewsFeedService topNewsFeedService;

    @Autowired
    public TopNewsFeedScheduler(TopNewsFeedService topNewsFeedService) {
        this.topNewsFeedService = topNewsFeedService;
    }

    @Scheduled(fixedDelayString = "${scheduler.top-news-feeds.triggeringIntervalMilliSeconds}",
            initialDelayString = "${scheduler.top-news-feeds.initialDelayIntervalMilliSeconds}")
    @SchedulerLock(name = "newsFeedingScheduler")
    public void checkTopNewsFeeds(){
        if(enabled){log.info("News Scheduler Running!");
            topNewsFeedService.checkTopNewsFeeds();
        }
    }

}
