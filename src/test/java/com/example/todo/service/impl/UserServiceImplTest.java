package com.example.todo.service.impl;

import com.example.todo.entity.User;
import com.example.todo.exception.NotFoundException;
import com.example.todo.exception.ValidationException;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    User returnUser;

    @BeforeEach
    void setUp() {
        returnUser = User.builder()
                .id(1L)
                .username("user")
                .email("email")
                .password("pass")
                .build();
    }

    @Test
    void findAll() {
        List<User> returnList = new ArrayList<>();
        returnList.add(User.builder().id(1L).password("").username("").email("").build());
        returnList.add(User.builder().id(2L).password("").username("").email("").build());

        when(userRepository.findAll()).thenReturn(returnList);
        List<User> users = userService.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    void save() throws ValidationException {
        when(userRepository.save(any())).thenReturn(returnUser);
        User userSaved = userService.save(returnUser);

        assertNotNull(userSaved);
        assertEquals(returnUser.getId(), userSaved.getId());
        verify(userRepository).save(any());
    }

    @Test
    void save_validationException() throws ValidationException {
        User user = returnUser;
        user.setUsername(null);

        assertThrows(ValidationException.class, () -> userService.save(user));
    }

    @Test
    void update() throws ValidationException, NotFoundException {
        User user = returnUser;
        User newUser = returnUser;
        newUser.setUsername("newName");

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        userService.update(user.getId(), newUser);

        verify(userRepository).findById(user.getId());
        verify(userRepository).save(user);
    }

    @Test
    void deleteById_ifFound() throws NotFoundException {
        User user = returnUser;

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        userService.deleteById(user.getId());

        verify(userRepository).deleteById(user.getId());
    }

    @Test
    public void deleteById_ifNotFound() throws NotFoundException {
        User user = returnUser;

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.deleteById(user.getId()));
    }

    @Test
    void findById_ifFound() throws NotFoundException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(returnUser));
        User user = userService.findById(1L);

        assertNotNull(user);
        assertEquals(1, user.getId());
        verify(userRepository).findById(anyLong());
    }

    @Test
    void findById_ifNotFound() throws NotFoundException {
        User user = returnUser;

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findById(user.getId()));
    }

    @Test
    void findByUsername_ifFound() throws NotFoundException {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(returnUser));
        User user = userService.findByUsername(returnUser.getUsername());

        assertNotNull(user);
        assertEquals(returnUser.getUsername(), user.getUsername());
        verify(userRepository).findByUsername(any());
    }

    @Test
    void findByUsername_ifNotFound() throws NotFoundException {
        assertThrows(NotFoundException.class, () -> userService.findByUsername(null));
        verify(userRepository).findByUsername(any());
    }

    @Test
    void findByEmail_ifFound() throws NotFoundException {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(returnUser));
        User user = userService.findByEmail(returnUser.getEmail());

        assertNotNull(user);
        assertEquals(returnUser.getEmail(), user.getEmail());
        verify(userRepository).findByEmail(any());
    }

    @Test
    void findByEmail_ifNotFound() throws NotFoundException {
        assertThrows(NotFoundException.class, () -> userService.findByEmail(null));
        verify(userRepository).findByEmail(any());
    }

    @Test
    void isPresent() throws NotFoundException {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(returnUser));
        Boolean user = userService.isPresent(returnUser.getUsername());

        verify(userRepository).findByUsername(any());
    }
}