package org.comppress.customnewsapi.controller;

import org.comppress.customnewsapi.dto.UserPreferenceDto;
import org.comppress.customnewsapi.entity.Category;
import org.comppress.customnewsapi.entity.UserEntity;
import org.comppress.customnewsapi.repository.ArticleRepository;
import org.comppress.customnewsapi.repository.CategoryRepository;
import org.comppress.customnewsapi.repository.UserRepository;
import org.comppress.customnewsapi.service.BaseSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HomeServiceImpl implements HomeService, BaseSpecification {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public HomeServiceImpl(ArticleRepository articleRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<UserPreferenceDto> getUserPreference() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userRepository.findByUsername(authentication.getName());

        UserPreferenceDto userPreferenceDto = new UserPreferenceDto();
        // TODO If null take all
        // TODO Add Pagination
        Specification<Category> categorySpecification = querySpecificationIn(
                Stream.of(userEntity.getListCategoryIds().split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList()),
                "id");
        Specification<Category> spec = Specification.where(categorySpecification);
        categoryRepository.findAll(spec);

        return null;
    }
}
