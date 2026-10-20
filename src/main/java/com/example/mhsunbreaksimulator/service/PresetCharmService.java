package com.example.mhsunbreaksimulator.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mhsunbreaksimulator.entity.PresetCharm;
import com.example.mhsunbreaksimulator.mapper.PresetCharmMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresetCharmService {

    private final PresetCharmMapper presetCharmMapper;


    /*
     * =====================================================
     * 護石保存
     * =====================================================
     */
    @Transactional
    public PresetCharm create(
            Integer presetId,
            Integer skill1Id,
            Integer skill1Level,
            Integer skill2Id,
            Integer skill2Level,
            Integer slot1Size,
            Integer slot2Size,
            Integer slot3Size) {

        PresetCharm presetCharm = new PresetCharm();

        presetCharm.setPresetId(
                presetId
        );

        presetCharm.setSkill1Id(
                skill1Id
        );

        presetCharm.setSkill1Level(
                skill1Level
        );

        presetCharm.setSkill2Id(
                skill2Id
        );

        presetCharm.setSkill2Level(
                skill2Level
        );

        presetCharm.setSlot1Size(
                slot1Size
        );

        presetCharm.setSlot2Size(
                slot2Size
        );

        presetCharm.setSlot3Size(
                slot3Size
        );

        presetCharmMapper.insert(
                presetCharm
        );

        return presetCharm;
    }


    /*
     * =====================================================
     * プリセットの護石取得
     * =====================================================
     */
    public PresetCharm findByPresetId(
            Integer presetId) {

        return presetCharmMapper.findByPresetId(
                presetId
        );
    }


    /*
     * =====================================================
     * プリセットの護石削除
     * =====================================================
     */
    @Transactional
    public void deleteByPresetId(
            Integer presetId) {

        presetCharmMapper.deleteByPresetId(
                presetId
        );
    }
}