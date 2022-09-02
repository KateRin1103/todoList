package com.example.todo.controller;

import com.example.todo.dto.NoteDTO;
import com.example.todo.entity.Note;
import com.example.todo.entity.User;
import com.example.todo.exception.NotFoundException;
import com.example.todo.exception.ValidationException;
import com.example.todo.mappers.NoteMapper;
import com.example.todo.service.impl.NoteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
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

    private final KafkaTemplate<Long, Note> kafkaTemplate;

    @Autowired
    public NoteController(NoteServiceImpl noteService, KafkaTemplate<Long, Note> kafkaTemplate) {
        this.noteService = noteService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<NoteDTO> addNote(@RequestBody String note,
                                           @PathVariable Long userId) throws ValidationException, NotFoundException {
        Note newNote = Note.builder().task(note).build();
        noteService.save(newNote, userId);
        sendKafkaMsg(newNote);
        return new ResponseEntity<NoteDTO>(NoteMapper.INSTANCE.toDTO(newNote), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> setDone(@PathVariable Long id) throws NotFoundException {
        noteService.setDone(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteNote(@PathVariable Long id) throws NotFoundException {
        noteService.deleteById(id);
        return new ResponseEntity<Long>(id, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<NoteDTO>> getAllNotesByUserId(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<List<NoteDTO>>(NoteMapper.INSTANCE.toDTO(noteService.getNotesByUserId(id)), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateNote(@PathVariable Long id,
                                           @RequestBody Note note) throws NotFoundException, ValidationException {
        noteService.update(id, note);
        sendKafkaMsg(note);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    private void sendKafkaMsg(Note note) {
        ListenableFuture<SendResult<Long, Note>> feature = kafkaTemplate.send("msg", 1L, note);
        feature.addCallback(System.out::println, System.err::println);
        kafkaTemplate.flush();
    }

}
