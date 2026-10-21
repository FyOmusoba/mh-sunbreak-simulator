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
     *
     * 通常弾・起爆竜弾など、
     * ボウガンの攻撃種別も含めて保存する。
     */
    @Transactional
    public PresetBowgunSetting create(
            Integer presetId,
            String bowgunAttackType,
            String wyvernblastExplosionSize,
            String ammoType,
            Integer ammoLevel,
            Integer customId) {

        PresetBowgunSetting presetBowgunSetting
                = new PresetBowgunSetting();

        presetBowgunSetting.setPresetId(
                presetId
        );

        presetBowgunSetting.setBowgunAttackType(
                bowgunAttackType
        );

        presetBowgunSetting.setWyvernblastExplosionSize(
                wyvernblastExplosionSize
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
     * 既存呼び出し互換
     * =====================================================
     *
     * 以前の通常弾保存コード・既存テストを
     * 壊さないため残しておく。
     *
     * 従来形式は
     * bowgunAttackType = NORMAL_AMMO
     * として保存する。
     */
    @Transactional
    public PresetBowgunSetting create(
            Integer presetId,
            String ammoType,
            Integer ammoLevel,
            Integer customId) {

        return create(
                presetId,
                "NORMAL_AMMO",
                null,
                ammoType,
                ammoLevel,
                customId
        );
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
