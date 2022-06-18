package com.example.todo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NoteDTO {

    private Long id;

    private String task;

    @JsonIgnore
    private UserDTO user;

    private Boolean done;

    private LocalDate date;
}
