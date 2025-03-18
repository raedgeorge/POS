package com.atech.pos.repository;

import com.atech.pos.entity.Role;
import com.atech.pos.entity.RoleType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RolesRepository extends MongoRepository<Role, String> {

    Role findRoleByRoleType(RoleType roleType);
}
