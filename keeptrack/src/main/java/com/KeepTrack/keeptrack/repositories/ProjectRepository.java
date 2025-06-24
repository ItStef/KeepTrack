package com.KeepTrack.keeptrack.repositories;
import com.KeepTrack.keeptrack.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository extends JpaRepository<Project, Long> {
}