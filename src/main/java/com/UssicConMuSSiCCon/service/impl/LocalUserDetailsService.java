package com.UssicConMuSSiCCon.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.UssicConMuSSiCCon.entity.User;
import com.UssicConMuSSiCCon.service.UserService;

@Service
public class LocalUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public LocalUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Email address {" + email + "} not found!");
        }
        return user;
    }
}
