package org.comppress.customnewsapi.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.comppress.customnewsapi.dto.json.PublisherDto;
import org.comppress.customnewsapi.dto.json.RootDto;
import org.comppress.customnewsapi.entity.Publisher;
import org.comppress.customnewsapi.entity.RssFeed;
import org.comppress.customnewsapi.repository.RssFeedRepository;
import org.comppress.customnewsapi.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
@Slf4j
public class EntityLoader implements ApplicationRunner {

    RssFeedRepository rssFeedRepository;
    PublisherRepository publisherRepository;

    @Autowired
    public EntityLoader(RssFeedRepository rssFeedRepository, PublisherRepository publisherRepository) {
        this.rssFeedRepository = rssFeedRepository;
        this.publisherRepository = publisherRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws IOException {
        // https://stackoverflow.com/questions/44399422/read-file-from-resources-folder-in-spring-boot
        File file = new ClassPathResource("json/news-feeds.json").getFile();
        RootDto jsonModel = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonModel = mapper.readValue(file, RootDto.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        int counterPublisher = 0;
        int counterRssFeed = 0;
        for (PublisherDto publisher : jsonModel.getPublisherDtos()) {
            // Create Publisher
            if (!publisherRepository.existsByName(publisher.getPublisher())) {
                counterPublisher++;
                publisherRepository.save(new Publisher(publisher.getPublisher()));
            }
            // Create RssFeed
            if (!rssFeedRepository.existsByUrl(publisher.getRSSFeed())) {
                counterRssFeed++;
                rssFeedRepository.save(new RssFeed(publisher.getRSSFeed(), publisherRepository.findByName(publisher.getPublisher()), publisher.getCategory()));
            }
        }
        log.info("Created " + counterPublisher + " Publisher and " + counterRssFeed + " Rss Feed");
        log.info("Exiting Entity Loader now");

    }
}
