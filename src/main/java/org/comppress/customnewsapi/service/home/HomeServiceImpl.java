package org.comppress.customnewsapi.service.home;

import lombok.extern.slf4j.Slf4j;
import org.comppress.customnewsapi.dto.*;
import org.comppress.customnewsapi.entity.Category;
import org.comppress.customnewsapi.entity.Publisher;
import org.comppress.customnewsapi.entity.UserEntity;
import org.comppress.customnewsapi.repository.*;
import org.comppress.customnewsapi.service.BaseSpecification;
import org.comppress.customnewsapi.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class HomeServiceImpl implements HomeService, BaseSpecification {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PublisherRepository publisherRepository;

    @Autowired
    public HomeServiceImpl(ArticleRepository articleRepository, CategoryRepository categoryRepository, UserRepository userRepository, PublisherRepository publisherRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.publisherRepository = publisherRepository;
    }

    public List<Long> getPublisher(List<Long> publisherIds, String lang){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepository.findByUsername(authentication.getName());

        if(userEntity != null){
            if(userEntity.getListPublisherIds() != null){
                publisherIds = Stream.of(userEntity.getListPublisherIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
            }
        }
        if (publisherIds == null || publisherIds.isEmpty() || doesNotContainAnyPublishersFromLang(publisherIds,lang)) {
            publisherIds = publisherRepository.findByLang(lang).stream().map(publisher -> publisher.getId()).collect(Collectors.toList());
        }
        return publisherIds;
    }

    public List<Long> getCategory(List<Long> categoryIds, String lang){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepository.findByUsername(authentication.getName());

        if(userEntity != null){
            if(userEntity.getListCategoryIds() != null){
                categoryIds = Stream.of(userEntity.getListCategoryIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
            }
        }

        if (categoryIds == null || categoryIds.isEmpty() || doesNotContainAnyCategoriesFromLang(categoryIds,lang)) {
            categoryIds = categoryRepository.findByLang(lang).stream().map(category -> category.getId()).collect(Collectors.toList());
        }
        return categoryIds;
    }

    @Override
    public ResponseEntity<UserPreferenceDto> getUserPreference(String lang, List<Long> categoryIds,
                                                               List<Long> publisherIds, String fromDate, String toDate) {

        final List<Long> finalPubIds = getPublisher(publisherIds, lang);
        categoryIds = getCategory(categoryIds,lang);
        // Get User via Security Context

        List<CustomCategoryDto> customCategoryDtos = categoryRepository.findByCategoryIds(categoryIds).stream().map(s -> setArticles(s, lang,
                finalPubIds, DateUtils.stringToLocalDateTime(fromDate), DateUtils.stringToLocalDateTime(toDate))).collect(Collectors.toList());
        UserPreferenceDto userPreferenceDto = new UserPreferenceDto();
        userPreferenceDto.setCategoryDtoList(customCategoryDtos);
        return ResponseEntity.ok().body(userPreferenceDto);
    }

    @Override
    public ResponseEntity<GenericPage<CustomRatedArticleDto>> getArticleForCategory(int page, int size, List<Long> categoryIds, List<Long> publisherIds, String lang, String fromDate, String toDate) {
        // TODO Interceptor Checking for Either JWT or GUID
        if (categoryIds == null || categoryIds.isEmpty()) {
            categoryIds = categoryRepository.findByLang(lang, PageRequest.of(page, size)).stream().map(category -> category.getId()).collect(Collectors.toList());
        }
        if (publisherIds == null || publisherIds.isEmpty()) {
            publisherIds = publisherRepository.findByLang(lang).stream().map(publisher -> publisher.getId()).collect(Collectors.toList());
        }

        Page<ArticleRepository.CustomRatedArticle> articlesPage = articleRepository.retrieveArticlesByCategoryIdsAndPublisherIdsAndLanguage(
                categoryIds, publisherIds, lang, DateUtils.stringToLocalDateTime(fromDate), DateUtils.stringToLocalDateTime(toDate), PageRequest.of(page, size)
        );
        GenericPage<CustomRatedArticleDto> genericPage = new GenericPage<>();
        List<CustomRatedArticleDto> customRatedArticleDtos = new ArrayList<>();
        for(ArticleRepository.CustomRatedArticle customRatedArticle: articlesPage.toList()){
            CustomRatedArticleDto customRatedArticleDto = new CustomRatedArticleDto();
            BeanUtils.copyProperties(customRatedArticle,customRatedArticleDto);
            customRatedArticleDtos.add(customRatedArticleDto);
        }
        genericPage.setData(customRatedArticleDtos);
        BeanUtils.copyProperties(articlesPage, genericPage);

        return ResponseEntity.ok().body(genericPage);
    }

    private CustomCategoryDto setArticles(Category category, String lang,
                                          List<Long> publisherIds, LocalDateTime fromDate, LocalDateTime toDate) {
        // TODO Limit 1, Publishers included, Rated
        if (publisherIds == null || publisherIds.isEmpty()) {
            publisherIds = publisherRepository.findByLang(lang).stream().map(publisher -> publisher.getId()).collect(Collectors.toList());
        }
        Long categoryId = category.getId();
        ArticleRepository.CustomRatedArticle article = articleRepository.retrieveArticlesByCategoryIdsAndPublisherIdsAndLanguageAndLimit(categoryId,publisherIds,lang,fromDate,toDate);
        CustomCategoryDto customCategoryDto = new CustomCategoryDto();
        if(article != null){
            CustomRatedArticleDto customRatedArticleDto = new CustomRatedArticleDto();
            BeanUtils.copyProperties(article,customRatedArticleDto);
            customCategoryDto.setArticle(customRatedArticleDto);
        }else {
            customCategoryDto.setArticle(null);
        }
        BeanUtils.copyProperties(category, customCategoryDto);
        return customCategoryDto;
    }

    private boolean doesNotContainAnyCategoriesFromLang(List<Long> listCategoryIds, String lang) {
        for(Category category:categoryRepository.findByLang(lang)){
            if(listCategoryIds.contains(category.getId())) return false;
        }
        return true;
    }

    private boolean doesNotContainAnyPublishersFromLang(List<Long> listPublisherIds, String lang) {
        for (Publisher publisher : publisherRepository.findByLang(lang)) {
            if (listPublisherIds.contains(publisher.getId())) return false;
        }
        return true;
    }

}
