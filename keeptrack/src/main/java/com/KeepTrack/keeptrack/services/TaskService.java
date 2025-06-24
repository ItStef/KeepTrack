package com.KeepTrack.keeptrack.services;
import java.util.HashSet;

import org.springframework.stereotype.Service;

import com.KeepTrack.keeptrack.models.Project;
import com.KeepTrack.keeptrack.models.Task;
import com.KeepTrack.keeptrack.repositories.ProjectRepository;
import com.KeepTrack.keeptrack.repositories.TagRepository;
import com.KeepTrack.keeptrack.repositories.TaskRepository;
import com.KeepTrack.keeptrack.models.Tag;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TagRepository tagRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, TagRepository tagRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.tagRepository = tagRepository;
        this.projectRepository = projectRepository;
    }

    public String createTask(String name, String description, Long projectId, Set<Long> tagIds) {
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);

        if (projectId != null) {
            projectRepository.findById(projectId).ifPresent(project -> task.setProject(project));
        }

        if (tagIds != null && !tagIds.isEmpty()) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(tagIds));
            task.setTags(tags);
        }

        taskRepository.save(task);
        return "Task created successfully";
    }

    public Task addTagsToTask(Long taskId, List<Long> tagIds) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(tagIds));
        
        task.getTags().addAll(tags);
        return taskRepository.save(task);
    }

    public Task addTaskToProject(Long taskId, Long projectId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        Project project = projectRepository.findById(projectId).orElseThrow();

        task.setProject(project);
        project.getTasks().add(task);
        projectRepository.save(project);
        return taskRepository.save(task);
    }

    public Optional<Task> getTask(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}