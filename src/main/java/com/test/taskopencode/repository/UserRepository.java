package com.test.taskopencode.repository;

import com.test.taskopencode.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface
UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
