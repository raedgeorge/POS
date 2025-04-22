package com.atech.pos.controllers;

import com.atech.pos.dtos.*;
import com.atech.pos.service.AppUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

        PaginationRequest paginationRequest =
                getPaginationRequest(pageNumber, pageSize, sortDirection, sortBy, filterText);

        return ResponseEntity.ok(appUserService.getUsersList(paginationRequest));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AppUserDto> getUserById(
            @PathVariable @NotBlank(message = "User Id is required") String userId){

        return ResponseEntity.ok(appUserService.findUserById(userId));
    }

    @GetMapping("/name/{username}")
    public ResponseEntity<AppUserDto> getUserByUsername(
            @PathVariable @NotBlank(message = "Username is required") String username){

        return ResponseEntity.ok(appUserService.findUserByUsername(username));
    }

    @PutMapping("/change-username")
    public ResponseEntity<Boolean> changeUsername(
            @RequestBody @Valid ChangeUsernameRequestDto changeUsernameRequestDto){

        return ResponseEntity.ok(appUserService.changeUsername(changeUsernameRequestDto));
    }

    @PutMapping("/change-password")
    public ResponseEntity<Boolean> changePassword(
            @RequestBody @Valid ChangePasswordRequestDto changePasswordRequestDto){

        return ResponseEntity.ok(appUserService.changePassword(changePasswordRequestDto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(
            @PathVariable @NotBlank(message = "User Id is required") String userId){

        return ResponseEntity.ok(appUserService.deleteUser(userId));
    }
}
