package com.atech.pos.service.impl;

import com.atech.pos.dtos.RoleDto;
import com.atech.pos.dtos.RoleUpsertDto;
import com.atech.pos.entity.Permissions;
import com.atech.pos.entity.Role;
import com.atech.pos.repository.RolesRepository;
import com.atech.pos.service.RolesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolesServiceImpl implements RolesService {

    private final RolesRepository rolesRepository;

    @Override
    public List<RoleDto> getAllRoles() {
        return rolesRepository.findAll().stream().map(role -> {
            RoleDto roleDto = new RoleDto();
            roleDto.setId(role.getId());
            roleDto.setName(role.getName());
            roleDto.setPermissions(role.getPermissions().stream().map(Enum::name).toList());

            return roleDto;
        }).toList();
    }

    @Override
    public String addNewRole(RoleUpsertDto roleUpsertDto) {

        List<Permissions> permissionsList = roleUpsertDto.permissions()
                .stream()
                .map(Permissions::valueOf)
                .toList();

        Role role = new Role(roleUpsertDto.roleName(), permissionsList);

        Role savedRole = rolesRepository.save(role);

        return savedRole.getId();
    }

    @Override
    public void updateRole(RoleUpsertDto roleUpsertDto) {

    }

    @Override
    public boolean deleteRole(String roleId) {
        return false;
    }
}
