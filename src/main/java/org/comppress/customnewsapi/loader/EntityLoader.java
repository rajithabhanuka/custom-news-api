package org.comppress.customnewsapi.loader;

import lombok.extern.slf4j.Slf4j;
import org.comppress.customnewsapi.entity.Category;
import org.comppress.customnewsapi.entity.Publisher;
import org.comppress.customnewsapi.entity.RssFeed;
import org.comppress.customnewsapi.repository.CategoryRepository;
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

    private PublisherRepository publisherRepository;
    private RssFeedRepository rssFeedRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public EntityLoader(PublisherRepository publisherRepository, RssFeedRepository rssFeedRepository, CategoryRepository categoryRepository) {
        this.publisherRepository = publisherRepository;
        this.rssFeedRepository = rssFeedRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(ApplicationArguments args)  {
        Optional<Publisher> optionalPublisher = publisherRepository.findByName("Spiegel");
        if(optionalPublisher.isEmpty()){
            Publisher publisher = publisherRepository.save(new Publisher("Spiegel"));
            optionalPublisher =  Optional.of(publisher);
        }

        Optional<Category> optionalCategory = categoryRepository.findByName("Sport");
        if(optionalCategory.isEmpty()){
            Category category = categoryRepository.save(new Category("Sport"));
            optionalCategory =  Optional.of(category);
        }
        String rssFeedUrl = "https://www.spiegel.de/sport/fussball/index.rss";
        Optional<RssFeed> rssFeedOptional = rssFeedRepository.findByUrlRssFeed(rssFeedUrl);
        if(rssFeedOptional.isEmpty()){
            log.info("Create new Rss Feed Object {} in DB", rssFeedOptional);
            rssFeedRepository.save(
                    new RssFeed(optionalCategory.get(), rssFeedUrl, optionalPublisher.get()));
        }else {
            log.info("DB entry for Table Rss Feed with url {} is already there", rssFeedUrl);
        }
    }
}
