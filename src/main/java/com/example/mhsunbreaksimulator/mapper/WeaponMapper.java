package com.example.mhsunbreaksimulator.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.Weapon;

@Mapper
public interface WeaponMapper {

    List<Weapon> findAll();
    

    List<Weapon> findByWeaponType(
            @Param("weaponType") String weaponType
    );
    Weapon findById(
            @Param("weaponId") Integer weaponId
    );
}