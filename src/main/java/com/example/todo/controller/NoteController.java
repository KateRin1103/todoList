package com.example.todo.controller;

import com.example.todo.dto.NoteDTO;
import com.example.todo.entity.Note;
import com.example.todo.exception.NotFoundException;
import com.example.todo.exception.ValidationException;
import com.example.todo.mappers.NoteMapper;
import com.example.todo.service.impl.NoteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteServiceImpl noteService;

    @Autowired
    public NoteController(NoteServiceImpl noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<Void> addNote(@RequestBody Note note,
                                        @RequestParam Long userId) throws ValidationException {
        noteService.save(note, userId);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> setDone(@RequestParam Long id) throws NotFoundException {
        noteService.setDone(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) throws NotFoundException {
        noteService.deleteById(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<NoteDTO>> getAllNotesByUserId(@RequestParam Long id) throws NotFoundException {
        return new ResponseEntity<List<NoteDTO>>(NoteMapper.INSTANCE.toDTO(noteService.getNotesByUserId(id)), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateNote(@RequestParam Long id,
                                           @RequestBody Note note) throws NotFoundException, ValidationException {
        noteService.update(id, note);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
