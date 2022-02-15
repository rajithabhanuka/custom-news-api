package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.Criteria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CriteriaRepository extends JpaRepository<Criteria, Long> {
    boolean existsById(Long id);
}
