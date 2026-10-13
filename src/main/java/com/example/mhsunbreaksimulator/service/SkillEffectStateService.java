package com.example.mhsunbreaksimulator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.entity.SkillEffectState;
import com.example.mhsunbreaksimulator.mapper.SkillEffectStateMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillEffectStateService {

    private final SkillEffectStateMapper skillEffectStateMapper;


    /*
     * =========================================
     * スキルID・Lv・武器種から
     * 状態別効果一覧を取得
     * =========================================
     */
    public List<SkillEffectState> findBySkillIdAndLevel(
            Integer skillId,
            Integer level,
            String weaponType) {

        if (skillId == null
                || level == null
                || level < 1) {

            return List.of();
        }

        return skillEffectStateMapper.findBySkillIdAndLevel(
                skillId,
                level,
                weaponType
        );
    }


    /*
     * =========================================
     * スキルID・Lv・状態コード・武器種から
     * 状態別効果を1件取得
     * =========================================
     */
    public SkillEffectState findBySkillIdAndLevelAndStateCode(
            Integer skillId,
            Integer level,
            String stateCode,
            String weaponType) {

        if (skillId == null
                || level == null
                || level < 1
                || stateCode == null
                || stateCode.isBlank()) {

            return null;
        }

        return skillEffectStateMapper.findBySkillIdAndLevelAndStateCode(
                skillId,
                level,
                stateCode,
                weaponType
        );
    }
}