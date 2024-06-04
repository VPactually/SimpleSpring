package com.vpactually.dto.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateDTO {
    private JsonNullable<String> title;
    private JsonNullable<String> description;
    private JsonNullable<String> taskStatus;
    private JsonNullable<Long> assigneeId;
    private JsonNullable<Set<Long>> labelIds;

    public TaskUpdateDTO(String name, String description, String taskStatus, Long assigneeId, Set<Long> labelIds) {
        this.title = JsonNullable.of(name);
        this.description = JsonNullable.of(description);
        this.taskStatus = JsonNullable.of(taskStatus);
        this.assigneeId = JsonNullable.of(assigneeId);
        this.labelIds = JsonNullable.of(labelIds);
    }
}