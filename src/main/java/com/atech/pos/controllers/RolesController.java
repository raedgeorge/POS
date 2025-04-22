package com.atech.pos.controllers;

import com.atech.pos.dtos.RoleDto;
import com.atech.pos.dtos.RoleUpsertDto;
import com.atech.pos.service.RolesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping(value = "/api/v1/roles", produces = MediaType.APPLICATION_JSON_VALUE)
public class RolesController {

    private final RolesService rolesService;

    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles(){
        return ResponseEntity.ok(rolesService.getAllRoles());
    }

    @PostMapping
    public ResponseEntity<String> addNewRole(@RequestBody @Valid RoleUpsertDto roleUpsertDto,
                                             HttpServletRequest servletRequest){

        String id = rolesService.addNewRole(roleUpsertDto);

        String location = servletRequest.getRequestURL() + "/" + id;

        return ResponseEntity.created(URI.create(location)).build();
    }
}
