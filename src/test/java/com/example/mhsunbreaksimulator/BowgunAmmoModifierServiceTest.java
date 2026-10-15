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
    void testPiercingDragonAmmo() {

        BowgunAmmo ammo
                = createAmmo(
                        "貫通滅龍弾",
                        1
                );


        BowgunAmmoModifier result
                = bowgunAmmoModifierService.calculate(
                        ammo
                );


        assertBigDecimalEquals(
                "20",
                result.getPhysicalMotionValuePerHit()
        );

        assertBigDecimalEquals(
                "44",
                result.getElementMotionValuePerHit()
        );

        assertEquals(
                7,
                result.getMaxHitCount()
        );


        System.out.println(
                "貫通滅龍弾：physical="
                + result.getPhysicalMotionValuePerHit()
                + " / element="
                + result.getElementMotionValuePerHit()
                + " / hit="
                + result.getMaxHitCount()
        );
    }


    @Test
    void testSlicingAmmo() {

        BowgunAmmo ammo
                = createAmmo(
                        "斬裂弾",
                        1
                );


        BowgunAmmoModifier result
                = bowgunAmmoModifierService.calculate(
                        ammo
                );


        assertBigDecimalEquals(
                "1",
                result.getImpactPhysicalMotionValue()
        );

        assertBigDecimalEquals(
                "14",
                result.getSecondaryPhysicalMotionValuePerHit()
        );

        assertEquals(
                3,
                result.getSecondaryHitCount()
        );

        assertTrue(
                result.isSecondaryUsesSeveringHitzone()
        );

        assertFalse(
                result.isSecondaryIgnoresPhysicalHitzone()
        );


        System.out.println(
                "斬裂弾：impact="
                + result.getImpactPhysicalMotionValue()
                + " / secondary="
                + result.getSecondaryPhysicalMotionValuePerHit()
                + "×"
                + result.getSecondaryHitCount()
                + " / severingHitzone="
                + result.isSecondaryUsesSeveringHitzone()
                + " / ignorePhysicalHitzone="
                + result.isSecondaryIgnoresPhysicalHitzone()
        );
    }


    @Test
    void testStickyAmmo() {

        BowgunAmmo lv1 = createAmmo("徹甲榴弾", 1);
        BowgunAmmo lv2 = createAmmo("徹甲榴弾", 2);
        BowgunAmmo lv3 = createAmmo("徹甲榴弾", 3);

        BowgunAmmoModifier result1
                = bowgunAmmoModifierService.calculate(lv1);

        BowgunAmmoModifier result2
                = bowgunAmmoModifierService.calculate(lv2);

        BowgunAmmoModifier result3
                = bowgunAmmoModifierService.calculate(lv3);


        assertBigDecimalEquals(
                "1",
                result1.getImpactPhysicalMotionValue()
        );

        assertBigDecimalEquals(
                "12",
                result1.getSecondaryFixedDamageMotionValuePerHit()
        );

        assertBigDecimalEquals(
                "3",
                result1.getSecondaryFixedElementValuePerHit()
        );

        assertEquals(
                1,
                result1.getSecondaryHitCount()
        );

        assertTrue(
                result1.isSecondaryIgnoresPhysicalHitzone()
        );


        assertBigDecimalEquals(
                "1",
                result2.getImpactPhysicalMotionValue()
        );

        assertBigDecimalEquals(
                "15",
                result2.getSecondaryFixedDamageMotionValuePerHit()
        );

        assertBigDecimalEquals(
                "5",
                result2.getSecondaryFixedElementValuePerHit()
        );

        assertEquals(
                1,
                result2.getSecondaryHitCount()
        );

        assertTrue(
                result2.isSecondaryIgnoresPhysicalHitzone()
        );


        assertBigDecimalEquals(
                "1",
                result3.getImpactPhysicalMotionValue()
        );

        assertBigDecimalEquals(
                "19",
                result3.getSecondaryFixedDamageMotionValuePerHit()
        );

        assertBigDecimalEquals(
                "5",
                result3.getSecondaryFixedElementValuePerHit()
        );

        assertEquals(
                1,
                result3.getSecondaryHitCount()
        );

        assertTrue(
                result3.isSecondaryIgnoresPhysicalHitzone()
        );


        System.out.println(
                "徹甲榴弾Lv1：impact="
                + result1.getImpactPhysicalMotionValue()
                + " / fixedMV="
                + result1.getSecondaryFixedDamageMotionValuePerHit()
                + " / fire="
                + result1.getSecondaryFixedElementValuePerHit()
                + " / hit="
                + result1.getSecondaryHitCount()
                + " / ignoreHitzone="
                + result1.isSecondaryIgnoresPhysicalHitzone()
        );

        System.out.println(
                "徹甲榴弾Lv2：impact="
                + result2.getImpactPhysicalMotionValue()
                + " / fixedMV="
                + result2.getSecondaryFixedDamageMotionValuePerHit()
                + " / fire="
                + result2.getSecondaryFixedElementValuePerHit()
                + " / hit="
                + result2.getSecondaryHitCount()
                + " / ignoreHitzone="
                + result2.isSecondaryIgnoresPhysicalHitzone()
        );

        System.out.println(
                "徹甲榴弾Lv3：impact="
                + result3.getImpactPhysicalMotionValue()
                + " / fixedMV="
                + result3.getSecondaryFixedDamageMotionValuePerHit()
                + " / fire="
                + result3.getSecondaryFixedElementValuePerHit()
                + " / hit="
                + result3.getSecondaryHitCount()
                + " / ignoreHitzone="
                + result3.isSecondaryIgnoresPhysicalHitzone()
        );
    }


    @Test
    void testClusterAmmo() {

        BowgunAmmo lv1 = createAmmo("拡散弾", 1);
        BowgunAmmo lv2 = createAmmo("拡散弾", 2);
        BowgunAmmo lv3 = createAmmo("拡散弾", 3);

        BowgunAmmoModifier result1
                = bowgunAmmoModifierService.calculate(lv1);

        BowgunAmmoModifier result2
                = bowgunAmmoModifierService.calculate(lv2);

        BowgunAmmoModifier result3
                = bowgunAmmoModifierService.calculate(lv3);


        assertBigDecimalEquals(
                "1",
                result1.getImpactPhysicalMotionValue()
        );

        assertBigDecimalEquals(
                "10",
                result1.getSecondaryFixedDamageMotionValuePerHit()
        );

        assertBigDecimalEquals(
                "5",
                result1.getSecondaryFixedElementValuePerHit()
        );

        assertEquals(
                3,
                result1.getSecondaryHitCount()
        );

        assertTrue(
                result1.isSecondaryIgnoresPhysicalHitzone()
        );


        assertBigDecimalEquals(
                "1",
                result2.getImpactPhysicalMotionValue()
        );

        assertBigDecimalEquals(
                "15",
                result2.getSecondaryFixedDamageMotionValuePerHit()
        );

        assertBigDecimalEquals(
                "8",
                result2.getSecondaryFixedElementValuePerHit()
        );

        assertEquals(
                4,
                result2.getSecondaryHitCount()
        );

        assertTrue(
                result2.isSecondaryIgnoresPhysicalHitzone()
        );


        assertBigDecimalEquals(
                "1",
                result3.getImpactPhysicalMotionValue()
        );

        assertBigDecimalEquals(
                "20",
                result3.getSecondaryFixedDamageMotionValuePerHit()
        );

        assertBigDecimalEquals(
                "10",
                result3.getSecondaryFixedElementValuePerHit()
        );

        assertEquals(
                5,
                result3.getSecondaryHitCount()
        );

        assertTrue(
                result3.isSecondaryIgnoresPhysicalHitzone()
        );


        System.out.println(
                "拡散弾Lv1：impact="
                + result1.getImpactPhysicalMotionValue()
                + " / fixedMV="
                + result1.getSecondaryFixedDamageMotionValuePerHit()
                + " / fire="
                + result1.getSecondaryFixedElementValuePerHit()
                + " / hit="
                + result1.getSecondaryHitCount()
                + " / ignoreHitzone="
                + result1.isSecondaryIgnoresPhysicalHitzone()
        );

        System.out.println(
                "拡散弾Lv2：impact="
                + result2.getImpactPhysicalMotionValue()
                + " / fixedMV="
                + result2.getSecondaryFixedDamageMotionValuePerHit()
                + " / fire="
                + result2.getSecondaryFixedElementValuePerHit()
                + " / hit="
                + result2.getSecondaryHitCount()
                + " / ignoreHitzone="
                + result2.isSecondaryIgnoresPhysicalHitzone()
        );

        System.out.println(
                "拡散弾Lv3：impact="
                + result3.getImpactPhysicalMotionValue()
                + " / fixedMV="
                + result3.getSecondaryFixedDamageMotionValuePerHit()
                + " / fire="
                + result3.getSecondaryFixedElementValuePerHit()
                + " / hit="
                + result3.getSecondaryHitCount()
                + " / ignoreHitzone="
                + result3.isSecondaryIgnoresPhysicalHitzone()
        );
    }


    @Test
    void testWyvernAmmo() {

        BowgunAmmo ammo
                = createAmmo(
                        "竜撃弾",
                        1
                );


        BowgunAmmoModifier result
                = bowgunAmmoModifierService.calculate(
                        ammo
                );


        assertBigDecimalEquals(
                "80",
                result.getSecondaryFixedDamageMotionValuePerHit()
        );

        assertBigDecimalEquals(
                "40",
                result.getSecondaryFixedElementValuePerHit()
        );

        assertEquals(
                1,
                result.getSecondaryHitCount()
        );

        assertTrue(
                result.isSecondaryIgnoresPhysicalHitzone()
        );

        assertFalse(
                result.isSecondaryUsesSeveringHitzone()
        );


        assertBigDecimalEquals(
                "19",
                result.getAdditionalFixedDamageMotionValuePerHit()
        );

        assertBigDecimalEquals(
                "20",
                result.getAdditionalFixedElementValuePerHit()
        );

        assertEquals(
                1,
                result.getAdditionalFixedHitCount()
        );

        assertTrue(
                result.isAdditionalIgnoresPhysicalHitzone()
        );


        System.out.println(
                "竜撃弾1段目：fixedMV="
                + result.getSecondaryFixedDamageMotionValuePerHit()
                + " / fire="
                + result.getSecondaryFixedElementValuePerHit()
                + " / hit="
                + result.getSecondaryHitCount()
                + " / ignoreHitzone="
                + result.isSecondaryIgnoresPhysicalHitzone()
        );

        System.out.println(
                "竜撃弾2段目：fixedMV="
                + result.getAdditionalFixedDamageMotionValuePerHit()
                + " / fire="
                + result.getAdditionalFixedElementValuePerHit()
                + " / hit="
                + result.getAdditionalFixedHitCount()
                + " / ignoreHitzone="
                + result.isAdditionalIgnoresPhysicalHitzone()
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