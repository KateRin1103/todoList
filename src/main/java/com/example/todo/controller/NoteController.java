package com.example.todo.controller;

import com.example.todo.entity.Note;
import com.example.todo.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<Void> addNote(@RequestBody Note note,
                                  @RequestParam Long userId){
        noteService.addNote(note, userId);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> setDone(@RequestParam Long id){
        noteService.setDone(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<Note>> getAllNotesByUserId(@RequestParam Long id) {
        return new ResponseEntity<List<Note>>(noteService.getNotesByUserId(id), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateNote(@RequestParam Long id,
                                           @RequestBody Note note){
       noteService.updateNote(id, note);
       return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
