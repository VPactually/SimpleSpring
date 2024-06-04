package com.vpactually.mappers;

import com.vpactually.dto.tasks.TaskCreateDTO;
import com.vpactually.dto.tasks.TaskDTO;
import com.vpactually.dto.tasks.TaskUpdateDTO;
import com.vpactually.entities.Label;
import com.vpactually.entities.Task;
import com.vpactually.entities.TaskStatus;
import com.vpactually.entities.User;
import com.vpactually.repositories.TaskStatusRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;


@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Mapping(source = "assigneeId", target = "assignee", qualifiedByName = "assigneeIdToUser")
    @Mapping(source = "taskStatus", target = "taskStatus", qualifiedByName = "slugToTaskStatus")
    @Mapping(source = "labelIds", target = "labels", qualifiedByName = "labelIdsToLabels")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "taskStatus", source = "taskStatus.slug")
    @Mapping(target = "labelIds", source = "labels", qualifiedByName = "labelsToLabelsIds")
    public abstract TaskDTO map(Task model);

    @Mapping(source = "assigneeId", target = "assignee", qualifiedByName = "assigneeIdToUser")
    @Mapping(source = "taskStatus", target = "taskStatus", qualifiedByName = "slugToTaskStatus")
    @Mapping(source = "labelIds", target = "labels", qualifiedByName = "labelIdsToLabels")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);

    @Named("slugToTaskStatus")
    public TaskStatus slugToTaskStatus(String slug) {
        return taskStatusRepository.findBySlug(slug).orElseThrow();
    }

    @Named("labelIdsToLabels")
    public Set<Label> labelIdToLabel(Set<Long> labelsIds) {
        return labelsIds == null ? null : labelsIds.stream().map(Label::new).collect(Collectors.toSet());
    }

    @Named("assigneeIdToUser")
    public User assigneeIdToUser(Long id) {
        return id == null ? null : new User(id);
    }

    @Named("labelsToLabelsIds")
    public Set<Long> labelsToLabelsIds(Set<Label> labels) {
        return labels == null ? null : labels.stream().map(Label::getId).collect(Collectors.toSet());
    }
}
