package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.mhsunbreaksimulator.entity.Preset;
import com.example.mhsunbreaksimulator.entity.User;
import com.example.mhsunbreaksimulator.entity.Weapon;
import com.example.mhsunbreaksimulator.form.SimulatorForm;
import com.example.mhsunbreaksimulator.service.ArmorService;
import com.example.mhsunbreaksimulator.service.BowChargeService;
import com.example.mhsunbreaksimulator.service.BowCoatingService;
import com.example.mhsunbreaksimulator.service.BowgunAmmoService;
import com.example.mhsunbreaksimulator.service.DecorationService;
import com.example.mhsunbreaksimulator.service.PresetArmorService;
import com.example.mhsunbreaksimulator.service.PresetBowSettingService;
import com.example.mhsunbreaksimulator.service.PresetBowgunSettingService;
import com.example.mhsunbreaksimulator.service.PresetCharmService;
import com.example.mhsunbreaksimulator.service.PresetDecorationService;
import com.example.mhsunbreaksimulator.service.PresetSaveService;
import com.example.mhsunbreaksimulator.service.PresetService;
import com.example.mhsunbreaksimulator.service.PresetSkillStateService;
import com.example.mhsunbreaksimulator.service.RampageDecorationService;
import com.example.mhsunbreaksimulator.service.SkillService;
import com.example.mhsunbreaksimulator.service.UserService;
import com.example.mhsunbreaksimulator.service.WeaponService;

@ExtendWith(MockitoExtension.class)
class PresetSaveServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private WeaponService weaponService;

    @Mock
    private ArmorService armorService;

    @Mock
    private DecorationService decorationService;

    @Mock
    private RampageDecorationService rampageDecorationService;

    @Mock
    private BowChargeService bowChargeService;

    @Mock
    private BowCoatingService bowCoatingService;

    @Mock
    private BowgunAmmoService bowgunAmmoService;

    @Mock
    private SkillService skillService;

    @Mock
    private PresetService presetService;

    @Mock
    private PresetArmorService presetArmorService;

    @Mock
    private PresetCharmService presetCharmService;

    @Mock
    private PresetDecorationService presetDecorationService;

    @Mock
    private PresetSkillStateService presetSkillStateService;

    @Mock
    private PresetBowSettingService presetBowSettingService;

    @Mock
    private PresetBowgunSettingService presetBowgunSettingService;

    @InjectMocks
    private PresetSaveService presetSaveService;


    @Test
    void save_最低限の構成を保存できる() {

        User user = new User();

        user.setUserId(10);
        user.setUsername("testuser");

        when(
                userService.findByUsername(
                        "testuser"
                )
        ).thenReturn(
                user
        );


        Weapon weapon = new Weapon();

        weapon.setWeaponId(100);
        weapon.setWeaponType("太刀");
        weapon.setSlot1Size(0);
        weapon.setSlot2Size(0);
        weapon.setSlot3Size(0);
        weapon.setRampageSlotSize(0);

        when(
                weaponService.findById(
                        100
                )
        ).thenReturn(
                weapon
        );


        SimulatorForm simulatorForm
                = new SimulatorForm();

        simulatorForm.setWeaponId(
                100
        );


        Preset savedPreset
                = new Preset();

        savedPreset.setPresetId(500);
        savedPreset.setUserId(10);
        savedPreset.setPresetName("テスト装備");
        savedPreset.setWeaponId(100);

        when(
                presetService.create(
                        10,
                        "テスト装備",
                        100,
                        null
                )
        ).thenReturn(
                savedPreset
        );


        Preset result
                = presetSaveService.save(
                        "testuser",
                        "テスト装備",
                        simulatorForm,
                        Map.of()
                );


        assertSame(
                savedPreset,
                result
        );


        verify(
                presetService
        ).create(
                10,
                "テスト装備",
                100,
                null
        );


        verify(
                presetCharmService
        ).create(
                500,
                null,
                null,
                null,
                null,
                0,
                0,
                0
        );
    }
}