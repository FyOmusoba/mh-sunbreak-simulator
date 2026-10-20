package com.example.mhsunbreaksimulator.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mhsunbreaksimulator.entity.PresetBowgunSetting;
import com.example.mhsunbreaksimulator.mapper.PresetBowgunSettingMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresetBowgunSettingService {

    private final PresetBowgunSettingMapper
            presetBowgunSettingMapper;


    /*
     * =====================================================
     * ボウガン設定保存
     * =====================================================
     */
    @Transactional
    public PresetBowgunSetting create(
            Integer presetId,
            String ammoType,
            Integer ammoLevel,
            Integer customId) {

        PresetBowgunSetting presetBowgunSetting
                = new PresetBowgunSetting();

        presetBowgunSetting.setPresetId(
                presetId
        );

        presetBowgunSetting.setAmmoType(
                ammoType
        );

        presetBowgunSetting.setAmmoLevel(
                ammoLevel
        );

        presetBowgunSetting.setCustomId(
                customId
        );

        presetBowgunSettingMapper.insert(
                presetBowgunSetting
        );

        return presetBowgunSetting;
    }


    /*
     * =====================================================
     * プリセットのボウガン設定取得
     * =====================================================
     */
    public PresetBowgunSetting findByPresetId(
            Integer presetId) {

        return presetBowgunSettingMapper
                .findByPresetId(
                        presetId
                );
    }


    /*
     * =====================================================
     * プリセットのボウガン設定削除
     * =====================================================
     */
    @Transactional
    public void deleteByPresetId(
            Integer presetId) {

        presetBowgunSettingMapper
                .deleteByPresetId(
                        presetId
                );
    }
}