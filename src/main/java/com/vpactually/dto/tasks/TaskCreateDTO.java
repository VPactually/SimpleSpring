package com.vpactually.dto.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateDTO {
    private String title;
    private String description;
    private String taskStatus;
    private Long assigneeId;
    private Set<Long> labelIds;
}
