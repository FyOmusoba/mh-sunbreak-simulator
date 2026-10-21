package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.example.mhsunbreaksimulator.controller.PresetController;
import com.example.mhsunbreaksimulator.entity.Preset;
import com.example.mhsunbreaksimulator.entity.User;
import com.example.mhsunbreaksimulator.form.SimulatorForm;
import com.example.mhsunbreaksimulator.service.PresetDeleteService;
import com.example.mhsunbreaksimulator.service.PresetLoadService;
import com.example.mhsunbreaksimulator.service.PresetSaveService;
import com.example.mhsunbreaksimulator.service.PresetService;
import com.example.mhsunbreaksimulator.service.UserService;

@ExtendWith(MockitoExtension.class)
class PresetControllerTest {

    @Mock
    private PresetSaveService presetSaveService;

    @Mock
    private PresetLoadService presetLoadService;

    @Mock
    private PresetDeleteService presetDeleteService;

    @Mock
    private PresetService presetService;

    @Mock
    private UserService userService;

    @Mock
    private Principal principal;

    @Mock
    private Model model;

    @InjectMocks
    private PresetController presetController;


    @Test
    void savePreset_正常保存できる() {

        when(principal.getName())
                .thenReturn("testuser");

        SimulatorForm simulatorForm
                = new SimulatorForm();

        simulatorForm.setWeaponId(100);

        Map<String, String> requestParams
                = new LinkedHashMap<>();

        requestParams.put("presetName", "テスト装備");
        requestParams.put("weaponId", "100");
        requestParams.put("monsterId", "16");
        requestParams.put("_csrf", "dummy-token");

        Preset savedPreset
                = new Preset();

        savedPreset.setPresetId(500);
        savedPreset.setPresetName("テスト装備");
        savedPreset.setWeaponId(100);

        when(
                presetSaveService.save(
                        "testuser",
                        "テスト装備",
                        simulatorForm,
                        requestParams
                )
        ).thenReturn(savedPreset);

        String result
                = presetController.savePreset(
                        principal,
                        "テスト装備",
                        simulatorForm,
                        requestParams
                );

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


    @Test
    void showPresetList_ログインユーザーの一覧を表示できる() {

        when(principal.getName())
                .thenReturn("testuser");

        User user
                = new User();

        user.setUserId(1);
        user.setUsername("testuser");

        when(
                userService.findByUsername(
                        "testuser"
                )
        ).thenReturn(user);

        Preset preset1
                = new Preset();

        preset1.setPresetId(10);
        preset1.setPresetName("プリセット1");
        preset1.setUserId(1);
        preset1.setWeaponId(100);

        Preset preset2
                = new Preset();

        preset2.setPresetId(11);
        preset2.setPresetName("プリセット2");
        preset2.setUserId(1);
        preset2.setWeaponId(200);

        List<Preset> presets
                = List.of(
                        preset1,
                        preset2
                );

        when(
                presetService.findByUserId(
                        1
                )
        ).thenReturn(presets);

        String result
                = presetController.showPresetList(
                        principal,
                        model
                );

        assertEquals(
                "preset-list",
                result
        );

        verify(
                presetService
        ).findByUserId(1);

        verify(
                model
        ).addAttribute(
                "presets",
                presets
        );
    }


    @Test
    void loadPreset_保存内容をシミュレーターURLへ復元できる() {

        when(principal.getName())
                .thenReturn("testuser");

        Map<String, String> parameters
                = new LinkedHashMap<>();

        parameters.put("weaponId", "801");
        parameters.put("rampageDecorationId", "34");
        parameters.put("headArmorId", "1403");
        parameters.put("skillState_40", "OVERCOME");

        when(
                presetLoadService.load(
                        "testuser",
                        10
                )
        ).thenReturn(parameters);

        String result
                = presetController.loadPreset(
                        principal,
                        10
                );

        assertEquals(
                "redirect:/simulator"
                        + "?weaponId=801"
                        + "&rampageDecorationId=34"
                        + "&headArmorId=1403"
                        + "&skillState_40=OVERCOME",
                result
        );

        verify(
                presetLoadService
        ).load(
                "testuser",
                10
        );
    }


    @Test
    void deletePreset_正常削除できる() {

        when(principal.getName())
                .thenReturn("testuser");

        when(
                presetDeleteService.delete(
                        10,
                        "testuser"
                )
        ).thenReturn(true);

        String result
                = presetController.deletePreset(
                        principal,
                        10
                );

        assertEquals(
                "redirect:/preset/list"
                        + "?presetDeleted=true",
                result
        );

        verify(
                presetDeleteService
        ).delete(
                10,
                "testuser"
        );
    }


    @Test
    void showPresetList_未ログインならログイン画面へ戻る() {

        String result
                = presetController.showPresetList(
                        null,
                        model
                );

        assertEquals(
                "redirect:/login",
                result
        );
    }


    @Test
    void loadPreset_未ログインならログイン画面へ戻る() {

        String result
                = presetController.loadPreset(
                        null,
                        10
                );

        assertEquals(
                "redirect:/login",
                result
        );
    }


    @Test
    void deletePreset_未ログインならログイン画面へ戻る() {

        String result
                = presetController.deletePreset(
                        null,
                        10
                );

        assertEquals(
                "redirect:/login",
                result
        );
    }
}