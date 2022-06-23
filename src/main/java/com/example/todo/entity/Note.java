package com.example.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity
@Table(name = "notes")
public class Note implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    private Long id;

    @Column(name = "task")
    private String task;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "done")
    private Boolean done;

    @Column(name = "date")
    private LocalDate date;

    public Note(String task, User user, Boolean done, LocalDate date) {
        this.task = task;
        this.user = user;
        this.done = done;
        this.date = date;
    }
}
