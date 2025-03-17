package com.atech.pos.repository;

import com.atech.pos.entity.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AppUserRepository extends MongoRepository<AppUser, String> {

    Optional<AppUser> findAppUserByUsername(String username);

    boolean existsAppUsersByUsername(String username);
}
