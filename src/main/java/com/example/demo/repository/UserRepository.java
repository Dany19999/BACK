package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User;

// JpaRepository da métodos CRUD automáticos. <Entidad, TipoID>
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring genera la consulta: SELECT * FROM user WHERE username = ?
    Optional<User> findByUsername(String username);
}