package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.mhsunbreaksimulator.entity.Preset;
import com.example.mhsunbreaksimulator.entity.PresetArmor;
import com.example.mhsunbreaksimulator.entity.User;
import com.example.mhsunbreaksimulator.service.PresetArmorAugmentSkillService;
import com.example.mhsunbreaksimulator.service.PresetArmorService;
import com.example.mhsunbreaksimulator.service.PresetBowSettingService;
import com.example.mhsunbreaksimulator.service.PresetBowgunSettingService;
import com.example.mhsunbreaksimulator.service.PresetCharmService;
import com.example.mhsunbreaksimulator.service.PresetDecorationService;
import com.example.mhsunbreaksimulator.service.PresetDeleteService;
import com.example.mhsunbreaksimulator.service.PresetService;
import com.example.mhsunbreaksimulator.service.PresetSkillStateService;
import com.example.mhsunbreaksimulator.service.PresetWeaponAugmentService;
import com.example.mhsunbreaksimulator.service.UserService;

@ExtendWith(MockitoExtension.class)
class PresetDeleteServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PresetService presetService;

    @Mock
    private PresetArmorService presetArmorService;

    @Mock
    private PresetArmorAugmentSkillService
            presetArmorAugmentSkillService;

    @Mock
    private PresetDecorationService
            presetDecorationService;

    @Mock
    private PresetSkillStateService
            presetSkillStateService;

    @Mock
    private PresetCharmService
            presetCharmService;

    @Mock
    private PresetBowSettingService
            presetBowSettingService;

    @Mock
    private PresetBowgunSettingService
            presetBowgunSettingService;

    @Mock
    private PresetWeaponAugmentService
            presetWeaponAugmentService;

    @InjectMocks
    private PresetDeleteService presetDeleteService;


    /*
     * =====================================================
     * 正常削除
     * =====================================================
     */

    @Test
    void delete_所有プリセットを子テーブルから順番に削除できる() {

        User user
                = new User();

        user.setUserId(1);
        user.setUsername("testuser");


        Preset preset
                = new Preset();

        preset.setPresetId(10);
        preset.setUserId(1);
        preset.setPresetName("テストプリセット");
        preset.setWeaponId(100);


        PresetArmor headArmor
                = new PresetArmor();

        headArmor.setPresetArmorId(101);
        headArmor.setPresetId(10);
        headArmor.setPart("頭");


        PresetArmor chestArmor
                = new PresetArmor();

        chestArmor.setPresetArmorId(102);
        chestArmor.setPresetId(10);
        chestArmor.setPart("胴");


        when(
                userService.findByUsername(
                        "testuser"
                )
        ).thenReturn(
                user
        );


        when(
                presetService.findByIdAndUserId(
                        10,
                        1
                )
        ).thenReturn(
                preset
        );


        when(
                presetArmorService.findByPresetId(
                        10
                )
        ).thenReturn(
                List.of(
                        headArmor,
                        chestArmor
                )
        );


        when(
                presetService.delete(
                        10,
                        1
                )
        ).thenReturn(
                true
        );


        boolean result
                = presetDeleteService.delete(
                        10,
                        "testuser"
                );


        assertTrue(
                result
        );


        verify(
                presetArmorAugmentSkillService
        ).deleteByPresetArmorId(
                101
        );


        verify(
                presetArmorAugmentSkillService
        ).deleteByPresetArmorId(
                102
        );


        verify(
                presetDecorationService
        ).deleteByPresetId(
                10
        );


        verify(
                presetSkillStateService
        ).deleteByPresetId(
                10
        );


        verify(
                presetCharmService
        ).deleteByPresetId(
                10
        );


        verify(
                presetBowSettingService
        ).deleteByPresetId(
                10
        );


        verify(
                presetBowgunSettingService
        ).deleteByPresetId(
                10
        );


        verify(
                presetWeaponAugmentService
        ).deleteByPresetId(
                10
        );


        verify(
                presetArmorService
        ).deleteByPresetId(
                10
        );


        verify(
                presetService
        ).delete(
                10,
                1
        );


        /*
         * 子 → 親の削除順も確認する。
         */

        InOrder inOrder
                = inOrder(
                        presetArmorAugmentSkillService,
                        presetDecorationService,
                        presetSkillStateService,
                        presetCharmService,
                        presetBowSettingService,
                        presetBowgunSettingService,
                        presetWeaponAugmentService,
                        presetArmorService,
                        presetService
                );


        inOrder.verify(
                presetArmorAugmentSkillService
        ).deleteByPresetArmorId(
                101
        );


        inOrder.verify(
                presetArmorAugmentSkillService
        ).deleteByPresetArmorId(
                102
        );


        inOrder.verify(
                presetDecorationService
        ).deleteByPresetId(
                10
        );


        inOrder.verify(
                presetSkillStateService
        ).deleteByPresetId(
                10
        );


        inOrder.verify(
                presetCharmService
        ).deleteByPresetId(
                10
        );


        inOrder.verify(
                presetBowSettingService
        ).deleteByPresetId(
                10
        );


        inOrder.verify(
                presetBowgunSettingService
        ).deleteByPresetId(
                10
        );


        inOrder.verify(
                presetWeaponAugmentService
        ).deleteByPresetId(
                10
        );


        inOrder.verify(
                presetArmorService
        ).deleteByPresetId(
                10
        );


        inOrder.verify(
                presetService
        ).delete(
                10,
                1
        );
    }


    /*
     * =====================================================
     * 未ログイン相当
     * =====================================================
     */

    @Test
    void delete_usernameが空なら削除しない() {

        boolean result
                = presetDeleteService.delete(
                        10,
                        ""
                );


        assertFalse(
                result
        );


        verify(
                presetService,
                never()
        ).delete(
                10,
                1
        );
    }


    /*
     * =====================================================
     * 存在しないユーザー
     * =====================================================
     */

    @Test
    void delete_ユーザーが存在しなければ削除しない() {

        when(
                userService.findByUsername(
                        "unknown"
                )
        ).thenReturn(
                null
        );


        boolean result
                = presetDeleteService.delete(
                        10,
                        "unknown"
                );


        assertFalse(
                result
        );


        verify(
                presetService,
                never()
        ).findByIdAndUserId(
                10,
                1
        );
    }


    /*
     * =====================================================
     * 他ユーザーのプリセット
     * =====================================================
     */

    @Test
    void delete_自分の所有物でなければ削除しない() {

        User user
                = new User();

        user.setUserId(1);
        user.setUsername("testuser");


        when(
                userService.findByUsername(
                        "testuser"
                )
        ).thenReturn(
                user
        );


        when(
                presetService.findByIdAndUserId(
                        10,
                        1
                )
        ).thenReturn(
                null
        );


        boolean result
                = presetDeleteService.delete(
                        10,
                        "testuser"
                );


        assertFalse(
                result
        );


        verify(
                presetDecorationService,
                never()
        ).deleteByPresetId(
                10
        );


        verify(
                presetArmorService,
                never()
        ).deleteByPresetId(
                10
        );


        verify(
                presetService,
                never()
        ).delete(
                10,
                1
        );
    }
}