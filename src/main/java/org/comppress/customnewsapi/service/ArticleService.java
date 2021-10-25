package org.comppress.customnewsapi.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.comppress.customnewsapi.dto.ArticleDto;
import org.comppress.customnewsapi.dto.GenericPage;
import org.comppress.customnewsapi.dto.ItemDto;
import org.comppress.customnewsapi.entity.Article;
import org.comppress.customnewsapi.entity.RssFeed;
import org.comppress.customnewsapi.mapper.MapstructMapper;
import org.comppress.customnewsapi.repository.ArticleRepository;
import org.comppress.customnewsapi.repository.RssFeedRepository;
import org.comppress.customnewsapi.dto.RssDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleService implements BaseSpecification {

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
                        log.error("Duplicate Record found while saving data {}" , e.getLocalizedMessage());
                    } catch (Exception e) {
                        log.error("Error while saving data {}" , e.getLocalizedMessage());
                    }
                });
            } catch (Exception e) {
                log.error("Error while converting data from xml {}" , e.getLocalizedMessage());
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

    public ResponseEntity<GenericPage> getArticles(int page, int size,
                                                   String title, String category,
                                                   String publisherNewsPaper,
                                                   String fromDate, String toDate) {

        /*
        // Building query according to the arguments
        Specification<Article> spec1 = querySpecificationLike(title, "title");
        Specification<Article> spec2 = querySpecificationLike(category, "description");
        Specification<Article> spec3 = querySpecificationLike(publisherNewsPaper, "author");
        Specification<Article> spec4 = querySpecificationGreaterThanOrEqual(fromDate, "publishedAt");
        Specification<Article> spec5 = querySpecificationLessThanOrEqual(toDate, "publishedAt");

        // Concat each specification
        Specification<Article> spec = Specification.where(spec1).or(spec2).or(spec3).and(spec4).and(spec5);
*/

        LocalDateTime dateTime1 = null;
        LocalDateTime dateTime2 = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (fromDate != null){
            dateTime1 = LocalDateTime.parse(fromDate, formatter);
        }

        if (toDate != null){
            dateTime2 = LocalDateTime.parse(toDate, formatter);
        }


        Page<Article> articlesPage = articleRepository
                .retrieveByCategoryOrPublisherName(category,
                        publisherNewsPaper, title,
                        dateTime1, dateTime2,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));

//        Page<Article> articlesPage = articleRepository
//                .findAll(spec, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));

        GenericPage<ArticleDto> genericPage = new GenericPage<>();
        genericPage.setData(articlesPage.stream().map(s -> s.toDto()).collect(Collectors.toList()));
        BeanUtils.copyProperties(articlesPage, genericPage);

        return ResponseEntity.status(HttpStatus.OK).body(genericPage);
    }


}
