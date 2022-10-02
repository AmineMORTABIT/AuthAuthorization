package com.example.authauthorization;

import com.example.authauthorization.entities.Role;
import com.example.authauthorization.entities.User;
import com.example.authauthorization.services.RoleService;
import com.example.authauthorization.services.UserService;
import com.example.authauthorization.utils.RoleName;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@AllArgsConstructor
public class AuthAuthorizationApplication {

    private UserService userService;
    private RoleService roleService;

    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(AuthAuthorizationApplication.class, args);
    }


    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Role userRole = Role.builder()
                    .roleName(RoleName.ROLE_USER)
                    .build();
            Role adminRole = Role.builder()
                    .roleName(RoleName.ROLE_ADMIN)
                    .build();

            this.roleService.save(userRole);
            this.roleService.save(adminRole);

            User user1 = User.builder()
                    .username("Hibernate")
                    .firstName("Med")
                    .lastName("ANGULAR")
                    .email("med.angular@gmail.com")
                    .password(passwordEncoder.encode("azerty")).build();

            User user2 = User.builder()
                    .username("Mouhoubinho")
                    .firstName("Mohammed")
                    .lastName("SPRING")
                    .email("med.spring@gmail.com")
                    .password(passwordEncoder.encode("azerty")).build();

            User admin = User.builder()
                    .username("Mtefel")
                    .firstName("Amine")
                    .lastName("MORTABIT")
                    .email("med.amine.mortabit@gmail.com")
                    .password(passwordEncoder.encode("azerty")).build();

            this.userService.saveNewUser(admin);
            this.userService.saveNewUser(user1);
            this.userService.saveNewUser(user2);

            this.userService.addRoleToUser("Mtefel", RoleName.ROLE_ADMIN);
            this.userService.addRoleToUser("Mina", RoleName.ROLE_USER);
            this.userService.addRoleToUser("Mouhoubinho", RoleName.ROLE_USER);


        };
    }

}
