package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.mhsunbreaksimulator.entity.BowCharge;
import com.example.mhsunbreaksimulator.entity.BowCoating;
import com.example.mhsunbreaksimulator.entity.BowgunAmmo;
import com.example.mhsunbreaksimulator.entity.Preset;
import com.example.mhsunbreaksimulator.entity.Skill;
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
import com.example.mhsunbreaksimulator.service.PresetWeaponAugmentService;
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

    @Mock
    private PresetWeaponAugmentService presetWeaponAugmentService;

    @InjectMocks
    private PresetSaveService presetSaveService;


    /*
     * =====================================================
     * 最低限の構成
     * =====================================================
     */

    @Test
    void save_最低限の構成を保存できる() {

        User user
                = createUser();

        Weapon weapon
                = createWeapon(
                        100,
                        "太刀"
                );

        Preset savedPreset
                = createPreset(
                        500,
                        100
                );


        when(
                userService.findByUsername(
                        "testuser"
                )
        ).thenReturn(user);


        when(
                weaponService.findById(
                        100
                )
        ).thenReturn(weapon);


        when(
                presetService.create(
                        10,
                        "テスト装備",
                        100,
                        null
                )
        ).thenReturn(savedPreset);


        SimulatorForm simulatorForm
                = new SimulatorForm();

        simulatorForm.setWeaponId(
                100
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


    /*
     * =====================================================
     * 弓
     * =====================================================
     */

    @Test
    void save_弓のビンと溜め段階を保存できる() {

        User user
                = createUser();

        Weapon weapon
                = createWeapon(
                        801,
                        "弓"
                );

        Preset savedPreset
                = createPreset(
                        501,
                        801
                );


        BowCoating bowCoating
                = new BowCoating();

        bowCoating.setBowCoatingId(
                81
        );

        bowCoating.setWeaponId(
                801
        );

        bowCoating.setCoatingType(
                "接撃ビン"
        );


        BowCharge bowCharge
                = new BowCharge();

        bowCharge.setBowChargeId(
                94
        );

        bowCharge.setWeaponId(
                801
        );


        Skill skill
                = new Skill();

        skill.setSkillId(
                40
        );


        when(
                userService.findByUsername(
                        "testuser"
                )
        ).thenReturn(user);


        when(
                weaponService.findById(
                        801
                )
        ).thenReturn(weapon);


        when(
                presetService.create(
                        10,
                        "弓装備",
                        801,
                        null
                )
        ).thenReturn(savedPreset);


        /*
         * 護石スキル未選択時は
         * findById(null) が呼ばれるため、
         * null を返すことを明示しておく。
         */
        when(
                skillService.findById(
                        (Integer) null
                )
        ).thenReturn(null);


        when(
                bowCoatingService.findById(
                        81
                )
        ).thenReturn(bowCoating);


        when(
                bowChargeService.findById(
                        94
                )
        ).thenReturn(bowCharge);


        when(
                skillService.findById(
                        40
                )
        ).thenReturn(skill);


        SimulatorForm simulatorForm
                = new SimulatorForm();

        simulatorForm.setWeaponId(
                801
        );

        simulatorForm.setBowCoatingId(
                81
        );

        simulatorForm.setBowChargeId(
                94
        );


        Preset result
                = presetSaveService.save(
                        "testuser",
                        "弓装備",
                        simulatorForm,
                        Map.of(
                                "skillState_40",
                                "OVERCOME"
                        )
                );


        assertSame(
                savedPreset,
                result
        );


        verify(
                presetBowSettingService
        ).create(
                501,
                "接撃ビン",
                94,
                "NORMAL"
        );


        verify(
                presetSkillStateService
        ).create(
                501,
                40,
                "OVERCOME"
        );


        verify(
                presetBowgunSettingService,
                never()
        ).create(
                501,
                "貫通弾",
                2,
                null
        );
    }


    /*
     * =====================================================
     * 弓・竜の一矢
     * =====================================================
     */

    @Test
    void save_竜の一矢攻撃種別を保存できる() {

        User user
                = createUser();

        Weapon weapon
                = createWeapon(
                        800,
                        "弓"
                );

        Preset savedPreset
                = createPreset(
                        505,
                        800
                );


        BowCharge bowCharge
                = new BowCharge();

        bowCharge.setBowChargeId(
                90
        );

        bowCharge.setWeaponId(
                800
        );


        when(
                userService.findByUsername(
                        "testuser"
                )
        ).thenReturn(user);


        when(
                weaponService.findById(
                        800
                )
        ).thenReturn(weapon);


        when(
                presetService.create(
                        10,
                        "竜の一矢装備",
                        800,
                        null
                )
        ).thenReturn(savedPreset);


        when(
                bowChargeService.findById(
                        90
                )
        ).thenReturn(bowCharge);


        SimulatorForm simulatorForm
                = new SimulatorForm();

        simulatorForm.setWeaponId(
                800
        );

        simulatorForm.setBowChargeId(
                90
        );

        simulatorForm.setBowAttackType(
                "DRAGON_PIERCER"
        );


        Preset result
                = presetSaveService.save(
                        "testuser",
                        "竜の一矢装備",
                        simulatorForm,
                        Map.of()
                );


        assertSame(
                savedPreset,
                result
        );


        verify(
                presetBowSettingService
        ).create(
                505,
                null,
                90,
                "DRAGON_PIERCER"
        );
    }


    /*
     * =====================================================
     * ボウガン
     * =====================================================
     */

    @Test
    void save_ボウガンの弾を保存できる() {

        User user
                = createUser();

        Weapon weapon
                = createWeapon(
                        916,
                        "ライトボウガン"
                );

        Preset savedPreset
                = createPreset(
                        502,
                        916
                );


        BowgunAmmo ammo
                = new BowgunAmmo();

        ammo.setBowgunAmmoId(
                1104
        );

        ammo.setWeaponId(
                916
        );

        ammo.setAmmoType(
                "貫通弾"
        );

        ammo.setAmmoLevel(
                2
        );


        when(
                userService.findByUsername(
                        "testuser"
                )
        ).thenReturn(user);


        when(
                weaponService.findById(
                        916
                )
        ).thenReturn(weapon);


        when(
                presetService.create(
                        10,
                        "ボウガン装備",
                        916,
                        null
                )
        ).thenReturn(savedPreset);


        when(
                bowgunAmmoService.findById(
                        1104
                )
        ).thenReturn(ammo);


        SimulatorForm simulatorForm
                = new SimulatorForm();

        simulatorForm.setWeaponId(
                916
        );

        simulatorForm.setBowgunAmmoId(
                1104
        );


        Preset result
                = presetSaveService.save(
                        "testuser",
                        "ボウガン装備",
                        simulatorForm,
                        Map.of()
                );


        assertSame(
                savedPreset,
                result
        );


        verify(
                presetBowgunSettingService
        ).create(
                502,
                "貫通弾",
                2,
                null
        );
    }


    /*
     * =====================================================
     * ライトボウガン・起爆竜弾
     * =====================================================
     */

    @Test
    void save_起爆竜弾の攻撃種別と爆発段階を保存できる() {

        User user
                = createUser();

        Weapon weapon
                = createWeapon(
                        916,
                        "ライトボウガン"
                );

        Preset savedPreset
                = createPreset(
                        506,
                        916
                );


        when(
                userService.findByUsername(
                        "testuser"
                )
        ).thenReturn(user);


        when(
                weaponService.findById(
                        916
                )
        ).thenReturn(weapon);


        when(
                presetService.create(
                        10,
                        "起爆竜弾装備",
                        916,
                        null
                )
        ).thenReturn(savedPreset);


        SimulatorForm simulatorForm
                = new SimulatorForm();

        simulatorForm.setWeaponId(
                916
        );

        simulatorForm.setBowgunAttackType(
                "WYVERNBLAST"
        );

        simulatorForm.setWyvernblastExplosionSize(
                "MEDIUM"
        );

        /*
         * URLに古い弾IDが残っていたとしても、
         * 起爆竜弾では参照されないことも確認する。
         */
        simulatorForm.setBowgunAmmoId(
                1104
        );


        Preset result
                = presetSaveService.save(
                        "testuser",
                        "起爆竜弾装備",
                        simulatorForm,
                        Map.of()
                );


        assertSame(
                savedPreset,
                result
        );


        verify(
                presetBowgunSettingService
        ).create(
                506,
                "WYVERNBLAST",
                "MEDIUM",
                null,
                null,
                null
        );


        verify(
                bowgunAmmoService,
                never()
        ).findById(
                anyInt()
        );
    }


    /*
     * =====================================================
     * 武器傀異錬成
     * =====================================================
     */

    @Test
    void save_武器傀異錬成を保存できる() {

        User user
                = createUser();

        Weapon weapon
                = createWeapon(
                        100,
                        "太刀"
                );

        Preset savedPreset
                = createPreset(
                        503,
                        100
                );


        when(
                userService.findByUsername(
                        "testuser"
                )
        ).thenReturn(user);


        when(
                weaponService.findById(
                        100
                )
        ).thenReturn(weapon);


        when(
                presetService.create(
                        10,
                        "傀異錬成テスト",
                        100,
                        null
                )
        ).thenReturn(savedPreset);


        SimulatorForm simulatorForm
                = new SimulatorForm();

        simulatorForm.setWeaponId(
                100
        );

        simulatorForm.setWeaponAugmentAttackLevel(
                4
        );

        simulatorForm.setWeaponAugmentElementLevel(
                8
        );

        simulatorForm.setWeaponAugmentAffinityLevel(
                3
        );

        simulatorForm.setWeaponAugmentSharpnessLevel(
                4
        );


        Preset result
                = presetSaveService.save(
                        "testuser",
                        "傀異錬成テスト",
                        simulatorForm,
                        Map.of()
                );


        assertSame(
                savedPreset,
                result
        );


        verify(
                presetWeaponAugmentService
        ).create(
                503,
                4,
                8,
                3,
                4
        );
    }


    /*
     * =====================================================
     * 武器傀異錬成・未選択項目は0保存
     * =====================================================
     */

    @Test
    void save_武器傀異錬成の未選択項目を0で保存できる() {

        User user
                = createUser();

        Weapon weapon
                = createWeapon(
                        801,
                        "弓"
                );

        Preset savedPreset
                = createPreset(
                        504,
                        801
                );


        when(
                userService.findByUsername(
                        "testuser"
                )
        ).thenReturn(user);


        when(
                weaponService.findById(
                        801
                )
        ).thenReturn(weapon);


        when(
                presetService.create(
                        10,
                        "弓傀異錬成テスト",
                        801,
                        null
                )
        ).thenReturn(savedPreset);


        SimulatorForm simulatorForm
                = new SimulatorForm();

        simulatorForm.setWeaponId(
                801
        );

        simulatorForm.setWeaponAugmentAttackLevel(
                4
        );

        simulatorForm.setWeaponAugmentElementLevel(
                8
        );

        simulatorForm.setWeaponAugmentAffinityLevel(
                3
        );

        /*
         * 弓では斬れ味強化を選択しないため、
         * sharpnessLevel は null のまま。
         */


        Preset result
                = presetSaveService.save(
                        "testuser",
                        "弓傀異錬成テスト",
                        simulatorForm,
                        Map.of()
                );


        assertSame(
                savedPreset,
                result
        );


        verify(
                presetWeaponAugmentService
        ).create(
                504,
                4,
                8,
                3,
                0
        );
    }


    /*
     * =====================================================
     * プリセット名未入力
     * =====================================================
     */

    @Test
    void save_プリセット名が空なら保存できない() {

        SimulatorForm simulatorForm
                = new SimulatorForm();

        simulatorForm.setWeaponId(
                100
        );


        assertThrows(
                IllegalArgumentException.class,
                () -> presetSaveService.save(
                        "testuser",
                        "",
                        simulatorForm,
                        Map.of()
                )
        );


        verify(
                presetService,
                never()
        ).create(
                10,
                "",
                100,
                null
        );
    }


    /*
     * =====================================================
     * 武器なし
     * =====================================================
     */

    @Test
    void save_存在しない武器なら保存できない() {

        User user
                = createUser();


        when(
                userService.findByUsername(
                        "testuser"
                )
        ).thenReturn(user);


        SimulatorForm simulatorForm
                = new SimulatorForm();

        simulatorForm.setWeaponId(
                999999
        );


        when(
                weaponService.findById(
                        999999
                )
        ).thenReturn(null);


        assertThrows(
                IllegalArgumentException.class,
                () -> presetSaveService.save(
                        "testuser",
                        "不正武器",
                        simulatorForm,
                        Map.of()
                )
        );
    }


    /*
     * =====================================================
     * テスト用共通データ
     * =====================================================
     */

    private User createUser() {

        User user
                = new User();

        user.setUserId(
                10
        );

        user.setUsername(
                "testuser"
        );

        return user;
    }


    private Weapon createWeapon(
            Integer weaponId,
            String weaponType) {

        Weapon weapon
                = new Weapon();

        weapon.setWeaponId(
                weaponId
        );

        weapon.setWeaponType(
                weaponType
        );

        weapon.setSlot1Size(
                0
        );

        weapon.setSlot2Size(
                0
        );

        weapon.setSlot3Size(
                0
        );

        weapon.setRampageSlotSize(
                0
        );

        return weapon;
    }


    private Preset createPreset(
            Integer presetId,
            Integer weaponId) {

        Preset preset
                = new Preset();

        preset.setPresetId(
                presetId
        );

        preset.setUserId(
                10
        );

        preset.setWeaponId(
                weaponId
        );

        return preset;
    }
}
