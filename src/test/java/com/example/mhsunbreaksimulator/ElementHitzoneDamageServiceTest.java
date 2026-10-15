package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.CriticalModifierResult;
import com.example.mhsunbreaksimulator.dto.ElementHitzoneDamageResult;
import com.example.mhsunbreaksimulator.service.ElementHitzoneDamageService;

public class ElementHitzoneDamageServiceTest {

    private final ElementHitzoneDamageService
            elementHitzoneDamageService
            = new ElementHitzoneDamageService();


    /*
     * =====================================================
     * 属性肉質20
     * =====================================================
     *
     * 肉質適用前100
     * ×
     * 0.20
     *
     * = 20
     */
    @Test
    void testElementHitzone20() {

        ElementHitzoneDamageResult result
                = elementHitzoneDamageService.calculate(
                        new BigDecimal("100"),
                        20,
                        BigDecimal.ONE,
                        null
                );


        assertEquals(
                20,
                result.getElementHitzone()
        );

        assertDecimalEquals(
                "0.20",
                result.getElementHitzoneRate()
        );

        assertDecimalEquals(
                "20",
                result.getNormalElementDamagePerHit()
        );


        System.out.println(
                "属性肉質20："
                + "rate="
                + result.getElementHitzoneRate()
                + " / normal="
                + result.getNormalElementDamagePerHit()
        );
    }


    /*
     * =====================================================
     * 弱点特効【属性】Lv3
     * =====================================================
     *
     * 100
     * × 0.20
     * × 1.15
     *
     * = 23
     */
    @Test
    void testElementExploitLevel3() {

        ElementHitzoneDamageResult result
                = elementHitzoneDamageService.calculate(
                        new BigDecimal("100"),
                        20,
                        new BigDecimal("1.15"),
                        null
                );


        assertDecimalEquals(
                "1.15",
                result.getWeakpointElementDamageMul()
        );

        assertDecimalEquals(
                "23",
                result.getNormalElementDamagePerHit()
        );


        System.out.println(
                "弱点特効【属性】Lv3："
                + "normal="
                + result.getNormalElementDamagePerHit()
        );
    }


    /*
     * =====================================================
     * 会心撃【属性】Lv3
     * =====================================================
     *
     * 弱点特効【属性】なし
     *
     * 100
     * × 0.20
     * = 20
     *
     * 会心撃【属性】Lv3
     *
     * 20
     * × 1.15
     * = 23
     */
    @Test
    void testCriticalElementLevel3() {

        CriticalModifierResult criticalModifier
                = new CriticalModifierResult();

        criticalModifier.setPositiveElementCriticalDamageMul(
                new BigDecimal("1.15")
        );


        ElementHitzoneDamageResult result
                = elementHitzoneDamageService.calculate(
                        new BigDecimal("100"),
                        20,
                        BigDecimal.ONE,
                        criticalModifier
                );


        assertDecimalEquals(
                "20",
                result.getNormalElementDamagePerHit()
        );

        assertDecimalEquals(
                "1.15",
                result.getPositiveElementCriticalDamageMul()
        );

        assertDecimalEquals(
                "23",
                result.getPositiveCriticalElementDamagePerHit()
        );


        System.out.println(
                "会心撃【属性】Lv3："
                + "normal="
                + result.getNormalElementDamagePerHit()
                + " / critical="
                + result.getPositiveCriticalElementDamagePerHit()
        );
    }


    /*
     * =====================================================
     * 弱点特効【属性】Lv3
     * ＋
     * 会心撃【属性】Lv3
     * =====================================================
     *
     * 100
     * × 0.20
     * × 1.15
     *
     * = 23
     *
     * 23
     * × 1.15
     *
     * = 26.45
     */
    @Test
    void testElementExploitAndCriticalElement() {

        CriticalModifierResult criticalModifier
                = new CriticalModifierResult();

        criticalModifier.setPositiveElementCriticalDamageMul(
                new BigDecimal("1.15")
        );


        ElementHitzoneDamageResult result
                = elementHitzoneDamageService.calculate(
                        new BigDecimal("100"),
                        20,
                        new BigDecimal("1.15"),
                        criticalModifier
                );


        assertDecimalEquals(
                "23",
                result.getNormalElementDamagePerHit()
        );

        assertDecimalEquals(
                "26.45",
                result.getPositiveCriticalElementDamagePerHit()
        );


        System.out.println(
                "弱点特効【属性】Lv3"
                + "＋会心撃【属性】Lv3："
                + "normal="
                + result.getNormalElementDamagePerHit()
                + " / critical="
                + result.getPositiveCriticalElementDamagePerHit()
        );
    }


    /*
     * =====================================================
     * 通常会心だけでは属性ダメージは増えない
     * =====================================================
     *
     * 会心撃【属性】なし
     * ↓
     * 属性会心倍率1.0
     */
    @Test
    void testNormalCriticalDoesNotIncreaseElementDamage() {

        CriticalModifierResult criticalModifier
                = new CriticalModifierResult();

        /*
         * 物理側の会心倍率を1.40にしても、
         * 属性計算では使用しない。
         */
        criticalModifier.setPositiveCriticalDamageMul(
                new BigDecimal("1.40")
        );

        criticalModifier.setPositiveElementCriticalDamageMul(
                BigDecimal.ONE
        );


        ElementHitzoneDamageResult result
                = elementHitzoneDamageService.calculate(
                        new BigDecimal("100"),
                        20,
                        BigDecimal.ONE,
                        criticalModifier
                );


        assertDecimalEquals(
                "20",
                result.getNormalElementDamagePerHit()
        );

        assertDecimalEquals(
                "20",
                result.getPositiveCriticalElementDamagePerHit()
        );


        System.out.println(
                "通常会心のみ："
                + "normal="
                + result.getNormalElementDamagePerHit()
                + " / criticalElement="
                + result.getPositiveCriticalElementDamagePerHit()
        );
    }


    /*
     * =====================================================
     * 小数を途中で切り捨てない
     * =====================================================
     *
     * 123.45
     * × 0.20
     * × 1.125
     *
     * = 27.77625
     *
     * さらに属性会心1.10
     *
     * = 30.553875
     */
    @Test
    void testDecimalIsPreserved() {

        CriticalModifierResult criticalModifier
                = new CriticalModifierResult();

        criticalModifier.setPositiveElementCriticalDamageMul(
                new BigDecimal("1.10")
        );


        ElementHitzoneDamageResult result
                = elementHitzoneDamageService.calculate(
                        new BigDecimal("123.45"),
                        20,
                        new BigDecimal("1.125"),
                        criticalModifier
                );


        assertDecimalEquals(
                "27.77625",
                result.getNormalElementDamagePerHit()
        );

        assertDecimalEquals(
                "30.553875",
                result.getPositiveCriticalElementDamagePerHit()
        );


        System.out.println(
                "属性小数保持："
                + "normal="
                + result.getNormalElementDamagePerHit()
                + " / critical="
                + result.getPositiveCriticalElementDamagePerHit()
        );
    }


    /*
     * =====================================================
     * 属性肉質0
     * =====================================================
     */
    @Test
    void testElementHitzoneZero() {

        CriticalModifierResult criticalModifier
                = new CriticalModifierResult();

        criticalModifier.setPositiveElementCriticalDamageMul(
                new BigDecimal("1.15")
        );


        ElementHitzoneDamageResult result
                = elementHitzoneDamageService.calculate(
                        new BigDecimal("100"),
                        0,
                        new BigDecimal("1.15"),
                        criticalModifier
                );


        assertDecimalEquals(
                "0",
                result.getElementHitzoneRate()
        );

        assertDecimalEquals(
                "0",
                result.getNormalElementDamagePerHit()
        );

        assertDecimalEquals(
                "0",
                result.getPositiveCriticalElementDamagePerHit()
        );


        System.out.println(
                "属性肉質0：全属性ダメージ0 正常"
        );
    }


    /*
     * =====================================================
     * elementDamageBeforeHitzone = null
     * =====================================================
     */
    @Test
    void testNullElementDamage() {

        ElementHitzoneDamageResult result
                = elementHitzoneDamageService.calculate(
                        null,
                        20,
                        new BigDecimal("1.15"),
                        null
                );


        assertDecimalEquals(
                "0",
                result.getElementDamageBeforeHitzone()
        );

        assertDecimalEquals(
                "0",
                result.getNormalElementDamagePerHit()
        );

        assertDecimalEquals(
                "0",
                result.getPositiveCriticalElementDamagePerHit()
        );


        System.out.println(
                "elementDamageBeforeHitzone=null：正常"
        );
    }


    /*
     * =====================================================
     * elementHitzone = null
     * =====================================================
     */
    @Test
    void testNullElementHitzone() {

        ElementHitzoneDamageResult result
                = elementHitzoneDamageService.calculate(
                        new BigDecimal("100"),
                        null,
                        BigDecimal.ONE,
                        null
                );


        assertEquals(
                0,
                result.getElementHitzone()
        );

        assertDecimalEquals(
                "0",
                result.getElementHitzoneRate()
        );

        assertDecimalEquals(
                "0",
                result.getNormalElementDamagePerHit()
        );


        System.out.println(
                "elementHitzone=null：正常"
        );
    }


    /*
     * =====================================================
     * 弱点特効【属性】倍率 = null
     * =====================================================
     *
     * nullなら1.0。
     */
    @Test
    void testNullWeakpointElementMultiplier() {

        ElementHitzoneDamageResult result
                = elementHitzoneDamageService.calculate(
                        new BigDecimal("100"),
                        20,
                        null,
                        null
                );


        assertDecimalEquals(
                "1",
                result.getWeakpointElementDamageMul()
        );

        assertDecimalEquals(
                "20",
                result.getNormalElementDamagePerHit()
        );


        System.out.println(
                "弱点特効【属性】倍率null："
                + "mul="
                + result.getWeakpointElementDamageMul()
                + " / normal="
                + result.getNormalElementDamagePerHit()
        );
    }


    /*
     * =====================================================
     * CriticalModifierResult = null
     * =====================================================
     *
     * 会心撃【属性】倍率は1.0。
     */
    @Test
    void testNullCriticalModifier() {

        ElementHitzoneDamageResult result
                = elementHitzoneDamageService.calculate(
                        new BigDecimal("100"),
                        20,
                        new BigDecimal("1.15"),
                        null
                );


        assertDecimalEquals(
                "23",
                result.getNormalElementDamagePerHit()
        );

        assertDecimalEquals(
                "1",
                result.getPositiveElementCriticalDamageMul()
        );

        assertDecimalEquals(
                "23",
                result.getPositiveCriticalElementDamagePerHit()
        );


        System.out.println(
                "criticalModifier=null："
                + "normal="
                + result.getNormalElementDamagePerHit()
                + " / critical="
                + result.getPositiveCriticalElementDamagePerHit()
        );
    }


    /*
     * =====================================================
     * 属性会心倍率 = null
     * =====================================================
     *
     * DTOの値自体がnullでも1.0。
     */
    @Test
    void testNullElementCriticalMultiplier() {

        CriticalModifierResult criticalModifier
                = new CriticalModifierResult();

        criticalModifier.setPositiveElementCriticalDamageMul(
                null
        );


        ElementHitzoneDamageResult result
                = elementHitzoneDamageService.calculate(
                        new BigDecimal("100"),
                        20,
                        BigDecimal.ONE,
                        criticalModifier
                );


        assertDecimalEquals(
                "1",
                result.getPositiveElementCriticalDamageMul()
        );

        assertDecimalEquals(
                "20",
                result.getPositiveCriticalElementDamagePerHit()
        );


        System.out.println(
                "属性会心倍率null："
                + "mul="
                + result.getPositiveElementCriticalDamageMul()
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