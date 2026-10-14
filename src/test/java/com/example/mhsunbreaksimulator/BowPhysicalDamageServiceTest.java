package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.BowPhysicalDamageResult;
import com.example.mhsunbreaksimulator.dto.BowPhysicalModifier;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.service.BowPhysicalDamageService;

public class BowPhysicalDamageServiceTest {

    private final BowPhysicalDamageService
            bowPhysicalDamageService
            = new BowPhysicalDamageService();


    @Test
    void testPhysicalDamageCalculation() {

        BowPhysicalModifier bowPhysicalModifier
                = new BowPhysicalModifier();

        bowPhysicalModifier.setPhysicalRatePerHit(
                new BigDecimal("0.2187")
        );

        bowPhysicalModifier.setMaxPhysicalRate(
                new BigDecimal("1.5309")
        );

        bowPhysicalModifier.setMaxHitCount(
                7
        );


        ConditionalSkillModifierTotal conditionalModifier
                = new ConditionalSkillModifierTotal();

        conditionalModifier.setAttackAdd(
                new BigDecimal("10")
        );

        conditionalModifier.setAttackMul(
                new BigDecimal("1.10")
        );

        conditionalModifier.setPhysicalDamageMul(
                new BigDecimal("1.05")
        );


        BowPhysicalDamageResult result
                = bowPhysicalDamageService.calculate(
                        300,
                        bowPhysicalModifier,
                        conditionalModifier
                );


        assertEquals(
                0,
                new BigDecimal("341.00")
                        .compareTo(
                                result.getEffectiveAttack()
                        )
        );

        assertEquals(
                0,
                new BigDecimal("78.30553500")
                        .compareTo(
                                result.getPhysicalDamagePerHit()
                        )
        );

        assertEquals(
                0,
                new BigDecimal("548.13874500")
                        .compareTo(
                                result.getMaxPhysicalDamage()
                        )
        );


        System.out.println(
                "effectiveAttack="
                + result.getEffectiveAttack()
        );

        System.out.println(
                "physicalDamagePerHit="
                + result.getPhysicalDamagePerHit()
        );

        System.out.println(
                "maxPhysicalDamage="
                + result.getMaxPhysicalDamage()
        );
    }


    @Test
    void testWithoutConditionalModifier() {

        BowPhysicalModifier bowPhysicalModifier
                = new BowPhysicalModifier();

        bowPhysicalModifier.setPhysicalRatePerHit(
                new BigDecimal("0.10")
        );

        bowPhysicalModifier.setMaxPhysicalRate(
                new BigDecimal("0.40")
        );


        BowPhysicalDamageResult result
                = bowPhysicalDamageService.calculate(
                        300,
                        bowPhysicalModifier,
                        null
                );


        assertEquals(
                0,
                new BigDecimal("300")
                        .compareTo(
                                result.getEffectiveAttack()
                        )
        );

        assertEquals(
                0,
                new BigDecimal("30.00")
                        .compareTo(
                                result.getPhysicalDamagePerHit()
                        )
        );

        assertEquals(
                0,
                new BigDecimal("120.00")
                        .compareTo(
                                result.getMaxPhysicalDamage()
                        )
        );


        System.out.println(
                "補正なし：effectiveAttack="
                + result.getEffectiveAttack()
                + " / perHit="
                + result.getPhysicalDamagePerHit()
                + " / max="
                + result.getMaxPhysicalDamage()
        );
    }


    @Test
    void testNullWeaponAttackReturnsZero() {

        BowPhysicalDamageResult result
                = bowPhysicalDamageService.calculate(
                        null,
                        new BowPhysicalModifier(),
                        null
                );


        assertEquals(
                BigDecimal.ZERO,
                result.getEffectiveAttack()
        );

        assertEquals(
                BigDecimal.ZERO,
                result.getPhysicalDamagePerHit()
        );

        assertEquals(
                BigDecimal.ZERO,
                result.getMaxPhysicalDamage()
        );


        System.out.println(
                "武器攻撃力null：正常"
        );
    }


    @Test
    void testNullBowModifierReturnsZero() {

        BowPhysicalDamageResult result
                = bowPhysicalDamageService.calculate(
                        300,
                        null,
                        null
                );


        assertEquals(
                BigDecimal.ZERO,
                result.getEffectiveAttack()
        );

        assertEquals(
                BigDecimal.ZERO,
                result.getPhysicalDamagePerHit()
        );

        assertEquals(
                BigDecimal.ZERO,
                result.getMaxPhysicalDamage()
        );


        System.out.println(
                "弓係数null：正常"
        );
    }
}