package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.BowChargeModifier;
import com.example.mhsunbreaksimulator.dto.BowElementDamageResult;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.service.BowElementDamageService;

public class BowElementDamageServiceTest {

    private final BowElementDamageService
            bowElementDamageService
            = new BowElementDamageService();


    @Test
    void testElementDamageCalculation() {

        BowChargeModifier chargeModifier
                = new BowChargeModifier();

        chargeModifier.setElementMul(
                new BigDecimal("1.20")
        );


        ConditionalSkillModifierTotal conditionalModifier
                = new ConditionalSkillModifierTotal();

        conditionalModifier.setElementAdd(
                new BigDecimal("10")
        );

        conditionalModifier.setElementMul(
                new BigDecimal("1.10")
        );

        conditionalModifier.setElementDamageMul(
                new BigDecimal("1.05")
        );


        BowElementDamageResult result
                = bowElementDamageService.calculate(
                        50,
                        chargeModifier,
                        7,
                        conditionalModifier
                );


        assertEquals(
                0,
                new BigDecimal("66.00")
                        .compareTo(
                                result.getEffectiveElement()
                        )
        );

        assertEquals(
                0,
                new BigDecimal("83.16")
                        .compareTo(
                                result.getElementDamagePerHit()
                        )
        );

        assertEquals(
                0,
                new BigDecimal("582.12")
                        .compareTo(
                                result.getMaxElementDamage()
                        )
        );


        System.out.println(
                "effectiveElement="
                + result.getEffectiveElement()
        );

        System.out.println(
                "elementDamagePerHit="
                + result.getElementDamagePerHit()
        );

        System.out.println(
                "maxElementDamage="
                + result.getMaxElementDamage()
        );
    }


    @Test
    void testWithoutConditionalModifier() {

        BowChargeModifier chargeModifier
                = new BowChargeModifier();

        chargeModifier.setElementMul(
                new BigDecimal("1.20")
        );


        BowElementDamageResult result
                = bowElementDamageService.calculate(
                        50,
                        chargeModifier,
                        4,
                        null
                );


        assertEquals(
                0,
                new BigDecimal("50")
                        .compareTo(
                                result.getEffectiveElement()
                        )
        );

        assertEquals(
                0,
                new BigDecimal("60.00")
                        .compareTo(
                                result.getElementDamagePerHit()
                        )
        );

        assertEquals(
                0,
                new BigDecimal("240.00")
                        .compareTo(
                                result.getMaxElementDamage()
                        )
        );


        System.out.println(
                "補正なし：effectiveElement="
                + result.getEffectiveElement()
                + " / perHit="
                + result.getElementDamagePerHit()
                + " / max="
                + result.getMaxElementDamage()
        );
    }


    @Test
    void testNullWeaponElementReturnsZero() {

        BowElementDamageResult result
                = bowElementDamageService.calculate(
                        null,
                        new BowChargeModifier(),
                        4,
                        null
                );


        assertEquals(
                BigDecimal.ZERO,
                result.getEffectiveElement()
        );

        assertEquals(
                BigDecimal.ZERO,
                result.getElementDamagePerHit()
        );

        assertEquals(
                BigDecimal.ZERO,
                result.getMaxElementDamage()
        );


        System.out.println(
                "武器属性値null：正常"
        );
    }


    @Test
    void testNullBowChargeModifierReturnsZero() {

        BowElementDamageResult result
                = bowElementDamageService.calculate(
                        50,
                        null,
                        4,
                        null
                );


        assertEquals(
                BigDecimal.ZERO,
                result.getEffectiveElement()
        );

        assertEquals(
                BigDecimal.ZERO,
                result.getElementDamagePerHit()
        );

        assertEquals(
                BigDecimal.ZERO,
                result.getMaxElementDamage()
        );


        System.out.println(
                "溜め属性倍率null：正常"
        );
    }


    @Test
    void testInvalidHitCountReturnsZero() {

        BowElementDamageResult result
                = bowElementDamageService.calculate(
                        50,
                        new BowChargeModifier(),
                        0,
                        null
                );


        assertEquals(
                BigDecimal.ZERO,
                result.getEffectiveElement()
        );

        assertEquals(
                BigDecimal.ZERO,
                result.getElementDamagePerHit()
        );

        assertEquals(
                BigDecimal.ZERO,
                result.getMaxElementDamage()
        );


        System.out.println(
                "ヒット数0：正常"
        );
    }
}