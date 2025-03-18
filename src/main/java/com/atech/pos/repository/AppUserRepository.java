package com.atech.pos.repository;

import com.atech.pos.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface AppUserRepository extends MongoRepository<AppUser, String> {

    Optional<AppUser> findAppUserByUsername(String username);

    boolean existsAppUsersByUsername(String username);

    @Query("{ '$or' : [ { 'first_name' : { $regex: ?0, $options: 'i' } },  " +
                       "{ 'last_name' : { $regex: ?0, $options: 'i' } } ] }")
    Page<AppUser> findAllAppUsersFiltered(Pageable pageable, String filterText);
}
