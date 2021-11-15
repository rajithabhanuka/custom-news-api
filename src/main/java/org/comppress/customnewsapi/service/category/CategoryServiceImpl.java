package org.comppress.customnewsapi.service.category;

import org.comppress.customnewsapi.dto.CategoryDto;
import org.comppress.customnewsapi.entity.Category;
import org.comppress.customnewsapi.mapper.MapstructMapper;
import org.comppress.customnewsapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        List<CategoryDto> categoryDtoList = categoryList.stream().map(category -> mapstructMapper.categoryToCategoryDto(category)).collect(Collectors.toList());
        return ResponseEntity.ok().body(categoryDtoList);
    }
}
