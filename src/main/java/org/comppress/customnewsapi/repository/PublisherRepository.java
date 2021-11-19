package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher,Long> {

    Boolean existsByName(String name);
    Publisher findByName(String name);
    List<Publisher> findByLang(String lang);
    Page<Publisher> findByLang(String lang, Pageable pageable);

}
