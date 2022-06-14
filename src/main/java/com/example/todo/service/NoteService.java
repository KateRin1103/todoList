package com.example.todo.service;

import com.example.todo.entity.Note;
import com.example.todo.repository.NoteRepository;
import com.example.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NoteService {

    private final UserRepository userRepository;

    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(UserRepository userRepository, NoteRepository noteRepository) {
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
    }

    public Note addNote(Note note, Long userId) {
        return noteRepository.save(Note
                .builder()
                .done(false)
                .date(LocalDate.now())
                .task(note.getTask())
                .user(userRepository.findById(userId).orElseThrow())
                .build());
    }

    public Note findById(Long id) {
        return noteRepository.findById(id).orElseThrow();
    }

    public List<Note> getNotesByUserId(Long id) {
        return noteRepository.findByUser_IdOrderByDate(id);
    }

    public List<Note> getDone(Long userId) {
        return noteRepository.findByUser_IdAndDoneIsTrue(userId);
    }

    public List<Note> getNotDone(Long userId) {
        return noteRepository.findByUser_IdAndDoneIsFalse(userId);
    }

    public Long deleteNote(Long id) {
        noteRepository.deleteById(id);
        return id;
    }

    public Note setDone(Long id) {
        Note note = noteRepository.findById(id).get();
        note.setDone(true);
        return noteRepository.save(note);
    }

    public Note updateNote(Long id, Note note) {
        Note update = noteRepository.findById(id).get();
        update.setTask(note.getTask());
        return noteRepository.save(update);
    }
}
