package org.comppress.customnewsapi.service.article;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.ParsingFeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.comppress.customnewsapi.dto.ArticleDto;
import org.comppress.customnewsapi.dto.CustomRatedArticleDto;
import org.comppress.customnewsapi.dto.GenericPage;
import org.comppress.customnewsapi.entity.Article;
import org.comppress.customnewsapi.entity.Publisher;
import org.comppress.customnewsapi.entity.RssFeed;
import org.comppress.customnewsapi.repository.ArticleRepository;
import org.comppress.customnewsapi.repository.PublisherRepository;
import org.comppress.customnewsapi.repository.RssFeedRepository;
import org.comppress.customnewsapi.service.BaseSpecification;
import org.comppress.customnewsapi.utils.CustomStringUtils;
import org.comppress.customnewsapi.utils.DateUtils;
import org.comppress.customnewsapi.utils.PageHolderUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService, BaseSpecification {

    private final RssFeedRepository rssFeedRepository;
    private final ArticleRepository articleRepository;
    private final PublisherRepository publisherRepository;

    public ArticleServiceImpl(RssFeedRepository rssFeedRepository, ArticleRepository articleRepository, PublisherRepository publisherRepository) {
        this.rssFeedRepository = rssFeedRepository;
        this.articleRepository = articleRepository;
        this.publisherRepository = publisherRepository;
    }

    public void fetchArticlesFromRssFeeds() {

        for (RssFeed rssFeed : rssFeedRepository.findAll()) {
            SyndFeed feed;
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
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            log.info("Feed of size " + feed.getEntries().size());
            saveArticle(rssFeed, feed);
        }
    }

    private void saveArticle(RssFeed rssFeed, SyndFeed feed) {
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

    @Override
    @Async("ThreadPoolExecutor")
    public void update(Article article) throws URISyntaxException, IOException {
        try {
            String response = urlReader(article.getUrl());
            if(response.contains("\"isAccessibleForFree\":false") || response.contains("\"isAccessibleForFree\": false")){
                article.setAccessible(false);
            }else {
                article.setAccessible(true);
            }
            article.setAccessibleUpdated(true);
            articleRepository.save(article);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        if (syndEntry.getLink() != null) {
            article.setUrl(syndEntry.getLink());
        }
        if (syndEntry.getEnclosures() != null && !syndEntry.getEnclosures().isEmpty()) {
            article.setUrlToImage(syndEntry.getEnclosures().get(0).getUrl());
        }
        else {
            String imgUrl = null;
            if (syndEntry.getDescription() != null) {
                imgUrl = CustomStringUtils.getImgLinkFromTagSrc(syndEntry.getDescription().getValue(), "src=\"");
            }
            if(imgUrl == null && syndEntry.getContents() != null && syndEntry.getContents().size() > 0){
                imgUrl = CustomStringUtils.getImgLinkFromTagSrc(syndEntry.getContents().get(0).getValue(), "src=\"");
            }
            if(imgUrl == null && syndEntry.getContents() != null && syndEntry.getContents().size() > 0){
                imgUrl =  CustomStringUtils.getImgLinkFromTagSrc(syndEntry.getContents().get(0).getValue(), "url=\"");
            }
            if (imgUrl == null || imgUrl.isEmpty()) {
                // TODO Default Image
                article.setUrlToImage("No Image Found");
            }else {
                article.setUrlToImage(imgUrl);
            }
        }
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

    public ResponseEntity<GenericPage<ArticleDto>> getArticles(int page, int size, String title, String category, String publisherNewsPaper,String lang, String fromDate, String toDate) {

        Page<Article> articlesPage = articleRepository
                .retrieveByCategoryOrPublisherName(category,
                        publisherNewsPaper, title, lang,
                        DateUtils.stringToLocalDateTime(fromDate), DateUtils.stringToLocalDateTime(toDate),
                        PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));


        GenericPage<ArticleDto> genericPage = new GenericPage<>();
        genericPage.setData(articlesPage.stream().map(Article::toDto).collect(Collectors.toList()));
        BeanUtils.copyProperties(articlesPage, genericPage);

        return ResponseEntity.status(HttpStatus.OK).body(genericPage);

    }

    @Override
    public ResponseEntity<GenericPage> getRatedArticles(int page, int size, String title, String category, String publisherNewsPaper, String lang, String fromDate, String toDate) {
        List<ArticleRepository.CustomRatedArticle> customRatedArticleList = articleRepository.retrieveAllRatedArticlesInDescOrder(
                title, category, publisherNewsPaper, lang,
                DateUtils.stringToLocalDateTime(fromDate), DateUtils.stringToLocalDateTime(toDate));
        /* // TODO WHY CAN WE NOT USE THE COUNT QUERY HERE; SQL ERROR
        Page<ArticleRepository.CustomRatedArticle> customRatedArticlePage = articleRepository.retrieveAllRatedArticlesInDescOrder(
                title, category, publisherNewsPaper, lang,
                DateUtils.stringToLocalDateTime(fromDate), DateUtils.stringToLocalDateTime(toDate),PageRequest.of(page,size));
        getCustomRatedArticleDtoGenericPage(customRatedArticlePage)
        */
        List<CustomRatedArticleDto> customRatedArticleDtoList = new ArrayList<>();
        customRatedArticleList.forEach(customRatedArticle -> {
            CustomRatedArticleDto customRatedArticleDto = new CustomRatedArticleDto();
            BeanUtils.copyProperties(customRatedArticle,customRatedArticleDto);
            customRatedArticleDtoList.add(customRatedArticleDto);
        });
        return PageHolderUtils.getResponseEntityGenericPage(page,size,customRatedArticleDtoList);
    }

    @Override
    public ResponseEntity<GenericPage<ArticleDto>> getArticlesNotRated(int page, int size, Long categoryId, List<Long> listPublisherIds, String lang, String fromDate, String toDate) {
        if(listPublisherIds == null){
            listPublisherIds = publisherRepository.findAll().stream().map(Publisher::getId).collect(Collectors.toList());
        }
        Page<Article> articlesPage = articleRepository.retrieveUnratedArticlesByCategoryIdAndPublisherIdsAndLanguage(categoryId, listPublisherIds,lang,DateUtils.stringToLocalDateTime(fromDate),DateUtils.stringToLocalDateTime(fromDate), PageRequest.of(page, size));

        GenericPage<ArticleDto> genericPage = new GenericPage<>();
        genericPage.setData(articlesPage.stream().map(Article::toDto).collect(Collectors.toList()));
        BeanUtils.copyProperties(articlesPage, genericPage);

        return ResponseEntity.status(HttpStatus.OK).body(genericPage);

    }
}
