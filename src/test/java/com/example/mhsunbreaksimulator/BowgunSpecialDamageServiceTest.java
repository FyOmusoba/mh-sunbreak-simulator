package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.BowgunAmmoModifier;
import com.example.mhsunbreaksimulator.dto.BowgunSpecialDamageResult;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.entity.BowgunAmmo;
import com.example.mhsunbreaksimulator.service.BowgunAmmoModifierService;
import com.example.mhsunbreaksimulator.service.BowgunSpecialDamageService;

public class BowgunSpecialDamageServiceTest {

    private final BowgunAmmoModifierService
            bowgunAmmoModifierService
            = new BowgunAmmoModifierService();

    private final BowgunSpecialDamageService
            bowgunSpecialDamageService
            = new BowgunSpecialDamageService();


    /*
     * =====================================================
     * 徹甲榴弾Lv3
     * =====================================================
     *
     * 攻撃力300
     *
     * 300 × 19%
     * = 57
     *
     * 火属性成分
     * = 5
     */
    @Test
    void testStickyAmmoLv3() {

        BowgunAmmo ammo
                = createAmmo(
                        "徹甲榴弾",
                        3
                );


        BowgunAmmoModifier ammoModifier
                = bowgunAmmoModifierService.calculate(
                        ammo
                );


        BowgunSpecialDamageResult result
                = bowgunSpecialDamageService.calculate(
                        300,
                        ammoModifier,
                        null
                );


        assertBigDecimalEquals(
                "300",
                result.getEffectiveAttack()
        );

        assertBigDecimalEquals(
                "1",
                result.getImpactPhysicalMotionValue()
        );

        assertBigDecimalEquals(
                "57",
                result.getSecondaryFixedDamagePerHit()
        );

        assertBigDecimalEquals(
                "57",
                result.getSecondaryFixedDamageTotal()
        );

        assertBigDecimalEquals(
                "5",
                result.getSecondaryFixedElementValuePerHit()
        );

        assertBigDecimalEquals(
                "5",
                result.getSecondaryFixedElementValueTotal()
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


        System.out.println(
                "徹甲榴弾Lv3："
                + "effectiveAttack="
                + result.getEffectiveAttack()
                + " / fixedPerHit="
                + result.getSecondaryFixedDamagePerHit()
                + " / fixedTotal="
                + result.getSecondaryFixedDamageTotal()
                + " / firePerHit="
                + result.getSecondaryFixedElementValuePerHit()
                + " / fireTotal="
                + result.getSecondaryFixedElementValueTotal()
        );
    }


    /*
     * =====================================================
     * 拡散弾Lv3
     * =====================================================
     *
     * 攻撃力300
     *
     * 300 × 20%
     * = 60 / 1爆発
     *
     * 60 × 5
     * = 300
     *
     * 火10 × 5
     * = 50
     */
    @Test
    void testClusterAmmoLv3() {

        BowgunAmmo ammo
                = createAmmo(
                        "拡散弾",
                        3
                );


        BowgunAmmoModifier ammoModifier
                = bowgunAmmoModifierService.calculate(
                        ammo
                );


        BowgunSpecialDamageResult result
                = bowgunSpecialDamageService.calculate(
                        300,
                        ammoModifier,
                        null
                );


        assertBigDecimalEquals(
                "300",
                result.getEffectiveAttack()
        );

        assertBigDecimalEquals(
                "1",
                result.getImpactPhysicalMotionValue()
        );

        assertBigDecimalEquals(
                "60",
                result.getSecondaryFixedDamagePerHit()
        );

        assertBigDecimalEquals(
                "300",
                result.getSecondaryFixedDamageTotal()
        );

        assertBigDecimalEquals(
                "10",
                result.getSecondaryFixedElementValuePerHit()
        );

        assertBigDecimalEquals(
                "50",
                result.getSecondaryFixedElementValueTotal()
        );

        assertEquals(
                5,
                result.getSecondaryHitCount()
        );

        assertTrue(
                result.isSecondaryIgnoresPhysicalHitzone()
        );


        System.out.println(
                "拡散弾Lv3："
                + "fixedPerHit="
                + result.getSecondaryFixedDamagePerHit()
                + " / hit="
                + result.getSecondaryHitCount()
                + " / fixedTotal="
                + result.getSecondaryFixedDamageTotal()
                + " / fireTotal="
                + result.getSecondaryFixedElementValueTotal()
        );
    }


    /*
     * =====================================================
     * 竜撃弾
     * =====================================================
     *
     * 攻撃力300
     *
     * 1段目
     * 300 × 80%
     * = 240
     *
     * 火40
     *
     * 2段目
     * 300 × 19%
     * = 57
     *
     * 火20
     */
    @Test
    void testWyvernAmmo() {

        BowgunAmmo ammo
                = createAmmo(
                        "竜撃弾",
                        1
                );


        BowgunAmmoModifier ammoModifier
                = bowgunAmmoModifierService.calculate(
                        ammo
                );


        BowgunSpecialDamageResult result
                = bowgunSpecialDamageService.calculate(
                        300,
                        ammoModifier,
                        null
                );


        assertBigDecimalEquals(
                "300",
                result.getEffectiveAttack()
        );


        /*
         * 1段目
         */
        assertBigDecimalEquals(
                "240",
                result.getSecondaryFixedDamagePerHit()
        );

        assertBigDecimalEquals(
                "240",
                result.getSecondaryFixedDamageTotal()
        );

        assertBigDecimalEquals(
                "40",
                result.getSecondaryFixedElementValuePerHit()
        );

        assertBigDecimalEquals(
                "40",
                result.getSecondaryFixedElementValueTotal()
        );

        assertEquals(
                1,
                result.getSecondaryHitCount()
        );

        assertTrue(
                result.isSecondaryIgnoresPhysicalHitzone()
        );


        /*
         * 2段目
         */
        assertBigDecimalEquals(
                "57",
                result.getAdditionalFixedDamagePerHit()
        );

        assertBigDecimalEquals(
                "57",
                result.getAdditionalFixedDamageTotal()
        );

        assertBigDecimalEquals(
                "20",
                result.getAdditionalFixedElementValuePerHit()
        );

        assertBigDecimalEquals(
                "20",
                result.getAdditionalFixedElementValueTotal()
        );

        assertEquals(
                1,
                result.getAdditionalHitCount()
        );

        assertTrue(
                result.isAdditionalIgnoresPhysicalHitzone()
        );


        System.out.println(
                "竜撃弾1段目："
                + "fixed="
                + result.getSecondaryFixedDamageTotal()
                + " / fire="
                + result.getSecondaryFixedElementValueTotal()
        );

        System.out.println(
                "竜撃弾2段目："
                + "fixed="
                + result.getAdditionalFixedDamageTotal()
                + " / fire="
                + result.getAdditionalFixedElementValueTotal()
        );
    }


    /*
     * =====================================================
     * 斬裂弾
     * =====================================================
     *
     * 固定ダメージではなく、
     *
     * 着弾MV1
     * 炸裂MV14 × 3
     *
     * をそのまま保持する。
     */
    @Test
    void testSlicingAmmo() {

        BowgunAmmo ammo
                = createAmmo(
                        "斬裂弾",
                        1
                );


        BowgunAmmoModifier ammoModifier
                = bowgunAmmoModifierService.calculate(
                        ammo
                );


        BowgunSpecialDamageResult result
                = bowgunSpecialDamageService.calculate(
                        300,
                        ammoModifier,
                        null
                );


        assertBigDecimalEquals(
                "300",
                result.getEffectiveAttack()
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

        assertBigDecimalEquals(
                "0",
                result.getSecondaryFixedDamagePerHit()
        );

        assertBigDecimalEquals(
                "0",
                result.getSecondaryFixedDamageTotal()
        );


        System.out.println(
                "斬裂弾：impactMV="
                + result.getImpactPhysicalMotionValue()
                + " / secondaryMV="
                + result.getSecondaryPhysicalMotionValuePerHit()
                + "×"
                + result.getSecondaryHitCount()
                + " / severing="
                + result.isSecondaryUsesSeveringHitzone()
        );
    }


    /*
     * =====================================================
     * 条件付き攻撃力補正
     * =====================================================
     *
     * 武器攻撃力300
     * +10
     * ×1.10
     * = 341
     *
     * 徹甲榴弾Lv3
     * 341 × 19%
     * = 64.79
     */
    @Test
    void testStickyAmmoWithAttackModifier() {

        BowgunAmmo ammo
                = createAmmo(
                        "徹甲榴弾",
                        3
                );


        BowgunAmmoModifier ammoModifier
                = bowgunAmmoModifierService.calculate(
                        ammo
                );


        ConditionalSkillModifierTotal conditionalModifier
                = new ConditionalSkillModifierTotal();

        conditionalModifier.setAttackAdd(
                new BigDecimal("10")
        );

        conditionalModifier.setAttackMul(
                new BigDecimal("1.10")
        );


        BowgunSpecialDamageResult result
                = bowgunSpecialDamageService.calculate(
                        300,
                        ammoModifier,
                        conditionalModifier
                );


        assertBigDecimalEquals(
                "341",
                result.getEffectiveAttack()
        );

        assertBigDecimalEquals(
                "64.79",
                result.getSecondaryFixedDamagePerHit()
        );

        assertBigDecimalEquals(
                "64.79",
                result.getSecondaryFixedDamageTotal()
        );


        System.out.println(
                "徹甲榴弾Lv3・攻撃補正あり："
                + "effectiveAttack="
                + result.getEffectiveAttack()
                + " / fixed="
                + result.getSecondaryFixedDamageTotal()
        );
    }


    /*
     * =====================================================
     * 徹甲榴弾Lv3 + 砲術Lv3
     * =====================================================
     *
     * 攻撃力300
     *
     * 元固定ダメージ
     * 300 × 19%
     * = 57
     *
     * 砲術Lv3
     * ×1.30
     *
     * = 74.1
     *
     * 火属性5には砲術を掛けない。
     */
    @Test
    void testStickyAmmoLv3WithArtilleryLv3() {

        BowgunAmmo ammo
                = createAmmo(
                        "徹甲榴弾",
                        3
                );


        BowgunAmmoModifier ammoModifier
                = bowgunAmmoModifierService.calculate(
                        ammo
                );


        ConditionalSkillModifierTotal conditionalModifier
                = createArtilleryModifier(
                        "1.30"
                );


        BowgunSpecialDamageResult result
                = bowgunSpecialDamageService.calculate(
                        300,
                        ammoModifier,
                        conditionalModifier,
                        ammo.getAmmoType()
                );


        assertBigDecimalEquals(
                "300",
                result.getEffectiveAttack()
        );


        /*
         * 固定爆発のみ1.30倍
         */
        assertBigDecimalEquals(
                "74.1",
                result.getSecondaryFixedDamagePerHit()
        );

        assertBigDecimalEquals(
                "74.1",
                result.getSecondaryFixedDamageTotal()
        );


        /*
         * 火属性部分はそのまま
         */
        assertBigDecimalEquals(
                "5",
                result.getSecondaryFixedElementValuePerHit()
        );

        assertBigDecimalEquals(
                "5",
                result.getSecondaryFixedElementValueTotal()
        );


        System.out.println(
                "徹甲榴弾Lv3＋砲術Lv3："
                + "fixed="
                + result.getSecondaryFixedDamageTotal()
                + " / fire="
                + result.getSecondaryFixedElementValueTotal()
        );
    }


    /*
     * =====================================================
     * 拡散弾Lv3 + 砲術Lv3
     * =====================================================
     *
     * 拡散弾は砲術対象外。
     *
     * 300 × 20%
     * = 60
     *
     * 砲術Lv3があっても
     * 60のまま。
     */
    @Test
    void testClusterAmmoLv3WithArtilleryLv3() {

        BowgunAmmo ammo
                = createAmmo(
                        "拡散弾",
                        3
                );


        BowgunAmmoModifier ammoModifier
                = bowgunAmmoModifierService.calculate(
                        ammo
                );


        ConditionalSkillModifierTotal conditionalModifier
                = createArtilleryModifier(
                        "1.30"
                );


        BowgunSpecialDamageResult result
                = bowgunSpecialDamageService.calculate(
                        300,
                        ammoModifier,
                        conditionalModifier,
                        ammo.getAmmoType()
                );


        /*
         * 砲術対象外なので変化なし
         */
        assertBigDecimalEquals(
                "60",
                result.getSecondaryFixedDamagePerHit()
        );

        assertBigDecimalEquals(
                "300",
                result.getSecondaryFixedDamageTotal()
        );


        /*
         * 火属性も変化なし
         */
        assertBigDecimalEquals(
                "10",
                result.getSecondaryFixedElementValuePerHit()
        );

        assertBigDecimalEquals(
                "50",
                result.getSecondaryFixedElementValueTotal()
        );


        System.out.println(
                "拡散弾Lv3＋砲術Lv3："
                + "fixedPerHit="
                + result.getSecondaryFixedDamagePerHit()
                + " / fixedTotal="
                + result.getSecondaryFixedDamageTotal()
                + " / fireTotal="
                + result.getSecondaryFixedElementValueTotal()
        );
    }


    /*
     * =====================================================
     * 竜撃弾 + 砲術Lv3
     * =====================================================
     *
     * 1段目
     *
     * 300 × 80%
     * = 240
     *
     * ×1.30
     * = 312
     *
     *
     * 2段目
     *
     * 300 × 19%
     * = 57
     *
     * ×1.30
     * = 74.1
     *
     *
     * 火属性
     *
     * 40
     * 20
     *
     * は変化なし。
     */
    @Test
    void testWyvernAmmoWithArtilleryLv3() {

        BowgunAmmo ammo
                = createAmmo(
                        "竜撃弾",
                        1
                );


        BowgunAmmoModifier ammoModifier
                = bowgunAmmoModifierService.calculate(
                        ammo
                );


        ConditionalSkillModifierTotal conditionalModifier
                = createArtilleryModifier(
                        "1.30"
                );


        BowgunSpecialDamageResult result
                = bowgunSpecialDamageService.calculate(
                        300,
                        ammoModifier,
                        conditionalModifier,
                        ammo.getAmmoType()
                );


        /*
         * 1段目固定ダメージ
         */
        assertBigDecimalEquals(
                "312",
                result.getSecondaryFixedDamagePerHit()
        );

        assertBigDecimalEquals(
                "312",
                result.getSecondaryFixedDamageTotal()
        );


        /*
         * 1段目火属性
         */
        assertBigDecimalEquals(
                "40",
                result.getSecondaryFixedElementValuePerHit()
        );

        assertBigDecimalEquals(
                "40",
                result.getSecondaryFixedElementValueTotal()
        );


        /*
         * 2段目固定ダメージ
         */
        assertBigDecimalEquals(
                "74.1",
                result.getAdditionalFixedDamagePerHit()
        );

        assertBigDecimalEquals(
                "74.1",
                result.getAdditionalFixedDamageTotal()
        );


        /*
         * 2段目火属性
         */
        assertBigDecimalEquals(
                "20",
                result.getAdditionalFixedElementValuePerHit()
        );

        assertBigDecimalEquals(
                "20",
                result.getAdditionalFixedElementValueTotal()
        );


        System.out.println(
                "竜撃弾＋砲術Lv3："
                + "stage1Fixed="
                + result.getSecondaryFixedDamageTotal()
                + " / stage1Fire="
                + result.getSecondaryFixedElementValueTotal()
                + " / stage2Fixed="
                + result.getAdditionalFixedDamageTotal()
                + " / stage2Fire="
                + result.getAdditionalFixedElementValueTotal()
        );
    }


    /*
     * =====================================================
     * weaponAttack=null
     * =====================================================
     */
    @Test
    void testNullWeaponAttack() {

        BowgunSpecialDamageResult result
                = bowgunSpecialDamageService.calculate(
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
                result.getSecondaryFixedDamageTotal()
        );

        assertBigDecimalEquals(
                "0",
                result.getAdditionalFixedDamageTotal()
        );


        System.out.println(
                "武器攻撃力null：正常"
        );
    }


    /*
     * =====================================================
     * Modifier=null
     * =====================================================
     */
    @Test
    void testNullAmmoModifier() {

        BowgunSpecialDamageResult result
                = bowgunSpecialDamageService.calculate(
                        300,
                        null,
                        null
                );


        assertBigDecimalEquals(
                "0",
                result.getEffectiveAttack()
        );

        assertBigDecimalEquals(
                "0",
                result.getSecondaryFixedDamageTotal()
        );

        assertBigDecimalEquals(
                "0",
                result.getAdditionalFixedDamageTotal()
        );


        System.out.println(
                "ammoModifier=null：正常"
        );
    }


    /*
     * =====================================================
     * 砲術補正作成
     * =====================================================
     */
    private ConditionalSkillModifierTotal createArtilleryModifier(
            String multiplier) {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();


        modifier.setArtilleryDamageMul(
                new BigDecimal(
                        multiplier
                )
        );


        return modifier;
    }


    /*
     * =====================================================
     * 弾作成
     * =====================================================
     */
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


    /*
     * =====================================================
     * BigDecimal比較
     * =====================================================
     */
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