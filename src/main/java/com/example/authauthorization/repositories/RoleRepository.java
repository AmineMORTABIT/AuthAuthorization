package com.example.authauthorization.repositories;

import com.example.authauthorization.entities.Role;
import com.example.authauthorization.utils.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(RoleName roleName);

    boolean existsByRoleName(RoleName roleName);
}
