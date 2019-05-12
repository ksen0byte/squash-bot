package com.edu.squashbot.telegram.repository;

import com.edu.squashbot.telegram.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Integer> {
}
