package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.dto.MeleeDamageResult;
import com.example.mhsunbreaksimulator.dto.SharpnessModifier;
import com.example.mhsunbreaksimulator.entity.Weapon;
import com.example.mhsunbreaksimulator.entity.WeaponTypeAttack;
import com.example.mhsunbreaksimulator.service.MeleeDamageService;

public class MeleeDamageServiceTest {

    private final MeleeDamageService meleeDamageService
            = new MeleeDamageService();


    /*
     * =====================================================
     * 双剣
     * 連撃Lv2
     * 5ヒット以上
     * =====================================================
     *
     * 刻銀の滅刃
     *
     * 攻撃力：310
     * 龍属性：40
     *
     * 連撃Lv2
     * HIT_5_PLUS
     *
     * 攻撃力 +12
     * 属性値 +8
     *
     *
     * テスト用基準攻撃
     *
     * モーション値：18
     * 属性補正：1.00
     * ヒット数：2
     *
     *
     * 斬れ味倍率は、
     * 今回は条件付きスキルだけを確認するため
     * 物理・属性ともに1.00。
     */
    @Test
    void testDualBladesBurstLevel2Hit5Plus() {

        /*
         * =================================================
         * 武器
         * =================================================
         */
        Weapon weapon
                = new Weapon();

        weapon.setWeaponId(
                263
        );

        weapon.setWeaponName(
                "刻銀の滅刃"
        );

        weapon.setWeaponType(
                "双剣"
        );

        weapon.setAttack(
                310
        );

        weapon.setElementType(
                "龍"
        );

        weapon.setElementValue(
                40
        );


        /*
         * =================================================
         * 基準攻撃
         * =================================================
         */
        WeaponTypeAttack weaponTypeAttack
                = new WeaponTypeAttack();

        weaponTypeAttack.setWeaponType(
                "双剣"
        );

        weaponTypeAttack.setBaseAttackName(
                "テスト用二段攻撃"
        );

        weaponTypeAttack.setAttackProperty(
                "斬"
        );

        weaponTypeAttack.setMotionValue(
                new BigDecimal(
                        "18"
                )
        );

        weaponTypeAttack.setElementModifier(
                new BigDecimal(
                        "1.00"
                )
        );

        weaponTypeAttack.setHitCount(
                2
        );


        /*
         * =================================================
         * 斬れ味
         * =================================================
         *
         * 今回は連撃補正だけを見るため
         * 1.00固定。
         */
        SharpnessModifier sharpnessModifier
                = new SharpnessModifier();

        sharpnessModifier.setSharpnessColor(
                "TEST"
        );

        sharpnessModifier.setPhysicalMultiplier(
                BigDecimal.ONE
        );

        sharpnessModifier.setElementMultiplier(
                BigDecimal.ONE
        );


        /*
         * =================================================
         * 連撃Lv2
         * 5ヒット以上
         * =================================================
         */
        ConditionalSkillModifierTotal conditionalModifier
                = new ConditionalSkillModifierTotal();

        conditionalModifier.setAttackAdd(
                new BigDecimal(
                        "12.000"
                )
        );

        conditionalModifier.setElementAdd(
                new BigDecimal(
                        "8.000"
                )
        );


        /*
         * =================================================
         * 計算
         * =================================================
         */
        MeleeDamageResult result
                = meleeDamageService.calculate(
                        weapon,
                        weaponTypeAttack,
                        sharpnessModifier,
                        conditionalModifier
                );


        /*
         * =================================================
         * 補正後攻撃力
         *
         * 310 + 12
         * = 322
         * =================================================
         */
        assertDecimalEquals(
                "322",
                result.getEffectiveAttack()
        );


        /*
         * =================================================
         * 補正後属性値
         *
         * 40 + 8
         * = 48
         * =================================================
         */
        assertDecimalEquals(
                "48",
                result.getEffectiveElement()
        );


        /*
         * =================================================
         * 肉質適用前物理
         *
         * 322
         * × 18 / 100
         *
         * = 57.96
         * =================================================
         */
        assertDecimalEquals(
                "57.96",
                result.getPhysicalDamageBeforeHitzone()
        );


        /*
         * =================================================
         * 肉質適用前属性
         *
         * 48
         * × 1.00
         * × 2ヒット
         *
         * = 96
         * =================================================
         */
        assertDecimalEquals(
                "96",
                result.getElementDamageBeforeHitzone()
        );


        System.out.println(
                "連撃Lv2 HIT_5_PLUS"
        );

        System.out.println(
                "補正後攻撃力："
                + result.getEffectiveAttack()
        );

        System.out.println(
                "補正後属性値："
                + result.getEffectiveElement()
        );

        System.out.println(
                "肉質前物理："
                + result.getPhysicalDamageBeforeHitzone()
        );

        System.out.println(
                "肉質前属性："
                + result.getElementDamageBeforeHitzone()
        );
    }


    /*
     * =====================================================
     * BigDecimal比較
     * =====================================================
     *
     * 322
     * 322.0
     * 322.000
     *
     * を同じ値として扱う。
     */
    private void assertDecimalEquals(
            String expected,
            BigDecimal actual) {

        assertEquals(
                0,
                new BigDecimal(
                        expected
                ).compareTo(
                        actual
                )
        );
    }
}