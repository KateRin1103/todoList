package com.example.todo.controller;

import com.example.todo.entity.Note;
import com.example.todo.entity.User;
import com.example.todo.mappers.NoteMapper;
import com.example.todo.mappers.UserMapper;
import com.example.todo.service.impl.NoteServiceImpl;
import com.example.todo.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class NoteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    NoteController noteController;

    @MockBean
    NoteServiceImpl noteService;

    @MockBean
    UserServiceImpl userService;

    User returnUser;
    Note returnNote;

    @BeforeEach
    void init() {
        returnUser = User.builder()
                .id(1L)
                .username("user")
                .email("email")
                .password("pass")
                .build();
        returnNote = Note.builder()
                .id(1L)
                /*.date(now())
                .done(false)
                .user(returnUser)*/
                .task("task")
                .build();

    }



    @Test
    void addNote() throws Exception {
        when(noteService.save(any(), anyLong())).thenReturn(returnNote);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/notes/" + returnUser.getId())
                .contentType(MediaType.TEXT_HTML)
                .content(returnNote.getTask());

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void setDone() {
    }

    @Test
    void deleteNote() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .delete("/notes/" + returnNote.getId());

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNoContent()).andReturn();
    }

    @Test
    void getAllNotesByUserId() {
    }

    @Test
    void updateNote() {
    }
}