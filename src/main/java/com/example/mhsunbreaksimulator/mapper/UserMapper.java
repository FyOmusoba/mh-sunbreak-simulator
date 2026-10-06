package com.example.mhsunbreaksimulator.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.User;

@Mapper
public interface UserMapper {

    User findByUsername(@Param("username") String username);

    int insert(User user);
}