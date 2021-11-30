package org.comppress.customnewsapi.scheduler;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.comppress.customnewsapi.repository.TwitterRepository;
import org.comppress.customnewsapi.service.twitter.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.net.URISyntaxException;

@Configuration
@EnableScheduling
@Slf4j
public class TwitterCommentCountScheduler {

    @Value("${scheduler.twitter.enabled}")
    private boolean enabled;

    private final TwitterRepository twitterRepository;
    private final TwitterService twitterService;

    @Autowired
    public TwitterCommentCountScheduler(TwitterRepository twitterRepository, TwitterService twitterService) {
        this.twitterRepository = twitterRepository;
        this.twitterService = twitterService;
    }

    @Scheduled(fixedDelayString = "${scheduler.twitter.triggeringIntervalMilliSeconds}",
            initialDelayString = "${scheduler.twitter.initialDelayIntervalMilliSeconds}")
    @SchedulerLock(name = "twitterScheduler")
    public void saveNewsFeed() throws URISyntaxException, IOException {
        if(enabled){
            log.info("Twitter Scheduler Running!");
            twitterService.getTweetDetails();
        }
    }

}
