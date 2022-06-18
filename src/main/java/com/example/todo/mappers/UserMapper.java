package com.example.todo.mappers;

import com.example.todo.dto.UserDTO;
import com.example.todo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends MapperTemplate<UserDTO, User>{

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDTO(User user);

    List<UserDTO> toDTO(List<User> users);

    User toModel(UserDTO obj);

    List<User> toModel(List<UserDTO> obj);
}
