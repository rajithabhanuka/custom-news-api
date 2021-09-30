package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher,Long> {
}
