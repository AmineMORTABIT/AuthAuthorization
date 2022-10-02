package com.example.authauthorization.services;

import com.example.authauthorization.entities.Role;
import com.example.authauthorization.repositories.RoleRepository;
import com.example.authauthorization.utils.exceptions.RoleAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {

    private RoleRepository roleRepository;

    public Role save(Role role) {
        boolean exists = this.roleRepository.existsByRoleName(role.getRoleName());
        if (exists) {
            throw new RoleAlreadyExistsException("Le rôle " + role.getRoleName() + " existe déjà !");
        }

        return this.roleRepository.save(role);
    }
}
