package com.example.todo.repository;

import com.example.todo.TodoAppApplication;
import com.example.todo.config.ContainersEnvironment;
import com.example.todo.entity.Note;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest(classes = TodoAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class NoteRepositoryTest extends ContainersEnvironment {

    @Autowired
    NoteRepository noteRepository;

    @Test
    @SqlGroup({
            @Sql(scripts = "/schema.sql",
                    config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)),
            @Sql("/data.sql")})
    void test1_findAll() {
        assertEquals(4, noteRepository.findAll().size());
    }

    @Test
    void test2_findById() throws Exception {
        Long id = 1L;
        assertNotNull(noteRepository.findById(id));
        assertEquals(id, noteRepository.findById(id)
                .orElseThrow(Exception::new).getId());
    }

    @Test
    void test3_findByUser_IdOrderByDate() {
        Long id = 1L;

        List<Note> notes = noteRepository.findByUser_IdOrderByDate(id);

        assertNotNull(notes);
        assertEquals(3, notes.stream()
                .filter(i -> i.getUser().getId().equals(id))
                .count());

        Note prev = null;
        for (Note note : notes) {
            if (prev != null)
                assertThat(note.compareTo(prev)).isIn(1, 0);
            prev = note;
        }
    }

    @Test
    void test4_findByUser_IdAndDoneIsFalse() {
        Long id = 1L;
        assertNotNull(noteRepository.findByUser_IdAndDoneIsFalse(id));
        assertEquals(2, noteRepository.findByUser_IdAndDoneIsFalse(id)
                .stream()
                .filter(i -> !i.getDone())
                .count());
    }

    @Test
    void test5_findByUser_IdAndDoneIsTrue() {
        Long id = 1L;
        assertNotNull(noteRepository.findByUser_IdAndDoneIsTrue(id));
        assertEquals(1, noteRepository.findByUser_IdAndDoneIsTrue(id)
                .stream()
                .filter(Note::getDone)
                .count());
    }

    @Test
    void test6_findById_notFound() throws Exception {
        Long id = 0L;
        assertEquals(Optional.empty(), noteRepository.findById(id));
    }

    @Test
    void test7_deleteById() {
        Long id = 1L;
        assertTrue(noteRepository.findById(id).isPresent());
        noteRepository.deleteById(id);
        assertFalse(noteRepository.findById(id).isPresent());
    }
}