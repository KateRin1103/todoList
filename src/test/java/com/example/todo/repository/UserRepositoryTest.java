package com.example.todo.repository;

import com.example.todo.TodoAppApplication;
import com.example.todo.config.ContainersEnvironment;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SqlGroup({
        @Sql(scripts = "/schema.sql",
                config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED),
                executionPhase = BEFORE_TEST_METHOD),
        @Sql("/data.sql"),
        @Sql(scripts = "/schema-delete.sql",
                executionPhase = AFTER_TEST_METHOD)})
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
@SpringBootTest(classes = TodoAppApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class UserRepositoryTest extends ContainersEnvironment {

    @Autowired
    private UserRepository userRepository;

    @Test
    void test1_findAll() {
        assertEquals(3, userRepository.findAll().size());
    }

    @Test
    void test2_findByUsername() throws Exception {
        String name = "name";
        assertNotNull(userRepository.findByUsername(name));
        assertEquals(name, userRepository.findByUsername(name)
                .orElseThrow(Exception::new).getUsername());
    }

    @Test
    void test3_findByUsername_notFound() {
        String name = "";
        assertEquals(Optional.empty(), userRepository.findByUsername(name));
    }

    @Test
    void test4_findByEmail() throws Exception {
        String mail = "mail";
        assertNotNull(userRepository.findByEmail(mail));
        assertEquals(mail, userRepository.findByEmail(mail)
                .orElseThrow(Exception::new).getEmail());
    }

    @Test
    void test5_findByEmail_notFound() {
        String mail = "";
        assertEquals(Optional.empty(), userRepository.findByEmail(mail));
    }

    @Test
    void test6_findById() throws Exception {
        Long id = 1L;
        assertNotNull(userRepository.findById(id));
        assertEquals(id, userRepository.findById(id)
                .orElseThrow(Exception::new).getId());
    }

    @Test
    void test7_findById_notFound() {
        Long id = 0L;
        assertEquals(Optional.empty(), userRepository.findById(id));
    }

    @Test
    void test8_deleteById() {
        Long id = 1L;
        assertTrue(userRepository.findById(id).isPresent());
        userRepository.deleteById(id);
        assertFalse(userRepository.findById(id).isPresent());
    }
}