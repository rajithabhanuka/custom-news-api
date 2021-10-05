package org.comppress.customnewsapi.loader;

import lombok.extern.slf4j.Slf4j;
import org.comppress.customnewsapi.entity.RssFeed;
import org.comppress.customnewsapi.entity.Source;
import org.comppress.customnewsapi.repository.RssFeedRepository;
import org.comppress.customnewsapi.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EntityLoader implements ApplicationRunner {

    RssFeedRepository rssFeedRepository;
    SourceRepository sourceRepository;

    @Autowired
    public EntityLoader(RssFeedRepository rssFeedRepository, SourceRepository sourceRepository) {
        this.rssFeedRepository = rssFeedRepository;
        this.sourceRepository = sourceRepository;
    }

    @Override
    public void run(ApplicationArguments args)  {

        /*
        String [] arrayRssFeedUrls = new String[]{
                "https://www.spiegel.de/sport/fussball/index.rss",
        };

        Source source = new Source("Spiegel");
        sourceRepository.save(source);

        for (String url:arrayRssFeedUrls) {
            RssFeed rssFeed = new RssFeed(url,source,"Sport");
            rssFeedRepository.save(rssFeed);
        }
         */
        log.info("Exit Entity Loader");
    }
}
