package org.comppress.customnewsapi.service.home;

import lombok.extern.slf4j.Slf4j;
import org.comppress.customnewsapi.dto.*;
import org.comppress.customnewsapi.entity.Category;
import org.comppress.customnewsapi.entity.UserEntity;
import org.comppress.customnewsapi.repository.*;
import org.comppress.customnewsapi.service.BaseSpecification;
import org.comppress.customnewsapi.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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

    @Override
    public ResponseEntity<UserPreferenceDto> getUserPreference(String lang, Long categoryId,
                                                               List<Long> publisherIds, String fromDate, String toDate) {

        // TODO Ignore Article for Category that have an Empty thing
        // TODO Possibility to pass categoryIds list for User with GUID Auth

        // Get User via Security Context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepository.findByUsername(authentication.getName());
        // TODO Add Pagination Categories
        Specification<Category> categorySpecification = null;
        if (userEntity.getListCategoryIds() != null) {
            categorySpecification = querySpecificationIn(
                    Stream.of(userEntity.getListCategoryIds().split(","))
                            .map(Integer::parseInt)
                            .collect(Collectors.toList()), "id");
        }
        Specification<Category> categorySpecification1 = querySpecificationLike(lang,"lang");
        Specification<Category> spec = Specification.where(categorySpecification).and(categorySpecification1);
        // Add Lang here

        List<CustomCategoryDto> customCategoryDtos = categoryRepository.findAll(spec).stream().map(s -> setArticles(s, lang,
                publisherIds, DateUtils.stringToLocalDateTime(fromDate), DateUtils.stringToLocalDateTime(toDate))).collect(Collectors.toList());
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
        // TODO Do we need the language here? As we filter with pub and category before
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
        List<ArticleRepository.CustomRatedArticle> list = new ArrayList<>();
        list.add(article);
        customCategoryDto.setArticleDtoList(list);
        BeanUtils.copyProperties(category, customCategoryDto);
        // TODO return Name Lang, Problem Name is saved at CategoryTranslation, would require another db query, or load initially a Table of Category Translations once and pass it to the Method
        return customCategoryDto;
    }
}
