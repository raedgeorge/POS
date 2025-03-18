package com.atech.pos.service.impl;

import com.atech.pos.dtos.*;
import com.atech.pos.entity.AppUser;
import com.atech.pos.entity.Role;
import com.atech.pos.entity.RoleType;
import com.atech.pos.exceptions.ResourceExistsException;
import com.atech.pos.exceptions.ResourceNotFoundException;
import com.atech.pos.mappers.AppUserMapper;
import com.atech.pos.repository.AppUserRepository;
import com.atech.pos.repository.RolesRepository;
import com.atech.pos.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static com.atech.pos.utils.EntityUtils.resolveSortByField;
import static com.atech.pos.utils.StringUtils.convertEachWorldToFirstLetterUpperCase;

@Service
@RequiredArgsConstructor
public class AppUsersServiceImpl implements AppUserService {

    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppUserMapper appUserMapper;
    private final AppUserRepository appUserRepository;

    private static final String DEFAULT_SORT_BY_FIELD = "lastName";

    @Override
    public PagedUsers getUsersList(PaginationRequest paginationRequest) {

        String sortBy = resolveSortByField(AppUser.class, paginationRequest.getSortBy(), DEFAULT_SORT_BY_FIELD);

        Page<AppUser> page;

        if (!ObjectUtils.isEmpty(paginationRequest.getFilterText())){
            page = appUserRepository.findAllAppUsersFiltered(PageRequest.of(
                        paginationRequest.getPageNumber(),
                        paginationRequest.getPageSize(),
                        Sort.by(Sort.Direction.fromString(paginationRequest.getSortDirection()), sortBy)),
                    paginationRequest.getFilterText());

        } else {
            page = appUserRepository.findAll(PageRequest.of(
                    paginationRequest.getPageNumber(),
                    paginationRequest.getPageSize(),
                    Sort.by(Sort.Direction.fromString(paginationRequest.getSortDirection()), sortBy)));
        }

        return new PagedUsers(page.getContent().stream().map(appUserMapper::mapToDto).toList(),
                page.getPageable().getPageNumber(),
                page.getPageable().getPageSize(),
                page.getNumberOfElements(),
                page.getTotalElements(),
                page.getTotalPages());
    }

    @Override
    public AppUserDto findUserById(String userId) {

        return appUserRepository.findById(userId)
                .map(appUserMapper::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
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

        Role role = rolesRepository.findRoleByRoleType(RoleType.Cashier);

        AppUser appUser = new AppUser();
        BeanUtils.copyProperties(registrationRequestDto, appUser);
        appUser.setEnabled(true);
        appUser.setExpired(false);
        appUser.setEnteredBy("raed george");
        appUser.setRole(role);
        appUser.setFirstName(convertEachWorldToFirstLetterUpperCase(appUser.getFirstName()));
        appUser.setLastName(convertEachWorldToFirstLetterUpperCase(appUser.getLastName()));
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
