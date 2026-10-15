package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.dto.CriticalModifierResult;
import com.example.mhsunbreaksimulator.dto.WeakpointSkillModifier;
import com.example.mhsunbreaksimulator.service.CriticalModifierService;

public class CriticalModifierServiceTest {

    private final CriticalModifierService
            criticalModifierService
            = new CriticalModifierService();


    /*
     * =====================================================
     * 武器会心のみ
     * =====================================================
     */
    @Test
    void testWeaponAffinityOnly() {

        CriticalModifierResult result
                = criticalModifierService.calculate(
                        20,
                        null,
                        null
                );


        assertDecimalEquals(
                "20",
                result.getWeaponAffinity()
        );

        assertDecimalEquals(
                "20",
                result.getRawAffinity()
        );

        assertDecimalEquals(
                "20",
                result.getFinalAffinity()
        );

        assertDecimalEquals(
                "20",
                result.getPositiveCriticalRate()
        );

        assertDecimalEquals(
                "0",
                result.getNegativeCriticalRate()
        );


        System.out.println(
                "武器会心20："
                + "final="
                + result.getFinalAffinity()
                + " / positive="
                + result.getPositiveCriticalRate()
        );
    }


    /*
     * =====================================================
     * 武器会心
     * ＋
     * 通常スキル
     * ＋
     * 弱点特効
     * =====================================================
     */
    @Test
    void testAffinityAggregation() {

        ConditionalSkillModifierTotal conditionalModifier
                = new ConditionalSkillModifierTotal();

        conditionalModifier.setAffinityAdd(
                new BigDecimal("40")
        );


        WeakpointSkillModifier weakpointModifier
                = new WeakpointSkillModifier();

        weakpointModifier.setAffinityAdd(
                new BigDecimal("50")
        );


        CriticalModifierResult result
                = criticalModifierService.calculate(
                        -20,
                        conditionalModifier,
                        weakpointModifier
                );


        assertDecimalEquals(
                "-20",
                result.getWeaponAffinity()
        );

        assertDecimalEquals(
                "40",
                result.getConditionalAffinityAdd()
        );

        assertDecimalEquals(
                "50",
                result.getWeakpointAffinityAdd()
        );

        assertDecimalEquals(
                "70",
                result.getRawAffinity()
        );

        assertDecimalEquals(
                "70",
                result.getFinalAffinity()
        );

        assertDecimalEquals(
                "70",
                result.getPositiveCriticalRate()
        );


        System.out.println(
                "会心率合計："
                + "-20 + 40 + 50"
                + " = "
                + result.getFinalAffinity()
        );
    }


    /*
     * =====================================================
     * 会心率上限
     * =====================================================
     *
     * 100を超えても最終値は100。
     */
    @Test
    void testAffinityUpperClamp() {

        ConditionalSkillModifierTotal conditionalModifier
                = new ConditionalSkillModifierTotal();

        conditionalModifier.setAffinityAdd(
                new BigDecimal("80")
        );


        WeakpointSkillModifier weakpointModifier
                = new WeakpointSkillModifier();

        weakpointModifier.setAffinityAdd(
                new BigDecimal("50")
        );


        CriticalModifierResult result
                = criticalModifierService.calculate(
                        20,
                        conditionalModifier,
                        weakpointModifier
                );


        assertDecimalEquals(
                "150",
                result.getRawAffinity()
        );

        assertDecimalEquals(
                "100",
                result.getFinalAffinity()
        );

        assertDecimalEquals(
                "100",
                result.getPositiveCriticalRate()
        );

        assertDecimalEquals(
                "0",
                result.getNegativeCriticalRate()
        );


        System.out.println(
                "会心率上限：raw="
                + result.getRawAffinity()
                + " / final="
                + result.getFinalAffinity()
        );
    }


    /*
     * =====================================================
     * 会心率下限
     * =====================================================
     *
     * -100未満でも最終値は-100。
     */
    @Test
    void testAffinityLowerClamp() {

        CriticalModifierResult result
                = criticalModifierService.calculate(
                        -130,
                        null,
                        null
                );


        assertDecimalEquals(
                "-130",
                result.getRawAffinity()
        );

        assertDecimalEquals(
                "-100",
                result.getFinalAffinity()
        );

        assertDecimalEquals(
                "0",
                result.getPositiveCriticalRate()
        );

        assertDecimalEquals(
                "100",
                result.getNegativeCriticalRate()
        );


        System.out.println(
                "会心率下限：raw="
                + result.getRawAffinity()
                + " / final="
                + result.getFinalAffinity()
                + " / negative="
                + result.getNegativeCriticalRate()
        );
    }


    /*
     * =====================================================
     * マイナス会心
     * =====================================================
     */
    @Test
    void testNegativeAffinity() {

        CriticalModifierResult result
                = criticalModifierService.calculate(
                        -30,
                        null,
                        null
                );


        assertDecimalEquals(
                "-30",
                result.getFinalAffinity()
        );

        assertDecimalEquals(
                "0",
                result.getPositiveCriticalRate()
        );

        assertDecimalEquals(
                "30",
                result.getNegativeCriticalRate()
        );

        assertDecimalEquals(
                "0.75",
                result.getNegativeCriticalDamageMul()
        );


        System.out.println(
                "マイナス会心："
                + "rate="
                + result.getNegativeCriticalRate()
                + " / damageMul="
                + result.getNegativeCriticalDamageMul()
        );
    }


    /*
     * =====================================================
     * 会心率0
     * =====================================================
     */
    @Test
    void testZeroAffinity() {

        CriticalModifierResult result
                = criticalModifierService.calculate(
                        0,
                        null,
                        null
                );


        assertDecimalEquals(
                "0",
                result.getFinalAffinity()
        );

        assertDecimalEquals(
                "0",
                result.getPositiveCriticalRate()
        );

        assertDecimalEquals(
                "0",
                result.getNegativeCriticalRate()
        );


        System.out.println(
                "会心率0：正常"
        );
    }


    /*
     * =====================================================
     * 超会心なし
     * =====================================================
     *
     * 通常会心倍率は1.25。
     */
    @Test
    void testDefaultPositiveCriticalMultiplier() {

        CriticalModifierResult result
                = criticalModifierService.calculate(
                        50,
                        new ConditionalSkillModifierTotal(),
                        null
                );


        assertDecimalEquals(
                "1.25",
                result.getPositiveCriticalDamageMul()
        );


        System.out.println(
                "超会心なし："
                + "criticalMul="
                + result.getPositiveCriticalDamageMul()
        );
    }


    /*
     * =====================================================
     * 超会心Lv1
     * =====================================================
     */
    @Test
    void testCriticalBoostLevel1() {

        ConditionalSkillModifierTotal conditionalModifier
                = new ConditionalSkillModifierTotal();

        conditionalModifier.setCritDamageMul(
                new BigDecimal("1.30000")
        );


        CriticalModifierResult result
                = criticalModifierService.calculate(
                        50,
                        conditionalModifier,
                        null
                );


        assertDecimalEquals(
                "1.30",
                result.getPositiveCriticalDamageMul()
        );


        System.out.println(
                "超会心Lv1："
                + "criticalMul="
                + result.getPositiveCriticalDamageMul()
        );
    }


    /*
     * =====================================================
     * 超会心Lv2
     * =====================================================
     */
    @Test
    void testCriticalBoostLevel2() {

        ConditionalSkillModifierTotal conditionalModifier
                = new ConditionalSkillModifierTotal();

        conditionalModifier.setCritDamageMul(
                new BigDecimal("1.35000")
        );


        CriticalModifierResult result
                = criticalModifierService.calculate(
                        50,
                        conditionalModifier,
                        null
                );


        assertDecimalEquals(
                "1.35",
                result.getPositiveCriticalDamageMul()
        );


        System.out.println(
                "超会心Lv2："
                + "criticalMul="
                + result.getPositiveCriticalDamageMul()
        );
    }


    /*
     * =====================================================
     * 超会心Lv3
     * =====================================================
     */
    @Test
    void testCriticalBoostLevel3() {

        ConditionalSkillModifierTotal conditionalModifier
                = new ConditionalSkillModifierTotal();

        conditionalModifier.setCritDamageMul(
                new BigDecimal("1.40000")
        );


        CriticalModifierResult result
                = criticalModifierService.calculate(
                        50,
                        conditionalModifier,
                        null
                );


        assertDecimalEquals(
                "1.40",
                result.getPositiveCriticalDamageMul()
        );


        System.out.println(
                "超会心Lv3："
                + "criticalMul="
                + result.getPositiveCriticalDamageMul()
        );
    }


    /*
     * =====================================================
     * 会心撃【属性】なし
     * =====================================================
     *
     * 通常会心だけでは
     * 属性倍率は1.0。
     */
    @Test
    void testDefaultElementCriticalMultiplier() {

        CriticalModifierResult result
                = criticalModifierService.calculate(
                        50,
                        new ConditionalSkillModifierTotal(),
                        null
                );


        assertDecimalEquals(
                "1",
                result.getNormalElementCriticalDamageMul()
        );

        assertDecimalEquals(
                "1",
                result.getPositiveElementCriticalDamageMul()
        );


        System.out.println(
                "会心撃【属性】なし："
                + "elementCriticalMul="
                + result.getPositiveElementCriticalDamageMul()
        );
    }


    /*
     * =====================================================
     * 会心撃【属性】Lv1
     * =====================================================
     */
    @Test
    void testCriticalElementLevel1() {

        ConditionalSkillModifierTotal conditionalModifier
                = new ConditionalSkillModifierTotal();

        conditionalModifier.setElementCritDamageMul(
                new BigDecimal("1.05000")
        );


        CriticalModifierResult result
                = criticalModifierService.calculate(
                        50,
                        conditionalModifier,
                        null
                );


        assertDecimalEquals(
                "1.05",
                result.getPositiveElementCriticalDamageMul()
        );


        System.out.println(
                "会心撃【属性】Lv1："
                + "elementCriticalMul="
                + result.getPositiveElementCriticalDamageMul()
        );
    }


    /*
     * =====================================================
     * 会心撃【属性】Lv2
     * =====================================================
     */
    @Test
    void testCriticalElementLevel2() {

        ConditionalSkillModifierTotal conditionalModifier
                = new ConditionalSkillModifierTotal();

        conditionalModifier.setElementCritDamageMul(
                new BigDecimal("1.10000")
        );


        CriticalModifierResult result
                = criticalModifierService.calculate(
                        50,
                        conditionalModifier,
                        null
                );


        assertDecimalEquals(
                "1.10",
                result.getPositiveElementCriticalDamageMul()
        );


        System.out.println(
                "会心撃【属性】Lv2："
                + "elementCriticalMul="
                + result.getPositiveElementCriticalDamageMul()
        );
    }


    /*
     * =====================================================
     * 会心撃【属性】Lv3
     * =====================================================
     */
    @Test
    void testCriticalElementLevel3() {

        ConditionalSkillModifierTotal conditionalModifier
                = new ConditionalSkillModifierTotal();

        conditionalModifier.setElementCritDamageMul(
                new BigDecimal("1.15000")
        );


        CriticalModifierResult result
                = criticalModifierService.calculate(
                        50,
                        conditionalModifier,
                        null
                );


        assertDecimalEquals(
                "1.15",
                result.getPositiveElementCriticalDamageMul()
        );


        System.out.println(
                "会心撃【属性】Lv3："
                + "elementCriticalMul="
                + result.getPositiveElementCriticalDamageMul()
        );
    }


    /*
     * =====================================================
     * 超会心＋会心撃【属性】
     * =====================================================
     *
     * 物理会心倍率と
     * 属性会心倍率を別々に保持できることを確認。
     */
    @Test
    void testCriticalBoostAndCriticalElement() {

        ConditionalSkillModifierTotal conditionalModifier
                = new ConditionalSkillModifierTotal();

        conditionalModifier.setAffinityAdd(
                new BigDecimal("40")
        );

        conditionalModifier.setCritDamageMul(
                new BigDecimal("1.40000")
        );

        conditionalModifier.setElementCritDamageMul(
                new BigDecimal("1.15000")
        );


        WeakpointSkillModifier weakpointModifier
                = new WeakpointSkillModifier();

        weakpointModifier.setAffinityAdd(
                new BigDecimal("50")
        );


        CriticalModifierResult result
                = criticalModifierService.calculate(
                        10,
                        conditionalModifier,
                        weakpointModifier
                );


        assertDecimalEquals(
                "100",
                result.getFinalAffinity()
        );

        assertDecimalEquals(
                "1.40",
                result.getPositiveCriticalDamageMul()
        );

        assertDecimalEquals(
                "1.15",
                result.getPositiveElementCriticalDamageMul()
        );


        System.out.println(
                "会心100＋超会心Lv3＋会心撃【属性】Lv3："
                + "affinity="
                + result.getFinalAffinity()
                + " / physicalMul="
                + result.getPositiveCriticalDamageMul()
                + " / elementMul="
                + result.getPositiveElementCriticalDamageMul()
        );
    }


    /*
     * =====================================================
     * weaponAffinity = null
     * =====================================================
     */
    @Test
    void testNullWeaponAffinity() {

        CriticalModifierResult result
                = criticalModifierService.calculate(
                        null,
                        null,
                        null
                );


        assertDecimalEquals(
                "0",
                result.getWeaponAffinity()
        );

        assertDecimalEquals(
                "0",
                result.getRawAffinity()
        );

        assertDecimalEquals(
                "0",
                result.getFinalAffinity()
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
                "1",
                result.getPositiveElementCriticalDamageMul()
        );


        System.out.println(
                "weaponAffinity=null：正常"
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