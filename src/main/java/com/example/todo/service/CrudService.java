package com.example.todo.service;

import com.example.todo.exception.NotFoundException;
import com.example.todo.exception.ValidationException;

import java.util.List;

public interface CrudService<T, ID> {
    List<T> findAll();

    T update(ID id, T object) throws NotFoundException, ValidationException;

    T findById(ID id) throws NotFoundException;

    ID deleteById(ID id)  throws NotFoundException;
}
