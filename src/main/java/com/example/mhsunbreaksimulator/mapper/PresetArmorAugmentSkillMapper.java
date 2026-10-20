package com.example.mhsunbreaksimulator.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.PresetArmorAugmentSkill;

@Mapper
public interface PresetArmorAugmentSkillMapper {

    int insert(
            PresetArmorAugmentSkill presetArmorAugmentSkill
    );

    List<PresetArmorAugmentSkill> findByPresetArmorId(
            @Param("presetArmorId") Integer presetArmorId
    );

    int deleteByPresetArmorId(
            @Param("presetArmorId") Integer presetArmorId
    );
}