package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.BowChargeModifier;
import com.example.mhsunbreaksimulator.service.BowChargeModifierService;

public class BowChargeModifierServiceTest {

    private final BowChargeModifierService
            bowChargeModifierService
            = new BowChargeModifierService();


    @Test
    void testChargeLevel1() {

        BowChargeModifier modifier
                = bowChargeModifierService.calculate(
                        1
                );

        assertEquals(
                new BigDecimal("0.65"),
                modifier.getPhysicalMul()
        );

        assertEquals(
                new BigDecimal("0.80"),
                modifier.getElementMul()
        );

        System.out.println(
                "溜め1：physical="
                + modifier.getPhysicalMul()
                + " / element="
                + modifier.getElementMul()
        );
    }


    @Test
    void testChargeLevel2() {

        BowChargeModifier modifier
                = bowChargeModifierService.calculate(
                        2
                );

        assertEquals(
                new BigDecimal("1.00"),
                modifier.getPhysicalMul()
        );

        assertEquals(
                new BigDecimal("1.00"),
                modifier.getElementMul()
        );

        System.out.println(
                "溜め2：physical="
                + modifier.getPhysicalMul()
                + " / element="
                + modifier.getElementMul()
        );
    }


    @Test
    void testChargeLevel3() {

        BowChargeModifier modifier
                = bowChargeModifierService.calculate(
                        3
                );

        assertEquals(
                new BigDecimal("1.25"),
                modifier.getPhysicalMul()
        );

        assertEquals(
                new BigDecimal("1.10"),
                modifier.getElementMul()
        );

        System.out.println(
                "溜め3：physical="
                + modifier.getPhysicalMul()
                + " / element="
                + modifier.getElementMul()
        );
    }


    @Test
    void testChargeLevel4() {

        BowChargeModifier modifier
                = bowChargeModifierService.calculate(
                        4
                );

        assertEquals(
                new BigDecimal("1.35"),
                modifier.getPhysicalMul()
        );

        assertEquals(
                new BigDecimal("1.20"),
                modifier.getElementMul()
        );

        System.out.println(
                "溜め4：physical="
                + modifier.getPhysicalMul()
                + " / element="
                + modifier.getElementMul()
        );
    }


    @Test
    void testNullChargeLevelReturnsDefault() {

        BowChargeModifier modifier
                = bowChargeModifierService.calculate(
                        null
                );

        assertEquals(
                BigDecimal.ONE,
                modifier.getPhysicalMul()
        );

        assertEquals(
                BigDecimal.ONE,
                modifier.getElementMul()
        );

        System.out.println(
                "null：physical="
                + modifier.getPhysicalMul()
                + " / element="
                + modifier.getElementMul()
        );
    }


    @Test
    void testInvalidChargeLevelReturnsDefault() {

        BowChargeModifier modifier
                = bowChargeModifierService.calculate(
                        99
                );

        assertEquals(
                BigDecimal.ONE,
                modifier.getPhysicalMul()
        );

        assertEquals(
                BigDecimal.ONE,
                modifier.getElementMul()
        );

        System.out.println(
                "不正値：physical="
                + modifier.getPhysicalMul()
                + " / element="
                + modifier.getElementMul()
        );
    }
}