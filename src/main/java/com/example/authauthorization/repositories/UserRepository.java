package com.example.authauthorization.repositories;

import com.example.authauthorization.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    Optional<User> findByEmailOrUsername(String userNameOrEmail, String userNameOrEmail1);

    Optional<User>  findByUsername(String username);
}
