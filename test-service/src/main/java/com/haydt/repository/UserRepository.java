package com.haydt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haydt.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
