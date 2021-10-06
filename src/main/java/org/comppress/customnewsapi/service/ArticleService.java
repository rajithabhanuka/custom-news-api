package org.comppress.customnewsapi.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.comppress.customnewsapi.dto.ItemDto;
import org.comppress.customnewsapi.entity.Article;
import org.comppress.customnewsapi.entity.RssFeed;
import org.comppress.customnewsapi.mapper.MapstructMapper;
import org.comppress.customnewsapi.repository.ArticleRepository;
import org.comppress.customnewsapi.repository.RssFeedRepository;
import org.comppress.customnewsapi.dto.RssDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Slf4j
@Service
public class ArticleService {

    private final RssFeedRepository rssFeedRepository;
    private final MapstructMapper mapstructMapper;
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(RssFeedRepository rssFeedRepository, MapstructMapper mapstructMapper, ArticleRepository articleRepository) {
        this.rssFeedRepository = rssFeedRepository;
        this.mapstructMapper = mapstructMapper;
        this.articleRepository = articleRepository;
    }

    public String fetchArticles() {

        for (RssFeed rssFeed : rssFeedRepository.findAll()) {

            RssDto rssDto = null;

            try {
                String webPage = urlReader(rssFeed.getUrl());
                XmlMapper xmlMapper = new XmlMapper();
                xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                xmlMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                rssDto = xmlMapper.readValue(webPage, RssDto.class);

                List<ItemDto> itemDtoList = rssDto.getChannel().getItem();
                itemDtoList.forEach(itemDto -> {
                    Article article = mapstructMapper.itemDtoToArticle(itemDto);
                    article.setRssFeed(rssFeed); // rssFeed
                    try {
                        articleRepository.save(article);
                    } catch (DataIntegrityViolationException e) {
                        log.error("Duplicate Record found while saving data {}", e.getLocalizedMessage());
                    } catch (Exception e) {
                        log.error("Error while saving data {}", e.getLocalizedMessage());
                    }
                });

            } catch (Exception e) {
                log.error("Error while converting data from xml {}", e.getLocalizedMessage());
            }
        }

        return "Fetched News";
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
