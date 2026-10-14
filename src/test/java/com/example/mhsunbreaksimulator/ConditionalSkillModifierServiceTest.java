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


    @Test
    void testConditionalSkillModifiersAreSummedAndMultiplied() {

        SkillEffectState firstEffect
                = new SkillEffectState();

        firstEffect.setAttackAdd(
                new BigDecimal("15.000")
        );

        firstEffect.setElementAdd(
                new BigDecimal("10.000")
        );

        firstEffect.setAffinityAdd(
                new BigDecimal("20.000")
        );

        firstEffect.setFixedDamageAdd(
                new BigDecimal("5.000")
        );

        firstEffect.setAttackMul(
                new BigDecimal("1.100")
        );

        firstEffect.setElementMul(
                new BigDecimal("1.050")
        );

        firstEffect.setCritDamageMul(
                new BigDecimal("1.100")
        );

        firstEffect.setPhysicalDamageMul(
                new BigDecimal("1.020")
        );

        firstEffect.setElementDamageMul(
                new BigDecimal("1.030")
        );

        firstEffect.setElementCritDamageMul(
                new BigDecimal("1.040")
        );

        firstEffect.setShotMul(
                new BigDecimal("1.100")
        );

        firstEffect.setPhialMul(
                new BigDecimal("1.050")
        );

        firstEffect.setArtilleryDamageMul(
                new BigDecimal("1.150")
        );

        firstEffect.setAdditionalDamageAttackMul(
                new BigDecimal("1.020")
        );


        SkillEffectState secondEffect
                = new SkillEffectState();

        secondEffect.setAttackAdd(
                new BigDecimal("10.000")
        );

        secondEffect.setElementAdd(
                new BigDecimal("5.000")
        );

        secondEffect.setAffinityAdd(
                new BigDecimal("10.000")
        );

        secondEffect.setFixedDamageAdd(
                new BigDecimal("7.500")
        );

        secondEffect.setAttackMul(
                new BigDecimal("1.050")
        );

        secondEffect.setElementMul(
                new BigDecimal("1.100")
        );

        secondEffect.setCritDamageMul(
                new BigDecimal("1.200")
        );

        secondEffect.setPhysicalDamageMul(
                new BigDecimal("1.100")
        );

        secondEffect.setElementDamageMul(
                new BigDecimal("1.200")
        );

        secondEffect.setElementCritDamageMul(
                new BigDecimal("1.100")
        );

        secondEffect.setShotMul(
                new BigDecimal("1.200")
        );

        secondEffect.setPhialMul(
                new BigDecimal("1.100")
        );

        secondEffect.setArtilleryDamageMul(
                new BigDecimal("1.200")
        );

        secondEffect.setAdditionalDamageAttackMul(
                new BigDecimal("1.050")
        );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                1,
                firstEffect
        );

        effects.put(
                2,
                secondEffect
        );


        ConditionalSkillModifierTotal total
                = conditionalSkillModifierService.calculate(
                        effects
                );


        /* ========================================== */
        /* 加算系 */
        /* ========================================== */

        assertEquals(
                new BigDecimal("25.000"),
                total.getAttackAdd()
        );

        assertEquals(
                new BigDecimal("15.000"),
                total.getElementAdd()
        );

        assertEquals(
                new BigDecimal("30.000"),
                total.getAffinityAdd()
        );

        assertEquals(
                new BigDecimal("12.500"),
                total.getFixedDamageAdd()
        );


        /* ========================================== */
        /* 倍率系 */
        /* ========================================== */

        assertEquals(
                new BigDecimal("1.155000"),
                total.getAttackMul()
        );

        assertEquals(
                new BigDecimal("1.155000"),
                total.getElementMul()
        );

        assertEquals(
                new BigDecimal("1.320000"),
                total.getCritDamageMul()
        );

        assertEquals(
                new BigDecimal("1.122000"),
                total.getPhysicalDamageMul()
        );

        assertEquals(
                new BigDecimal("1.236000"),
                total.getElementDamageMul()
        );

        assertEquals(
                new BigDecimal("1.144000"),
                total.getElementCritDamageMul()
        );

        assertEquals(
                new BigDecimal("1.320000"),
                total.getShotMul()
        );

        assertEquals(
                new BigDecimal("1.155000"),
                total.getPhialMul()
        );

        assertEquals(
                new BigDecimal("1.380000"),
                total.getArtilleryDamageMul()
        );

        assertEquals(
                new BigDecimal("1.071000"),
                total.getAdditionalDamageAttackMul()
        );


        System.out.println(
                "attackAdd="
                + total.getAttackAdd()
        );

        System.out.println(
                "elementAdd="
                + total.getElementAdd()
        );

        System.out.println(
                "affinityAdd="
                + total.getAffinityAdd()
        );

        System.out.println(
                "fixedDamageAdd="
                + total.getFixedDamageAdd()
        );

        System.out.println(
                "attackMul="
                + total.getAttackMul()
        );

        System.out.println(
                "elementMul="
                + total.getElementMul()
        );

        System.out.println(
                "critDamageMul="
                + total.getCritDamageMul()
        );

        System.out.println(
                "physicalDamageMul="
                + total.getPhysicalDamageMul()
        );

        System.out.println(
                "elementDamageMul="
                + total.getElementDamageMul()
        );

        System.out.println(
                "elementCritDamageMul="
                + total.getElementCritDamageMul()
        );

        System.out.println(
                "shotMul="
                + total.getShotMul()
        );

        System.out.println(
                "phialMul="
                + total.getPhialMul()
        );

        System.out.println(
                "artilleryDamageMul="
                + total.getArtilleryDamageMul()
        );

        System.out.println(
                "additionalDamageAttackMul="
                + total.getAdditionalDamageAttackMul()
        );
    }


    @Test
    void testNullValuesUseDefaultValues() {

        SkillEffectState effect
                = new SkillEffectState();


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                1,
                effect
        );


        ConditionalSkillModifierTotal total
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertEquals(
                BigDecimal.ZERO,
                total.getAttackAdd()
        );

        assertEquals(
                BigDecimal.ZERO,
                total.getElementAdd()
        );

        assertEquals(
                BigDecimal.ZERO,
                total.getAffinityAdd()
        );

        assertEquals(
                BigDecimal.ZERO,
                total.getFixedDamageAdd()
        );

        assertEquals(
                BigDecimal.ONE,
                total.getAttackMul()
        );

        assertEquals(
                BigDecimal.ONE,
                total.getElementMul()
        );

        assertEquals(
                BigDecimal.ONE,
                total.getCritDamageMul()
        );

        assertEquals(
                BigDecimal.ONE,
                total.getPhysicalDamageMul()
        );

        assertEquals(
                BigDecimal.ONE,
                total.getElementDamageMul()
        );

        assertEquals(
                BigDecimal.ONE,
                total.getElementCritDamageMul()
        );

        assertEquals(
                BigDecimal.ONE,
                total.getShotMul()
        );

        assertEquals(
                BigDecimal.ONE,
                total.getPhialMul()
        );

        assertEquals(
                BigDecimal.ONE,
                total.getArtilleryDamageMul()
        );

        assertEquals(
                BigDecimal.ONE,
                total.getAdditionalDamageAttackMul()
        );


        System.out.println(
                "null値テスト：正常"
        );
    }


    @Test
    void testEmptyMapReturnsDefaultValues() {

        ConditionalSkillModifierTotal total
                = conditionalSkillModifierService.calculate(
                        Map.of()
                );


        assertEquals(
                BigDecimal.ZERO,
                total.getAttackAdd()
        );

        assertEquals(
                BigDecimal.ZERO,
                total.getElementAdd()
        );

        assertEquals(
                BigDecimal.ZERO,
                total.getAffinityAdd()
        );

        assertEquals(
                BigDecimal.ZERO,
                total.getFixedDamageAdd()
        );

        assertEquals(
                BigDecimal.ONE,
                total.getAttackMul()
        );

        assertEquals(
                BigDecimal.ONE,
                total.getElementMul()
        );

        assertEquals(
                BigDecimal.ONE,
                total.getCritDamageMul()
        );

        assertEquals(
                BigDecimal.ONE,
                total.getPhysicalDamageMul()
        );

        assertEquals(
                BigDecimal.ONE,
                total.getElementDamageMul()
        );

        assertEquals(
                BigDecimal.ONE,
                total.getElementCritDamageMul()
        );

        assertEquals(
                BigDecimal.ONE,
                total.getShotMul()
        );

        assertEquals(
                BigDecimal.ONE,
                total.getPhialMul()
        );

        assertEquals(
                BigDecimal.ONE,
                total.getArtilleryDamageMul()
        );

        assertEquals(
                BigDecimal.ONE,
                total.getAdditionalDamageAttackMul()
        );


        System.out.println(
                "空データテスト：正常"
        );
    }
}