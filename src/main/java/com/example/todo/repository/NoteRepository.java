package com.example.todo.repository;

import com.example.todo.entity.Note;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface NoteRepository extends CrudRepository<Note,Long> {

    @Transactional
    void deleteById(Long id);

    Optional<Note> findById(Long id);

    List<Note> findByUser_IdOrderByDate(Long id);

    List<Note> findByUser_IdAndDoneIsFalse(Long id);

    List<Note> findByUser_IdAndDoneIsTrue(Long id);
}
