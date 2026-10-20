package com.example.mhsunbreaksimulator.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.Preset;

@Mapper
public interface PresetMapper {

    int insert(Preset preset);

    Preset findByIdAndUserId(
            @Param("presetId") Integer presetId,
            @Param("userId") Integer userId
    );

    List<Preset> findByUserId(
            @Param("userId") Integer userId
    );

    int updateName(
            @Param("presetId") Integer presetId,
            @Param("userId") Integer userId,
            @Param("presetName") String presetName
    );

    int deleteByIdAndUserId(
            @Param("presetId") Integer presetId,
            @Param("userId") Integer userId
    );
}