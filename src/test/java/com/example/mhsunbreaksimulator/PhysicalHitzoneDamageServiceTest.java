package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.CriticalModifierResult;
import com.example.mhsunbreaksimulator.dto.PhysicalHitzoneDamageResult;
import com.example.mhsunbreaksimulator.service.PhysicalHitzoneDamageService;

public class PhysicalHitzoneDamageServiceTest {

    private final PhysicalHitzoneDamageService
            physicalHitzoneDamageService
            = new PhysicalHitzoneDamageService();


    /*
     * =====================================================
     * 肉質45
     * =====================================================
     *
     * 肉質適用前100
     * ×
     * 0.45
     *
     * = 45
     */
    @Test
    void testPhysicalHitzone45() {

        CriticalModifierResult criticalModifier
                = new CriticalModifierResult();


        PhysicalHitzoneDamageResult result
                = physicalHitzoneDamageService.calculate(
                        new BigDecimal("100"),
                        45,
                        criticalModifier
                );


        assertEquals(
                45,
                result.getPhysicalHitzone()
        );

        assertDecimalEquals(
                "0.45",
                result.getPhysicalHitzoneRate()
        );

        assertDecimalEquals(
                "45",
                result.getNormalPhysicalDamagePerHit()
        );


        System.out.println(
                "肉質45："
                + "rate="
                + result.getPhysicalHitzoneRate()
                + " / normal="
                + result.getNormalPhysicalDamagePerHit()
        );
    }


    /*
     * =====================================================
     * 通常会心
     * =====================================================
     *
     * 100
     * × 0.45
     * × 1.25
     *
     * = 56.25
     */
    @Test
    void testNormalPositiveCritical() {

        CriticalModifierResult criticalModifier
                = new CriticalModifierResult();

        criticalModifier.setPositiveCriticalDamageMul(
                new BigDecimal("1.25")
        );


        PhysicalHitzoneDamageResult result
                = physicalHitzoneDamageService.calculate(
                        new BigDecimal("100"),
                        45,
                        criticalModifier
                );


        assertDecimalEquals(
                "45",
                result.getNormalPhysicalDamagePerHit()
        );

        assertDecimalEquals(
                "1.25",
                result.getPositiveCriticalDamageMul()
        );

        assertDecimalEquals(
                "56.25",
                result.getPositiveCriticalPhysicalDamagePerHit()
        );


        System.out.println(
                "通常会心："
                + "normal="
                + result.getNormalPhysicalDamagePerHit()
                + " / critical="
                + result.getPositiveCriticalPhysicalDamagePerHit()
        );
    }


    /*
     * =====================================================
     * 超会心Lv3
     * =====================================================
     *
     * 100
     * × 0.45
     * × 1.40
     *
     * = 63
     */
    @Test
    void testCriticalBoostLevel3() {

        CriticalModifierResult criticalModifier
                = new CriticalModifierResult();

        criticalModifier.setPositiveCriticalDamageMul(
                new BigDecimal("1.40")
        );


        PhysicalHitzoneDamageResult result
                = physicalHitzoneDamageService.calculate(
                        new BigDecimal("100"),
                        45,
                        criticalModifier
                );


        assertDecimalEquals(
                "1.40",
                result.getPositiveCriticalDamageMul()
        );

        assertDecimalEquals(
                "63",
                result.getPositiveCriticalPhysicalDamagePerHit()
        );


        System.out.println(
                "超会心Lv3："
                + "critical="
                + result.getPositiveCriticalPhysicalDamagePerHit()
        );
    }


    /*
     * =====================================================
     * マイナス会心
     * =====================================================
     *
     * 100
     * × 0.45
     * × 0.75
     *
     * = 33.75
     */
    @Test
    void testNegativeCritical() {

        CriticalModifierResult criticalModifier
                = new CriticalModifierResult();

        criticalModifier.setNegativeCriticalDamageMul(
                new BigDecimal("0.75")
        );


        PhysicalHitzoneDamageResult result
                = physicalHitzoneDamageService.calculate(
                        new BigDecimal("100"),
                        45,
                        criticalModifier
                );


        assertDecimalEquals(
                "0.75",
                result.getNegativeCriticalDamageMul()
        );

        assertDecimalEquals(
                "33.75",
                result.getNegativeCriticalPhysicalDamagePerHit()
        );


        System.out.println(
                "マイナス会心："
                + "negativeCritical="
                + result.getNegativeCriticalPhysicalDamagePerHit()
        );
    }


    /*
     * =====================================================
     * 肉質70
     * =====================================================
     *
     * 斬弱点などの確認。
     *
     * 120
     * × 0.70
     *
     * = 84
     */
    @Test
    void testPhysicalHitzone70() {

        CriticalModifierResult criticalModifier
                = new CriticalModifierResult();


        PhysicalHitzoneDamageResult result
                = physicalHitzoneDamageService.calculate(
                        new BigDecimal("120"),
                        70,
                        criticalModifier
                );


        assertDecimalEquals(
                "0.70",
                result.getPhysicalHitzoneRate()
        );

        assertDecimalEquals(
                "84",
                result.getNormalPhysicalDamagePerHit()
        );


        System.out.println(
                "肉質70：normal="
                + result.getNormalPhysicalDamagePerHit()
        );
    }


    /*
     * =====================================================
     * 小数を途中で切り捨てない
     * =====================================================
     *
     * 123.45
     * × 0.45
     *
     * = 55.5525
     *
     * この段階で55へ丸めないことを確認。
     */
    @Test
    void testDecimalIsPreserved() {

        CriticalModifierResult criticalModifier
                = new CriticalModifierResult();

        criticalModifier.setPositiveCriticalDamageMul(
                new BigDecimal("1.40")
        );

        criticalModifier.setNegativeCriticalDamageMul(
                new BigDecimal("0.75")
        );


        PhysicalHitzoneDamageResult result
                = physicalHitzoneDamageService.calculate(
                        new BigDecimal("123.45"),
                        45,
                        criticalModifier
                );


        assertDecimalEquals(
                "55.5525",
                result.getNormalPhysicalDamagePerHit()
        );

        assertDecimalEquals(
                "77.77350",
                result.getPositiveCriticalPhysicalDamagePerHit()
        );

        assertDecimalEquals(
                "41.664375",
                result.getNegativeCriticalPhysicalDamagePerHit()
        );


        System.out.println(
                "小数保持："
                + "normal="
                + result.getNormalPhysicalDamagePerHit()
                + " / positive="
                + result.getPositiveCriticalPhysicalDamagePerHit()
                + " / negative="
                + result.getNegativeCriticalPhysicalDamagePerHit()
        );
    }


    /*
     * =====================================================
     * 肉質0
     * =====================================================
     *
     * 肉質が0なら、
     * 通常・会心・マイナス会心すべて0。
     */
    @Test
    void testHitzoneZero() {

        CriticalModifierResult criticalModifier
                = new CriticalModifierResult();

        criticalModifier.setPositiveCriticalDamageMul(
                new BigDecimal("1.40")
        );


        PhysicalHitzoneDamageResult result
                = physicalHitzoneDamageService.calculate(
                        new BigDecimal("100"),
                        0,
                        criticalModifier
                );


        assertDecimalEquals(
                "0",
                result.getPhysicalHitzoneRate()
        );

        assertDecimalEquals(
                "0",
                result.getNormalPhysicalDamagePerHit()
        );

        assertDecimalEquals(
                "0",
                result.getPositiveCriticalPhysicalDamagePerHit()
        );

        assertDecimalEquals(
                "0",
                result.getNegativeCriticalPhysicalDamagePerHit()
        );


        System.out.println(
                "肉質0：全物理ダメージ0 正常"
        );
    }


    /*
     * =====================================================
     * physicalDamageBeforeHitzone = null
     * =====================================================
     */
    @Test
    void testNullPhysicalDamage() {

        CriticalModifierResult criticalModifier
                = new CriticalModifierResult();


        PhysicalHitzoneDamageResult result
                = physicalHitzoneDamageService.calculate(
                        null,
                        45,
                        criticalModifier
                );


        assertDecimalEquals(
                "0",
                result.getPhysicalDamageBeforeHitzone()
        );

        assertDecimalEquals(
                "0",
                result.getNormalPhysicalDamagePerHit()
        );

        assertDecimalEquals(
                "0",
                result.getPositiveCriticalPhysicalDamagePerHit()
        );

        assertDecimalEquals(
                "0",
                result.getNegativeCriticalPhysicalDamagePerHit()
        );


        System.out.println(
                "physicalDamageBeforeHitzone=null：正常"
        );
    }


    /*
     * =====================================================
     * physicalHitzone = null
     * =====================================================
     */
    @Test
    void testNullHitzone() {

        CriticalModifierResult criticalModifier
                = new CriticalModifierResult();


        PhysicalHitzoneDamageResult result
                = physicalHitzoneDamageService.calculate(
                        new BigDecimal("100"),
                        null,
                        criticalModifier
                );


        assertEquals(
                0,
                result.getPhysicalHitzone()
        );

        assertDecimalEquals(
                "0",
                result.getPhysicalHitzoneRate()
        );

        assertDecimalEquals(
                "0",
                result.getNormalPhysicalDamagePerHit()
        );


        System.out.println(
                "physicalHitzone=null：正常"
        );
    }


    /*
     * =====================================================
     * CriticalModifierResult = null
     * =====================================================
     *
     * nullでもデフォルト値
     *
     * プラス会心 1.25
     * マイナス会心 0.75
     *
     * を使用する。
     */
    @Test
    void testNullCriticalModifier() {

        PhysicalHitzoneDamageResult result
                = physicalHitzoneDamageService.calculate(
                        new BigDecimal("100"),
                        45,
                        null
                );


        assertDecimalEquals(
                "45",
                result.getNormalPhysicalDamagePerHit()
        );

        assertDecimalEquals(
                "1.25",
                result.getPositiveCriticalDamageMul()
        );

        assertDecimalEquals(
                "56.25",
                result.getPositiveCriticalPhysicalDamagePerHit()
        );

        assertDecimalEquals(
                "0.75",
                result.getNegativeCriticalDamageMul()
        );

        assertDecimalEquals(
                "33.75",
                result.getNegativeCriticalPhysicalDamagePerHit()
        );


        System.out.println(
                "criticalModifier=null："
                + "normal="
                + result.getNormalPhysicalDamagePerHit()
                + " / positive="
                + result.getPositiveCriticalPhysicalDamagePerHit()
                + " / negative="
                + result.getNegativeCriticalPhysicalDamagePerHit()
        );
    }


    /*
     * =====================================================
     * CriticalModifier内の倍率 = null
     * =====================================================
     *
     * DTOの値自体がnullでも、
     *
     * positive → 1.25
     * negative → 0.75
     *
     * に戻ることを確認。
     */
    @Test
    void testNullCriticalMultipliers() {

        CriticalModifierResult criticalModifier
                = new CriticalModifierResult();

        criticalModifier.setPositiveCriticalDamageMul(
                null
        );

        criticalModifier.setNegativeCriticalDamageMul(
                null
        );


        PhysicalHitzoneDamageResult result
                = physicalHitzoneDamageService.calculate(
                        new BigDecimal("100"),
                        45,
                        criticalModifier
                );


        assertDecimalEquals(
                "1.25",
                result.getPositiveCriticalDamageMul()
        );

        assertDecimalEquals(
                "0.75",
                result.getNegativeCriticalDamageMul()
        );

        assertDecimalEquals(
                "56.25",
                result.getPositiveCriticalPhysicalDamagePerHit()
        );

        assertDecimalEquals(
                "33.75",
                result.getNegativeCriticalPhysicalDamagePerHit()
        );


        System.out.println(
                "会心倍率null："
                + "positiveMul="
                + result.getPositiveCriticalDamageMul()
                + " / negativeMul="
                + result.getNegativeCriticalDamageMul()
        );
    }


    /*
     * =====================================================
     * BigDecimal比較
     * =====================================================
     *
     * 45
     * 45.0
     * 45.00
     *
     * のscale差を無視して比較する。
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