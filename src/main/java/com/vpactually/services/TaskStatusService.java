package com.vpactually.services;

import com.vpactually.dto.taskStatuses.TaskStatusCreateDTO;
import com.vpactually.dto.taskStatuses.TaskStatusDTO;
import com.vpactually.dto.taskStatuses.TaskStatusUpdateDTO;
import com.vpactually.mappers.TaskStatusMapper;
import com.vpactually.repositories.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskStatusService {

    @Autowired
    private TaskStatusRepository repository;

    @Autowired
    private TaskStatusMapper mapper;

    public List<TaskStatusDTO> findAll() {
        return repository.findAll().stream().map(mapper::map).toList();
    }

    public TaskStatusDTO findById(Long id) {
        return mapper.map(repository.findById(id).orElseThrow());
    }

    public TaskStatusDTO create(TaskStatusCreateDTO taskStatusCreateDTO) {
        var taskStatus = mapper.map(taskStatusCreateDTO);
        taskStatus = repository.save(taskStatus);
        return mapper.map(taskStatus);
    }

    public TaskStatusDTO update(Long id, TaskStatusUpdateDTO taskStatusUpdateDTO) {
        var taskStatus = repository.findById(id).orElseThrow();
        mapper.update(taskStatus, taskStatusUpdateDTO);
        repository.save(taskStatus);
        return mapper.map(taskStatus);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
