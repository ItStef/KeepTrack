package com.KeepTrack.keeptrack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.KeepTrack.keeptrack.models.Task;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByName(String name);
}