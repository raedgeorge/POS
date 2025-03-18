package com.atech.pos.service;

import com.atech.pos.dtos.*;

public interface AppUserService {

    PagedUsers getUsersList(PaginationRequest paginationRequest);

    AppUserDto findUserById(String userId);

    AppUserDto findUserByUsername(String userId);

    String createUser(RegistrationRequestDto registrationRequestDto);

    boolean changeUsername(ChangeUsernameRequestDto changeUsernameRequestDto);

    boolean changePassword(ChangePasswordRequestDto changePasswordRequestDto);

    String deleteUser(String userId);

}
