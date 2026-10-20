package com.example.mhsunbreaksimulator.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.PresetCharm;

@Mapper
public interface PresetCharmMapper {

    int insert(PresetCharm presetCharm);

    PresetCharm findByPresetId(
            @Param("presetId") Integer presetId
    );

    int deleteByPresetId(
            @Param("presetId") Integer presetId
    );
}