package com.example.todo.service.impl;

import com.example.todo.entity.Note;
import com.example.todo.entity.User;
import com.example.todo.exception.NotFoundException;
import com.example.todo.exception.ValidationException;
import com.example.todo.repository.NoteRepository;
import com.example.todo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoteServiceImplTest {

    @Mock
    NoteRepository noteRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    NoteServiceImpl noteService;

    @InjectMocks
    UserServiceImpl userService;

    Note returnNote;
    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .password("")
                .username("")
                .email("")
                .build();
        returnNote = Note.builder()
                .id(1L)
                .task("task")
                .date(now())
                .done(false)
                .user(user)
                .build();

    }

    @Test
    void save() throws ValidationException, NotFoundException {
        /*Note noteToSave = returnNote;
        when(userRepository.save(any())).thenReturn(user);
        User userSaved = userService.save(user);
        when(noteRepository.save(any())).thenReturn(returnNote);
        Note noteSaved = noteService.save(noteToSave, userSaved.getId());
        assertNotNull(noteSaved);
        verify(noteRepository).save(noteToSave);*/
    }

    @Test
    void findAll() {
        List<Note> returnList = new ArrayList<>();
        returnList.add(Note.builder().id(1L).user(user).build());
        returnList.add(Note.builder().id(2L).user(user).build());
        when(noteRepository.findAll()).thenReturn(returnList);
        List<Note> notes = noteService.findAll();
        assertNotNull(notes);
        assertEquals(2, notes.size());
    }

    @Test
    void findById() throws NotFoundException {
        when(noteRepository.findById(anyLong())).thenReturn(Optional.of(returnNote));
        Note note = noteService.findById(1L);
        assertNotNull(note);
        assertEquals(1, note.getId());
        verify(noteRepository).findById(anyLong());
    }

    @Test
    void getNotesByUserId() {
    }

    @Test
    void getDone() {
    }

    @Test
    void getNotDone() {
    }

    @Test
    void deleteById() throws NotFoundException {
        Note note = returnNote;
        when(noteRepository.findById(note.getId())).thenReturn(Optional.of(note));
        noteService.deleteById(note.getId());
        verify(noteRepository).deleteById(note.getId());
    }

    @Test
    void setDone() {
    }

    @Test
    void update() {
    }
}