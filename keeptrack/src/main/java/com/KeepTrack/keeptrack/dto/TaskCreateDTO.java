package com.KeepTrack.keeptrack.dto;

import lombok.Data;
import java.util.Set;

@Data
public class TaskCreateDTO {
    private String name;
    private String description;
    private Long projectId; 
    private Set<Long> tagIds; 
}