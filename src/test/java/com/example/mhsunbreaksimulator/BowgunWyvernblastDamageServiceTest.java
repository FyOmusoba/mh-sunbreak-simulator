package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.BowgunWyvernblastDamageResult;
import com.example.mhsunbreaksimulator.dto.BowgunWyvernblastModifier;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.service.BowgunWyvernblastDamageService;
import com.example.mhsunbreaksimulator.service.BowgunWyvernblastModifierService;

public class BowgunWyvernblastDamageServiceTest {

    private final BowgunWyvernblastDamageService damageService
            = new BowgunWyvernblastDamageService();

    private final BowgunWyvernblastModifierService modifierService
            = new BowgunWyvernblastModifierService();


    /*
     * =====================================================
     * 基準
     * =====================================================
     *
     * 攻撃力300
     * LARGE
     *
     * 300 × 49 ÷ 100
     * = 147
     *
     * 火属性値 = 7
     */
    @Test
    void testLargeBaseline() {

        BowgunWyvernblastModifier modifier
                = modifierService.calculate(
                        "LARGE"
                );


        BowgunWyvernblastDamageResult result
                = damageService.calculate(
                        300,
                        modifier,
                        null
                );


        assertDecimalEquals(
                "300",
                result.getEffectiveAttack()
        );

        assertDecimalEquals(
                "147",
                result.getFixedDamage()
        );

        assertDecimalEquals(
                "7",
                result.getFixedFireValue()
        );

        assertEquals(
                4,
                result.getDurabilityCost()
        );
    }


    /*
     * =====================================================
     * 特殊射撃強化Lv2
     * =====================================================
     *
     * 攻撃力300
     * LARGE MV49
     * specialShotMul = 1.20
     *
     * 300 × 49 ÷ 100 × 1.20
     * = 176.4
     *
     * 火属性値7には
     * specialShotMulを掛けない。
     */
    @Test
    void testSpecialShotBoostLevel2() {

        BowgunWyvernblastModifier modifier
                = modifierService.calculate(
                        "LARGE"
                );


        ConditionalSkillModifierTotal modifierTotal
                = new ConditionalSkillModifierTotal();

        modifierTotal.setSpecialShotMul(
                new BigDecimal(
                        "1.20"
                )
        );


        BowgunWyvernblastDamageResult result
                = damageService.calculate(
                        300,
                        modifier,
                        modifierTotal
                );


        assertDecimalEquals(
                "300",
                result.getEffectiveAttack()
        );

        assertDecimalEquals(
                "176.4",
                result.getFixedDamage()
        );

        /*
         * 特殊射撃強化を付けても
         * 火属性部分は7のまま。
         */
        assertDecimalEquals(
                "7",
                result.getFixedFireValue()
        );

        assertEquals(
                4,
                result.getDurabilityCost()
        );
    }


    /*
     * =====================================================
     * shotMul除外
     * =====================================================
     *
     * 通常弾・貫通弾・散弾などの
     * 通常射撃用倍率は
     * 起爆竜弾には適用しない。
     *
     * shotMulを大きくしても、
     * specialShotMulだけが反映されることを確認。
     */
    @Test
    void testShotMulIsIgnored() {

        BowgunWyvernblastModifier modifier
                = modifierService.calculate(
                        "LARGE"
                );


        ConditionalSkillModifierTotal modifierTotal
                = new ConditionalSkillModifierTotal();

        modifierTotal.setShotMul(
                new BigDecimal(
                        "1.50"
                )
        );

        modifierTotal.setSpecialShotMul(
                new BigDecimal(
                        "1.20"
                )
        );


        BowgunWyvernblastDamageResult result
                = damageService.calculate(
                        300,
                        modifier,
                        modifierTotal
                );


        /*
         * 300 × 49 ÷ 100 × 1.20
         *
         * shotMul 1.50は無視。
         */
        assertDecimalEquals(
                "176.4",
                result.getFixedDamage()
        );

        assertDecimalEquals(
                "7",
                result.getFixedFireValue()
        );
    }


    /*
     * =====================================================
     * 攻撃力加算・倍率
     * =====================================================
     *
     * 攻撃力300
     * attackAdd = 10
     * attackMul = 1.10
     *
     * effectiveAttack
     *
     * = (300 + 10) × 1.10
     * = 341
     *
     *
     * LARGE MV49
     * specialShotMul = 1.20
     *
     * 341 × 49 ÷ 100 × 1.20
     * = 200.508
     */
    @Test
    void testAttackModifierAndSpecialShotBoost() {

        BowgunWyvernblastModifier modifier
                = modifierService.calculate(
                        "LARGE"
                );


        ConditionalSkillModifierTotal modifierTotal
                = new ConditionalSkillModifierTotal();

        modifierTotal.setAttackAdd(
                new BigDecimal(
                        "10"
                )
        );

        modifierTotal.setAttackMul(
                new BigDecimal(
                        "1.10"
                )
        );

        modifierTotal.setSpecialShotMul(
                new BigDecimal(
                        "1.20"
                )
        );


        BowgunWyvernblastDamageResult result
                = damageService.calculate(
                        300,
                        modifier,
                        modifierTotal
                );


        assertDecimalEquals(
                "341",
                result.getEffectiveAttack()
        );

        assertDecimalEquals(
                "200.508",
                result.getFixedDamage()
        );

        assertDecimalEquals(
                "7",
                result.getFixedFireValue()
        );
    }


    /*
     * =====================================================
     * SMALL
     * =====================================================
     *
     * 攻撃力300
     *
     * 300 × 26 ÷ 100
     * = 78
     */
    @Test
    void testSmall() {

        BowgunWyvernblastModifier modifier
                = modifierService.calculate(
                        "SMALL"
                );


        BowgunWyvernblastDamageResult result
                = damageService.calculate(
                        300,
                        modifier,
                        null
                );


        assertDecimalEquals(
                "78",
                result.getFixedDamage()
        );

        assertDecimalEquals(
                "3",
                result.getFixedFireValue()
        );

        assertEquals(
                1,
                result.getDurabilityCost()
        );
    }


    /*
     * =====================================================
     * MEDIUM
     * =====================================================
     *
     * 攻撃力300
     *
     * 300 × 32 ÷ 100
     * = 96
     */
    @Test
    void testMedium() {

        BowgunWyvernblastModifier modifier
                = modifierService.calculate(
                        "MEDIUM"
                );


        BowgunWyvernblastDamageResult result
                = damageService.calculate(
                        300,
                        modifier,
                        null
                );


        assertDecimalEquals(
                "96",
                result.getFixedDamage()
        );

        assertDecimalEquals(
                "5",
                result.getFixedFireValue()
        );

        assertEquals(
                2,
                result.getDurabilityCost()
        );
    }


    /*
     * =====================================================
     * 武器攻撃力null
     * =====================================================
     */
    @Test
    void testNullWeaponAttack() {

        BowgunWyvernblastModifier modifier
                = modifierService.calculate(
                        "LARGE"
                );


        BowgunWyvernblastDamageResult result
                = damageService.calculate(
                        null,
                        modifier,
                        null
                );


        assertDecimalEquals(
                "0",
                result.getEffectiveAttack()
        );

        assertDecimalEquals(
                "0",
                result.getFixedDamage()
        );

        assertDecimalEquals(
                "0",
                result.getFixedFireValue()
        );

        assertEquals(
                0,
                result.getDurabilityCost()
        );
    }


    /*
     * =====================================================
     * Modifier null
     * =====================================================
     */
    @Test
    void testNullModifier() {

        BowgunWyvernblastDamageResult result
                = damageService.calculate(
                        300,
                        null,
                        null
                );


        assertDecimalEquals(
                "0",
                result.getEffectiveAttack()
        );

        assertDecimalEquals(
                "0",
                result.getFixedDamage()
        );

        assertDecimalEquals(
                "0",
                result.getFixedFireValue()
        );

        assertEquals(
                0,
                result.getDurabilityCost()
        );
    }


    /*
     * =====================================================
     * BigDecimal比較
     * =====================================================
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