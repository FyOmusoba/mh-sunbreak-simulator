package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;
import com.example.mhsunbreaksimulator.service.ConditionalSkillModifierService;

public class ConditionalSkillModifierServiceTest {

    private final ConditionalSkillModifierService
            conditionalSkillModifierService
            = new ConditionalSkillModifierService();


    /*
     * =====================================================
     * null
     * =====================================================
     */
    @Test
    void testNullSelectedEffects() {

        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        null
                );


        assertDecimalEquals(
                "0",
                result.getAttackAdd()
        );

        assertDecimalEquals(
                "1",
                result.getAttackMul()
        );

        assertDecimalEquals(
                "0",
                result.getAffinityAdd()
        );

        assertDecimalEquals(
                "1",
                result.getElementDamageMul()
        );

        assertDecimalEquals(
                "0",
                result.getDefenseAdd()
        );

        assertDecimalEquals(
                "1",
                result.getDefenseMul()
        );

        assertDecimalEquals(
                "0",
                result.getFireResAdd()
        );

        assertDecimalEquals(
                "0",
                result.getWaterResAdd()
        );

        assertDecimalEquals(
                "0",
                result.getThunderResAdd()
        );

        assertDecimalEquals(
                "0",
                result.getIceResAdd()
        );

        assertDecimalEquals(
                "0",
                result.getDragonResAdd()
        );

        assertNull(
                result.getAllElementResSet()
        );

        assertNull(
                result.getPostConversionAllElementResSet()
        );

        assertDecimalEquals(
                "0",
                result.getResistanceToElementRate()
        );

        assertFalse(
                result.getNullifyNonDragonElement()
        );


        System.out.println(
                "selectedEffects=null：正常"
        );
    }


    /*
     * =====================================================
     * empty
     * =====================================================
     */
    @Test
    void testEmptySelectedEffects() {

        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        Map.of()
                );


        assertDecimalEquals(
                "0",
                result.getAttackAdd()
        );

        assertDecimalEquals(
                "1",
                result.getAttackMul()
        );

        assertDecimalEquals(
                "0",
                result.getDefenseAdd()
        );

        assertDecimalEquals(
                "1",
                result.getDefenseMul()
        );

        assertNull(
                result.getAllElementResSet()
        );

        assertNull(
                result.getPostConversionAllElementResSet()
        );

        assertDecimalEquals(
                "0",
                result.getResistanceToElementRate()
        );

        assertFalse(
                result.getNullifyNonDragonElement()
        );


        System.out.println(
                "selectedEffects=empty：正常"
        );
    }


    /*
     * =====================================================
     * 通常スキル
     * =====================================================
     */
    @Test
    void testNormalModifierAggregation() {

        SkillEffectState effect
                = new SkillEffectState();

        effect.setAttackAdd(
                new BigDecimal("10")
        );

        effect.setAttackMul(
                new BigDecimal("1.05")
        );

        effect.setAffinityAdd(
                new BigDecimal("20")
        );

        effect.setElementDamageMul(
                new BigDecimal("1.10")
        );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                1,
                effect
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertDecimalEquals(
                "10",
                result.getAttackAdd()
        );

        assertDecimalEquals(
                "1.05",
                result.getAttackMul()
        );

        assertDecimalEquals(
                "20",
                result.getAffinityAdd()
        );

        assertDecimalEquals(
                "1.10",
                result.getElementDamageMul()
        );


        System.out.println(
                "通常スキル："
                + "attackAdd="
                + result.getAttackAdd()
                + " / attackMul="
                + result.getAttackMul()
                + " / affinity="
                + result.getAffinityAdd()
                + " / elementDamageMul="
                + result.getElementDamageMul()
        );
    }


    /*
     * =====================================================
     * 防御力
     * =====================================================
     */
    @Test
    void testDefenseAggregation() {

        SkillEffectState effect1
                = new SkillEffectState();

        effect1.setDefenseAdd(
                new BigDecimal("10")
        );

        effect1.setDefenseMul(
                new BigDecimal("1.10")
        );


        SkillEffectState effect2
                = new SkillEffectState();

        effect2.setDefenseAdd(
                new BigDecimal("20")
        );

        effect2.setDefenseMul(
                new BigDecimal("1.05")
        );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                1001,
                effect1
        );

        effects.put(
                1002,
                effect2
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertDecimalEquals(
                "30",
                result.getDefenseAdd()
        );

        assertDecimalEquals(
                "1.155",
                result.getDefenseMul()
        );


        System.out.println(
                "防御力集計："
                + "add="
                + result.getDefenseAdd()
                + " / mul="
                + result.getDefenseMul()
        );
    }


    /*
     * =====================================================
     * 防御力null
     * =====================================================
     */
    @Test
    void testNullDefenseModifiers() {

        SkillEffectState effect
                = new SkillEffectState();

        effect.setDefenseAdd(
                null
        );

        effect.setDefenseMul(
                null
        );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                1001,
                effect
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertDecimalEquals(
                "0",
                result.getDefenseAdd()
        );

        assertDecimalEquals(
                "1",
                result.getDefenseMul()
        );


        System.out.println(
                "防御力補正null：正常"
        );
    }


    /*
     * =====================================================
     * 5属性耐性
     * =====================================================
     */
    @Test
    void testElementResistanceAggregation() {

        SkillEffectState effect1
                = new SkillEffectState();

        effect1.setFireResAdd(
                new BigDecimal("5")
        );

        effect1.setWaterResAdd(
                new BigDecimal("3")
        );

        effect1.setThunderResAdd(
                new BigDecimal("10")
        );

        effect1.setIceResAdd(
                new BigDecimal("2")
        );

        effect1.setDragonResAdd(
                new BigDecimal("4")
        );


        SkillEffectState effect2
                = new SkillEffectState();

        effect2.setFireResAdd(
                new BigDecimal("3")
        );

        effect2.setWaterResAdd(
                new BigDecimal("-2")
        );

        effect2.setThunderResAdd(
                new BigDecimal("5")
        );

        effect2.setIceResAdd(
                new BigDecimal("6")
        );

        effect2.setDragonResAdd(
                new BigDecimal("-1")
        );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                2001,
                effect1
        );

        effects.put(
                2002,
                effect2
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertDecimalEquals(
                "8",
                result.getFireResAdd()
        );

        assertDecimalEquals(
                "1",
                result.getWaterResAdd()
        );

        assertDecimalEquals(
                "15",
                result.getThunderResAdd()
        );

        assertDecimalEquals(
                "8",
                result.getIceResAdd()
        );

        assertDecimalEquals(
                "3",
                result.getDragonResAdd()
        );


        System.out.println(
                "属性耐性集計："
                + "火="
                + result.getFireResAdd()
                + " / 水="
                + result.getWaterResAdd()
                + " / 雷="
                + result.getThunderResAdd()
                + " / 氷="
                + result.getIceResAdd()
                + " / 龍="
                + result.getDragonResAdd()
        );
    }


    /*
     * =====================================================
     * 属性耐性null
     * =====================================================
     */
    @Test
    void testNullElementResistanceModifiers() {

        SkillEffectState effect
                = new SkillEffectState();


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                2001,
                effect
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertDecimalEquals(
                "0",
                result.getFireResAdd()
        );

        assertDecimalEquals(
                "0",
                result.getWaterResAdd()
        );

        assertDecimalEquals(
                "0",
                result.getThunderResAdd()
        );

        assertDecimalEquals(
                "0",
                result.getIceResAdd()
        );

        assertDecimalEquals(
                "0",
                result.getDragonResAdd()
        );


        System.out.println(
                "属性耐性補正null：正常"
        );
    }


    /*
     * =====================================================
     * 攻撃＋防御＋耐性
     * =====================================================
     */
    @Test
    void testAttackDefenseResistanceTogether() {

        SkillEffectState effect
                = new SkillEffectState();

        effect.setAttackAdd(
                new BigDecimal("15")
        );

        effect.setAffinityAdd(
                new BigDecimal("10")
        );

        effect.setDefenseAdd(
                new BigDecimal("25")
        );

        effect.setDefenseMul(
                new BigDecimal("1.10")
        );

        effect.setFireResAdd(
                new BigDecimal("5")
        );

        effect.setDragonResAdd(
                new BigDecimal("10")
        );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                3001,
                effect
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertDecimalEquals(
                "15",
                result.getAttackAdd()
        );

        assertDecimalEquals(
                "10",
                result.getAffinityAdd()
        );

        assertDecimalEquals(
                "25",
                result.getDefenseAdd()
        );

        assertDecimalEquals(
                "1.10",
                result.getDefenseMul()
        );

        assertDecimalEquals(
                "5",
                result.getFireResAdd()
        );

        assertDecimalEquals(
                "10",
                result.getDragonResAdd()
        );


        System.out.println(
                "攻撃＋防御＋耐性："
                + "attack="
                + result.getAttackAdd()
                + " / affinity="
                + result.getAffinityAdd()
                + " / defenseAdd="
                + result.getDefenseAdd()
                + " / defenseMul="
                + result.getDefenseMul()
                + " / fire="
                + result.getFireResAdd()
                + " / dragon="
                + result.getDragonResAdd()
        );
    }


    /*
     * =====================================================
     * 龍気活性
     * =====================================================
     *
     * 例：
     * Lv2 ACTIVE
     *
     * allElementResSet = 50
     * nullifyNonDragonElement = true
     */
    @Test
    void testDragonheart() {

        SkillEffectState dragonheart
                = new SkillEffectState();

        dragonheart.setStateCode(
                "ACTIVE"
        );

        dragonheart.setAllElementResSet(
                50
        );

        dragonheart.setNullifyNonDragonElement(
                true
        );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        /*
         * 龍気活性
         * skill_id = 57
         */
        effects.put(
                57,
                dragonheart
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertEquals(
                50,
                result.getAllElementResSet()
        );

        assertNull(
                result.getPostConversionAllElementResSet()
        );

        assertDecimalEquals(
                "0",
                result.getResistanceToElementRate()
        );

        assertTrue(
                result.getNullifyNonDragonElement()
        );


        System.out.println(
                "龍気活性："
                + "allElementResSet="
                + result.getAllElementResSet()
                + " / postConversion="
                + result.getPostConversionAllElementResSet()
                + " / nullifyNonDragon="
                + result.getNullifyNonDragonElement()
        );
    }


    /*
     * =====================================================
     * 龍気変換【朱】
     * 近接 Lv3
     * =====================================================
     *
     * 通常耐性加算
     * +10
     *
     * 変換後全耐性
     * 0
     *
     * 変換率
     * 0.25
     */
    @Test
    void testDragonConversionRedMeleeLevel3() {

        SkillEffectState dragonConversion
                = new SkillEffectState();

        dragonConversion.setStateCode(
                "RED"
        );

        dragonConversion.setFireResAdd(
                new BigDecimal("10")
        );

        dragonConversion.setWaterResAdd(
                new BigDecimal("10")
        );

        dragonConversion.setThunderResAdd(
                new BigDecimal("10")
        );

        dragonConversion.setIceResAdd(
                new BigDecimal("10")
        );

        dragonConversion.setDragonResAdd(
                new BigDecimal("10")
        );

        dragonConversion.setAllElementResSet(
                0
        );

        dragonConversion.setResistanceToElementRate(
                new BigDecimal("0.25")
        );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        /*
         * 龍気変換
         * skill_id = 45
         */
        effects.put(
                45,
                dragonConversion
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertDecimalEquals(
                "10",
                result.getFireResAdd()
        );

        assertDecimalEquals(
                "10",
                result.getWaterResAdd()
        );

        assertDecimalEquals(
                "10",
                result.getThunderResAdd()
        );

        assertDecimalEquals(
                "10",
                result.getIceResAdd()
        );

        assertDecimalEquals(
                "10",
                result.getDragonResAdd()
        );


        /*
         * 龍気変換の0は
         * 変換前固定値へ入れてはいけない。
         */
        assertNull(
                result.getAllElementResSet()
        );


        assertEquals(
                0,
                result.getPostConversionAllElementResSet()
        );


        assertDecimalEquals(
                "0.25",
                result.getResistanceToElementRate()
        );


        assertFalse(
                result.getNullifyNonDragonElement()
        );


        System.out.println(
                "龍気変換【朱】近接Lv3："
                + "resAdd="
                + result.getFireResAdd()
                + " / allElementResSet="
                + result.getAllElementResSet()
                + " / postConversion="
                + result.getPostConversionAllElementResSet()
                + " / rate="
                + result.getResistanceToElementRate()
        );
    }


    /*
     * =====================================================
     * 龍気変換【朱】
     * 遠距離 Lv3
     * =====================================================
     *
     * ライト
     * ヘビィ
     * 弓
     *
     * Lv3変換率
     * = 0.08
     */
    @Test
    void testDragonConversionRedRangedLevel3() {

        SkillEffectState dragonConversion
                = new SkillEffectState();

        dragonConversion.setStateCode(
                "RED"
        );

        dragonConversion.setFireResAdd(
                new BigDecimal("10")
        );

        dragonConversion.setWaterResAdd(
                new BigDecimal("10")
        );

        dragonConversion.setThunderResAdd(
                new BigDecimal("10")
        );

        dragonConversion.setIceResAdd(
                new BigDecimal("10")
        );

        dragonConversion.setDragonResAdd(
                new BigDecimal("10")
        );

        dragonConversion.setAllElementResSet(
                0
        );

        dragonConversion.setResistanceToElementRate(
                new BigDecimal("0.08")
        );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                45,
                dragonConversion
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertEquals(
                0,
                result.getPostConversionAllElementResSet()
        );

        assertDecimalEquals(
                "0.08",
                result.getResistanceToElementRate()
        );


        System.out.println(
                "龍気変換【朱】遠距離Lv3："
                + "postConversion="
                + result.getPostConversionAllElementResSet()
                + " / rate="
                + result.getResistanceToElementRate()
        );
    }


    /*
     * =====================================================
     * 龍気活性
     * ＋
     * 龍気変換【朱】
     * =====================================================
     *
     * 龍気活性
     * → 変換前耐性50
     *
     * 龍気変換
     * → 変換後耐性0
     * → 変換率0.25
     *
     * 両方を同時保持できることを確認。
     */
    @Test
    void testDragonheartAndDragonConversionTogether() {

        SkillEffectState dragonheart
                = new SkillEffectState();

        dragonheart.setStateCode(
                "ACTIVE"
        );

        dragonheart.setAllElementResSet(
                50
        );

        dragonheart.setNullifyNonDragonElement(
                true
        );


        SkillEffectState dragonConversion
                = new SkillEffectState();

        dragonConversion.setStateCode(
                "RED"
        );

        dragonConversion.setFireResAdd(
                new BigDecimal("10")
        );

        dragonConversion.setWaterResAdd(
                new BigDecimal("10")
        );

        dragonConversion.setThunderResAdd(
                new BigDecimal("10")
        );

        dragonConversion.setIceResAdd(
                new BigDecimal("10")
        );

        dragonConversion.setDragonResAdd(
                new BigDecimal("10")
        );

        dragonConversion.setAllElementResSet(
                0
        );

        dragonConversion.setResistanceToElementRate(
                new BigDecimal("0.25")
        );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                57,
                dragonheart
        );

        effects.put(
                45,
                dragonConversion
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertEquals(
                50,
                result.getAllElementResSet()
        );

        assertEquals(
                0,
                result.getPostConversionAllElementResSet()
        );

        assertDecimalEquals(
                "0.25",
                result.getResistanceToElementRate()
        );

        assertTrue(
                result.getNullifyNonDragonElement()
        );


        assertDecimalEquals(
                "10",
                result.getFireResAdd()
        );

        assertDecimalEquals(
                "10",
                result.getWaterResAdd()
        );

        assertDecimalEquals(
                "10",
                result.getThunderResAdd()
        );

        assertDecimalEquals(
                "10",
                result.getIceResAdd()
        );

        assertDecimalEquals(
                "10",
                result.getDragonResAdd()
        );


        System.out.println(
                "龍気活性＋龍気変換【朱】："
                + "beforeSet="
                + result.getAllElementResSet()
                + " / afterSet="
                + result.getPostConversionAllElementResSet()
                + " / rate="
                + result.getResistanceToElementRate()
                + " / nullifyNonDragon="
                + result.getNullifyNonDragonElement()
        );
    }


    /*
     * =====================================================
     * 特殊耐性値null
     * =====================================================
     */
    @Test
    void testNullSpecialResistanceModifiers() {

        SkillEffectState effect
                = new SkillEffectState();

        effect.setAllElementResSet(
                null
        );

        effect.setResistanceToElementRate(
                null
        );

        effect.setNullifyNonDragonElement(
                null
        );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                999,
                effect
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertNull(
                result.getAllElementResSet()
        );

        assertNull(
                result.getPostConversionAllElementResSet()
        );

        assertDecimalEquals(
                "0",
                result.getResistanceToElementRate()
        );

        assertFalse(
                result.getNullifyNonDragonElement()
        );


        System.out.println(
                "特殊耐性補正null：正常"
        );
    }


    /*
     * =====================================================
     * 通常スキルの全耐性固定
     * =====================================================
     *
     * skill_id=45以外なら、
     * allElementResSet=30は
     * 変換前固定値へ入る。
     */
    @Test
    void testNormalAllElementResistanceSet() {

        SkillEffectState effect
                = new SkillEffectState();

        effect.setAllElementResSet(
                30
        );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                999,
                effect
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertEquals(
                30,
                result.getAllElementResSet()
        );

        assertNull(
                result.getPostConversionAllElementResSet()
        );


        System.out.println(
                "通常全耐性固定："
                + "allElementResSet="
                + result.getAllElementResSet()
        );
    }


    /*
     * =====================================================
     * 弱点特効
     * skill_id = 8
     * =====================================================
     */
    @Test
    void testWeaknessExploitIsExcluded() {

        SkillEffectState weaknessExploit
                = new SkillEffectState();

        weaknessExploit.setStateCode(
                "ACTIVE"
        );

        weaknessExploit.setAffinityAdd(
                new BigDecimal("50")
        );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                8,
                weaknessExploit
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertDecimalEquals(
                "0",
                result.getAffinityAdd()
        );


        System.out.println(
                "弱点特効：通常集計から除外 affinity="
                + result.getAffinityAdd()
        );
    }


    /*
     * =====================================================
     * 弱点特効【属性】
     * skill_id = 34
     * =====================================================
     */
    @Test
    void testElementExploitIsExcluded() {

        SkillEffectState elementExploit
                = new SkillEffectState();

        elementExploit.setStateCode(
                "ACTIVE"
        );

        elementExploit.setElementDamageMul(
                new BigDecimal("1.15000")
        );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                34,
                elementExploit
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertDecimalEquals(
                "1",
                result.getElementDamageMul()
        );


        System.out.println(
                "弱点特効【属性】：通常集計から除外 elementDamageMul="
                + result.getElementDamageMul()
        );
    }


    /*
     * =====================================================
     * 弱点系除外
     * ＋
     * 通常スキル
     * =====================================================
     */
    @Test
    void testWeakpointSkillsExcludedButNormalSkillRemains() {

        SkillEffectState weaknessExploit
                = new SkillEffectState();

        weaknessExploit.setAffinityAdd(
                new BigDecimal("50")
        );


        SkillEffectState elementExploit
                = new SkillEffectState();

        elementExploit.setElementDamageMul(
                new BigDecimal("1.15000")
        );


        SkillEffectState normalEffect
                = new SkillEffectState();

        normalEffect.setAttackAdd(
                new BigDecimal("15")
        );

        normalEffect.setAffinityAdd(
                new BigDecimal("10")
        );

        normalEffect.setElementDamageMul(
                new BigDecimal("1.05")
        );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                8,
                weaknessExploit
        );

        effects.put(
                34,
                elementExploit
        );

        effects.put(
                999,
                normalEffect
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertDecimalEquals(
                "15",
                result.getAttackAdd()
        );

        assertDecimalEquals(
                "10",
                result.getAffinityAdd()
        );

        assertDecimalEquals(
                "1.05",
                result.getElementDamageMul()
        );


        System.out.println(
                "弱点系除外＋通常スキル："
                + "attackAdd="
                + result.getAttackAdd()
                + " / affinity="
                + result.getAffinityAdd()
                + " / elementDamageMul="
                + result.getElementDamageMul()
        );
    }


    /*
     * =====================================================
     * statusTarget = ALL
     * =====================================================
     */
    @Test
    void testAllStatusModifier() {

        SkillEffectState effect
                = createStatusEffect(
                        "ALL",
                        "7",
                        "1.15"
                );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                31,
                effect
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertDecimalEquals(
                "7",
                result.getStatusAdd()
        );

        assertDecimalEquals(
                "1.15",
                result.getStatusMul()
        );


        System.out.println(
                "ALL：add="
                + result.getStatusAdd()
                + " / mul="
                + result.getStatusMul()
        );
    }


    /*
     * =====================================================
     * POISON
     * =====================================================
     */
    @Test
    void testPoisonStatusModifier() {

        SkillEffectState effect
                = createStatusEffect(
                        "POISON",
                        "5",
                        "1.20"
                );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                17,
                effect
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertDecimalEquals(
                "5",
                result.getPoisonStatusAdd()
        );

        assertDecimalEquals(
                "1.20",
                result.getPoisonStatusMul()
        );


        System.out.println(
                "POISON：add="
                + result.getPoisonStatusAdd()
                + " / mul="
                + result.getPoisonStatusMul()
        );
    }


    /*
     * =====================================================
     * PARALYSIS
     * =====================================================
     */
    @Test
    void testParalysisStatusModifier() {

        SkillEffectState effect
                = createStatusEffect(
                        "PARALYSIS",
                        "2",
                        "1.10"
                );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                18,
                effect
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertDecimalEquals(
                "2",
                result.getParalysisStatusAdd()
        );

        assertDecimalEquals(
                "1.10",
                result.getParalysisStatusMul()
        );


        System.out.println(
                "PARALYSIS：add="
                + result.getParalysisStatusAdd()
                + " / mul="
                + result.getParalysisStatusMul()
        );
    }


    /*
     * =====================================================
     * SLEEP
     * =====================================================
     */
    @Test
    void testSleepStatusModifier() {

        SkillEffectState effect
                = createStatusEffect(
                        "SLEEP",
                        "1",
                        "1.05"
                );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                19,
                effect
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertDecimalEquals(
                "1",
                result.getSleepStatusAdd()
        );

        assertDecimalEquals(
                "1.05",
                result.getSleepStatusMul()
        );


        System.out.println(
                "SLEEP：add="
                + result.getSleepStatusAdd()
                + " / mul="
                + result.getSleepStatusMul()
        );
    }


    /*
     * =====================================================
     * BLAST
     * =====================================================
     */
    @Test
    void testBlastStatusModifier() {

        SkillEffectState effect
                = createStatusEffect(
                        "BLAST",
                        "5",
                        "1.20"
                );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                20,
                effect
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertDecimalEquals(
                "5",
                result.getBlastStatusAdd()
        );

        assertDecimalEquals(
                "1.20",
                result.getBlastStatusMul()
        );


        System.out.println(
                "BLAST：add="
                + result.getBlastStatusAdd()
                + " / mul="
                + result.getBlastStatusMul()
        );
    }


    /*
     * =====================================================
     * POISON複数
     * =====================================================
     */
    @Test
    void testMultiplePoisonModifiers() {

        SkillEffectState effect1
                = createStatusEffect(
                        "POISON",
                        "2",
                        "1.20"
                );

        SkillEffectState effect2
                = createStatusEffect(
                        "POISON",
                        "4",
                        "1.05"
                );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                17,
                effect1
        );

        effects.put(
                999,
                effect2
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertDecimalEquals(
                "6",
                result.getPoisonStatusAdd()
        );

        assertDecimalEquals(
                "1.2600",
                result.getPoisonStatusMul()
        );


        System.out.println(
                "POISON複数集計：add="
                + result.getPoisonStatusAdd()
                + " / mul="
                + result.getPoisonStatusMul()
        );
    }


    /*
     * =====================================================
     * statusTarget = null
     * =====================================================
     */
    @Test
    void testNullStatusTargetIsIgnored() {

        SkillEffectState effect
                = createStatusEffect(
                        null,
                        "100",
                        "9.99"
                );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                999,
                effect
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertDecimalEquals(
                "0",
                result.getStatusAdd()
        );

        assertDecimalEquals(
                "1",
                result.getStatusMul()
        );

        assertDecimalEquals(
                "0",
                result.getPoisonStatusAdd()
        );

        assertDecimalEquals(
                "1",
                result.getPoisonStatusMul()
        );


        System.out.println(
                "statusTarget=null：状態異常補正を無視 正常"
        );
    }


    /*
     * =====================================================
     * 未知statusTarget
     * =====================================================
     */
    @Test
    void testUnknownStatusTargetIsIgnored() {

        SkillEffectState effect
                = createStatusEffect(
                        "UNKNOWN",
                        "100",
                        "9.99"
                );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                999,
                effect
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertDecimalEquals(
                "0",
                result.getStatusAdd()
        );

        assertDecimalEquals(
                "1",
                result.getStatusMul()
        );

        assertDecimalEquals(
                "0",
                result.getBlastStatusAdd()
        );

        assertDecimalEquals(
                "1",
                result.getBlastStatusMul()
        );


        System.out.println(
                "未知statusTarget：無視 正常"
        );
    }


    /*
     * =====================================================
     * テスト用
     * 状態異常効果生成
     * =====================================================
     */
    private SkillEffectState createStatusEffect(
            String statusTarget,
            String statusAdd,
            String statusMul) {

        SkillEffectState effect
                = new SkillEffectState();


        effect.setStatusTarget(
                statusTarget
        );

        effect.setStatusAdd(
                new BigDecimal(
                        statusAdd
                )
        );

        effect.setStatusMul(
                new BigDecimal(
                        statusMul
                )
        );


        return effect;
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