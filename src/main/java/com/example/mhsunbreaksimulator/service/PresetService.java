package com.example.mhsunbreaksimulator.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mhsunbreaksimulator.entity.Preset;
import com.example.mhsunbreaksimulator.mapper.PresetMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresetService {

    private final PresetMapper presetMapper;


    /*
     * =====================================================
     * プリセット新規作成
     * =====================================================
     */
    @Transactional
    public Preset create(
            Integer userId,
            String presetName,
            Integer weaponId,
            Integer rampageDecorationId) {

        Preset preset = new Preset();

        preset.setUserId(
                userId
        );

        preset.setPresetName(
                presetName
        );

        preset.setWeaponId(
                weaponId
        );

        preset.setRampageDecorationId(
                rampageDecorationId
        );

        presetMapper.insert(
                preset
        );

        return preset;
    }


    /*
     * =====================================================
     * プリセット1件取得
     * =====================================================
     */
    public Preset findByIdAndUserId(
            Integer presetId,
            Integer userId) {

        return presetMapper.findByIdAndUserId(
                presetId,
                userId
        );
    }


    /*
     * =====================================================
     * ユーザーのプリセット一覧取得
     * =====================================================
     */
    public List<Preset> findByUserId(
            Integer userId) {

        return presetMapper.findByUserId(
                userId
        );
    }


    /*
     * =====================================================
     * プリセット名変更
     * =====================================================
     */
    @Transactional
    public boolean updateName(
            Integer presetId,
            Integer userId,
            String presetName) {

        int updatedCount
                = presetMapper.updateName(
                        presetId,
                        userId,
                        presetName
                );

        return updatedCount > 0;
    }


    /*
     * =====================================================
     * プリセット本体削除
     * =====================================================
     */
    @Transactional
    public boolean delete(
            Integer presetId,
            Integer userId) {

        int deletedCount
                = presetMapper.deleteByIdAndUserId(
                        presetId,
                        userId
                );

        return deletedCount > 0;
    }
}