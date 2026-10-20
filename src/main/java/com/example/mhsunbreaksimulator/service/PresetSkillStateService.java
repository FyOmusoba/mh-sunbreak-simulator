package com.example.mhsunbreaksimulator.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mhsunbreaksimulator.entity.PresetSkillState;
import com.example.mhsunbreaksimulator.mapper.PresetSkillStateMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresetSkillStateService {

    private final PresetSkillStateMapper
            presetSkillStateMapper;


    /*
     * =====================================================
     * 条件付きスキル状態1件保存
     * =====================================================
     */
    @Transactional
    public PresetSkillState create(
            Integer presetId,
            Integer skillId,
            String stateCode) {

        PresetSkillState presetSkillState
                = new PresetSkillState();

        presetSkillState.setPresetId(
                presetId
        );

        presetSkillState.setSkillId(
                skillId
        );

        presetSkillState.setStateCode(
                stateCode
        );

        presetSkillStateMapper.insert(
                presetSkillState
        );

        return presetSkillState;
    }


    /*
     * =====================================================
     * プリセット内の条件付きスキル状態一覧取得
     * =====================================================
     */
    public List<PresetSkillState> findByPresetId(
            Integer presetId) {

        return presetSkillStateMapper
                .findByPresetId(
                        presetId
                );
    }


    /*
     * =====================================================
     * プリセット内の条件付きスキル状態全削除
     * =====================================================
     */
    @Transactional
    public void deleteByPresetId(
            Integer presetId) {

        presetSkillStateMapper
                .deleteByPresetId(
                        presetId
                );
    }
}