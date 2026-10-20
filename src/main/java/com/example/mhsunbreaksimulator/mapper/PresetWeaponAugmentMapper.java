package com.example.mhsunbreaksimulator.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.PresetWeaponAugment;

@Mapper
public interface PresetWeaponAugmentMapper {

    int insert(
            PresetWeaponAugment presetWeaponAugment
    );

    PresetWeaponAugment findByPresetId(
            @Param("presetId") Integer presetId
    );

    int deleteByPresetId(
            @Param("presetId") Integer presetId
    );
}