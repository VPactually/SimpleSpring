package com.vpactually.mappers;

import com.vpactually.dto.taskStatuses.TaskStatusCreateDTO;
import com.vpactually.dto.taskStatuses.TaskStatusDTO;
import com.vpactually.dto.taskStatuses.TaskStatusUpdateDTO;
import com.vpactually.entities.TaskStatus;
import org.mapstruct.*;

@Mapper(
        uses = JsonNullableMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskStatusMapper {

    public abstract TaskStatus map(TaskStatusCreateDTO taskStatusCreateDTO);

    public abstract TaskStatusDTO map(TaskStatus taskStatus);

    public abstract void update(@MappingTarget TaskStatus taskStatus, TaskStatusUpdateDTO taskStatusUpdateDTO);
}
