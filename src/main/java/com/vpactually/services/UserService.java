package com.vpactually.services;

import com.vpactually.dto.users.UserCreateDTO;
import com.vpactually.dto.users.UserDTO;
import com.vpactually.dto.users.UserUpdateDTO;
import com.vpactually.entities.User;
import com.vpactually.mappers.UserMapper;
import com.vpactually.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper mapper;

    public List<UserDTO> findAll() {
        return repository.findAll().stream().map(mapper::map).toList();
    }

    public UserDTO findById(Long id) {
        return repository.findById(id).map(mapper::map).orElseThrow();
    }

    public UserDTO create(UserCreateDTO userCreateDTO) {
        var user = mapper.map(userCreateDTO);
        repository.save(user);
        return mapper.map(user);
    }

    public UserDTO update(Long id, UserUpdateDTO userUpdateDTO) {
        User userEntity = repository.findById(id).orElseThrow();
        mapper.update(userEntity, userUpdateDTO);
        var user = repository.save(userEntity);
        return mapper.map(user);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }


}
