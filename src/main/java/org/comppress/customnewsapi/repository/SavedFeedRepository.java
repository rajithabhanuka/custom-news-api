package org.comppress.customnewsapi.repository;

import org.comppress.customnewsapi.entity.SavedFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SavedFeedRepository extends JpaRepository<SavedFeed,Long> {

    Optional<SavedFeed> findByTopByOrderByIdDesc();

}
