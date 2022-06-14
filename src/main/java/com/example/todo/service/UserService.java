package com.example.todo.service;

import com.example.todo.entity.User;
import com.example.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User user) {
        User update = userRepository.findById(id).get();
        update.setEmail(user.getEmail());
        update.setUsername(user.getUsername());
        update.setPassword(user.getPassword());
        return userRepository.save(update);
    }

    public Long deleteUser(Long id) {
        userRepository.deleteById(id);
        return id;
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    public Boolean isPresent(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
