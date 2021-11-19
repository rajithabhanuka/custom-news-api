package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long>, JpaSpecificationExecutor<Category> {
    Category findByName(String name);
    Page<Category> findByLang(String lang, Pageable pageable);
    List<Category> findByLang(String lang);
}