package com.KeepTrack.keeptrack.repositories;

import com.KeepTrack.keeptrack.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
    Tag findById(long id);
}