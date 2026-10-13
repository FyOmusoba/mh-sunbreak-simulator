package com.example.mhsunbreaksimulator.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.BowCoating;

@Mapper
public interface BowCoatingMapper {

    List<BowCoating> findByWeaponId(
            @Param("weaponId") Integer weaponId
    );

    BowCoating findById(
            @Param("bowCoatingId") Integer bowCoatingId
    );
}