package com.example.todo.service;

import com.example.todo.entity.Note;
import com.example.todo.repository.NoteRepository;
import com.example.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    NoteRepository noteRepository;

    public Note addNote(Note note, Long userId) {
        note.setUser(userRepository.findById(userId).get());
        note.setDone(false);
        return noteRepository.save(note);
    }

    public Note findById(Long id) {
        return noteRepository.findById(id).get();
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

    public Note updateNote(Long id, Note note){
        Note update = noteRepository.findById(id).get();
        update.setTask(note.getTask());
        return noteRepository.save(update);
    }
}
