package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.WeaponAugmentModifier;
import com.example.mhsunbreaksimulator.entity.Weapon;
import com.example.mhsunbreaksimulator.service.WeaponAugmentModifierService;

class WeaponAugmentModifierServiceTest {

    private final WeaponAugmentModifierService service
            = new WeaponAugmentModifierService();


    /*
     * =====================================================
     * null
     * =====================================================
     */

    @Test
    void calculate_武器nullなら全補正0() {

        WeaponAugmentModifier result
                = service.calculate(
                        null,
                        4,
                        8,
                        3,
                        4
                );


        assertEquals(
                0,
                result.getAttackAdd()
        );

        assertEquals(
                0,
                result.getElementAdd()
        );

        assertEquals(
                0,
                result.getSubElementAdd()
        );

        assertEquals(
                0,
                result.getAffinityAdd()
        );

        assertEquals(
                0,
                result.getSharpnessAdd()
        );
    }


    /*
     * =====================================================
     * 太刀
     * =====================================================
     */

    @Test
    void calculate_太刀の最大強化を計算できる() {

        Weapon weapon
                = new Weapon();

        weapon.setWeaponType(
                "太刀"
        );

        weapon.setElementType(
                "火"
        );


        WeaponAugmentModifier result
                = service.calculate(
                        weapon,
                        4,
                        8,
                        3,
                        4
                );


        assertEquals(
                20,
                result.getAttackAdd()
        );

        assertEquals(
                35,
                result.getElementAdd()
        );

        assertEquals(
                15,
                result.getAffinityAdd()
        );

        assertEquals(
                50,
                result.getSharpnessAdd()
        );
    }


    /*
     * =====================================================
     * 大剣
     * =====================================================
     */

    @Test
    void calculate_大剣は属性強化量が大きい() {

        Weapon weapon
                = new Weapon();

        weapon.setWeaponType(
                "大剣"
        );

        weapon.setElementType(
                "龍"
        );


        WeaponAugmentModifier result
                = service.calculate(
                        weapon,
                        0,
                        8,
                        0,
                        0
                );


        assertEquals(
                55,
                result.getElementAdd()
        );
    }


    /*
     * =====================================================
     * 弓
     * =====================================================
     */

    @Test
    void calculate_弓は斬れ味強化されない() {

        Weapon weapon
                = new Weapon();

        weapon.setWeaponType(
                "弓"
        );

        weapon.setElementType(
                "火"
        );


        WeaponAugmentModifier result
                = service.calculate(
                        weapon,
                        4,
                        8,
                        3,
                        4
                );


        assertEquals(
                20,
                result.getAttackAdd()
        );

        assertEquals(
                35,
                result.getElementAdd()
        );

        assertEquals(
                15,
                result.getAffinityAdd()
        );

        assertEquals(
                0,
                result.getSharpnessAdd()
        );
    }


    /*
     * =====================================================
     * ボウガン
     * =====================================================
     */

    @Test
    void calculate_ライトボウガンは属性強化と斬れ味強化を使用しない() {

        Weapon weapon
                = new Weapon();

        weapon.setWeaponType(
                "ライトボウガン"
        );

        weapon.setElementType(
                "火"
        );


        WeaponAugmentModifier result
                = service.calculate(
                        weapon,
                        4,
                        8,
                        3,
                        4
                );


        assertEquals(
                20,
                result.getAttackAdd()
        );

        assertEquals(
                0,
                result.getElementAdd()
        );

        assertEquals(
                15,
                result.getAffinityAdd()
        );

        assertEquals(
                0,
                result.getSharpnessAdd()
        );
    }


    /*
     * =====================================================
     * 双剣
     * 属性 + 状態異常
     * =====================================================
     */

    @Test
    void calculate_双剣の属性状態異常混合を計算できる() {

        Weapon weapon
                = new Weapon();

        weapon.setWeaponType(
                "双剣"
        );

        weapon.setElementType(
                "火"
        );

        weapon.setSubElementType(
                "毒"
        );


        WeaponAugmentModifier result
                = service.calculate(
                        weapon,
                        0,
                        8,
                        0,
                        0
                );


        /*
         * 混合双剣は最大Lv4として扱う。
         */
        assertEquals(
                12,
                result.getElementAdd()
        );

        assertEquals(
                12,
                result.getSubElementAdd()
        );
    }


    /*
     * =====================================================
     * Lv上下限
     * =====================================================
     */

    @Test
    void calculate_範囲外Lvは安全に補正される() {

        Weapon weapon
                = new Weapon();

        weapon.setWeaponType(
                "太刀"
        );

        weapon.setElementType(
                "雷"
        );


        WeaponAugmentModifier result
                = service.calculate(
                        weapon,
                        999,
                        999,
                        999,
                        999
                );


        assertEquals(
                20,
                result.getAttackAdd()
        );

        assertEquals(
                35,
                result.getElementAdd()
        );

        assertEquals(
                15,
                result.getAffinityAdd()
        );

        assertEquals(
                50,
                result.getSharpnessAdd()
        );
    }
}