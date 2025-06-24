package com.KeepTrack.keeptrack.controllers;

import com.KeepTrack.keeptrack.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.KeepTrack.keeptrack.dto.TaskCreateDTO;
import com.KeepTrack.keeptrack.models.Task;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody TaskCreateDTO taskDTO) {
        String result = taskService.createTask(
            taskDTO.getName(),
            taskDTO.getDescription(),
            taskDTO.getProjectId(),
            taskDTO.getTagIds()
        );
        return ResponseEntity.ok(result);
    }


    @PostMapping("/{taskId}/tags")
    public ResponseEntity<Task> addTagsToTask(@PathVariable Long taskId, @RequestBody List<Long> tagIds) {
        Task updatedTask = taskService.addTagsToTask(taskId, tagIds);
        return ResponseEntity.ok(updatedTask);
    }

    @PostMapping("/{taskId}/project/{projectId}")
    public ResponseEntity<Task> addTaskToProject(@PathVariable Long taskId, @PathVariable Long projectId) {
        Task updatedTask = taskService.addTaskToProject(taskId, projectId);
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Task task = taskService.getTask(id).orElseThrow();
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}