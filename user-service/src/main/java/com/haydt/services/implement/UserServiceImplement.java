package com.haydt.services.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haydt.entities.Authority;
import com.haydt.entities.User;
import com.haydt.repositories.AuthorityRepository;
import com.haydt.repositories.UserRepository;
import com.haydt.services.UserService;

@Service
public class UserServiceImplement implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }

    public List<String> getRolesByUsername(String username) {

        List<String> roleNames = new ArrayList<>();

        List<Authority> authorities = authorityRepository.findAll();

        for (Authority authority : authorities) {
            if (authority.getUser().getEmail().equals(username)) {
                roleNames.add("ROLE_" + authority.getRole().getId());
            }
        }
        return roleNames;
    }

}
