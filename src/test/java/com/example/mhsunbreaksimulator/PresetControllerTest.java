package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.mhsunbreaksimulator.controller.PresetController;
import com.example.mhsunbreaksimulator.entity.Preset;
import com.example.mhsunbreaksimulator.form.SimulatorForm;
import com.example.mhsunbreaksimulator.service.PresetSaveService;

@ExtendWith(MockitoExtension.class)
class PresetControllerTest {

    @Mock
    private PresetSaveService presetSaveService;

    @Mock
    private Principal principal;

    @InjectMocks
    private PresetController presetController;


    @Test
    void savePreset_正常保存できる() {

        /*
         * =====================================================
         * ログインユーザー
         * =====================================================
         */

        when(
                principal.getName()
        ).thenReturn(
                "testuser"
        );


        /*
         * =====================================================
         * シミュレーターフォーム
         * =====================================================
         */

        SimulatorForm simulatorForm
                = new SimulatorForm();

        simulatorForm.setWeaponId(
                100
        );


        /*
         * =====================================================
         * リクエストパラメータ
         * =====================================================
         */

        Map<String, String> requestParams
                = new LinkedHashMap<>();

        requestParams.put(
                "presetName",
                "テスト装備"
        );

        requestParams.put(
                "weaponId",
                "100"
        );

        requestParams.put(
                "monsterId",
                "16"
        );

        requestParams.put(
                "_csrf",
                "dummy-token"
        );


        /*
         * =====================================================
         * 保存結果
         * =====================================================
         */

        Preset savedPreset
                = new Preset();

        savedPreset.setPresetId(
                500
        );

        savedPreset.setPresetName(
                "テスト装備"
        );

        savedPreset.setWeaponId(
                100
        );


        when(
                presetSaveService.save(
                        "testuser",
                        "テスト装備",
                        simulatorForm,
                        requestParams
                )
        ).thenReturn(
                savedPreset
        );


        /*
         * =====================================================
         * 実行
         * =====================================================
         */

        String result
                = presetController.savePreset(
                        principal,
                        "テスト装備",
                        simulatorForm,
                        requestParams
                );


        /*
         * =====================================================
         * 確認
         * =====================================================
         */

        assertEquals(
                "redirect:/simulator"
                        + "?weaponId=100"
                        + "&monsterId=16"
                        + "&presetSaved=true",
                result
        );


        verify(
                presetSaveService
        ).save(
                "testuser",
                "テスト装備",
                simulatorForm,
                requestParams
        );
    }
}