package com.atech.pos.controllers;

import com.atech.pos.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppUsersController {

    private final AppUserService appUserService;


//    @PutMapping("/{userId}/assign-role")
//    public ResponseEntity<?> assignRoleToUser(@PathVariable String userId,
//                                              @RequestBody){
//
//    }
}
