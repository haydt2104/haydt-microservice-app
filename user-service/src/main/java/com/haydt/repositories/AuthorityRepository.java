package com.haydt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haydt.entities.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
