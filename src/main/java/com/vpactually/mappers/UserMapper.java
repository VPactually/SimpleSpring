package com.vpactually.mappers;

import com.vpactually.dto.users.UserCreateDTO;
import com.vpactually.dto.users.UserDTO;
import com.vpactually.dto.users.UserUpdateDTO;
import com.vpactually.entities.User;
import org.mapstruct.*;

@Mapper(
        uses = JsonNullableMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {

    public abstract User map(UserCreateDTO userDTO);

    public abstract UserDTO map(User user);

    public abstract void update(@MappingTarget User user, UserUpdateDTO userUpdateDTO);
}
