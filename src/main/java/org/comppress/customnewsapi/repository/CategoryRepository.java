package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.Category;
import org.comppress.customnewsapi.service.BaseSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long>, JpaSpecificationExecutor<Category> {
    Category findByName(String name);
    List<Category> findByLang(String lang);
}