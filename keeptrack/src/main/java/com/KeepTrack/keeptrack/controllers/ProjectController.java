package com.KeepTrack.keeptrack.controllers;

import com.KeepTrack.keeptrack.models.Project;
import com.KeepTrack.keeptrack.services.ProjectService;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    @PostMapping("/{projectId}/tasks/{taskId}")
    public Project addTaskToProject(@PathVariable Long projectId, @PathVariable Long taskId) {
        return projectService.addTaskToProject(projectId, taskId);
    }

    @DeleteMapping("/{projectId}/tasks/{taskId}")
    public Project removeTaskFromProject(@PathVariable Long projectId, @PathVariable Long taskId) {
        return projectService.removeTaskFromProject(projectId, taskId);
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }
}