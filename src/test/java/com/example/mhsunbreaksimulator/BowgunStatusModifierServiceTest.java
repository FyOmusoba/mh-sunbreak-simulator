package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.BowgunStatusModifier;
import com.example.mhsunbreaksimulator.entity.BowgunAmmo;
import com.example.mhsunbreaksimulator.service.BowgunStatusModifierService;

public class BowgunStatusModifierServiceTest {

    private final BowgunStatusModifierService
            bowgunStatusModifierService
            = new BowgunStatusModifierService();


    /*
     * 毒弾
     *
     * Lv1 = 40
     * Lv2 = 80
     */
    @Test
    void testPoisonAmmo() {

        BowgunStatusModifier lv1
                = bowgunStatusModifierService.calculate(
                        createAmmo(
                                "毒弾",
                                1
                        )
                );

        BowgunStatusModifier lv2
                = bowgunStatusModifierService.calculate(
                        createAmmo(
                                "毒弾",
                                2
                        )
                );


        assertEquals(
                "poison",
                lv1.getStatusType()
        );

        assertBigDecimalEquals(
                "40",
                lv1.getStatusBuildupPerHit()
        );

        assertEquals(
                1,
                lv1.getMaxHitCount()
        );

        assertTrue(
                lv1.isStatusAmmo()
        );


        assertEquals(
                "poison",
                lv2.getStatusType()
        );

        assertBigDecimalEquals(
                "80",
                lv2.getStatusBuildupPerHit()
        );

        assertEquals(
                1,
                lv2.getMaxHitCount()
        );

        assertTrue(
                lv2.isStatusAmmo()
        );


        System.out.println(
                "毒弾：Lv1="
                + lv1.getStatusBuildupPerHit()
                + " / Lv2="
                + lv2.getStatusBuildupPerHit()
        );
    }


    /*
     * 麻痺弾
     *
     * Lv1 = 40
     * Lv2 = 80
     */
    @Test
    void testParalysisAmmo() {

        BowgunStatusModifier lv1
                = bowgunStatusModifierService.calculate(
                        createAmmo(
                                "麻痺弾",
                                1
                        )
                );

        BowgunStatusModifier lv2
                = bowgunStatusModifierService.calculate(
                        createAmmo(
                                "麻痺弾",
                                2
                        )
                );


        assertEquals(
                "paralysis",
                lv1.getStatusType()
        );

        assertBigDecimalEquals(
                "40",
                lv1.getStatusBuildupPerHit()
        );

        assertEquals(
                1,
                lv1.getMaxHitCount()
        );

        assertTrue(
                lv1.isStatusAmmo()
        );


        assertEquals(
                "paralysis",
                lv2.getStatusType()
        );

        assertBigDecimalEquals(
                "80",
                lv2.getStatusBuildupPerHit()
        );

        assertEquals(
                1,
                lv2.getMaxHitCount()
        );

        assertTrue(
                lv2.isStatusAmmo()
        );


        System.out.println(
                "麻痺弾：Lv1="
                + lv1.getStatusBuildupPerHit()
                + " / Lv2="
                + lv2.getStatusBuildupPerHit()
        );
    }


    /*
     * 睡眠弾
     *
     * Lv1 = 40
     * Lv2 = 80
     */
    @Test
    void testSleepAmmo() {

        BowgunStatusModifier lv1
                = bowgunStatusModifierService.calculate(
                        createAmmo(
                                "睡眠弾",
                                1
                        )
                );

        BowgunStatusModifier lv2
                = bowgunStatusModifierService.calculate(
                        createAmmo(
                                "睡眠弾",
                                2
                        )
                );


        assertEquals(
                "sleep",
                lv1.getStatusType()
        );

        assertBigDecimalEquals(
                "40",
                lv1.getStatusBuildupPerHit()
        );

        assertEquals(
                1,
                lv1.getMaxHitCount()
        );

        assertTrue(
                lv1.isStatusAmmo()
        );


        assertEquals(
                "sleep",
                lv2.getStatusType()
        );

        assertBigDecimalEquals(
                "80",
                lv2.getStatusBuildupPerHit()
        );

        assertEquals(
                1,
                lv2.getMaxHitCount()
        );

        assertTrue(
                lv2.isStatusAmmo()
        );


        System.out.println(
                "睡眠弾：Lv1="
                + lv1.getStatusBuildupPerHit()
                + " / Lv2="
                + lv2.getStatusBuildupPerHit()
        );
    }


    /*
     * 減気弾
     *
     * Lv1
     * 減気50
     * 気絶20
     *
     * Lv2
     * 減気100
     * 気絶30
     */
    @Test
    void testExhaustAmmo() {

        BowgunStatusModifier lv1
                = bowgunStatusModifierService.calculate(
                        createAmmo(
                                "減気弾",
                                1
                        )
                );

        BowgunStatusModifier lv2
                = bowgunStatusModifierService.calculate(
                        createAmmo(
                                "減気弾",
                                2
                        )
                );


        assertEquals(
                "exhaust",
                lv1.getStatusType()
        );

        assertBigDecimalEquals(
                "0",
                lv1.getStatusBuildupPerHit()
        );

        assertBigDecimalEquals(
                "50",
                lv1.getExhaustBuildupPerHit()
        );

        assertBigDecimalEquals(
                "20",
                lv1.getStunBuildupPerHit()
        );

        assertEquals(
                1,
                lv1.getMaxHitCount()
        );

        assertTrue(
                lv1.isStatusAmmo()
        );


        assertEquals(
                "exhaust",
                lv2.getStatusType()
        );

        assertBigDecimalEquals(
                "0",
                lv2.getStatusBuildupPerHit()
        );

        assertBigDecimalEquals(
                "100",
                lv2.getExhaustBuildupPerHit()
        );

        assertBigDecimalEquals(
                "30",
                lv2.getStunBuildupPerHit()
        );

        assertEquals(
                1,
                lv2.getMaxHitCount()
        );

        assertTrue(
                lv2.isStatusAmmo()
        );


        System.out.println(
                "減気弾Lv1：exhaust="
                + lv1.getExhaustBuildupPerHit()
                + " / stun="
                + lv1.getStunBuildupPerHit()
        );

        System.out.println(
                "減気弾Lv2：exhaust="
                + lv2.getExhaustBuildupPerHit()
                + " / stun="
                + lv2.getStunBuildupPerHit()
        );
    }


    /*
     * 通常弾などは
     * 状態異常弾として扱わない。
     */
    @Test
    void testNormalAmmo() {

        BowgunStatusModifier result
                = bowgunStatusModifierService.calculate(
                        createAmmo(
                                "通常弾",
                                3
                        )
                );


        assertEquals(
                null,
                result.getStatusType()
        );

        assertBigDecimalEquals(
                "0",
                result.getStatusBuildupPerHit()
        );

        assertBigDecimalEquals(
                "0",
                result.getExhaustBuildupPerHit()
        );

        assertBigDecimalEquals(
                "0",
                result.getStunBuildupPerHit()
        );

        assertFalse(
                result.isStatusAmmo()
        );


        System.out.println(
                "通常弾：状態異常弾ではない 正常"
        );
    }


    /*
     * nullでも安全に終了する。
     */
    @Test
    void testNullAmmo() {

        BowgunStatusModifier result
                = bowgunStatusModifierService.calculate(
                        null
                );


        assertEquals(
                null,
                result.getStatusType()
        );

        assertBigDecimalEquals(
                "0",
                result.getStatusBuildupPerHit()
        );

        assertBigDecimalEquals(
                "0",
                result.getExhaustBuildupPerHit()
        );

        assertBigDecimalEquals(
                "0",
                result.getStunBuildupPerHit()
        );

        assertFalse(
                result.isStatusAmmo()
        );


        System.out.println(
                "ammo=null：正常"
        );
    }


    private BowgunAmmo createAmmo(
            String ammoType,
            Integer ammoLevel) {

        BowgunAmmo ammo
                = new BowgunAmmo();

        ammo.setAmmoType(
                ammoType
        );

        ammo.setAmmoLevel(
                ammoLevel
        );

        return ammo;
    }


    private void assertBigDecimalEquals(
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