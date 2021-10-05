package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SourceRepository extends JpaRepository<Source,Long> {
}
