package com.github.noteitdown.auth.repository;

import com.github.noteitdown.auth.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Created by cenkakin
 */
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

}
