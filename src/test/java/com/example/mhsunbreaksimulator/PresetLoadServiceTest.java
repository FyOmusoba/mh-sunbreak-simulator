package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.mhsunbreaksimulator.entity.BowCoating;
import com.example.mhsunbreaksimulator.entity.BowgunAmmo;
import com.example.mhsunbreaksimulator.entity.Preset;
import com.example.mhsunbreaksimulator.entity.PresetArmor;
import com.example.mhsunbreaksimulator.entity.PresetBowSetting;
import com.example.mhsunbreaksimulator.entity.PresetBowgunSetting;
import com.example.mhsunbreaksimulator.entity.PresetCharm;
import com.example.mhsunbreaksimulator.entity.PresetDecoration;
import com.example.mhsunbreaksimulator.entity.PresetSkillState;
import com.example.mhsunbreaksimulator.entity.User;
import com.example.mhsunbreaksimulator.service.BowCoatingService;
import com.example.mhsunbreaksimulator.service.BowgunAmmoService;
import com.example.mhsunbreaksimulator.service.PresetArmorService;
import com.example.mhsunbreaksimulator.service.PresetBowSettingService;
import com.example.mhsunbreaksimulator.service.PresetBowgunSettingService;
import com.example.mhsunbreaksimulator.service.PresetCharmService;
import com.example.mhsunbreaksimulator.service.PresetDecorationService;
import com.example.mhsunbreaksimulator.service.PresetLoadService;
import com.example.mhsunbreaksimulator.service.PresetService;
import com.example.mhsunbreaksimulator.service.PresetSkillStateService;
import com.example.mhsunbreaksimulator.service.UserService;

@ExtendWith(MockitoExtension.class)
class PresetLoadServiceTest {

    @Mock
    private UserService userService;

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

    @Mock
    private BowCoatingService bowCoatingService;

    @Mock
    private BowgunAmmoService bowgunAmmoService;

    @InjectMocks
    private PresetLoadService presetLoadService;


    /*
     * =====================================================
     * 弓プリセット
     * =====================================================
     */

    @Test
    void load_弓プリセットをURLパラメータへ復元できる() {

        User user
                = new User();

        user.setUserId(1);
        user.setUsername("testuser");


        Preset preset
                = new Preset();

        preset.setPresetId(10);
        preset.setUserId(1);
        preset.setPresetName("弓テスト");
        preset.setWeaponId(801);
        preset.setRampageDecorationId(34);


        PresetArmor headArmor
                = new PresetArmor();

        headArmor.setPresetArmorId(1001);
        headArmor.setPresetId(10);
        headArmor.setPart("頭");
        headArmor.setArmorId(1403);


        PresetCharm charm
                = new PresetCharm();

        charm.setPresetId(10);
        charm.setSkill1Id(43);
        charm.setSkill1Level(3);
        charm.setSkill2Id(137);
        charm.setSkill2Level(1);
        charm.setSlot1Size(3);
        charm.setSlot2Size(1);
        charm.setSlot3Size(0);


        PresetDecoration weaponDecoration
                = new PresetDecoration();

        weaponDecoration.setPresetId(10);
        weaponDecoration.setEquipmentKind("WEAPON");
        weaponDecoration.setSlotNo(1);
        weaponDecoration.setDecorationId(166);


        PresetDecoration headDecoration
                = new PresetDecoration();

        headDecoration.setPresetId(10);
        headDecoration.setEquipmentKind("ARMOR");
        headDecoration.setEquipmentPart("頭");
        headDecoration.setSlotNo(1);
        headDecoration.setDecorationId(72);


        PresetDecoration charmDecoration
                = new PresetDecoration();

        charmDecoration.setPresetId(10);
        charmDecoration.setEquipmentKind("CHARM");
        charmDecoration.setSlotNo(1);
        charmDecoration.setDecorationId(62);


        PresetSkillState skillState
                = new PresetSkillState();

        skillState.setPresetId(10);
        skillState.setSkillId(40);
        skillState.setStateCode("OVERCOME");


        PresetBowSetting bowSetting
                = new PresetBowSetting();

        bowSetting.setPresetId(10);
        bowSetting.setCoatingType("接撃ビン");
        bowSetting.setBowChargeId(94);


        BowCoating bowCoating
                = new BowCoating();

        bowCoating.setBowCoatingId(81);
        bowCoating.setWeaponId(801);
        bowCoating.setCoatingType("接撃ビン");


        when(
                userService.findByUsername(
                        "testuser"
                )
        ).thenReturn(user);


        when(
                presetService.findByIdAndUserId(
                        10,
                        1
                )
        ).thenReturn(preset);


        when(
                presetArmorService.findByPresetId(
                        10
                )
        ).thenReturn(
                List.of(headArmor)
        );


        when(
                presetCharmService.findByPresetId(
                        10
                )
        ).thenReturn(charm);


        when(
                presetDecorationService.findByPresetId(
                        10
                )
        ).thenReturn(
                List.of(
                        weaponDecoration,
                        headDecoration,
                        charmDecoration
                )
        );


        when(
                presetSkillStateService.findByPresetId(
                        10
                )
        ).thenReturn(
                List.of(skillState)
        );


        when(
                presetBowSettingService.findByPresetId(
                        10
                )
        ).thenReturn(bowSetting);


        when(
                bowCoatingService.findByWeaponId(
                        801
                )
        ).thenReturn(
                List.of(bowCoating)
        );


        Map<String, String> result
                = presetLoadService.load(
                        "testuser",
                        10
                );


        assertEquals(
                "801",
                result.get("weaponId")
        );

        assertEquals(
                "34",
                result.get("rampageDecorationId")
        );

        assertEquals(
                "1403",
                result.get("headArmorId")
        );

        assertEquals(
                "43",
                result.get("talismanSkill1Id")
        );

        assertEquals(
                "3",
                result.get("talismanSkill1Level")
        );

        assertEquals(
                "137",
                result.get("talismanSkill2Id")
        );

        assertEquals(
                "1",
                result.get("talismanSkill2Level")
        );

        assertEquals(
                "3",
                result.get("talismanSlot1Size")
        );

        assertEquals(
                "1",
                result.get("talismanSlot2Size")
        );

        assertEquals(
                "0",
                result.get("talismanSlot3Size")
        );

        assertEquals(
                "166",
                result.get("weaponDecoration1Id")
        );

        assertEquals(
                "72",
                result.get("headDecoration1Id")
        );

        assertEquals(
                "62",
                result.get("talismanDecoration1Id")
        );

        assertEquals(
                "94",
                result.get("bowChargeId")
        );

        assertEquals(
                "81",
                result.get("bowCoatingId")
        );

        assertEquals(
                "OVERCOME",
                result.get("skillState_40")
        );
    }


    /*
     * =====================================================
     * ボウガンプリセット
     * =====================================================
     */

    @Test
    void load_ボウガンプリセットをURLパラメータへ復元できる() {

        User user
                = new User();

        user.setUserId(1);
        user.setUsername("testuser");


        Preset preset
                = new Preset();

        preset.setPresetId(20);
        preset.setUserId(1);
        preset.setPresetName("ボウガンテスト");
        preset.setWeaponId(916);
        preset.setRampageDecorationId(34);


        PresetBowgunSetting bowgunSetting
                = new PresetBowgunSetting();

        bowgunSetting.setPresetId(20);
        bowgunSetting.setAmmoType("貫通弾");
        bowgunSetting.setAmmoLevel(2);


        BowgunAmmo bowgunAmmo
                = new BowgunAmmo();

        bowgunAmmo.setBowgunAmmoId(1104);
        bowgunAmmo.setWeaponId(916);
        bowgunAmmo.setAmmoType("貫通弾");
        bowgunAmmo.setAmmoLevel(2);


        when(
                userService.findByUsername(
                        "testuser"
                )
        ).thenReturn(user);


        when(
                presetService.findByIdAndUserId(
                        20,
                        1
                )
        ).thenReturn(preset);


        when(
                presetArmorService.findByPresetId(
                        20
                )
        ).thenReturn(
                List.of()
        );


        when(
                presetDecorationService.findByPresetId(
                        20
                )
        ).thenReturn(
                List.of()
        );


        when(
                presetSkillStateService.findByPresetId(
                        20
                )
        ).thenReturn(
                List.of()
        );


        when(
                presetBowgunSettingService.findByPresetId(
                        20
                )
        ).thenReturn(bowgunSetting);


        when(
                bowgunAmmoService.findByWeaponId(
                        916
                )
        ).thenReturn(
                List.of(bowgunAmmo)
        );


        Map<String, String> result
                = presetLoadService.load(
                        "testuser",
                        20
                );


        assertEquals(
                "916",
                result.get("weaponId")
        );

        assertEquals(
                "34",
                result.get("rampageDecorationId")
        );

        assertEquals(
                "1104",
                result.get("bowgunAmmoId")
        );
    }


    /*
     * =====================================================
     * 他ユーザーのプリセット
     * =====================================================
     */

    @Test
    void load_自分の所有物でなければ読み込めない() {

        User user
                = new User();

        user.setUserId(1);
        user.setUsername("testuser");


        when(
                userService.findByUsername(
                        "testuser"
                )
        ).thenReturn(user);


        when(
                presetService.findByIdAndUserId(
                        10,
                        1
                )
        ).thenReturn(null);


        assertThrows(
                IllegalArgumentException.class,
                () -> presetLoadService.load(
                        "testuser",
                        10
                )
        );
    }


    /*
     * =====================================================
     * 不正ユーザー
     * =====================================================
     */

    @Test
    void load_存在しないユーザーなら読み込めない() {

        when(
                userService.findByUsername(
                        "unknown"
                )
        ).thenReturn(null);


        assertThrows(
                IllegalArgumentException.class,
                () -> presetLoadService.load(
                        "unknown",
                        10
                )
        );
    }
}