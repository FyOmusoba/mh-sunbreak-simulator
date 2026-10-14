package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.BowShotModifier;
import com.example.mhsunbreaksimulator.service.BowShotModifierService;

public class BowShotModifierServiceTest {

    private final BowShotModifierService
            bowShotModifierService
            = new BowShotModifierService();


    /* ========================================== */
    /* 連射 */
    /* ========================================== */

    @Test
    void testRapidLevel1() {

        BowShotModifier modifier
                = bowShotModifierService.calculate(
                        "連射",
                        1
                );

        assertEquals(
                new BigDecimal("14"),
                modifier.getMotionValuePerHit()
        );

        assertEquals(
                1,
                modifier.getMaxHitCount()
        );

        printResult(
                "連射Lv1",
                modifier
        );
    }


    @Test
    void testRapidLevel5() {

        BowShotModifier modifier
                = bowShotModifierService.calculate(
                        "連射",
                        5
                );

        assertEquals(
                new BigDecimal("11"),
                modifier.getMotionValuePerHit()
        );

        assertEquals(
                4,
                modifier.getMaxHitCount()
        );

        printResult(
                "連射Lv5",
                modifier
        );
    }


    /* ========================================== */
    /* 拡散 */
    /* ========================================== */

    @Test
    void testSpreadLevel1() {

        BowShotModifier modifier
                = bowShotModifierService.calculate(
                        "拡散",
                        1
                );

        assertEquals(
                new BigDecimal("5"),
                modifier.getMotionValuePerHit()
        );

        assertEquals(
                3,
                modifier.getMaxHitCount()
        );

        printResult(
                "拡散Lv1",
                modifier
        );
    }


    @Test
    void testSpreadLevel5() {

        BowShotModifier modifier
                = bowShotModifierService.calculate(
                        "拡散",
                        5
                );

        assertEquals(
                new BigDecimal("10"),
                modifier.getMotionValuePerHit()
        );

        assertEquals(
                5,
                modifier.getMaxHitCount()
        );

        printResult(
                "拡散Lv5",
                modifier
        );
    }


    /* ========================================== */
    /* 貫通 */
    /* ========================================== */

    @Test
    void testPierceLevel1() {

        BowShotModifier modifier
                = bowShotModifierService.calculate(
                        "貫通",
                        1
                );

        assertEquals(
                new BigDecimal("4"),
                modifier.getMotionValuePerHit()
        );

        assertEquals(
                7,
                modifier.getMaxHitCount()
        );

        printResult(
                "貫通Lv1",
                modifier
        );
    }


    @Test
    void testPierceLevel5() {

        BowShotModifier modifier
                = bowShotModifierService.calculate(
                        "貫通",
                        5
                );

        assertEquals(
                new BigDecimal("12"),
                modifier.getMotionValuePerHit()
        );

        assertEquals(
                7,
                modifier.getMaxHitCount()
        );

        printResult(
                "貫通Lv5",
                modifier
        );
    }


    /* ========================================== */
    /* null */
    /* ========================================== */

    @Test
    void testNullValuesReturnDefault() {

        BowShotModifier modifier
                = bowShotModifierService.calculate(
                        null,
                        null
                );

        assertEquals(
                BigDecimal.ZERO,
                modifier.getMotionValuePerHit()
        );

        assertEquals(
                0,
                modifier.getMaxHitCount()
        );

        printResult(
                "null",
                modifier
        );
    }


    /* ========================================== */
    /* 不正な矢タイプ */
    /* ========================================== */

    @Test
    void testInvalidShotTypeReturnsDefault() {

        BowShotModifier modifier
                = bowShotModifierService.calculate(
                        "不正",
                        5
                );

        assertEquals(
                BigDecimal.ZERO,
                modifier.getMotionValuePerHit()
        );

        assertEquals(
                0,
                modifier.getMaxHitCount()
        );

        printResult(
                "不正タイプ",
                modifier
        );
    }


    /* ========================================== */
    /* 不正な矢Lv */
    /* ========================================== */

    @Test
    void testInvalidShotLevelReturnsDefault() {

        BowShotModifier modifier
                = bowShotModifierService.calculate(
                        "連射",
                        99
                );

        assertEquals(
                BigDecimal.ZERO,
                modifier.getMotionValuePerHit()
        );

        assertEquals(
                0,
                modifier.getMaxHitCount()
        );

        printResult(
                "不正Lv",
                modifier
        );
    }


    /* ========================================== */
    /* 表示用 */
    /* ========================================== */

    private void printResult(
            String name,
            BowShotModifier modifier) {

        System.out.println(
                name
                + "：MV="
                + modifier.getMotionValuePerHit()
                + " / maxHit="
                + modifier.getMaxHitCount()
        );
    }
}