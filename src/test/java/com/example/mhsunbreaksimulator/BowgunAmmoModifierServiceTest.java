package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.BowgunAmmoModifier;
import com.example.mhsunbreaksimulator.entity.BowgunAmmo;
import com.example.mhsunbreaksimulator.service.BowgunAmmoModifierService;

public class BowgunAmmoModifierServiceTest {

    private final BowgunAmmoModifierService
            bowgunAmmoModifierService
            = new BowgunAmmoModifierService();


    @Test
    void testNormalAmmo() {

        BowgunAmmo lv1 = createAmmo("通常弾", 1);
        BowgunAmmo lv2 = createAmmo("通常弾", 2);
        BowgunAmmo lv3 = createAmmo("通常弾", 3);

        BowgunAmmoModifier result1
                = bowgunAmmoModifierService.calculate(lv1);

        BowgunAmmoModifier result2
                = bowgunAmmoModifierService.calculate(lv2);

        BowgunAmmoModifier result3
                = bowgunAmmoModifierService.calculate(lv3);


        assertBigDecimalEquals(
                "16",
                result1.getPhysicalMotionValuePerHit()
        );

        assertEquals(
                1,
                result1.getMaxHitCount()
        );


        assertBigDecimalEquals(
                "22",
                result2.getPhysicalMotionValuePerHit()
        );

        assertEquals(
                1,
                result2.getMaxHitCount()
        );


        assertBigDecimalEquals(
                "34",
                result3.getPhysicalMotionValuePerHit()
        );

        assertEquals(
                1,
                result3.getMaxHitCount()
        );


        System.out.println(
                "通常弾：Lv1="
                + result1.getPhysicalMotionValuePerHit()
                + " / Lv2="
                + result2.getPhysicalMotionValuePerHit()
                + " / Lv3="
                + result3.getPhysicalMotionValuePerHit()
        );
    }


    @Test
    void testPierceAmmo() {

        BowgunAmmo lv1 = createAmmo("貫通弾", 1);
        BowgunAmmo lv2 = createAmmo("貫通弾", 2);
        BowgunAmmo lv3 = createAmmo("貫通弾", 3);

        BowgunAmmoModifier result1
                = bowgunAmmoModifierService.calculate(lv1);

        BowgunAmmoModifier result2
                = bowgunAmmoModifierService.calculate(lv2);

        BowgunAmmoModifier result3
                = bowgunAmmoModifierService.calculate(lv3);


        assertBigDecimalEquals(
                "7",
                result1.getPhysicalMotionValuePerHit()
        );

        assertEquals(
                7,
                result1.getMaxHitCount()
        );


        assertBigDecimalEquals(
                "7",
                result2.getPhysicalMotionValuePerHit()
        );

        assertEquals(
                7,
                result2.getMaxHitCount()
        );


        assertBigDecimalEquals(
                "9",
                result3.getPhysicalMotionValuePerHit()
        );

        assertEquals(
                7,
                result3.getMaxHitCount()
        );


        System.out.println(
                "貫通弾：Lv1="
                + result1.getPhysicalMotionValuePerHit()
                + "×"
                + result1.getMaxHitCount()
                + " / Lv2="
                + result2.getPhysicalMotionValuePerHit()
                + "×"
                + result2.getMaxHitCount()
                + " / Lv3="
                + result3.getPhysicalMotionValuePerHit()
                + "×"
                + result3.getMaxHitCount()
        );
    }


    @Test
    void testSpreadAmmo() {

        BowgunAmmo lv1 = createAmmo("散弾", 1);
        BowgunAmmo lv2 = createAmmo("散弾", 2);
        BowgunAmmo lv3 = createAmmo("散弾", 3);

        BowgunAmmoModifier result1
                = bowgunAmmoModifierService.calculate(lv1);

        BowgunAmmoModifier result2
                = bowgunAmmoModifierService.calculate(lv2);

        BowgunAmmoModifier result3
                = bowgunAmmoModifierService.calculate(lv3);


        assertBigDecimalEquals(
                "7",
                result1.getPhysicalMotionValuePerHit()
        );

        assertEquals(
                3,
                result1.getMaxHitCount()
        );


        assertBigDecimalEquals(
                "7",
                result2.getPhysicalMotionValuePerHit()
        );

        assertEquals(
                4,
                result2.getMaxHitCount()
        );


        assertBigDecimalEquals(
                "10",
                result3.getPhysicalMotionValuePerHit()
        );

        assertEquals(
                5,
                result3.getMaxHitCount()
        );


        System.out.println(
                "散弾：Lv1="
                + result1.getPhysicalMotionValuePerHit()
                + "×"
                + result1.getMaxHitCount()
                + " / Lv2="
                + result2.getPhysicalMotionValuePerHit()
                + "×"
                + result2.getMaxHitCount()
                + " / Lv3="
                + result3.getPhysicalMotionValuePerHit()
                + "×"
                + result3.getMaxHitCount()
        );
    }


    @Test
    void testShrapnelAmmo() {

        BowgunAmmo lv1 = createAmmo("放散弾", 1);
        BowgunAmmo lv2 = createAmmo("放散弾", 2);
        BowgunAmmo lv3 = createAmmo("放散弾", 3);

        BowgunAmmoModifier result1
                = bowgunAmmoModifierService.calculate(lv1);

        BowgunAmmoModifier result2
                = bowgunAmmoModifierService.calculate(lv2);

        BowgunAmmoModifier result3
                = bowgunAmmoModifierService.calculate(lv3);


        assertBigDecimalEquals(
                "6",
                result1.getPhysicalMotionValuePerHit()
        );

        assertEquals(
                3,
                result1.getMaxHitCount()
        );


        assertBigDecimalEquals(
                "5",
                result2.getPhysicalMotionValuePerHit()
        );

        assertEquals(
                4,
                result2.getMaxHitCount()
        );


        assertBigDecimalEquals(
                "5",
                result3.getPhysicalMotionValuePerHit()
        );

        assertEquals(
                5,
                result3.getMaxHitCount()
        );


        System.out.println(
                "放散弾：Lv1="
                + result1.getPhysicalMotionValuePerHit()
                + "×"
                + result1.getMaxHitCount()
                + " / Lv2="
                + result2.getPhysicalMotionValuePerHit()
                + "×"
                + result2.getMaxHitCount()
                + " / Lv3="
                + result3.getPhysicalMotionValuePerHit()
                + "×"
                + result3.getMaxHitCount()
        );
    }


    @Test
    void testElementAmmo() {

        String[] ammoTypes = {
                "火炎弾",
                "水冷弾",
                "電撃弾",
                "氷結弾"
        };


        for (String ammoType : ammoTypes) {

            BowgunAmmo ammo
                    = createAmmo(
                            ammoType,
                            1
                    );

            BowgunAmmoModifier result
                    = bowgunAmmoModifierService.calculate(
                            ammo
                    );


            assertBigDecimalEquals(
                    "16",
                    result.getPhysicalMotionValuePerHit()
            );

            assertBigDecimalEquals(
                    "40",
                    result.getElementMotionValuePerHit()
            );

            assertEquals(
                    1,
                    result.getMaxHitCount()
            );


            System.out.println(
                    ammoType
                    + "：physical="
                    + result.getPhysicalMotionValuePerHit()
                    + " / element="
                    + result.getElementMotionValuePerHit()
                    + " / hit="
                    + result.getMaxHitCount()
            );
        }
    }


    @Test
    void testPiercingElementAmmo() {

        String[] ammoTypes = {
                "貫通火炎弾",
                "貫通水冷弾",
                "貫通電撃弾",
                "貫通氷結弾"
        };


        for (String ammoType : ammoTypes) {

            BowgunAmmo ammo
                    = createAmmo(
                            ammoType,
                            1
                    );

            BowgunAmmoModifier result
                    = bowgunAmmoModifierService.calculate(
                            ammo
                    );


            assertBigDecimalEquals(
                    "10",
                    result.getPhysicalMotionValuePerHit()
            );

            assertBigDecimalEquals(
                    "22",
                    result.getElementMotionValuePerHit()
            );

            assertBigDecimalEquals(
                    "0",
                    result.getFixedDamageValuePerHit()
            );

            assertEquals(
                    7,
                    result.getMaxHitCount()
            );


            System.out.println(
                    ammoType
                    + "：physical="
                    + result.getPhysicalMotionValuePerHit()
                    + " / element="
                    + result.getElementMotionValuePerHit()
                    + " / hit="
                    + result.getMaxHitCount()
            );
        }
    }


    @Test
    void testDragonAmmo() {

        BowgunAmmo ammo
                = createAmmo(
                        "滅龍弾",
                        1
                );


        BowgunAmmoModifier result
                = bowgunAmmoModifierService.calculate(
                        ammo
                );


        assertBigDecimalEquals(
                "48",
                result.getPhysicalMotionValuePerHit()
        );

        assertBigDecimalEquals(
                "80",
                result.getElementMotionValuePerHit()
        );

        assertBigDecimalEquals(
                "0",
                result.getFixedDamageValuePerHit()
        );

        assertEquals(
                1,
                result.getMaxHitCount()
        );


        System.out.println(
                "滅龍弾：physical="
                + result.getPhysicalMotionValuePerHit()
                + " / element="
                + result.getElementMotionValuePerHit()
                + " / hit="
                + result.getMaxHitCount()
        );
    }


    @Test
    void testNullAmmo() {

        BowgunAmmoModifier result
                = bowgunAmmoModifierService.calculate(
                        null
                );


        assertBigDecimalEquals(
                "0",
                result.getPhysicalMotionValuePerHit()
        );

        assertBigDecimalEquals(
                "0",
                result.getElementMotionValuePerHit()
        );

        assertBigDecimalEquals(
                "0",
                result.getFixedDamageValuePerHit()
        );

        assertEquals(
                0,
                result.getMaxHitCount()
        );


        System.out.println(
                "null：正常"
        );
    }


    @Test
    void testUnknownAmmo() {

        BowgunAmmo ammo
                = createAmmo(
                        "存在しない弾",
                        1
                );


        BowgunAmmoModifier result
                = bowgunAmmoModifierService.calculate(
                        ammo
                );


        assertBigDecimalEquals(
                "0",
                result.getPhysicalMotionValuePerHit()
        );

        assertBigDecimalEquals(
                "0",
                result.getElementMotionValuePerHit()
        );

        assertBigDecimalEquals(
                "0",
                result.getFixedDamageValuePerHit()
        );

        assertEquals(
                0,
                result.getMaxHitCount()
        );


        System.out.println(
                "未対応弾：正常"
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