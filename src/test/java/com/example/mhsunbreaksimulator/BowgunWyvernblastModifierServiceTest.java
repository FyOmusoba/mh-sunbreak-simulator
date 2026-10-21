package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.BowgunWyvernblastModifier;
import com.example.mhsunbreaksimulator.service.BowgunWyvernblastModifierService;

public class BowgunWyvernblastModifierServiceTest {

    private final BowgunWyvernblastModifierService service
            = new BowgunWyvernblastModifierService();


    /*
     * =====================================================
     * 小爆発
     * =====================================================
     */
    @Test
    void testSmall() {

        BowgunWyvernblastModifier result
                = service.calculate(
                        "SMALL"
                );

        assertDecimalEquals(
                "26",
                result.getFixedDamageMotionValue()
        );

        assertDecimalEquals(
                "3",
                result.getFixedFireValue()
        );

        assertEquals(
                1,
                result.getDurabilityCost()
        );
    }


    /*
     * =====================================================
     * 中爆発
     * =====================================================
     */
    @Test
    void testMedium() {

        BowgunWyvernblastModifier result
                = service.calculate(
                        "MEDIUM"
                );

        assertDecimalEquals(
                "32",
                result.getFixedDamageMotionValue()
        );

        assertDecimalEquals(
                "5",
                result.getFixedFireValue()
        );

        assertEquals(
                2,
                result.getDurabilityCost()
        );
    }


    /*
     * =====================================================
     * 大爆発
     * =====================================================
     */
    @Test
    void testLarge() {

        BowgunWyvernblastModifier result
                = service.calculate(
                        "LARGE"
                );

        assertDecimalEquals(
                "49",
                result.getFixedDamageMotionValue()
        );

        assertDecimalEquals(
                "7",
                result.getFixedFireValue()
        );

        assertEquals(
                4,
                result.getDurabilityCost()
        );
    }


    /*
     * =====================================================
     * null
     * =====================================================
     */
    @Test
    void testNull() {

        BowgunWyvernblastModifier result
                = service.calculate(
                        null
                );

        assertDecimalEquals(
                "0",
                result.getFixedDamageMotionValue()
        );

        assertDecimalEquals(
                "0",
                result.getFixedFireValue()
        );

        assertEquals(
                0,
                result.getDurabilityCost()
        );
    }


    /*
     * =====================================================
     * 不正値
     * =====================================================
     */
    @Test
    void testInvalid() {

        BowgunWyvernblastModifier result
                = service.calculate(
                        "INVALID"
                );

        assertDecimalEquals(
                "0",
                result.getFixedDamageMotionValue()
        );

        assertDecimalEquals(
                "0",
                result.getFixedFireValue()
        );

        assertEquals(
                0,
                result.getDurabilityCost()
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