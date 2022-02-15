package org.comppress.customnewsapi.controller;

import org.comppress.customnewsapi.dto.CategoryDto;
import org.comppress.customnewsapi.dto.GenericPage;
import org.comppress.customnewsapi.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<GenericPage<CategoryDto>> getCategories(
            @RequestParam(value = "lang", required = false, defaultValue = "en") String lang,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    ){
        return categoryService.getCategories(lang, page, size);
    }

    @GetMapping("/user")
    public ResponseEntity<GenericPage> getCategoriesUser(
            @RequestParam(value = "lang", required = false, defaultValue = "en") String lang,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    ){
        return categoryService.getCategoriesUser(lang, page, size);
    }

}
