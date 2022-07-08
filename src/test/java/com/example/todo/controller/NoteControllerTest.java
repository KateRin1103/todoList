package com.example.todo.controller;

import com.example.todo.entity.Note;
import com.example.todo.entity.User;
import com.example.todo.exception.ApiExceptionHandler;
import com.example.todo.exception.ValidationException;
import com.example.todo.service.impl.NoteServiceImpl;
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

import static java.time.LocalDate.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Mock
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
                .date(now())
                .done(false)
                .user(returnUser)
                .task("task")
                .build();
    }

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(noteController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void mockMvcCreated() {
        Assertions.assertNotNull(mockMvc);
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
                .andExpect(status().isCreated());
    }

    @Test
    void addNote_validationException() throws Exception {
        when(noteController.addNote(any(), anyLong())).thenThrow(new ValidationException(""));


        RequestBuilder request = MockMvcRequestBuilders
                .post("/notes/" + returnNote.getId());

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void setDone() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .put("/notes/{id}", returnNote.getId());

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        verify(noteService).setDone(anyLong());
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
    void getAllNotesByUserId() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/notes/" + returnUser.getId());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        verify(noteService).getNotesByUserId(anyLong());
    }

    @Test
    void updateNote() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .put("/notes/update/{id}", returnNote.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"task\" : \"newTask\"}");

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        verify(noteService).update(anyLong(), any(Note.class));
    }

    @Test
    void updateUser_validationException() throws Exception {
        when(noteController.updateNote(anyLong(), any(Note.class))).thenThrow(new ValidationException(""));

        RequestBuilder request = MockMvcRequestBuilders
                .put("/notes/update/" + returnNote.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("");

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}