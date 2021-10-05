package org.comppress.customnewsapi.loader;

import lombok.extern.slf4j.Slf4j;
import org.comppress.customnewsapi.entity.Category;
import org.comppress.customnewsapi.entity.Feed;
import org.comppress.customnewsapi.entity.Publisher;
import org.comppress.customnewsapi.entity.RssFeed;
import org.comppress.customnewsapi.repository.CategoryRepository;
import org.comppress.customnewsapi.repository.FeedRepository;
import org.comppress.customnewsapi.repository.PublisherRepository;
import org.comppress.customnewsapi.repository.RssFeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class EntityLoader implements ApplicationRunner {

    private FeedRepository feedRepository;

    @Autowired
    public EntityLoader(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    @Override
    public void run(ApplicationArguments args)  {

        String [] arrayRssFeedUrls = new String[]{
                "https://www.spiegel.de/sport/fussball/index.rss",
                "https://www.spiegel.de/auto/index.rss",
                "https://www.spiegel.de/politik/index.rss",
                "https://www.spiegel.de/reise/index.rss"
        };

        int counter = 0;
        for (String url:arrayRssFeedUrls) {
            if(!feedRepository.existsByUrl(url)){
                feedRepository.save(
                        new Feed(url, "none", 0L)
                );
                counter++;
            }
        }
        log.info(counter + " database entries were added");
    }
}
