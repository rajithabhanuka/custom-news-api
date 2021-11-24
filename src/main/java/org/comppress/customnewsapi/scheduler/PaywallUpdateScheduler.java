package org.comppress.customnewsapi.scheduler;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.comppress.customnewsapi.service.article.ArticleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Slf4j
public class PaywallUpdateScheduler {

    @Value("${scheduler.paywall.enabled}")
    private boolean enabled;
    @Value("${scheduler.paywall.page-size}")
    private int pageSize;

    private final ArticleService articleService;

    public PaywallUpdateScheduler(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Scheduled(fixedDelayString = "${scheduler.paywall.triggeringIntervalMilliSeconds}",
            initialDelayString = "${scheduler.paywall.initialDelayIntervalMilliSeconds}")
    @SchedulerLock(name = "paywallScheduler")
    public void saveNewsFeed(){
        if(enabled){
            log.info("Scheduler Running");
            articleService.updateArticlePayWall(pageSize);
        }
    }

}
