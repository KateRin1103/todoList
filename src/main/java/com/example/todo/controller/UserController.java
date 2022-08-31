package com.example.todo.controller;

import com.example.todo.dto.UserDTO;
import com.example.todo.entity.User;
import com.example.todo.exception.NotFoundException;
import com.example.todo.exception.ValidationException;
import com.example.todo.mappers.UserMapper;
import com.example.todo.service.impl.UserServiceImpl;
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

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;

    private final KafkaTemplate<Long, User> kafkaTemplate;

    @Autowired
    public UserController(UserServiceImpl userService, KafkaTemplate<Long, User> kafkaTemplate) {
        this.userService = userService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        return new ResponseEntity<List<UserDTO>>(UserMapper.INSTANCE.toDTO(userService.findAll()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDTO> registration(@RequestBody User user) throws ValidationException {
        userService.save(user);
        sendKafkaMsg(user);
        return new ResponseEntity<UserDTO>(UserMapper.INSTANCE.toDTO(user), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<UserDTO>(UserMapper.INSTANCE.toDTO(userService.findById(id)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteUser(@PathVariable Long id) throws NotFoundException {
        userService.deleteById(id);
        return new ResponseEntity<Long>(id, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
                                              @RequestBody User user) throws NotFoundException, ValidationException {
        userService.update(id, user);
        sendKafkaMsg(user);
        return new ResponseEntity<UserDTO>(UserMapper.INSTANCE.toDTO(user), HttpStatus.OK);
    }

    private void sendKafkaMsg(User user) {
        ListenableFuture<SendResult<Long, User>> feature = kafkaTemplate.send("msg", 1L, user);
        feature.addCallback(System.out::println, System.err::println);
        kafkaTemplate.flush();
    }
}
