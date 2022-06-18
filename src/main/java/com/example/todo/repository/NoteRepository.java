package com.example.todo.repository;

import com.example.todo.entity.Note;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends CrudRepository<Note,Long> {

    void deleteById(Long id);

    List<Note> findAll();

    Optional<Note> findById(Long id);

    List<Note> findByUser_IdOrderByDate(Long id);

    List<Note> findByUser_IdAndDoneIsFalse(Long id);

    List<Note> findByUser_IdAndDoneIsTrue(Long id);
}
