package org.comppress.customnewsapi.service.article;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.ParsingFeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.comppress.customnewsapi.dto.ArticleDto;
import org.comppress.customnewsapi.dto.GenericPage;
import org.comppress.customnewsapi.dto.RatingSumDto;
import org.comppress.customnewsapi.dto.xml.ItemDto;
import org.comppress.customnewsapi.entity.Article;
import org.comppress.customnewsapi.entity.Rating;
import org.comppress.customnewsapi.entity.RssFeed;
import org.comppress.customnewsapi.mapper.MapstructMapper;
import org.comppress.customnewsapi.repository.ArticleRepository;
import org.comppress.customnewsapi.repository.PublisherRepository;
import org.comppress.customnewsapi.repository.RatingRepository;
import org.comppress.customnewsapi.repository.RssFeedRepository;
import org.comppress.customnewsapi.dto.xml.RssDto;
import org.comppress.customnewsapi.service.BaseSpecification;
import org.comppress.customnewsapi.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService, BaseSpecification {

    private final RssFeedRepository rssFeedRepository;
    private final MapstructMapper mapstructMapper;
    private final ArticleRepository articleRepository;
    private final RatingRepository ratingRepository;
    private final PublisherRepository publisherRepository;

    public ArticleServiceImpl(RssFeedRepository rssFeedRepository, MapstructMapper mapstructMapper, ArticleRepository articleRepository, RatingRepository ratingRepository, PublisherRepository publisherRepository) {
        this.rssFeedRepository = rssFeedRepository;
        this.mapstructMapper = mapstructMapper;
        this.articleRepository = articleRepository;
        this.ratingRepository = ratingRepository;
        this.publisherRepository = publisherRepository;
    }

    public void fetchArticlesWithRome() {
        for (RssFeed rssFeed : rssFeedRepository.findAll()) {
            SyndFeed feed = null;
            try {
                URL feedSource = new URL(rssFeed.getUrl());
                SyndFeedInput input = new SyndFeedInput();
                feed = input.build(new XmlReader(feedSource));
                log.info("Fetching News from " + rssFeed.getUrl());
            } catch (ParsingFeedException e) {
                log.error("Feed can not be parsed, please recheck the url " + rssFeed.getUrl());
                continue;
            } catch (FileNotFoundException e) {
                log.error("FileNotFoundException most likely a dead link, check the url " + rssFeed.getUrl());
                continue;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                continue;
            } catch (FeedException e) {
                e.printStackTrace();
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }


            log.info("Feed of size " + feed.getEntries().size());
            for (SyndEntry syndEntry : feed.getEntries()) {
                Article article = customMappingSyndEntryImplToArticle(syndEntry, rssFeed);
                if (articleRepository.findByGuid(article.getGuid()).isPresent()) continue;
                try {
                    articleRepository.save(article);
                } catch (DataIntegrityViolationException e) {
                    log.error("Duplicate Record found while saving data {}", e.getLocalizedMessage());
                } catch (Exception e) {
                    log.error("Error while saving data {}", e.getLocalizedMessage());
                }
            }
        }
    }

    public Article customMappingSyndEntryImplToArticle(SyndEntry syndEntry, RssFeed rssFeed) {
        Article article = new Article();
        if (syndEntry.getAuthor() != null) {
            article.setAuthor(syndEntry.getAuthor());
        }
        if (syndEntry.getTitle() != null) {
            article.setTitle(syndEntry.getTitle());
        }
        if (syndEntry.getDescription() != null) {
            article.setDescription(syndEntry.getDescription().getValue());
        }
        if (syndEntry.getLink() != null) {
            article.setUrl(syndEntry.getLink());
        }
        if (syndEntry.getEnclosures() != null && !syndEntry.getEnclosures().isEmpty()) {
            article.setUrlToImage(syndEntry.getEnclosures().get(0).getUrl());
        } else {
            // User Dom Parser to check for the Image
            article.setUrlToImage("No Image Found");
        }

        // Sometimes the url, sometimes guid provided by the news agencies
        if (syndEntry.getUri() != null) {
            article.setGuid(syndEntry.getUri());
        }
        if (syndEntry.getPublishedDate() != null) {
            article.setPublishedAt(syndEntry.getPublishedDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
        }
        if (syndEntry.getContents() != null && !syndEntry.getContents().isEmpty()) {
            article.setContent(syndEntry.getContents().get(0).getValue());
        }
        article.setRssFeedId(rssFeed.getId());
        return article;
    }

    public void fetchArticles() {

        List<String> listWhereExceptionsHappen = new ArrayList<>();

        for (RssFeed rssFeed : rssFeedRepository.findAll()) {

            RssDto rssDto = null;
            String webPage;
            try {
                webPage = urlReader(rssFeed.getUrl());
                XmlMapper xmlMapper = new XmlMapper();
                xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                xmlMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                rssDto = xmlMapper.readValue(webPage, RssDto.class);
            } catch (Exception e) {
                listWhereExceptionsHappen.add(rssFeed.getUrl());
                log.error("Error while converting data from xml {}", e.getLocalizedMessage());
                e.printStackTrace();
            }
            if (rssDto == null) {
                log.error("rssDto is null for Rss Feed " + rssFeed.getUrl());
                continue;
            }
            List<ItemDto> itemDtoList = rssDto.getChannel().getItem();
            if (itemDtoList == null) {
                log.error("itemDtoList is null for Rss Feed " + rssFeed.getUrl());
                continue;
            }
            // Only want a map of guid
            List<Article> articlesInDatabase = articleRepository.findAll();
            Map<String, String> mapGuid = new HashMap<>();
            /*
            // not scalable, but a good idea :)
            articlesInDatabase.forEach(article -> {
                mapGuid.put(article.getGuid(), "exists");
            });
             */
            itemDtoList.forEach(itemDto -> {
                Article article = mapstructMapper.itemDtoToArticle(itemDto);
                article.setRssFeedId(rssFeed.getId());
                try {
                    articleRepository.save(article);
                } catch (DataIntegrityViolationException e) {
                    mapGuid.put(article.getGuid(), "duplicate");
                    log.error("Duplicate Record found while saving data {}", e.getLocalizedMessage());
                } catch (Exception e) {
                    log.error("Error while saving data {}", e.getLocalizedMessage());
                }
            });
        }
        log.info("Exception happened in " + listWhereExceptionsHappen + " urls");
    }

    private String urlReader(String url) throws URISyntaxException, IOException, InterruptedException {
        log.info("Send GET request to " + url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Accept-Encoding", "identity")
                .GET()
                .build();

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        var response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public ResponseEntity<GenericPage> getArticles(int page, int size, String title, String category, String publisherNewsPaper,String lang, String fromDate, String toDate) {

        Page<Article> articlesPage = articleRepository
                .retrieveByCategoryOrPublisherName(category,
                        publisherNewsPaper, title, lang,
                        DateUtils.stringToLocalDateTime(fromDate), DateUtils.stringToLocalDateTime(toDate),
                        PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));


        GenericPage<ArticleDto> genericPage = new GenericPage<>();
        genericPage.setData(articlesPage.stream().map(s -> s.toDto()).collect(Collectors.toList()));
        BeanUtils.copyProperties(articlesPage, genericPage);

        return ResponseEntity.status(HttpStatus.OK).body(genericPage);

    }

    @Override
    public ResponseEntity<GenericPage> getRatedArticles(int page, int size, String title, String category, String publisherNewsPaper,String lang, String fromDate, String toDate) {

        List<ArticleRepository.CustomRatedArticle> customRatedArticleList = articleRepository.retrieveAllRatedArticlesInDescOrder(
                title, category, publisherNewsPaper, lang,
                DateUtils.stringToLocalDateTime(fromDate), DateUtils.stringToLocalDateTime(toDate));

        PagedListHolder pagedListHolder = new PagedListHolder(customRatedArticleList);
        pagedListHolder.setPageSize(size);  // number of items per page
        pagedListHolder.setPage(page);      // set to first page

        return ResponseEntity.status(HttpStatus.OK).body(
                GenericPage.builder()
                        .totalPages(pagedListHolder.getPageCount())
                        .totalElements(pagedListHolder.getSource().size())
                        .isFirst(pagedListHolder.isFirstPage())
                        .isLast(pagedListHolder.isLastPage())
                        .pageNumber(pagedListHolder.getPage())
                        .pageNumberOfElements(pagedListHolder.getPageSize())
                        .data(pagedListHolder.getPageList())
                        .build()
        );
    }

    @Override
    public ResponseEntity<GenericPage> getArticlesNotRated(int page, int size, Long categoryId, List<Long> listPublisherIds, String lang, String fromDate, String toDate) {
        if(listPublisherIds == null){
            listPublisherIds = publisherRepository.findAll().stream().map(p -> p.getId()).collect(Collectors.toList());
        }
        Page<Article> articlesPage = articleRepository.retrieveUnratedArticlesByCategoryIdAndPublisherIdsAndLanguage(categoryId, listPublisherIds,lang,DateUtils.stringToLocalDateTime(fromDate),DateUtils.stringToLocalDateTime(fromDate), PageRequest.of(page, size));

        GenericPage<ArticleDto> genericPage = new GenericPage<>();
        genericPage.setData(articlesPage.stream().map(s -> s.toDto()).collect(Collectors.toList()));
        BeanUtils.copyProperties(articlesPage, genericPage);

        return ResponseEntity.status(HttpStatus.OK).body(genericPage);

    }
}
