package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.BowChargeModifier;
import com.example.mhsunbreaksimulator.dto.BowCoatingModifier;
import com.example.mhsunbreaksimulator.dto.BowDragonPiercerModifier;
import com.example.mhsunbreaksimulator.dto.BowPhysicalDamageResult;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.service.BowDragonPiercerDamageService;

public class BowDragonPiercerDamageServiceTest {

    private final BowDragonPiercerDamageService service
            = new BowDragonPiercerDamageService();


    /*
     * =====================================================
     * 基準
     * 特殊射撃強化なし
     * =====================================================
     *
     * 攻撃力300
     * 竜の一矢 溜め4 MV34
     * 溜め補正1.35
     * ビンなし
     *
     * 300 × 0.34 × 1.35
     * = 137.7 / hit
     *
     * 最大7hit
     * = 963.9
     */
    @Test
    void testWithoutSpecialShotBoost() {

        BowDragonPiercerModifier dragonPiercerModifier
                = createDragonPiercerModifier();

        BowChargeModifier chargeModifier
                = createChargeLevel4Modifier();

        BowCoatingModifier coatingModifier
                = new BowCoatingModifier();

        ConditionalSkillModifierTotal conditional
                = new ConditionalSkillModifierTotal();


        BowPhysicalDamageResult result
                = service.calculate(
                        300,
                        dragonPiercerModifier,
                        chargeModifier,
                        coatingModifier,
                        conditional
                );


        assertDecimalEquals(
                "300",
                result.getEffectiveAttack()
        );

        assertDecimalEquals(
                "137.7",
                result.getPhysicalDamagePerHit()
        );

        assertDecimalEquals(
                "963.9",
                result.getMaxPhysicalDamage()
        );


        System.out.println(
                "竜の一矢 基準："
                + "perHit="
                + result.getPhysicalDamagePerHit()
                + " / max="
                + result.getMaxPhysicalDamage()
        );
    }


    /*
     * =====================================================
     * 特殊射撃強化 Lv2
     * =====================================================
     *
     * 基準137.7
     *
     * × 1.20
     *
     * = 165.24 / hit
     *
     * 最大7hit
     * = 1156.68
     */
    @Test
    void testSpecialShotBoostLevel2() {

        BowDragonPiercerModifier dragonPiercerModifier
                = createDragonPiercerModifier();

        BowChargeModifier chargeModifier
                = createChargeLevel4Modifier();

        BowCoatingModifier coatingModifier
                = new BowCoatingModifier();

        ConditionalSkillModifierTotal conditional
                = new ConditionalSkillModifierTotal();

        conditional.setSpecialShotMul(
                new BigDecimal("1.20")
        );


        BowPhysicalDamageResult result
                = service.calculate(
                        300,
                        dragonPiercerModifier,
                        chargeModifier,
                        coatingModifier,
                        conditional
                );


        assertDecimalEquals(
                "165.24",
                result.getPhysicalDamagePerHit()
        );

        assertDecimalEquals(
                "1156.68",
                result.getMaxPhysicalDamage()
        );


        System.out.println(
                "竜の一矢 特殊射撃強化Lv2："
                + "perHit="
                + result.getPhysicalDamagePerHit()
                + " / max="
                + result.getMaxPhysicalDamage()
        );
    }


    /*
     * =====================================================
     * shotMulは竜の一矢へ適用しない
     * =====================================================
     *
     * 通常射撃倍率を異常に大きくしても、
     * 竜の一矢の結果が変わらないことを確認する。
     */
    @Test
    void testNormalShotMultiplierIsIgnored() {

        BowDragonPiercerModifier dragonPiercerModifier
                = createDragonPiercerModifier();

        BowChargeModifier chargeModifier
                = createChargeLevel4Modifier();

        BowCoatingModifier coatingModifier
                = new BowCoatingModifier();

        ConditionalSkillModifierTotal conditional
                = new ConditionalSkillModifierTotal();

        conditional.setShotMul(
                new BigDecimal("9.99")
        );

        conditional.setSpecialShotMul(
                new BigDecimal("1.20")
        );


        BowPhysicalDamageResult result
                = service.calculate(
                        300,
                        dragonPiercerModifier,
                        chargeModifier,
                        coatingModifier,
                        conditional
                );


        /*
         * shotMul=9.99でも
         * specialShotMul=1.20だけが有効。
         */
        assertDecimalEquals(
                "165.24",
                result.getPhysicalDamagePerHit()
        );

        assertDecimalEquals(
                "1156.68",
                result.getMaxPhysicalDamage()
        );


        System.out.println(
                "竜の一矢 shotMul除外："
                + "perHit="
                + result.getPhysicalDamagePerHit()
                + " / max="
                + result.getMaxPhysicalDamage()
        );
    }


    /*
     * =====================================================
     * テスト用
     * 竜の一矢 溜め4
     * =====================================================
     */
    private BowDragonPiercerModifier
            createDragonPiercerModifier() {

        BowDragonPiercerModifier modifier
                = new BowDragonPiercerModifier();

        modifier.setMotionValuePerHit(
                new BigDecimal("34")
        );

        modifier.setMaxHitCount(
                7
        );

        return modifier;
    }


    /*
     * =====================================================
     * テスト用
     * 溜め4
     * =====================================================
     */
    private BowChargeModifier
            createChargeLevel4Modifier() {

        BowChargeModifier modifier
                = new BowChargeModifier();

        modifier.setPhysicalMul(
                new BigDecimal("1.35")
        );

        return modifier;
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