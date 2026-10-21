package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.BowDragonPiercerModifier;
import com.example.mhsunbreaksimulator.service.BowDragonPiercerModifierService;

public class BowDragonPiercerModifierServiceTest {

    private final BowDragonPiercerModifierService service
            = new BowDragonPiercerModifierService();


    /*
     * =====================================================
     * 溜め1
     * =====================================================
     */
    @Test
    void testChargeLevel1() {

        BowDragonPiercerModifier result
                = service.calculate(1);

        assertDecimalEquals(
                "28",
                result.getMotionValuePerHit()
        );

        assertEquals(
                7,
                result.getMaxHitCount()
        );
    }


    /*
     * =====================================================
     * 溜め2
     * =====================================================
     */
    @Test
    void testChargeLevel2() {

        BowDragonPiercerModifier result
                = service.calculate(2);

        assertDecimalEquals(
                "30",
                result.getMotionValuePerHit()
        );

        assertEquals(
                7,
                result.getMaxHitCount()
        );
    }


    /*
     * =====================================================
     * 溜め3
     * =====================================================
     */
    @Test
    void testChargeLevel3() {

        BowDragonPiercerModifier result
                = service.calculate(3);

        assertDecimalEquals(
                "32",
                result.getMotionValuePerHit()
        );

        assertEquals(
                7,
                result.getMaxHitCount()
        );
    }


    /*
     * =====================================================
     * 溜め4
     * =====================================================
     */
    @Test
    void testChargeLevel4() {

        BowDragonPiercerModifier result
                = service.calculate(4);

        assertDecimalEquals(
                "34",
                result.getMotionValuePerHit()
        );

        assertEquals(
                7,
                result.getMaxHitCount()
        );
    }


    /*
     * =====================================================
     * null
     * =====================================================
     */
    @Test
    void testNullChargeLevel() {

        BowDragonPiercerModifier result
                = service.calculate(null);

        assertDecimalEquals(
                "0",
                result.getMotionValuePerHit()
        );

        assertEquals(
                0,
                result.getMaxHitCount()
        );
    }


    /*
     * =====================================================
     * 範囲外
     * =====================================================
     */
    @Test
    void testInvalidChargeLevel() {

        BowDragonPiercerModifier result
                = service.calculate(5);

        assertDecimalEquals(
                "0",
                result.getMotionValuePerHit()
        );

        assertEquals(
                0,
                result.getMaxHitCount()
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