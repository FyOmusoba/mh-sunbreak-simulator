package com.example.mhsunbreaksimulator.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.SkillEffectState;

@Mapper
public interface SkillEffectStateMapper {

    /*
     * スキルID・Lv・武器種から
     * 使用可能な状態別効果を取得
     */
    List<SkillEffectState> findBySkillIdAndLevel(
            @Param("skillId") Integer skillId,
            @Param("level") Integer level,
            @Param("weaponType") String weaponType
    );


    /*
     * スキルID・Lv・状態コード・武器種から
     * 状態別効果を1件取得
     */
    SkillEffectState findBySkillIdAndLevelAndStateCode(
            @Param("skillId") Integer skillId,
            @Param("level") Integer level,
            @Param("stateCode") String stateCode,
            @Param("weaponType") String weaponType
    );
}