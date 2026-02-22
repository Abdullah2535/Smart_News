package com.smartnews.backend.repositories;

import com.smartnews.backend.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    boolean existsByUserName(String email);

    User findUserById(Integer id);
    Optional<User> findByUserName(String userName);
}