package com.example.todo.service.impl;

import com.example.todo.entity.User;
import com.example.todo.exception.NotFoundException;
import com.example.todo.exception.ValidationException;
import com.example.todo.repository.UserRepository;
import com.example.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User save(User user) throws ValidationException {
        validation(user);
        return userRepository.save(user);
    }

    @Transactional
    public User update(Long id, User user) throws NotFoundException, ValidationException {
        User update = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + id));
        validation(user);
        update.setEmail(user.getEmail());
        update.setUsername(user.getUsername());
        update.setPassword(user.getPassword());
        return userRepository.save(update);
    }

    public Long deleteById(Long id) throws NotFoundException {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + id));
        userRepository.deleteById(id);
        return id;
    }

    public User findById(Long id) throws NotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + id));
    }

    public User findByUsername(String username) throws NotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Not found user with username = " + username));
    }

    public User findByEmail(String email) throws NotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Not found user with email = " + email));
    }

    public Boolean isPresent(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public void validation(User user) throws ValidationException {
        if (isNull(user.getUsername())) {
            throw new ValidationException("Username can't be empty");
        } else if (isNull(user.getPassword())) {
            throw new ValidationException("Password can't be empty");
        } else if (isNull(user.getEmail())) {
            throw new ValidationException("Email can't be empty");
        }
    }
}
