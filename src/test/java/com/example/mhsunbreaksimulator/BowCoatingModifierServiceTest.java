package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.BowCoatingModifier;
import com.example.mhsunbreaksimulator.service.BowCoatingModifierService;

public class BowCoatingModifierServiceTest {

    private final BowCoatingModifierService
            bowCoatingModifierService
            = new BowCoatingModifierService();


    @Test
    void testPowerCoating() {

        BowCoatingModifier modifier
                = bowCoatingModifierService.calculate(
                        "強撃ビン"
                );

        assertEquals(
                new BigDecimal("1.35"),
                modifier.getPhysicalMul()
        );

        assertEquals(
                BigDecimal.ONE,
                modifier.getStatusMul()
        );

        printResult(
                "強撃ビン",
                modifier
        );
    }


    @Test
    void testCloseRangeCoating() {

        BowCoatingModifier modifier
                = bowCoatingModifierService.calculate(
                        "接撃ビン"
                );

        assertEquals(
                new BigDecimal("1.20"),
                modifier.getPhysicalMul()
        );

        assertEquals(
                BigDecimal.ONE,
                modifier.getStatusMul()
        );

        printResult(
                "接撃ビン",
                modifier
        );
    }


    @Test
    void testPoisonCoating() {

        BowCoatingModifier modifier
                = bowCoatingModifierService.calculate(
                        "毒ビン"
                );

        assertEquals(
                BigDecimal.ONE,
                modifier.getPhysicalMul()
        );

        assertEquals(
                BigDecimal.ONE,
                modifier.getStatusMul()
        );

        printResult(
                "毒ビン",
                modifier
        );
    }


    @Test
    void testParalysisCoating() {

        BowCoatingModifier modifier
                = bowCoatingModifierService.calculate(
                        "麻痺ビン"
                );

        assertEquals(
                BigDecimal.ONE,
                modifier.getPhysicalMul()
        );

        assertEquals(
                BigDecimal.ONE,
                modifier.getStatusMul()
        );

        printResult(
                "麻痺ビン",
                modifier
        );
    }


    @Test
    void testSleepCoating() {

        BowCoatingModifier modifier
                = bowCoatingModifierService.calculate(
                        "睡眠ビン"
                );

        assertEquals(
                BigDecimal.ONE,
                modifier.getPhysicalMul()
        );

        assertEquals(
                BigDecimal.ONE,
                modifier.getStatusMul()
        );

        printResult(
                "睡眠ビン",
                modifier
        );
    }


    @Test
    void testBlastCoating() {

        BowCoatingModifier modifier
                = bowCoatingModifierService.calculate(
                        "爆破ビン"
                );

        assertEquals(
                BigDecimal.ONE,
                modifier.getPhysicalMul()
        );

        assertEquals(
                BigDecimal.ONE,
                modifier.getStatusMul()
        );

        printResult(
                "爆破ビン",
                modifier
        );
    }


    @Test
    void testExhaustCoating() {

        BowCoatingModifier modifier
                = bowCoatingModifierService.calculate(
                        "減気ビン"
                );

        assertEquals(
                BigDecimal.ONE,
                modifier.getPhysicalMul()
        );

        assertEquals(
                BigDecimal.ONE,
                modifier.getStatusMul()
        );

        printResult(
                "減気ビン",
                modifier
        );
    }


    @Test
    void testWithoutBinSuffix() {

        BowCoatingModifier modifier
                = bowCoatingModifierService.calculate(
                        "強撃"
                );

        assertEquals(
                new BigDecimal("1.35"),
                modifier.getPhysicalMul()
        );

        printResult(
                "強撃",
                modifier
        );
    }


    @Test
    void testNullReturnsDefault() {

        BowCoatingModifier modifier
                = bowCoatingModifierService.calculate(
                        null
                );

        assertEquals(
                BigDecimal.ONE,
                modifier.getPhysicalMul()
        );

        assertEquals(
                BigDecimal.ONE,
                modifier.getStatusMul()
        );

        printResult(
                "null",
                modifier
        );
    }


    @Test
    void testInvalidTypeReturnsDefault() {

        BowCoatingModifier modifier
                = bowCoatingModifierService.calculate(
                        "不正ビン"
                );

        assertEquals(
                BigDecimal.ONE,
                modifier.getPhysicalMul()
        );

        assertEquals(
                BigDecimal.ONE,
                modifier.getStatusMul()
        );

        printResult(
                "不正ビン",
                modifier
        );
    }


    private void printResult(
            String name,
            BowCoatingModifier modifier) {

        System.out.println(
                name
                + "：physical="
                + modifier.getPhysicalMul()
                + " / status="
                + modifier.getStatusMul()
        );
    }
}