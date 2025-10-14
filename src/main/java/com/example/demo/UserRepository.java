package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository da métodos CRUD automáticos. <Entidad, TipoID>
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring genera la consulta: SELECT * FROM user WHERE username = ?
    Optional<User> findByUsername(String username);
}
