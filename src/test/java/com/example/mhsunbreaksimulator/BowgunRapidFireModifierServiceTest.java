package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.BowgunRapidFireModifier;
import com.example.mhsunbreaksimulator.entity.BowgunAmmo;
import com.example.mhsunbreaksimulator.service.BowgunRapidFireModifierService;

public class BowgunRapidFireModifierServiceTest {

    private final BowgunRapidFireModifierService
            bowgunRapidFireModifierService
            = new BowgunRapidFireModifierService();


    /*
     * 3発速射グループ
     */
    @Test
    void testThreeShotRapidFireAmmo() {

        String[] ammoTypes = {
                "通常弾",
                "貫通弾",
                "散弾",
                "放散弾",
                "火炎弾",
                "水冷弾",
                "電撃弾",
                "氷結弾"
        };


        for (String ammoType : ammoTypes) {

            BowgunAmmo ammo
                    = createAmmo(
                            ammoType,
                            true
                    );


            BowgunRapidFireModifier result
                    = bowgunRapidFireModifierService.calculate(
                            ammo
                    );


            assertTrue(
                    result.isRapidFire()
            );

            assertEquals(
                    3,
                    result.getShotCount()
            );

            assertBigDecimalEquals(
                    "0.70",
                    result.getPhysicalMulPerShot()
            );

            assertBigDecimalEquals(
                    "0.50",
                    result.getElementMulPerShot()
            );


            System.out.println(
                    "3発速射："
                    + ammoType
                    + " / shotCount="
                    + result.getShotCount()
            );
        }
    }


    /*
     * 貫通属性弾の2発速射
     */
    @Test
    void testTwoShotPiercingElementRapidFireAmmo() {

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
                            true
                    );


            BowgunRapidFireModifier result
                    = bowgunRapidFireModifierService.calculate(
                            ammo
                    );


            assertTrue(
                    result.isRapidFire()
            );

            assertEquals(
                    2,
                    result.getShotCount()
            );


            System.out.println(
                    "2発速射・貫通属性："
                    + ammoType
                    + " / shotCount="
                    + result.getShotCount()
            );
        }
    }


    /*
     * 龍属性・特殊弾・状態異常弾の
     * 2発速射グループ
     */
    @Test
    void testTwoShotSpecialRapidFireAmmo() {

        String[] ammoTypes = {
                "滅龍弾",
                "貫通滅龍弾",
                "斬裂弾",
                "徹甲榴弾",
                "毒弾",
                "麻痺弾",
                "睡眠弾",
                "減気弾"
        };


        for (String ammoType : ammoTypes) {

            BowgunAmmo ammo
                    = createAmmo(
                            ammoType,
                            true
                    );


            BowgunRapidFireModifier result
                    = bowgunRapidFireModifierService.calculate(
                            ammo
                    );


            assertTrue(
                    result.isRapidFire()
            );

            assertEquals(
                    2,
                    result.getShotCount()
            );


            System.out.println(
                    "2発速射・特殊："
                    + ammoType
                    + " / shotCount="
                    + result.getShotCount()
            );
        }
    }


    /*
     * rapidFire=falseなら
     * 弾種に関係なく1発
     */
    @Test
    void testNonRapidFireAmmo() {

        BowgunAmmo ammo
                = createAmmo(
                        "通常弾",
                        false
                );


        BowgunRapidFireModifier result
                = bowgunRapidFireModifierService.calculate(
                        ammo
                );


        assertFalse(
                result.isRapidFire()
        );

        assertEquals(
                1,
                result.getShotCount()
        );

        assertBigDecimalEquals(
                "1.00",
                result.getPhysicalMulPerShot()
        );

        assertBigDecimalEquals(
                "1.00",
                result.getElementMulPerShot()
        );


        System.out.println(
                "非速射：shotCount="
                + result.getShotCount()
        );
    }


    /*
     * rapidFire=nullなら
     * 非速射として扱う
     */
    @Test
    void testNullRapidFireFlag() {

        BowgunAmmo ammo
                = createAmmo(
                        "通常弾",
                        null
                );


        BowgunRapidFireModifier result
                = bowgunRapidFireModifierService.calculate(
                        ammo
                );


        assertFalse(
                result.isRapidFire()
        );

        assertEquals(
                1,
                result.getShotCount()
        );

        assertBigDecimalEquals(
                "1.00",
                result.getPhysicalMulPerShot()
        );

        assertBigDecimalEquals(
                "1.00",
                result.getElementMulPerShot()
        );


        System.out.println(
                "rapidFire=null：shotCount="
                + result.getShotCount()
        );
    }


    /*
     * ammo自体がnullでも
     * 安全に1発扱い
     */
    @Test
    void testNullAmmo() {

        BowgunRapidFireModifier result
                = bowgunRapidFireModifierService.calculate(
                        null
                );


        assertFalse(
                result.isRapidFire()
        );

        assertEquals(
                1,
                result.getShotCount()
        );

        assertBigDecimalEquals(
                "1.00",
                result.getPhysicalMulPerShot()
        );

        assertBigDecimalEquals(
                "1.00",
                result.getElementMulPerShot()
        );


        System.out.println(
                "ammo=null：shotCount="
                + result.getShotCount()
        );
    }


    private BowgunAmmo createAmmo(
            String ammoType,
            Boolean rapidFire) {

        BowgunAmmo ammo
                = new BowgunAmmo();

        ammo.setAmmoType(
                ammoType
        );

        ammo.setRapidFire(
                rapidFire
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