package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.SharpnessModifier;
import com.example.mhsunbreaksimulator.entity.Weapon;
import com.example.mhsunbreaksimulator.service.SharpnessModifierService;

public class SharpnessModifierServiceTest {

    private final SharpnessModifierService sharpnessModifierService
            = new SharpnessModifierService();


    /*
     * =====================================================
     * 通常の斬れ味倍率
     * =====================================================
     */

    @Test
    void testPurpleSharpness() {

        SharpnessModifier result
                = sharpnessModifierService.calculate(
                        "紫"
                );


        assertEquals(
                "紫",
                result.getSharpnessColor()
        );


        assertEquals(
                0,
                result.getPhysicalMultiplier()
                        .compareTo(
                                new BigDecimal("1.39")
                        )
        );


        assertEquals(
                0,
                result.getElementMultiplier()
                        .compareTo(
                                new BigDecimal("1.25")
                        )
        );
    }


    @Test
    void testWhiteSharpness() {

        SharpnessModifier result
                = sharpnessModifierService.calculate(
                        "白"
                );


        assertEquals(
                0,
                result.getPhysicalMultiplier()
                        .compareTo(
                                new BigDecimal("1.32")
                        )
        );


        assertEquals(
                0,
                result.getElementMultiplier()
                        .compareTo(
                                new BigDecimal("1.15")
                        )
        );
    }


    @Test
    void testBlueSharpness() {

        SharpnessModifier result
                = sharpnessModifierService.calculate(
                        "青"
                );


        assertEquals(
                0,
                result.getPhysicalMultiplier()
                        .compareTo(
                                new BigDecimal("1.20")
                        )
        );


        assertEquals(
                0,
                result.getElementMultiplier()
                        .compareTo(
                                new BigDecimal("1.0625")
                        )
        );
    }


    @Test
    void testGreenSharpness() {

        SharpnessModifier result
                = sharpnessModifierService.calculate(
                        "緑"
                );


        assertEquals(
                0,
                result.getPhysicalMultiplier()
                        .compareTo(
                                new BigDecimal("1.05")
                        )
        );


        assertEquals(
                0,
                result.getElementMultiplier()
                        .compareTo(
                                BigDecimal.ONE
                        )
        );
    }


    @Test
    void testYellowSharpness() {

        SharpnessModifier result
                = sharpnessModifierService.calculate(
                        "黄"
                );


        assertEquals(
                0,
                result.getPhysicalMultiplier()
                        .compareTo(
                                BigDecimal.ONE
                        )
        );


        assertEquals(
                0,
                result.getElementMultiplier()
                        .compareTo(
                                new BigDecimal("0.75")
                        )
        );
    }


    @Test
    void testOrangeSharpness() {

        SharpnessModifier result
                = sharpnessModifierService.calculate(
                        "橙"
                );


        assertEquals(
                0,
                result.getPhysicalMultiplier()
                        .compareTo(
                                new BigDecimal("0.75")
                        )
        );


        assertEquals(
                0,
                result.getElementMultiplier()
                        .compareTo(
                                new BigDecimal("0.50")
                        )
        );
    }


    @Test
    void testRedSharpness() {

        SharpnessModifier result
                = sharpnessModifierService.calculate(
                        "赤"
                );


        assertEquals(
                0,
                result.getPhysicalMultiplier()
                        .compareTo(
                                new BigDecimal("0.50")
                        )
        );


        assertEquals(
                0,
                result.getElementMultiplier()
                        .compareTo(
                                new BigDecimal("0.25")
                        )
        );
    }


    @Test
    void testNullSharpness() {

        SharpnessModifier result
                = sharpnessModifierService.calculate(
                        null
                );


        assertEquals(
                BigDecimal.ONE,
                result.getPhysicalMultiplier()
        );


        assertEquals(
                BigDecimal.ONE,
                result.getElementMultiplier()
        );
    }


    /*
     * =====================================================
     * 武器傀異錬成による斬れ味色変化
     * =====================================================
     *
     * フルマーク改相当
     *
     * 最大斬れ味：白
     * takumiVal1：20
     * takumiVal2：30
     *
     * +20 → 白
     * +30 → 紫
     * +50 → 紫
     * =====================================================
     */

    @Test
    void testAugmentedSharpnessPlus20StaysWhite() {

        Weapon weapon
                = createFullMarkLikeWeapon();


        String result
                = sharpnessModifierService
                        .calculateAugmentedSharpnessColor(
                                weapon,
                                20
                        );


        assertEquals(
                "白",
                result
        );
    }


    @Test
    void testAugmentedSharpnessPlus30BecomesPurple() {

        Weapon weapon
                = createFullMarkLikeWeapon();


        String result
                = sharpnessModifierService
                        .calculateAugmentedSharpnessColor(
                                weapon,
                                30
                        );


        assertEquals(
                "紫",
                result
        );
    }


    @Test
    void testAugmentedSharpnessPlus50BecomesPurple() {

        Weapon weapon
                = createFullMarkLikeWeapon();


        String result
                = sharpnessModifierService
                        .calculateAugmentedSharpnessColor(
                                weapon,
                                50
                        );


        assertEquals(
                "紫",
                result
        );
    }


    @Test
    void testAugmentedSharpnessAlreadyPurpleStaysPurple() {

        Weapon weapon
                = new Weapon();

        weapon.setMaxSharpnessColor(
                "紫"
        );

        weapon.setTakumiVal1(
                50
        );


        String result
                = sharpnessModifierService
                        .calculateAugmentedSharpnessColor(
                                weapon,
                                50
                        );


        assertEquals(
                "紫",
                result
        );
    }


    @Test
    void testAugmentedSharpnessWithoutTakumiDataStaysCurrentColor() {

        Weapon weapon
                = new Weapon();

        weapon.setMaxSharpnessColor(
                "白"
        );

        weapon.setTakumiVal1(
                0
        );

        weapon.setTakumiVal2(
                0
        );

        weapon.setTakumiVal3(
                0
        );

        weapon.setTakumiVal4(
                0
        );


        String result
                = sharpnessModifierService
                        .calculateAugmentedSharpnessColor(
                                weapon,
                                50
                        );


        assertEquals(
                "白",
                result
        );
    }


    /*
     * =====================================================
     * テスト用
     * フルマーク改相当武器
     * =====================================================
     */
    private Weapon createFullMarkLikeWeapon() {

        Weapon weapon
                = new Weapon();

        weapon.setMaxSharpnessColor(
                "白"
        );

        weapon.setTakumiVal1(
                20
        );

        weapon.setTakumiVal2(
                30
        );

        weapon.setTakumiVal3(
                0
        );

        weapon.setTakumiVal4(
                0
        );


        return weapon;
    }
}