package com.smartnews.backend.repositories;

import com.smartnews.backend.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}