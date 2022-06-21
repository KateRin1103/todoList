package com.example.todo.service.impl;

import com.example.todo.entity.Note;
import com.example.todo.exception.NotFoundException;
import com.example.todo.exception.ValidationException;
import com.example.todo.repository.NoteRepository;
import com.example.todo.repository.UserRepository;
import com.example.todo.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class NoteServiceImpl implements NoteService {

    private final UserRepository userRepository;

    private final NoteRepository noteRepository;

    @Autowired
    public NoteServiceImpl(UserRepository userRepository, NoteRepository noteRepository) {
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
    }

    public Note save(Note note, Long userId) throws ValidationException, NotFoundException {
        if (isNull(note.getTask())) throw new ValidationException("Task can't be null");
        return noteRepository.save(new Note(note.getTask(),
                userRepository.findById(userId)
                        .orElseThrow(() -> new NotFoundException("Can't find user with id=" + userId)),
                false,
                LocalDate.now()));
    }

    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    public Note findById(Long id) throws NotFoundException {
        return noteRepository.findById(id).orElseThrow(() -> new NotFoundException("Note not found with id = " + id));
    }

    public List<Note> getNotesByUserId(Long id) throws NotFoundException {
        if (noteRepository.findByUser_IdOrderByDate(id).isEmpty())
            throw new NotFoundException("Can't find notes for user with id = " + id);
        return noteRepository.findByUser_IdOrderByDate(id);
    }

    public List<Note> getDone(Long userId) {
        return noteRepository.findByUser_IdAndDoneIsTrue(userId);
    }

    public List<Note> getNotDone(Long userId) {
        return noteRepository.findByUser_IdAndDoneIsFalse(userId);
    }

    public Long deleteById(Long id) throws NotFoundException {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find note with id = " + id));
        noteRepository.deleteById(id);
        return id;
    }

    public Note setDone(Long id) throws NotFoundException {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find note with id = " + id));
        note.setDone(true);
        return noteRepository.save(note);
    }

    public Note update(Long id, Note note) throws NotFoundException, ValidationException {
        Note update = noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find note with id = " + id));
        if (isNull(note.getTask())) throw new ValidationException("Task can't be null");
        update.setTask(note.getTask());
        return noteRepository.save(update);
    }
}
