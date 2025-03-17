package com.atech.pos.service;

import com.atech.pos.dtos.AppUserDto;
import com.atech.pos.dtos.ChangePasswordRequestDto;
import com.atech.pos.dtos.ChangeUsernameRequestDto;
import com.atech.pos.dtos.RegistrationRequestDto;

import java.util.List;

public interface AppUserService {

    List<AppUserDto> getUsersList();

    AppUserDto findUserById(String userId);

    AppUserDto findUserByUsername(String userId);

    String createUser(RegistrationRequestDto registrationRequestDto);

    boolean changeUsername(ChangeUsernameRequestDto changeUsernameRequestDto);

    boolean changePassword(ChangePasswordRequestDto changePasswordRequestDto);

    String deleteUser(String userId);

}
