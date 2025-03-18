package com.atech.pos.controllers;

import com.atech.pos.dtos.PagedUsers;
import com.atech.pos.dtos.PaginationRequest;
import com.atech.pos.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.atech.pos.dtos.PaginationRequest.getPaginationRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppUsersController {

    private final AppUserService appUserService;

    @GetMapping
    public ResponseEntity<PagedUsers> getUsersList(@RequestParam(required = false) Integer pageNumber,
                                                   @RequestParam(required = false) Integer pageSize,
                                                   @RequestParam(required = false) String sortDirection,
                                                   @RequestParam(required = false) String sortBy,
                                                   @RequestParam(required = false) String filterText) {

        PaginationRequest paginationRequest = getPaginationRequest(
                pageNumber, pageSize, sortDirection, sortBy, filterText);

        return ResponseEntity.ok(appUserService.getUsersList(paginationRequest));
    }

//    @PutMapping("/{userId}/assign-role")
//    public ResponseEntity<?> assignRoleToUser(@PathVariable String userId,
//                                              @RequestBody){
//
//    }
}
