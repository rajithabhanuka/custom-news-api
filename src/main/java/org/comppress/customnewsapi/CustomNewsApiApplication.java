package org.comppress.customnewsapi;

import lombok.extern.slf4j.Slf4j;
import org.comppress.customnewsapi.entity.Category;
import org.comppress.customnewsapi.entity.Publisher;
import org.comppress.customnewsapi.entity.RssFeed;
import org.comppress.customnewsapi.repository.CategoryRepository;
import org.comppress.customnewsapi.repository.PublisherRepository;
import org.comppress.customnewsapi.repository.RssFeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
@Slf4j
public class CustomNewsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomNewsApiApplication.class, args);
	}

//	@Bean
//	@Autowired
//	CommandLineRunner run(RssFeedRepository rssFeedRepository, PublisherRepository publisherRepository, CategoryRepository categoryRepository){
//		return args -> {
//			Optional<Publisher> optionalPublisher = publisherRepository.findByName("Spiegel");
//			if(optionalPublisher.isEmpty()){
//				Publisher publisher = publisherRepository.save(new Publisher("Spiegel"));
//				optionalPublisher =  Optional.of(publisher);
//			}
//
//			Optional<Category> optionalCategory = categoryRepository.findByName("Sport");
//			if(optionalCategory.isEmpty()){
//				Category category = categoryRepository.save(new Category("Sport"));
//				optionalCategory =  Optional.of(category);
//			}
//			String rssFeedUrl = "https://www.spiegel.de/sport/fussball/index.rss";
//			Optional<RssFeed> rssFeedOptional = rssFeedRepository.findByUrlRssFeed(rssFeedUrl);
//			if(rssFeedOptional.isEmpty()){
//				log.info("Create new Rss Feed Object {} in DB", rssFeedOptional);
//				rssFeedRepository.save(
//						new RssFeed(optionalCategory.get(), rssFeedUrl, optionalPublisher.get()));
//			}else {
//				log.info("DB entry for Table Rss Feed with url {} is already there", rssFeedUrl);
//			}
//		};
//	}

}
