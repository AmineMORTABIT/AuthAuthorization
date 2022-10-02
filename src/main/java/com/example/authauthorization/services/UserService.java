package com.example.authauthorization.services;

import com.example.authauthorization.entities.Role;
import com.example.authauthorization.entities.User;
import com.example.authauthorization.repositories.RoleRepository;
import com.example.authauthorization.repositories.UserRepository;
import com.example.authauthorization.utils.RoleName;
import com.example.authauthorization.utils.exceptions.EmailAlreadyExistsException;
import com.example.authauthorization.utils.exceptions.NotFoundException;
import com.example.authauthorization.utils.exceptions.UserNameAlreadyExistsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

  //  @Bean
  //  public PasswordEncoder passwordEncoder() {
  //      return new BCryptPasswordEncoder();
  //  }

    public User saveNewUser(User user){

        boolean usernameExists = this.userRepository.existsByUsername(user.getUsername());
        if (usernameExists) {
            throw new UserNameAlreadyExistsException(
                    "L'utilisateur avec le username \"" + user.getUsername() + "\" existe déjà !");
        }

        boolean emailExists = this.userRepository.existsByEmail(user.getEmail());
        if (emailExists) {
            throw new EmailAlreadyExistsException("L'utilisateur avec l'email \"" + user.getEmail() + "\" existe déjà !");
        }

        log.debug("User to save '{}'", user);

       // user.setPassword(passwordEncoder().encode(user.getPassword()));

        return userRepository.save(user);

    }

    @Transactional
    public void addRoleToUser(String username, RoleName roleName){
        Optional<User> userOptional = this.userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new NotFoundException(
                    "L'utilisateur  n'existe pas !");
        } else {
            User user = userOptional.get();
            Role role = this.roleRepository.findByRoleName(roleName);
            user.getRoles().add(role);
        }

    }

    public User update(User user) {

        Optional<User> existingUserOptional = this.userRepository.findById(user.getId());

        if (existingUserOptional.isEmpty()) {
            throw new NotFoundException(
                    "L'utilisateur " + user.getFirstName() + " " + user.getLastName() + " n'existe pas !");
        }

        User existingUser = existingUserOptional.get();

        if (!user.getEmail().equals(existingUser.getEmail()) && this.userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("L'utilisateur avec l'email \"" + user.getEmail() + "\" existe déjà!");
        }


        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setUsername(user.getUsername());
        existingUser.setRoles(user.getRoles());

        return this.userRepository.save(existingUser);

    }

    public Boolean deleteById(Long id) throws Exception {
        boolean isDeleted = false;
        try {
            this.userRepository.deleteById(id);
            isDeleted = true;
        } catch (Exception ignored) {

        }
        return isDeleted;
    }

    public User findByID(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("L'utilisateur avec l'id :: " + id + " :: n'existe pas !");
        }
        return userOptional.get();
    }

    public User findByUsernameOrEmail(String userNameOrEmail) {
        Optional<User> userOptional = userRepository.findByEmailOrUsername(userNameOrEmail, userNameOrEmail);
        if (userOptional.isEmpty()) {
            throw new NotFoundException(
                    "L'utilisateur avec l'email ou username :: " + userNameOrEmail + " :: n'existe pas !");
        }
        return userOptional.get();
    }

    public Page<User> findByPage(int page, int size) {
        return this.userRepository.findAll(PageRequest.of(page, size));
    }

    public Set<User> getAllUsers(){
        List<User> allUsersList = this.userRepository.findAll();
        Set<User> allUsersSet = new HashSet<>(allUsersList);
        return allUsersSet;
    }
}
