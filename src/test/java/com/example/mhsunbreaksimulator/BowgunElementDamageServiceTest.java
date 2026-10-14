package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.BowgunAmmoModifier;
import com.example.mhsunbreaksimulator.dto.BowgunElementDamageResult;
import com.example.mhsunbreaksimulator.dto.BowgunRapidFireModifier;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.service.BowgunElementDamageService;

public class BowgunElementDamageServiceTest {

    private final BowgunElementDamageService
            bowgunElementDamageService
            = new BowgunElementDamageService();


    @Test
    void testElementAmmoDamage() {

        BowgunAmmoModifier ammoModifier
                = new BowgunAmmoModifier();

        ammoModifier.setElementMotionValuePerHit(
                new BigDecimal("40")
        );

        ammoModifier.setMaxHitCount(
                1
        );


        ConditionalSkillModifierTotal conditionalModifier
                = new ConditionalSkillModifierTotal();

        conditionalModifier.setAttackAdd(
                new BigDecimal("10")
        );

        conditionalModifier.setAttackMul(
                new BigDecimal("1.10")
        );

        conditionalModifier.setElementAdd(
                new BigDecimal("10")
        );

        conditionalModifier.setElementMul(
                new BigDecimal("1.20")
        );

        conditionalModifier.setElementDamageMul(
                new BigDecimal("1.05")
        );


        BowgunElementDamageResult result
                = bowgunElementDamageService.calculate(
                        300,
                        ammoModifier,
                        conditionalModifier
                );


        assertBigDecimalEquals(
                "341.00",
                result.getEffectiveAttack()
        );

        assertBigDecimalEquals(
                "207.669",
                result.getElementDamagePerHit()
        );

        assertBigDecimalEquals(
                "207.669",
                result.getMaxElementDamage()
        );

        assertEquals(
                1,
                result.getShotCount()
        );

        assertBigDecimalEquals(
                "207.669",
                result.getMaxElementDamagePerAmmo()
        );


        System.out.println(
                "通常属性計算：effectiveAttack="
                + result.getEffectiveAttack()
                + " / perHit="
                + result.getElementDamagePerHit()
                + " / maxPerShot="
                + result.getMaxElementDamage()
                + " / shotCount="
                + result.getShotCount()
                + " / maxPerAmmo="
                + result.getMaxElementDamagePerAmmo()
        );
    }


    @Test
    void testRapidFireElementDamage() {

        BowgunAmmoModifier ammoModifier
                = new BowgunAmmoModifier();

        ammoModifier.setElementMotionValuePerHit(
                new BigDecimal("40")
        );

        ammoModifier.setMaxHitCount(
                1
        );


        ConditionalSkillModifierTotal conditionalModifier
                = new ConditionalSkillModifierTotal();

        conditionalModifier.setAttackAdd(
                new BigDecimal("10")
        );

        conditionalModifier.setAttackMul(
                new BigDecimal("1.10")
        );

        conditionalModifier.setElementAdd(
                new BigDecimal("10")
        );

        conditionalModifier.setElementMul(
                new BigDecimal("1.20")
        );

        conditionalModifier.setElementDamageMul(
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


        BowgunElementDamageResult result
                = bowgunElementDamageService.calculate(
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
                "103.8345",
                result.getElementDamagePerHit()
        );

        assertBigDecimalEquals(
                "103.8345",
                result.getMaxElementDamage()
        );

        assertEquals(
                3,
                result.getShotCount()
        );

        assertBigDecimalEquals(
                "311.5035",
                result.getMaxElementDamagePerAmmo()
        );


        System.out.println(
                "速射属性：effectiveAttack="
                + result.getEffectiveAttack()
                + " / perHit="
                + result.getElementDamagePerHit()
                + " / maxPerShot="
                + result.getMaxElementDamage()
                + " / shotCount="
                + result.getShotCount()
                + " / maxPerAmmo="
                + result.getMaxElementDamagePerAmmo()
        );
    }


    @Test
    void testNonRapidFireModifier() {

        BowgunAmmoModifier ammoModifier
                = new BowgunAmmoModifier();

        ammoModifier.setElementMotionValuePerHit(
                new BigDecimal("40")
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


        BowgunElementDamageResult result
                = bowgunElementDamageService.calculate(
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
                "120",
                result.getElementDamagePerHit()
        );

        assertBigDecimalEquals(
                "120",
                result.getMaxElementDamage()
        );

        assertEquals(
                1,
                result.getShotCount()
        );

        assertBigDecimalEquals(
                "120",
                result.getMaxElementDamagePerAmmo()
        );


        System.out.println(
                "非速射属性：perHit="
                + result.getElementDamagePerHit()
                + " / maxPerShot="
                + result.getMaxElementDamage()
                + " / shotCount="
                + result.getShotCount()
                + " / maxPerAmmo="
                + result.getMaxElementDamagePerAmmo()
        );
    }


    @Test
    void testMultipleHits() {

        BowgunAmmoModifier ammoModifier
                = new BowgunAmmoModifier();

        ammoModifier.setElementMotionValuePerHit(
                new BigDecimal("20")
        );

        ammoModifier.setMaxHitCount(
                5
        );


        BowgunElementDamageResult result
                = bowgunElementDamageService.calculate(
                        300,
                        ammoModifier,
                        null
                );


        assertBigDecimalEquals(
                "300",
                result.getEffectiveAttack()
        );

        assertBigDecimalEquals(
                "60",
                result.getElementDamagePerHit()
        );

        assertBigDecimalEquals(
                "300",
                result.getMaxElementDamage()
        );

        assertEquals(
                1,
                result.getShotCount()
        );

        assertBigDecimalEquals(
                "300",
                result.getMaxElementDamagePerAmmo()
        );


        System.out.println(
                "複数hit：perHit="
                + result.getElementDamagePerHit()
                + " / maxPerShot="
                + result.getMaxElementDamage()
                + " / maxPerAmmo="
                + result.getMaxElementDamagePerAmmo()
        );
    }


    @Test
    void testPhysicalAmmoReturnsZero() {

        BowgunAmmoModifier ammoModifier
                = new BowgunAmmoModifier();

        ammoModifier.setPhysicalMotionValuePerHit(
                new BigDecimal("22")
        );

        ammoModifier.setElementMotionValuePerHit(
                BigDecimal.ZERO
        );

        ammoModifier.setMaxHitCount(
                1
        );


        BowgunElementDamageResult result
                = bowgunElementDamageService.calculate(
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
                result.getElementDamagePerHit()
        );

        assertBigDecimalEquals(
                "0",
                result.getMaxElementDamage()
        );

        assertEquals(
                1,
                result.getShotCount()
        );

        assertBigDecimalEquals(
                "0",
                result.getMaxElementDamagePerAmmo()
        );


        System.out.println(
                "物理弾：属性ダメージ0 正常"
        );
    }


    @Test
    void testInvalidHitCount() {

        BowgunAmmoModifier ammoModifier
                = new BowgunAmmoModifier();

        ammoModifier.setElementMotionValuePerHit(
                new BigDecimal("40")
        );

        ammoModifier.setMaxHitCount(
                0
        );


        BowgunElementDamageResult result
                = bowgunElementDamageService.calculate(
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
                result.getElementDamagePerHit()
        );

        assertBigDecimalEquals(
                "0",
                result.getMaxElementDamage()
        );

        assertEquals(
                1,
                result.getShotCount()
        );

        assertBigDecimalEquals(
                "0",
                result.getMaxElementDamagePerAmmo()
        );


        System.out.println(
                "ヒット数0：正常"
        );
    }


    @Test
    void testNullWeaponAttack() {

        BowgunElementDamageResult result
                = bowgunElementDamageService.calculate(
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
                result.getElementDamagePerHit()
        );

        assertBigDecimalEquals(
                "0",
                result.getMaxElementDamage()
        );

        assertEquals(
                1,
                result.getShotCount()
        );

        assertBigDecimalEquals(
                "0",
                result.getMaxElementDamagePerAmmo()
        );


        System.out.println(
                "武器攻撃力null：正常"
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