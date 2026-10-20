package com.example.mhsunbreaksimulator.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.PresetBowgunSetting;

@Mapper
public interface PresetBowgunSettingMapper {

    int insert(
            PresetBowgunSetting presetBowgunSetting
    );

    PresetBowgunSetting findByPresetId(
            @Param("presetId") Integer presetId
    );

    int deleteByPresetId(
            @Param("presetId") Integer presetId
    );
}