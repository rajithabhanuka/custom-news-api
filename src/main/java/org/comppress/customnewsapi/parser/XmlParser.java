package org.comppress.customnewsapi.parser;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.comppress.customnewsapi.entity.Feed;
import org.comppress.customnewsapi.entity.SavedFeed;
import org.comppress.customnewsapi.repository.FeedRepository;
import org.comppress.customnewsapi.repository.SavedFeedRepository;
import org.comppress.customnewsapi.xmlModel.RssXmlModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Component
@RestController
public class XmlParser {

    FeedRepository feedRepository;
    SavedFeedRepository savedFeedRepository;

    @Autowired
    public XmlParser(FeedRepository feedRepository, SavedFeedRepository savedFeedRepository) {
        this.feedRepository = feedRepository;
        this.savedFeedRepository = savedFeedRepository;
    }

    @GetMapping("/news")
    public String fetchArticles() {

        for (Feed feed:feedRepository.findAll()) {
            try {
                String webPage = urlReader(feed.getUrl());
                XmlMapper xmlMapper = new XmlMapper();
                // Service Layer
                // RssXmlModel rssXmlModel = xmlMapper.readValue(webPage, RssXmlModel.class);
                int hashcode = webPage.hashCode();
                if(hashcode != feed.getHash()){
                    SavedFeed savedFeed = new SavedFeed(feed,webPage);
                    savedFeedRepository.save(savedFeed);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "fetched News from " + arrayRssFeedUrls.length + " different rss feeds";
    }

    private String urlReader(String url) throws URISyntaxException, IOException, InterruptedException {
        log.info("Send GET request to " + url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        var response = HttpClient.newHttpClient().send(request,
                HttpResponse.BodyHandlers.ofString());

        return response.body();
    }


}
