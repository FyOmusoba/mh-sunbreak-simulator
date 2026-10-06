package com.example.mhsunbreaksimulator.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.Armor;

@Mapper
public interface ArmorMapper {

    List<Armor> findAll();

    List<Armor> findByPart(
            @Param("part") String part
    );

    Armor findById(
            @Param("armorId") Integer armorId
    );
}