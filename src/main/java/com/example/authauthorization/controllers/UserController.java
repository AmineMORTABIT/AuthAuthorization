package com.example.authauthorization.controllers;

import com.example.authauthorization.dto.UserDto;
import com.example.authauthorization.entities.Role;
import com.example.authauthorization.entities.User;
import com.example.authauthorization.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping("/allUsersByAdmin")
    public ResponseEntity<Set<UserDto>> getAllUsersByAdmin(){
        Set<User> allUsers = this.userService.getAllUsers();
        Set<UserDto> usersDtos = allUsers.stream().map(u -> new UserDto(u.getUsername(), u.getFirstName(), u.getLastName(),u.getEmail(),u.getRoles().stream().map(r->r.getRoleName().name()).collect(Collectors.toSet()))).collect(Collectors.toSet());
        System.out.println("Fetching de tous les utilisateurs DTO !");
        return new ResponseEntity<>(usersDtos, HttpStatus.OK);
    }
}
