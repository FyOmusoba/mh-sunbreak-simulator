package com.example.mhsunbreaksimulator.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mhsunbreaksimulator.entity.PresetArmor;
import com.example.mhsunbreaksimulator.mapper.PresetArmorMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresetArmorService {

    private final PresetArmorMapper presetArmorMapper;


    /*
     * =====================================================
     * 防具1部位保存
     * =====================================================
     */
    @Transactional
    public PresetArmor create(
            Integer presetId,
            String part,
            Integer armorId,
            Integer slot1SizeFinal,
            Integer slot2SizeFinal,
            Integer slot3SizeFinal,
            Integer defenseDelta,
            Integer fireResDelta,
            Integer waterResDelta,
            Integer thunderResDelta,
            Integer iceResDelta,
            Integer dragonResDelta) {

        PresetArmor presetArmor = new PresetArmor();

        presetArmor.setPresetId(presetId);
        presetArmor.setPart(part);
        presetArmor.setArmorId(armorId);

        presetArmor.setSlot1SizeFinal(slot1SizeFinal);
        presetArmor.setSlot2SizeFinal(slot2SizeFinal);
        presetArmor.setSlot3SizeFinal(slot3SizeFinal);

        presetArmor.setDefenseDelta(defenseDelta);

        presetArmor.setFireResDelta(fireResDelta);
        presetArmor.setWaterResDelta(waterResDelta);
        presetArmor.setThunderResDelta(thunderResDelta);
        presetArmor.setIceResDelta(iceResDelta);
        presetArmor.setDragonResDelta(dragonResDelta);

        presetArmorMapper.insert(presetArmor);

        return presetArmor;
    }


    /*
     * =====================================================
     * プリセット防具一式取得
     * =====================================================
     */
    public List<PresetArmor> findByPresetId(
            Integer presetId) {

        return presetArmorMapper.findByPresetId(
                presetId
        );
    }


    /*
     * =====================================================
     * プリセット防具一式削除
     * =====================================================
     */
    @Transactional
    public void deleteByPresetId(
            Integer presetId) {

        presetArmorMapper.deleteByPresetId(
                presetId
        );
    }
}