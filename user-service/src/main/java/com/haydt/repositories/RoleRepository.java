package com.haydt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haydt.entities.Role;

public interface RoleRepository extends JpaRepository<Role, String> {

}
