package com.example.mhsunbreaksimulator.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.PresetBowSetting;

@Mapper
public interface PresetBowSettingMapper {

    int insert(
            PresetBowSetting presetBowSetting
    );

    PresetBowSetting findByPresetId(
            @Param("presetId") Integer presetId
    );

    int deleteByPresetId(
            @Param("presetId") Integer presetId
    );
}