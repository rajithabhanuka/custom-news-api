package org.comppress.customnewsapi.service.category;

import org.comppress.customnewsapi.dto.ArticleDto;
import org.comppress.customnewsapi.dto.CategoryDto;
import org.comppress.customnewsapi.dto.GenericPage;
import org.comppress.customnewsapi.entity.Category;
import org.comppress.customnewsapi.mapper.MapstructMapper;
import org.comppress.customnewsapi.repository.CategoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MapstructMapper mapstructMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, MapstructMapper mapstructMapper) {
        this.categoryRepository = categoryRepository;
        this.mapstructMapper = mapstructMapper;
    }

    @Override
    public ResponseEntity<GenericPage<CategoryDto>> getCategories(String lang, int page, int size) {
        Page<Category> categoryPage = categoryRepository.findByLang(lang, PageRequest.of(page, size));

        GenericPage<CategoryDto> genericPage = new GenericPage<>();
        genericPage.setData(categoryPage.stream().map(category -> mapstructMapper.categoryToCategoryDto(category)).collect(Collectors.toList()));
        BeanUtils.copyProperties(categoryPage, genericPage);

        return ResponseEntity.status(HttpStatus.OK).body(genericPage);
    }
}
