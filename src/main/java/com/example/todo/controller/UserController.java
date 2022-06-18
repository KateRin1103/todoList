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

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        return new ResponseEntity<List<UserDTO>>(UserMapper.INSTANCE.toDTO(userService.findAll()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> registration(@RequestBody User user) throws ValidationException {
        userService.save(user);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<UserDTO>(UserMapper.INSTANCE.toDTO(userService.findById(id)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws NotFoundException {
        userService.deleteById(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id,
                                           @RequestBody User user) throws NotFoundException, ValidationException {
        userService.update(id, user);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
