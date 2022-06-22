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
import java.util.stream.Collectors;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Note noteSaved = noteService.save(returnNote, userService.findById(user.getId()).getId());

        verify(userRepository, times(2)).findById(anyLong());
        verify(noteRepository).save(any());
    }

    @Test
    void save_notFoundException() throws ValidationException, NotFoundException {
        assertThrows(NotFoundException.class,
                () -> noteService.save(returnNote, userService.findById(null).getId()));
    }

    @Test
    void save_validationException() throws ValidationException, NotFoundException {
        returnNote.setTask(null);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        assertThrows(ValidationException.class,
                () -> noteService.save(returnNote, userService.findById(user.getId()).getId()));
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
        Note note = noteService.findById(returnNote.getId());

        assertNotNull(note);
        assertEquals(returnNote.getId(), note.getId());
        verify(noteRepository).findById(anyLong());
    }

    @Test
    void findById_notFoundException() throws NotFoundException {
        assertThrows(NotFoundException.class,
                () -> noteService.findById(null));
    }

    @Test
    void getNotesByUserId() throws NotFoundException {
        List<Note> returnList = new ArrayList<>();
        returnList.add(Note.builder().id(1L).user(user).build());
        returnList.add(Note.builder().id(2L).user(user).build());

        when(noteRepository.findByUser_IdOrderByDate(anyLong()))
                .thenReturn(returnList.stream()
                        .filter(e -> e.getId() == 1L)
                        .collect(Collectors.toList()));
        List<Note> notes = noteService.getNotesByUserId(1L);

        assertEquals(1, notes.size());
    }

    @Test
    void getDone() {
        List<Note> returnList = new ArrayList<>();
        returnList.add(Note.builder().id(1L).user(user).done(false).build());
        returnList.add(Note.builder().id(2L).user(user).done(true).build());
        returnList.add(Note.builder().id(1L).user(user).done(true).build());

        when(noteRepository.findByUser_IdAndDoneIsTrue(anyLong()))
                .thenReturn(returnList.stream()
                        .filter(Note::getDone)
                        .filter(e -> e.getId() == 1L)
                        .collect(Collectors.toList()));
        List<Note> notes = noteService.getDone(1L);

        assertEquals(1, notes.size());
    }

    @Test
    void getNotDone() {
        List<Note> returnList = new ArrayList<>();
        returnList.add(Note.builder().id(1L).user(user).done(false).build());
        returnList.add(Note.builder().id(2L).user(user).done(true).build());
        returnList.add(Note.builder().id(1L).user(user).done(true).build());

        when(noteRepository.findByUser_IdAndDoneIsFalse(anyLong()))
                .thenReturn(returnList.stream()
                        .filter(e-> !e.getDone())
                        .filter(e -> e.getId() == 1L)
                        .collect(Collectors.toList()));
        List<Note> notes = noteService.getNotDone(1L);

        assertEquals(1, notes.size());
    }

    @Test
    void deleteById() throws NotFoundException {
        Note note = returnNote;

        when(noteRepository.findById(note.getId())).thenReturn(Optional.of(note));
        noteService.deleteById(note.getId());

        verify(noteRepository).deleteById(note.getId());
    }

    @Test
    void deleteById_notFoundException() throws NotFoundException {
        assertThrows(NotFoundException.class,
                () -> noteService.deleteById(null));
    }

    @Test
    void setDone() throws NotFoundException {
        given(noteRepository.findById(returnNote.getId()))
                .willReturn(Optional.of(returnNote));
        noteService.setDone(returnNote.getId());

        assertTrue(returnNote.getDone());
    }

    @Test
    void setDone_notFoundException() throws NotFoundException {
        assertThrows(NotFoundException.class,
                () -> noteService.setDone(null));
    }

    @Test
    void update() throws ValidationException, NotFoundException {
        Note newNote = returnNote;
        newNote.setTask("new task");

        given(noteRepository.findById(returnNote.getId()))
                .willReturn(Optional.of(returnNote));
        noteService.update(returnNote.getId(), newNote);

        verify(noteRepository).findById(anyLong());
        verify(noteRepository).save(any());
    }

    @Test
    void update_validationException() throws ValidationException, NotFoundException {
        Note newNote = returnNote;
        newNote.setTask(null);

        given(noteRepository.findById(returnNote.getId()))
                .willReturn(Optional.of(returnNote));

        assertThrows(ValidationException.class, () ->
                noteService.update(returnNote.getId(), newNote));
        verify(noteRepository).findById(anyLong());
    }

    @Test
    void update_notFoundException() throws ValidationException, NotFoundException {
        assertThrows(NotFoundException.class,
                () -> noteService.update(null, new Note()));
    }
}