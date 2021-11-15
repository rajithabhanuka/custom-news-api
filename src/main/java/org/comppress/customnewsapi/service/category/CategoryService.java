package org.comppress.customnewsapi.service.category;

import org.comppress.customnewsapi.dto.CategoryDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {

    ResponseEntity<List<CategoryDto>> getCategories();

}
