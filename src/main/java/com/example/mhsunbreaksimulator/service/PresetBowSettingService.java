package com.example.mhsunbreaksimulator.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mhsunbreaksimulator.entity.PresetBowSetting;
import com.example.mhsunbreaksimulator.mapper.PresetBowSettingMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresetBowSettingService {

    private final PresetBowSettingMapper
            presetBowSettingMapper;


    /*
     * =====================================================
     * 弓設定保存
     * =====================================================
     */
    @Transactional
    public PresetBowSetting create(
            Integer presetId,
            String coatingType,
            Integer bowChargeId) {

        PresetBowSetting presetBowSetting
                = new PresetBowSetting();

        presetBowSetting.setPresetId(
                presetId
        );

        presetBowSetting.setCoatingType(
                coatingType
        );

        presetBowSetting.setBowChargeId(
                bowChargeId
        );

        presetBowSettingMapper.insert(
                presetBowSetting
        );

        return presetBowSetting;
    }


    /*
     * =====================================================
     * プリセットの弓設定取得
     * =====================================================
     */
    public PresetBowSetting findByPresetId(
            Integer presetId) {

        return presetBowSettingMapper
                .findByPresetId(
                        presetId
                );
    }


    /*
     * =====================================================
     * プリセットの弓設定削除
     * =====================================================
     */
    @Transactional
    public void deleteByPresetId(
            Integer presetId) {

        presetBowSettingMapper
                .deleteByPresetId(
                        presetId
                );
    }
}