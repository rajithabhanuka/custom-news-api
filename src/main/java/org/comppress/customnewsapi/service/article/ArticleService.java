package org.comppress.customnewsapi.service.article;

import org.comppress.customnewsapi.dto.ArticleDto;
import org.comppress.customnewsapi.dto.GenericPage;
import org.comppress.customnewsapi.entity.Article;
import org.comppress.customnewsapi.utils.GenerateGenericPageUtils;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface ArticleService extends GenerateGenericPageUtils {

    ResponseEntity<GenericPage<ArticleDto>> getArticles(int page, int size,
                                                        String title, String category,
                                                        String publisherNewsPaper, String lang,
                                                        String fromDate, String toDate);

    void fetchArticlesFromRssFeeds();

    void update(Article article) throws URISyntaxException, IOException;

    ResponseEntity<GenericPage> getRatedArticles(int page, int size,
                                                                        String title, String category,
                                                                        String publisherNewsPaper, String lang,
                                                                        String fromDate, String toDate);

    ResponseEntity<GenericPage<ArticleDto>> getArticlesNotRated(int page, int size, Long category,
                                                                List<Long> listPublisherIds, String lang,
                                                                String fromDate, String toDate);
}
