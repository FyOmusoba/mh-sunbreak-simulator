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
     *
     * 既存コード互換用。
     *
     * 攻撃種別が渡されない従来の呼び出しでは
     * NORMALとして保存する。
     */
    @Transactional
    public PresetBowSetting create(
            Integer presetId,
            String coatingType,
            Integer bowChargeId) {

        return create(
                presetId,
                coatingType,
                bowChargeId,
                "NORMAL"
        );
    }


    /*
     * =====================================================
     * 弓設定保存
     * =====================================================
     *
     * bowAttackType
     *
     * NORMAL
     * DRAGON_PIERCER
     *
     * を保存する。
     */
    @Transactional
    public PresetBowSetting create(
            Integer presetId,
            String coatingType,
            Integer bowChargeId,
            String bowAttackType) {

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

        presetBowSetting.setBowAttackType(
                normalizeBowAttackType(
                        bowAttackType
                )
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


    /*
     * =====================================================
     * 弓攻撃種別正規化
     * =====================================================
     *
     * 現在対応している値だけを許可する。
     *
     * 不正値・null・空文字は
     * 従来互換のNORMALへ戻す。
     */
    private String normalizeBowAttackType(
            String bowAttackType) {

        if ("DRAGON_PIERCER".equals(
                bowAttackType
        )) {

            return "DRAGON_PIERCER";
        }

        return "NORMAL";
    }
}