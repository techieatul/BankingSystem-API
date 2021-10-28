package com.ironhack.bankingsystem.services.impl;

import com.ironhack.bankingsystem.models.users.User;
import com.ironhack.bankingsystem.repositories.*;

import com.ironhack.bankingsystem.security.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import java.util.*;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            System.out.println("User not present!");
            throw new UsernameNotFoundException("User does not exist");
        }
        CustomUserDetails customUserDetails = new CustomUserDetails(user.get());
        System.out.println("User found");

        return customUserDetails;
    }
}
