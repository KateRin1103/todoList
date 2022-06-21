package com.example.todo.service;

import com.example.todo.entity.Note;
import com.example.todo.exception.ValidationException;

public interface NoteService extends CrudService<Note, Long>{
    Note save(Note object, Long id) throws ValidationException;
}
