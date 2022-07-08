package com.example.todo.controller;

import com.example.todo.entity.User;
import com.example.todo.exception.ApiExceptionHandler;
import com.example.todo.exception.ValidationException;
import com.example.todo.mappers.UserMapper;
import com.example.todo.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    UserController userController;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserServiceImpl userService;

    User returnUser;

    @BeforeEach
    void init() {
        returnUser = User.builder()
                .id(1L)
                .username("user")
                .email("email")
                .password("pass")
                .build();
    }

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void mockMvcCreated() {
        Assertions.assertNotNull(mockMvc);
    }

    @Test
    void getUsers() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/users");
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).findAll();
    }

    @Test
    void registration() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(returnUser));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(UserMapper.INSTANCE.toDTO(returnUser))))
                .andExpect(status().isCreated());

        verify(userService).save(any(User.class));
    }

    @Test
    void registration_validationException() throws Exception {
        when(userController.registration(any(User.class))).thenThrow(new ValidationException(""));

        RequestBuilder request = MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new User()));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUser() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/users/" + returnUser.getId());

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).findById(anyLong());
    }

    @Test
    void deleteUser() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .delete("/users/" + returnUser.getId());

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(returnUser.getId())))
                .andExpect(status().isNoContent());

        verify(userService).deleteById(anyLong());
    }

    @Test
    void updateUser() throws Exception {
        returnUser.setUsername("newName");

        RequestBuilder request = MockMvcRequestBuilders
                .put("/users/" + returnUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(returnUser));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(content()
                        .json(objectMapper
                                .writeValueAsString(UserMapper.INSTANCE.toDTO(returnUser))))
                .andExpect(status().isOk());

        verify(userService).update(anyLong(), any(User.class));
    }

    @Test
    void updateUser_validationException() throws Exception {
        when(userController.updateUser(anyLong(), any(User.class))).thenThrow(new ValidationException(""));

        RequestBuilder request = MockMvcRequestBuilders
                .put("/users/" + returnUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new User()));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}