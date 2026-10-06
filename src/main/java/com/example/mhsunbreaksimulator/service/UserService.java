package com.example.mhsunbreaksimulator.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.entity.User;
import com.example.mhsunbreaksimulator.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public boolean existsByUsername(String username) {
        return userMapper.findByUsername(username) != null;
    }

    public void register(String username, String password) {

        User user = new User();

        user.setUsername(username);

        user.setPasswordHash(
                passwordEncoder.encode(password)
        );

        userMapper.insert(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userMapper.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(
                    "ユーザーが見つかりません。"
            );
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .roles("USER")
                .build();
    }
}