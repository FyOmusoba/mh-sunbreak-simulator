package com.example.mhsunbreaksimulator.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mhsunbreaksimulator.entity.PresetDecoration;
import com.example.mhsunbreaksimulator.mapper.PresetDecorationMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresetDecorationService {

    private final PresetDecorationMapper presetDecorationMapper;


    /*
     * =====================================================
     * 装飾品1件保存
     * =====================================================
     */
    @Transactional
    public PresetDecoration create(
            Integer presetId,
            String equipmentKind,
            String equipmentPart,
            Integer slotNo,
            Integer decorationId) {

        PresetDecoration presetDecoration
                = new PresetDecoration();

        presetDecoration.setPresetId(
                presetId
        );

        presetDecoration.setEquipmentKind(
                equipmentKind
        );

        presetDecoration.setEquipmentPart(
                equipmentPart
        );

        presetDecoration.setSlotNo(
                slotNo
        );

        presetDecoration.setDecorationId(
                decorationId
        );

        presetDecorationMapper.insert(
                presetDecoration
        );

        return presetDecoration;
    }


    /*
     * =====================================================
     * プリセット内の装飾品一覧取得
     * =====================================================
     */
    public List<PresetDecoration> findByPresetId(
            Integer presetId) {

        return presetDecorationMapper.findByPresetId(
                presetId
        );
    }


    /*
     * =====================================================
     * プリセット内の装飾品全削除
     * =====================================================
     */
    @Transactional
    public void deleteByPresetId(
            Integer presetId) {

        presetDecorationMapper.deleteByPresetId(
                presetId
        );
    }
}