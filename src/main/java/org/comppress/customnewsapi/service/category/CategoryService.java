package org.comppress.customnewsapi.service.category;

import org.comppress.customnewsapi.dto.CategoryDto;
import org.comppress.customnewsapi.dto.CategoryUserDto;
import org.comppress.customnewsapi.dto.GenericPage;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {

    ResponseEntity<GenericPage<CategoryDto>> getCategories(String lang, int page, int size);

    ResponseEntity<GenericPage<CategoryUserDto>> getCategoriesUser(String lang, int page, int size);

}
