package com.vpactually.services;

import com.vpactually.dto.labels.LabelCreateDTO;
import com.vpactually.dto.labels.LabelDTO;
import com.vpactually.dto.labels.LabelUpdateDTO;
import com.vpactually.mappers.LabelMapper;
import com.vpactually.repositories.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LabelService {

    @Autowired
    private LabelRepository repository;

    @Autowired
    private LabelMapper mapper;

    public List<LabelDTO> findAll() {
        return repository.findAll().stream().map(mapper::map).toList();
    }

    public LabelDTO findById(Long id) {
        return mapper.map(repository.findById(id).orElseThrow());
    }

    public LabelDTO create(LabelCreateDTO labelCreateDTO) {
        var label = mapper.map(labelCreateDTO);
        label = repository.save(label);
        return mapper.map(label);
    }

    public LabelDTO update(Long id, LabelUpdateDTO labelUpdateDTO) {
        var label = repository.findById(id).orElseThrow();
        mapper.update(label, labelUpdateDTO);
        repository.save(label);
        return mapper.map(label);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
