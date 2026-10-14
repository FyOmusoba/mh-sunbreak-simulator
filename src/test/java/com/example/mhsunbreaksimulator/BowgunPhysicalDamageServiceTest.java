package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.BowgunAmmoModifier;
import com.example.mhsunbreaksimulator.dto.BowgunPhysicalDamageResult;
import com.example.mhsunbreaksimulator.dto.BowgunRapidFireModifier;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.service.BowgunPhysicalDamageService;

public class BowgunPhysicalDamageServiceTest {

    private final BowgunPhysicalDamageService
            bowgunPhysicalDamageService
            = new BowgunPhysicalDamageService();


    @Test
    void testPierceAmmoPhysicalDamage() {

        BowgunAmmoModifier ammoModifier
                = new BowgunAmmoModifier();

        ammoModifier.setPhysicalMotionValuePerHit(
                new BigDecimal("7")
        );

        ammoModifier.setMaxHitCount(
                7
        );


        ConditionalSkillModifierTotal conditionalModifier
                = new ConditionalSkillModifierTotal();

        conditionalModifier.setAttackAdd(
                new BigDecimal("10")
        );

        conditionalModifier.setAttackMul(
                new BigDecimal("1.10")
        );

        conditionalModifier.setShotMul(
                new BigDecimal("1.20")
        );

        conditionalModifier.setPhysicalDamageMul(
                new BigDecimal("1.05")
        );


        BowgunPhysicalDamageResult result
                = bowgunPhysicalDamageService.calculate(
                        300,
                        ammoModifier,
                        conditionalModifier
                );


        assertBigDecimalEquals(
                "341.00",
                result.getEffectiveAttack()
        );

        assertBigDecimalEquals(
                "30.0762",
                result.getPhysicalDamagePerHit()
        );

        assertBigDecimalEquals(
                "210.5334",
                result.getMaxPhysicalDamage()
        );

        assertEquals(
                1,
                result.getShotCount()
        );

        assertBigDecimalEquals(
                "210.5334",
                result.getMaxPhysicalDamagePerAmmo()
        );


        System.out.println(
                "通常計算：effectiveAttack="
                + result.getEffectiveAttack()
                + " / perHit="
                + result.getPhysicalDamagePerHit()
                + " / maxPerShot="
                + result.getMaxPhysicalDamage()
                + " / shotCount="
                + result.getShotCount()
                + " / maxPerAmmo="
                + result.getMaxPhysicalDamagePerAmmo()
        );
    }


    @Test
    void testRapidFirePhysicalDamage() {

        BowgunAmmoModifier ammoModifier
                = new BowgunAmmoModifier();

        ammoModifier.setPhysicalMotionValuePerHit(
                new BigDecimal("7")
        );

        ammoModifier.setMaxHitCount(
                7
        );


        ConditionalSkillModifierTotal conditionalModifier
                = new ConditionalSkillModifierTotal();

        conditionalModifier.setAttackAdd(
                new BigDecimal("10")
        );

        conditionalModifier.setAttackMul(
                new BigDecimal("1.10")
        );

        conditionalModifier.setShotMul(
                new BigDecimal("1.20")
        );

        conditionalModifier.setPhysicalDamageMul(
                new BigDecimal("1.05")
        );


        BowgunRapidFireModifier rapidFireModifier
                = new BowgunRapidFireModifier();

        rapidFireModifier.setRapidFire(
                true
        );

        rapidFireModifier.setShotCount(
                3
        );

        rapidFireModifier.setPhysicalMulPerShot(
                new BigDecimal("0.70")
        );

        rapidFireModifier.setElementMulPerShot(
                new BigDecimal("0.50")
        );


        BowgunPhysicalDamageResult result
                = bowgunPhysicalDamageService.calculate(
                        300,
                        ammoModifier,
                        conditionalModifier,
                        rapidFireModifier
                );


        assertBigDecimalEquals(
                "341.00",
                result.getEffectiveAttack()
        );

        assertBigDecimalEquals(
                "21.05334",
                result.getPhysicalDamagePerHit()
        );

        assertBigDecimalEquals(
                "147.37338",
                result.getMaxPhysicalDamage()
        );

        assertEquals(
                3,
                result.getShotCount()
        );

        assertBigDecimalEquals(
                "442.12014",
                result.getMaxPhysicalDamagePerAmmo()
        );


        System.out.println(
                "速射：effectiveAttack="
                + result.getEffectiveAttack()
                + " / perHit="
                + result.getPhysicalDamagePerHit()
                + " / maxPerShot="
                + result.getMaxPhysicalDamage()
                + " / shotCount="
                + result.getShotCount()
                + " / maxPerAmmo="
                + result.getMaxPhysicalDamagePerAmmo()
        );
    }


    @Test
    void testNonRapidFireModifier() {

        BowgunAmmoModifier ammoModifier
                = new BowgunAmmoModifier();

        ammoModifier.setPhysicalMotionValuePerHit(
                new BigDecimal("22")
        );

        ammoModifier.setMaxHitCount(
                1
        );


        BowgunRapidFireModifier rapidFireModifier
                = new BowgunRapidFireModifier();

        rapidFireModifier.setRapidFire(
                false
        );

        rapidFireModifier.setShotCount(
                1
        );

        rapidFireModifier.setPhysicalMulPerShot(
                BigDecimal.ONE
        );

        rapidFireModifier.setElementMulPerShot(
                BigDecimal.ONE
        );


        BowgunPhysicalDamageResult result
                = bowgunPhysicalDamageService.calculate(
                        300,
                        ammoModifier,
                        null,
                        rapidFireModifier
                );


        assertBigDecimalEquals(
                "300",
                result.getEffectiveAttack()
        );

        assertBigDecimalEquals(
                "66.00",
                result.getPhysicalDamagePerHit()
        );

        assertBigDecimalEquals(
                "66.00",
                result.getMaxPhysicalDamage()
        );

        assertEquals(
                1,
                result.getShotCount()
        );

        assertBigDecimalEquals(
                "66.00",
                result.getMaxPhysicalDamagePerAmmo()
        );


        System.out.println(
                "非速射：perHit="
                + result.getPhysicalDamagePerHit()
                + " / maxPerShot="
                + result.getMaxPhysicalDamage()
                + " / shotCount="
                + result.getShotCount()
                + " / maxPerAmmo="
                + result.getMaxPhysicalDamagePerAmmo()
        );
    }


    @Test
    void testWithoutConditionalModifier() {

        BowgunAmmoModifier ammoModifier
                = new BowgunAmmoModifier();

        ammoModifier.setPhysicalMotionValuePerHit(
                new BigDecimal("22")
        );

        ammoModifier.setMaxHitCount(
                1
        );


        BowgunPhysicalDamageResult result
                = bowgunPhysicalDamageService.calculate(
                        300,
                        ammoModifier,
                        null
                );


        assertBigDecimalEquals(
                "300",
                result.getEffectiveAttack()
        );

        assertBigDecimalEquals(
                "66.00",
                result.getPhysicalDamagePerHit()
        );

        assertBigDecimalEquals(
                "66.00",
                result.getMaxPhysicalDamage()
        );

        assertEquals(
                1,
                result.getShotCount()
        );

        assertBigDecimalEquals(
                "66.00",
                result.getMaxPhysicalDamagePerAmmo()
        );


        System.out.println(
                "補正なし：effectiveAttack="
                + result.getEffectiveAttack()
                + " / perHit="
                + result.getPhysicalDamagePerHit()
                + " / maxPerAmmo="
                + result.getMaxPhysicalDamagePerAmmo()
        );
    }


    @Test
    void testNullWeaponAttack() {

        BowgunPhysicalDamageResult result
                = bowgunPhysicalDamageService.calculate(
                        null,
                        new BowgunAmmoModifier(),
                        null
                );


        assertBigDecimalEquals(
                "0",
                result.getEffectiveAttack()
        );

        assertBigDecimalEquals(
                "0",
                result.getPhysicalDamagePerHit()
        );

        assertBigDecimalEquals(
                "0",
                result.getMaxPhysicalDamage()
        );

        assertEquals(
                1,
                result.getShotCount()
        );

        assertBigDecimalEquals(
                "0",
                result.getMaxPhysicalDamagePerAmmo()
        );


        System.out.println(
                "武器攻撃力null：正常"
        );
    }


    @Test
    void testInvalidAmmo() {

        BowgunAmmoModifier ammoModifier
                = new BowgunAmmoModifier();

        ammoModifier.setPhysicalMotionValuePerHit(
                BigDecimal.ZERO
        );

        ammoModifier.setMaxHitCount(
                0
        );


        BowgunPhysicalDamageResult result
                = bowgunPhysicalDamageService.calculate(
                        300,
                        ammoModifier,
                        null
                );


        assertBigDecimalEquals(
                "0",
                result.getEffectiveAttack()
        );

        assertBigDecimalEquals(
                "0",
                result.getPhysicalDamagePerHit()
        );

        assertBigDecimalEquals(
                "0",
                result.getMaxPhysicalDamage()
        );

        assertEquals(
                1,
                result.getShotCount()
        );

        assertBigDecimalEquals(
                "0",
                result.getMaxPhysicalDamagePerAmmo()
        );


        System.out.println(
                "無効な弾データ：正常"
        );
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