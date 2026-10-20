package com.example.mhsunbreaksimulator.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.PresetSkillState;

@Mapper
public interface PresetSkillStateMapper {

    int insert(
            PresetSkillState presetSkillState
    );

    List<PresetSkillState> findByPresetId(
            @Param("presetId") Integer presetId
    );

    int deleteByPresetId(
            @Param("presetId") Integer presetId
    );
}