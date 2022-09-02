package com.example.todo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import javax.persistence.Id;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class NoteDTO {

    private Long id;

    private String task;

    @JsonIgnore
    private UserDTO user;

    private Boolean done;

    private LocalDate date;
}
