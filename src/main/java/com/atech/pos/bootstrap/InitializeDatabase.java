package com.atech.pos.bootstrap;

import com.atech.pos.entity.AppUser;
import com.atech.pos.entity.Permissions;
import com.atech.pos.entity.Role;
import com.atech.pos.repository.AppUserRepository;
import com.atech.pos.repository.RolesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.atech.pos.utils.AppConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitializeDatabase implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public void run(String... args) throws Exception {

        initializeApplicationRoles();

        createAdminUser();
    }

    private void initializeApplicationRoles() {

        if (rolesRepository.count() == 0){

            Role roleAdmin = new Role(ROLE_ADMIN, populateAdminPermissions());
            Role roleManager = new Role(ROLE_MANAGER, populateAdminPermissions());
            Role roleCashier = new Role(ROLE_CASHIER, populateCashierPermissions());

            rolesRepository.saveAll(List.of(roleAdmin, roleManager, roleCashier));

            log.info("Added application roles");
        }
    }

    private void createAdminUser() {

        if (appUserRepository.count() == 0){
            AppUser appUser = new AppUser();
            appUser.setFirstName("System");
            appUser.setLastName("Admin");
            appUser.setUsername("admin");
            appUser.setEnteredBy("Application");
            appUser.setEnabled(true);
            appUser.setExpired(false);
            appUser.setPassword(passwordEncoder.encode("B@t@1234$$"));

            Optional<Role> optionalRole = rolesRepository.findAll()
                    .stream()
                    .filter(role -> role.getName().equals(ROLE_ADMIN))
                    .findFirst();

            if (optionalRole.isPresent()){

                appUser.setRole(optionalRole.get());

                appUserRepository.save(appUser);

                log.info("Added application admin user");
            }
        }
    }

    private static List<Permissions> populateAdminPermissions() {

        return new ArrayList<>(
                List.of(Permissions.AddUser, Permissions.EditUser,
                        Permissions.DeleteUser, Permissions.ViewUsers,
                        Permissions.ViewUser,
                        Permissions.AddCategory, Permissions.EditCategory,
                        Permissions.DeleteCategory, Permissions.ViewCategories,
                        Permissions.ViewCategory,
                        Permissions.AddProduct, Permissions.EditProduct,
                        Permissions.DeleteProduct, Permissions.ViewProduct,
                        Permissions.ViewProducts,
                        Permissions.AssignRoles, Permissions.ViewRoles,
                        Permissions.DeleteRole, Permissions.AddRole));
    }

    private static List<Permissions> populateCashierPermissions() {

        return new ArrayList<>(
                List.of(Permissions.EditUser,
                        Permissions.ViewUser,
                        Permissions.ViewCategories,
                        Permissions.ViewCategory));
    }
}
