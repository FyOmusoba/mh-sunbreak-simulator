package com.example.mhsunbreaksimulator.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mhsunbreaksimulator.entity.PresetWeaponAugment;
import com.example.mhsunbreaksimulator.mapper.PresetWeaponAugmentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresetWeaponAugmentService {

    private final PresetWeaponAugmentMapper
            presetWeaponAugmentMapper;


    /*
     * =====================================================
     * 武器傀異錬成設定保存
     * =====================================================
     */
    @Transactional
    public PresetWeaponAugment create(
            Integer presetId,
            Integer attackLevel,
            Integer elementLevel,
            Integer affinityLevel,
            Integer sharpnessLevel) {

        PresetWeaponAugment presetWeaponAugment
                = new PresetWeaponAugment();

        presetWeaponAugment.setPresetId(
                presetId
        );

        presetWeaponAugment.setAttackLevel(
                attackLevel
        );

        presetWeaponAugment.setElementLevel(
                elementLevel
        );

        presetWeaponAugment.setAffinityLevel(
                affinityLevel
        );

        presetWeaponAugment.setSharpnessLevel(
                sharpnessLevel
        );

        presetWeaponAugmentMapper.insert(
                presetWeaponAugment
        );

        return presetWeaponAugment;
    }


    /*
     * =====================================================
     * プリセットの武器傀異錬成設定取得
     * =====================================================
     */
    public PresetWeaponAugment findByPresetId(
            Integer presetId) {

        return presetWeaponAugmentMapper
                .findByPresetId(
                        presetId
                );
    }


    /*
     * =====================================================
     * プリセットの武器傀異錬成設定削除
     * =====================================================
     */
    @Transactional
    public void deleteByPresetId(
            Integer presetId) {

        presetWeaponAugmentMapper
                .deleteByPresetId(
                        presetId
                );
    }
}