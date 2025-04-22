package com.atech.pos.service;

import com.atech.pos.dtos.RoleDto;
import com.atech.pos.dtos.RoleUpsertDto;

import java.util.List;

public interface RolesService {

    List<RoleDto> getAllRoles();

    String addNewRole(RoleUpsertDto roleUpsertDto);

    void updateRole(RoleUpsertDto roleUpsertDto);

    boolean deleteRole(String roleId);
}
