package com.vpactually.services;

import com.vpactually.dto.tasks.TaskCreateDTO;
import com.vpactually.dto.tasks.TaskDTO;
import com.vpactually.dto.tasks.TaskUpdateDTO;
import com.vpactually.mappers.TaskMapper;
import com.vpactually.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskMapper mapper;

    public List<TaskDTO> findAll() {
        return repository.findAll().stream().map(mapper::map).toList();
    }

    public TaskDTO create(TaskCreateDTO taskCreateDTO) {

        var task = mapper.map(taskCreateDTO);
        repository.save(task);
        return mapper.map(task);
    }

    public TaskDTO findById(Long id) {
        return mapper.map(repository.findById(id)
                .orElseThrow());
    }

    public TaskDTO update(TaskUpdateDTO taskUpdateDTO, Long id) {
        var task = repository.findById(id)
                .orElseThrow();
        mapper.update(taskUpdateDTO, task);
        repository.save(task);
        return mapper.map(task);

    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}