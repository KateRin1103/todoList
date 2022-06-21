package com.example.todo.service;

import com.example.todo.entity.User;
import com.example.todo.exception.ValidationException;

public interface UserService extends CrudService<User, Long>{
    User save(User object) throws ValidationException;
}
