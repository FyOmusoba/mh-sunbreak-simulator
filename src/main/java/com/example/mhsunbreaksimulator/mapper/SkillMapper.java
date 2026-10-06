package com.example.mhsunbreaksimulator.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.Skill;

@Mapper
public interface SkillMapper {

    /*
     * 全スキル取得
     */
    List<Skill> findAll();


    /*
     * IDから1件取得
     */
    Skill findById(
            @Param("skillId") Integer skillId
    );
}