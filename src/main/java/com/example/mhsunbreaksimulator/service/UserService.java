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


    /*
     * =====================================================
     * ユーザー名存在確認
     * =====================================================
     */
    public boolean existsByUsername(
            String username) {

        return userMapper.findByUsername(
                username
        ) != null;
    }


    /*
     * =====================================================
     * ユーザー登録
     * =====================================================
     */
    public void register(
            String username,
            String password) {

        User user = new User();

        user.setUsername(
                username
        );

        user.setPasswordHash(
                passwordEncoder.encode(
                        password
                )
        );

        userMapper.insert(
                user
        );
    }


    /*
     * =====================================================
     * Userエンティティ取得
     * =====================================================
     *
     * プリセット保存時など、
     * username から user_id を取得したい場合に使用する。
     */
    public User findByUsername(
            String username) {

        if (username == null
                || username.isBlank()) {

            return null;
        }

        return userMapper.findByUsername(
                username
        );
    }


    /*
     * =====================================================
     * Spring Security 認証用ユーザー取得
     * =====================================================
     */
    @Override
    public UserDetails loadUserByUsername(
            String username)
            throws UsernameNotFoundException {

        User user
                = userMapper.findByUsername(
                        username
                );

        if (user == null) {

            throw new UsernameNotFoundException(
                    "ユーザーが見つかりません。"
            );
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(
                        user.getUsername()
                )
                .password(
                        user.getPasswordHash()
                )
                .roles(
                        "USER"
                )
                .build();
    }
}