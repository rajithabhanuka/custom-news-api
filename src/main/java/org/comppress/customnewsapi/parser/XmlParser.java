package org.comppress.customnewsapi.parser;

import lombok.extern.slf4j.Slf4j;
import org.comppress.customnewsapi.entity.Article;
import org.comppress.customnewsapi.entity.RssFeed;
import org.comppress.customnewsapi.repository.RssFeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class XmlParser {

    RssFeedRepository rssFeedRepository;

    @Autowired
    public XmlParser(RssFeedRepository rssFeedRepository) {
        this.rssFeedRepository = rssFeedRepository;
    }

    public List<Article> fetchArticles() {
        List<RssFeed> rssFeedList = rssFeedRepository.findAll();
        log.info("Fetched {} rss feeds from the database", rssFeedList.size());
        List<Article> allArticlesList = new ArrayList<>();

        for (RssFeed rssFeed:rssFeedList) {
            try {
                String webPage = urlReader(rssFeed.getUrlRssFeed());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        log.info("Returning {} articles", allArticlesList.size());
        return allArticlesList;
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
