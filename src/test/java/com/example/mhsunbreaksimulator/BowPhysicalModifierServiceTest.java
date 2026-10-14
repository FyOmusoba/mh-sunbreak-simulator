package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.BowChargeModifier;
import com.example.mhsunbreaksimulator.dto.BowCoatingModifier;
import com.example.mhsunbreaksimulator.dto.BowPhysicalModifier;
import com.example.mhsunbreaksimulator.dto.BowShotModifier;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.service.BowPhysicalModifierService;

public class BowPhysicalModifierServiceTest {

    private final BowPhysicalModifierService
            bowPhysicalModifierService
            = new BowPhysicalModifierService();


    @Test
    void testCharge4Pierce5PowerCoating() {

        BowChargeModifier chargeModifier
                = new BowChargeModifier();

        chargeModifier.setPhysicalMul(
                new BigDecimal("1.35")
        );


        BowShotModifier shotModifier
                = new BowShotModifier();

        shotModifier.setMotionValuePerHit(
                new BigDecimal("12")
        );

        shotModifier.setMaxHitCount(
                7
        );


        BowCoatingModifier coatingModifier
                = new BowCoatingModifier();

        coatingModifier.setPhysicalMul(
                new BigDecimal("1.35")
        );


        ConditionalSkillModifierTotal conditionalModifier
                = new ConditionalSkillModifierTotal();

        conditionalModifier.setShotMul(
                BigDecimal.ONE
        );


        BowPhysicalModifier result
                = bowPhysicalModifierService.calculate(
                        chargeModifier,
                        shotModifier,
                        coatingModifier,
                        conditionalModifier
                );


        assertEquals(
                0,
                new BigDecimal("0.2187")
                        .compareTo(
                                result.getPhysicalRatePerHit()
                        )
        );

        assertEquals(
                0,
                new BigDecimal("1.5309")
                        .compareTo(
                                result.getMaxPhysicalRate()
                        )
        );

        assertEquals(
                7,
                result.getMaxHitCount()
        );


        System.out.println(
                "溜め4・貫通Lv5・強撃ビン："
                + " perHit="
                + result.getPhysicalRatePerHit()
                + " / max="
                + result.getMaxPhysicalRate()
                + " / hit="
                + result.getMaxHitCount()
        );
    }


    @Test
    void testShotMulIsApplied() {

        BowChargeModifier chargeModifier
                = new BowChargeModifier();

        chargeModifier.setPhysicalMul(
                new BigDecimal("1.35")
        );


        BowShotModifier shotModifier
                = new BowShotModifier();

        shotModifier.setMotionValuePerHit(
                new BigDecimal("12")
        );

        shotModifier.setMaxHitCount(
                7
        );


        BowCoatingModifier coatingModifier
                = new BowCoatingModifier();

        coatingModifier.setPhysicalMul(
                new BigDecimal("1.35")
        );


        ConditionalSkillModifierTotal conditionalModifier
                = new ConditionalSkillModifierTotal();

        conditionalModifier.setShotMul(
                new BigDecimal("1.10")
        );


        BowPhysicalModifier result
                = bowPhysicalModifierService.calculate(
                        chargeModifier,
                        shotModifier,
                        coatingModifier,
                        conditionalModifier
                );


        assertEquals(
                0,
                new BigDecimal("0.24057")
                        .compareTo(
                                result.getPhysicalRatePerHit()
                        )
        );

        assertEquals(
                0,
                new BigDecimal("1.68399")
                        .compareTo(
                                result.getMaxPhysicalRate()
                        )
        );


        System.out.println(
                "shotMul 1.10："
                + " perHit="
                + result.getPhysicalRatePerHit()
                + " / max="
                + result.getMaxPhysicalRate()
        );
    }


    @Test
    void testNullModifiersUseDefaultMultiplier() {

        BowShotModifier shotModifier
                = new BowShotModifier();

        shotModifier.setMotionValuePerHit(
                new BigDecimal("10")
        );

        shotModifier.setMaxHitCount(
                4
        );


        BowPhysicalModifier result
                = bowPhysicalModifierService.calculate(
                        null,
                        shotModifier,
                        null,
                        null
                );


        assertEquals(
                0,
                new BigDecimal("0.10")
                        .compareTo(
                                result.getPhysicalRatePerHit()
                        )
        );

        assertEquals(
                0,
                new BigDecimal("0.40")
                        .compareTo(
                                result.getMaxPhysicalRate()
                        )
        );

        assertEquals(
                4,
                result.getMaxHitCount()
        );


        System.out.println(
                "補正null："
                + " perHit="
                + result.getPhysicalRatePerHit()
                + " / max="
                + result.getMaxPhysicalRate()
        );
    }


    @Test
    void testInvalidShotReturnsZero() {

        BowShotModifier shotModifier
                = new BowShotModifier();

        BowPhysicalModifier result
                = bowPhysicalModifierService.calculate(
                        null,
                        shotModifier,
                        null,
                        null
                );


        assertEquals(
                0,
                BigDecimal.ZERO.compareTo(
                        result.getPhysicalRatePerHit()
                )
        );

        assertEquals(
                0,
                BigDecimal.ZERO.compareTo(
                        result.getMaxPhysicalRate()
                )
        );

        assertEquals(
                0,
                result.getMaxHitCount()
        );


        System.out.println(
                "無効な矢データ："
                + " perHit="
                + result.getPhysicalRatePerHit()
                + " / max="
                + result.getMaxPhysicalRate()
                + " / hit="
                + result.getMaxHitCount()
        );
    }
}