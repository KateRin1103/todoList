package com.example.todo.repository;

import com.example.todo.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    void deleteById(Long id);

    List<User> findAll();

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
