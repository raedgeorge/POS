package com.atech.pos.service.impl;

import com.atech.pos.dtos.AppUserDto;
import com.atech.pos.dtos.ChangePasswordRequestDto;
import com.atech.pos.dtos.ChangeUsernameRequestDto;
import com.atech.pos.dtos.RegistrationRequestDto;
import com.atech.pos.entity.AppUser;
import com.atech.pos.exceptions.ResourceExistsException;
import com.atech.pos.exceptions.ResourceNotFoundException;
import com.atech.pos.mappers.AppUserMapper;
import com.atech.pos.repository.AppUserRepository;
import com.atech.pos.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUsersServiceImpl implements AppUserService {

    private final PasswordEncoder passwordEncoder;
    private final AppUserMapper appUserMapper;
    private final AppUserRepository appUserRepository;

    @Override
    public List<AppUserDto> getUsersList() {
        return List.of();
    }

    @Override
    public AppUserDto findUserById(String userId) {
        return null;
    }

    @Override
    public AppUserDto findUserByUsername(String username) {

        return appUserRepository.findAppUserByUsername(username)
                .map(appUserMapper::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Username", username));
    }

    @Override
    public String createUser(RegistrationRequestDto registrationRequestDto) {

        if (appUserRepository.existsAppUsersByUsername(registrationRequestDto.username()))
            throw new ResourceExistsException("User", "Username", registrationRequestDto.username());

        AppUser appUser = new AppUser();
        BeanUtils.copyProperties(registrationRequestDto, appUser);
        appUser.setEnabled(true);
        appUser.setExpired(false);
        appUser.setEnteredBy("raed george");
        appUser.setPassword(passwordEncoder.encode(registrationRequestDto.password()));

        AppUser savedUser = appUserRepository.save(appUser);

        return savedUser.getId();
    }

    @Override
    public boolean changeUsername(ChangeUsernameRequestDto changeUsernameRequestDto) {
        return false;
    }

    @Override
    public boolean changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        return false;
    }

    @Override
    public String deleteUser(String userId) {
        return "";
    }
}
