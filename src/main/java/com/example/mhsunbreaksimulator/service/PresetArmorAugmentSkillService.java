package com.example.mhsunbreaksimulator.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mhsunbreaksimulator.entity.PresetArmorAugmentSkill;
import com.example.mhsunbreaksimulator.mapper.PresetArmorAugmentSkillMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresetArmorAugmentSkillService {

    private final PresetArmorAugmentSkillMapper
            presetArmorAugmentSkillMapper;


    /*
     * =====================================================
     * 防具傀異錬成スキル1件保存
     * =====================================================
     */
    @Transactional
    public PresetArmorAugmentSkill create(
            Integer presetArmorId,
            Integer skillId,
            Integer levelDelta) {

        PresetArmorAugmentSkill augmentSkill
                = new PresetArmorAugmentSkill();

        augmentSkill.setPresetArmorId(
                presetArmorId
        );

        augmentSkill.setSkillId(
                skillId
        );

        augmentSkill.setLevelDelta(
                levelDelta
        );

        presetArmorAugmentSkillMapper.insert(
                augmentSkill
        );

        return augmentSkill;
    }


    /*
     * =====================================================
     * 防具1部位の傀異錬成スキル一覧取得
     * =====================================================
     */
    public List<PresetArmorAugmentSkill> findByPresetArmorId(
            Integer presetArmorId) {

        return presetArmorAugmentSkillMapper
                .findByPresetArmorId(
                        presetArmorId
                );
    }


    /*
     * =====================================================
     * 防具1部位の傀異錬成スキル全削除
     * =====================================================
     */
    @Transactional
    public void deleteByPresetArmorId(
            Integer presetArmorId) {

        presetArmorAugmentSkillMapper
                .deleteByPresetArmorId(
                        presetArmorId
                );
    }
}