package org.comppress.customnewsapi.controller;

import org.comppress.customnewsapi.dto.CustomRatedArticleDto;
import org.comppress.customnewsapi.dto.GenericPage;
import org.comppress.customnewsapi.dto.UserPreferenceDto;
import org.comppress.customnewsapi.entity.Article;
import org.comppress.customnewsapi.repository.ArticleRepository;
import org.comppress.customnewsapi.service.home.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/home")
public class HomeController {

    private final HomeService homeService;
    private final ArticleRepository articleRepository;

    @Autowired
    public HomeController(HomeService homeService, ArticleRepository articleRepository) {
        this.homeService = homeService;
        this.articleRepository = articleRepository;
    }

    @GetMapping
    public ResponseEntity<UserPreferenceDto> getUserPreference(@RequestParam(defaultValue = "en") String lang,
                                                               @RequestParam(value = "categoryId", required = false) Long categoryId,
                                                               @RequestParam(value = "publisherIds", required = false) List<Long> publisherIds,
                                                               @RequestParam(value = "fromDate", required = false) String fromDate,
                                                               @RequestParam(value = "toDate", required = false) String toDate

    ){
        return homeService.getUserPreference(lang,categoryId,publisherIds,fromDate,toDate);
    }

    @GetMapping("/category")
    public ResponseEntity<GenericPage<CustomRatedArticleDto>> getArticleForCategory(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "categoryIds", required = false) List<Long> categoryIds,
            @RequestParam(value = "publisherIds", required = false) List<Long> publisherIds,
            @RequestParam(value = "lang", required = false, defaultValue = "en") String lang,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate
    ){
        return homeService.getArticleForCategory(page,size,categoryIds,publisherIds,lang,fromDate,toDate);
    }

    @GetMapping("/test")
    public void testDb(){
        List<Long> idList = new ArrayList<>();
        idList.add(1L);
        idList = null;
        List<Article> articleList = articleRepository.customTestQuery(null);
        for(Article article:articleList){
            System.out.println(article.getId());
        }
    }

}
