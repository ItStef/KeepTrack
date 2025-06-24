package com.KeepTrack.keeptrack.services;
import org.springframework.stereotype.Service;

import com.KeepTrack.keeptrack.models.Project;
import java.util.Optional;
import com.KeepTrack.keeptrack.models.Task;
import com.KeepTrack.keeptrack.repositories.ProjectRepository;
import com.KeepTrack.keeptrack.repositories.TaskRepository;
import java.util.List;

@Service
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Optional<Project> getProject(Long id) {
        return projectRepository.findById(id);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project addTaskToProject(Long projectId, Long taskId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Task task = taskRepository.findById(taskId).orElseThrow();

        if (task.getProject() != null) {
            throw new IllegalArgumentException("Task is already assigned to a project..");
        }
        
        task.setProject(project);
        taskRepository.save(task);
        project.getTasks().add(task);
        return projectRepository.save(project);
    }

    public Project removeTaskFromProject(Long projectId, Long taskId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Task task = taskRepository.findById(taskId).orElseThrow();

        project.getTasks().remove(task);
        task.setProject(null);
        taskRepository.save(task);
        return projectRepository.save(project);
    }
}