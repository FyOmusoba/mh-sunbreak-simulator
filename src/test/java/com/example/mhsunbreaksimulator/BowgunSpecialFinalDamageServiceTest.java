package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.BowgunAmmoModifier;
import com.example.mhsunbreaksimulator.dto.BowgunSpecialDamageResult;
import com.example.mhsunbreaksimulator.dto.BowgunSpecialFinalDamageResult;
import com.example.mhsunbreaksimulator.entity.BowgunAmmo;
import com.example.mhsunbreaksimulator.entity.MonsterHitzone;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;
import com.example.mhsunbreaksimulator.service.BowgunAmmoModifierService;
import com.example.mhsunbreaksimulator.service.BowgunSpecialDamageService;
import com.example.mhsunbreaksimulator.service.BowgunSpecialFinalDamageService;
import com.example.mhsunbreaksimulator.service.CriticalModifierService;
import com.example.mhsunbreaksimulator.service.MonsterWeakpointService;
import com.example.mhsunbreaksimulator.service.WeakpointSkillModifierService;

public class BowgunSpecialFinalDamageServiceTest {

    private final BowgunAmmoModifierService
            bowgunAmmoModifierService
            = new BowgunAmmoModifierService();


    private final BowgunSpecialDamageService
            bowgunSpecialDamageService
            = new BowgunSpecialDamageService();


    private final BowgunSpecialFinalDamageService
            bowgunSpecialFinalDamageService
            = new BowgunSpecialFinalDamageService(
                    new MonsterWeakpointService(),
                    new WeakpointSkillModifierService(),
                    new CriticalModifierService()
            );


    /*
     * =====================================================
     * 徹甲榴弾Lv3
     * =====================================================
     *
     * 攻撃力350
     *
     * 着弾：
     * 350 × 0.01 × 弾45%
     * = 1.575
     * → 2
     *
     * 爆発：
     * 350 × 0.19
     * = 66.5
     * → 67
     *
     * 火：
     * 5 × 火25%
     * = 1.25
     * → 1
     *
     * 非会心合計：
     * 2 + 67 + 1
     * = 70
     */
    @Test
    void testStickyAmmoLv3() {

        BowgunAmmo ammo
                = createAmmo(
                        "徹甲榴弾",
                        3
                );


        BowgunSpecialDamageResult specialDamageResult
                = createSpecialDamageResult(
                        ammo,
                        350
                );


        MonsterHitzone hitzone
                = createHitzone();


        BowgunSpecialFinalDamageResult result
                = bowgunSpecialFinalDamageService.calculate(
                        ammo,
                        specialDamageResult,
                        hitzone,
                        0,
                        null,
                        Map.of()
                );


        assertBigDecimalEquals(
                "1.575",
                result.getImpactNormalPhysicalDamage()
        );


        assertBigDecimalEquals(
                "66.5",
                result.getSecondaryFixedDamageTotal()
        );


        assertBigDecimalEquals(
                "1.25",
                result.getSecondaryElementDamageTotal()
        );


        assertEquals(
                70,
                result.getNormalTotalDamage()
        );


        /*
         * 着弾部分だけ会心差が出る。
         *
         * プラス：
         * 1.575 × 1.25
         * = 1.96875
         * → 2
         *
         * マイナス：
         * 1.575 × 0.75
         * = 1.18125
         * → 1
         */
        assertEquals(
                70,
                result.getPositiveCriticalTotalDamage()
        );


        assertEquals(
                69,
                result.getNegativeCriticalTotalDamage()
        );


        System.out.println(
                "徹甲榴弾Lv3："
                + "impact="
                + result.getImpactNormalPhysicalDamage()
                + " / fixed="
                + result.getSecondaryFixedDamageTotal()
                + " / fire="
                + result.getSecondaryElementDamageTotal()
                + " / normal="
                + result.getNormalTotalDamage()
                + " / positive="
                + result.getPositiveCriticalTotalDamage()
                + " / negative="
                + result.getNegativeCriticalTotalDamage()
        );
    }


    /*
     * =====================================================
     * 斬裂弾
     * =====================================================
     *
     * 攻撃力350
     *
     * 着弾：
     * 350 × 0.01 × 弾45%
     * = 1.575
     * → 2
     *
     * 炸裂1hit：
     * 350 × 0.14 × 斬55%
     * = 26.95
     * → 27
     *
     * 炸裂3hit：
     * 27 × 3
     * = 81
     *
     * 非会心：
     * 2 + 81
     * = 83
     */
    @Test
    void testSlicingAmmo() {

        BowgunAmmo ammo
                = createAmmo(
                        "斬裂弾",
                        1
                );


        BowgunSpecialDamageResult specialDamageResult
                = createSpecialDamageResult(
                        ammo,
                        350
                );


        MonsterHitzone hitzone
                = createHitzone();


        BowgunSpecialFinalDamageResult result
                = bowgunSpecialFinalDamageService.calculate(
                        ammo,
                        specialDamageResult,
                        hitzone,
                        0,
                        null,
                        Map.of()
                );


        assertBigDecimalEquals(
                "1.575",
                result.getImpactNormalPhysicalDamage()
        );


        assertBigDecimalEquals(
                "26.95",
                result.getSecondaryNormalPhysicalDamagePerHit()
        );


        assertBigDecimalEquals(
                "80.85",
                result.getSecondaryNormalPhysicalDamageTotal()
        );


        assertEquals(
                83,
                result.getNormalTotalDamage()
        );


        /*
         * プラス会心
         *
         * 着弾：
         * 1.575 × 1.25
         * = 1.96875
         * → 2
         *
         * 炸裂：
         * 26.95 × 1.25
         * = 33.6875
         * → 34 × 3
         *
         * 合計104
         */
        assertEquals(
                104,
                result.getPositiveCriticalTotalDamage()
        );


        /*
         * マイナス会心
         *
         * 着弾：
         * 1.575 × 0.75
         * → 1
         *
         * 炸裂：
         * 26.95 × 0.75
         * = 20.2125
         * → 20 × 3
         *
         * 合計61
         */
        assertEquals(
                61,
                result.getNegativeCriticalTotalDamage()
        );


        System.out.println(
                "斬裂弾："
                + "impact="
                + result.getImpactNormalPhysicalDamage()
                + " / secondaryPerHit="
                + result.getSecondaryNormalPhysicalDamagePerHit()
                + " / normal="
                + result.getNormalTotalDamage()
                + " / positive="
                + result.getPositiveCriticalTotalDamage()
                + " / negative="
                + result.getNegativeCriticalTotalDamage()
        );
    }


    /*
     * =====================================================
     * 竜撃弾
     * =====================================================
     *
     * 攻撃力350
     *
     * 1段目：
     * 350 × 0.80
     * = 280
     *
     * 火：
     * 40 × 25%
     * = 10
     *
     * 2段目：
     * 350 × 0.19
     * = 66.5
     * → 67
     *
     * 火：
     * 20 × 25%
     * = 5
     *
     * 合計：
     * 280 + 10 + 67 + 5
     * = 362
     *
     * 爆発部分は会心しないため
     * 3パターンすべて同じ。
     */
    @Test
    void testWyvernAmmo() {

        BowgunAmmo ammo
                = createAmmo(
                        "竜撃弾",
                        1
                );


        BowgunSpecialDamageResult specialDamageResult
                = createSpecialDamageResult(
                        ammo,
                        350
                );


        MonsterHitzone hitzone
                = createHitzone();


        BowgunSpecialFinalDamageResult result
                = bowgunSpecialFinalDamageService.calculate(
                        ammo,
                        specialDamageResult,
                        hitzone,
                        0,
                        null,
                        Map.of()
                );


        assertBigDecimalEquals(
                "280",
                result.getSecondaryFixedDamageTotal()
        );


        assertBigDecimalEquals(
                "10",
                result.getSecondaryElementDamageTotal()
        );


        assertBigDecimalEquals(
                "66.5",
                result.getAdditionalFixedDamageTotal()
        );


        assertBigDecimalEquals(
                "5",
                result.getAdditionalElementDamageTotal()
        );


        assertEquals(
                362,
                result.getNormalTotalDamage()
        );


        assertEquals(
                362,
                result.getPositiveCriticalTotalDamage()
        );


        assertEquals(
                362,
                result.getNegativeCriticalTotalDamage()
        );


        System.out.println(
                "竜撃弾："
                + "stage1Fixed="
                + result.getSecondaryFixedDamageTotal()
                + " / stage1Fire="
                + result.getSecondaryElementDamageTotal()
                + " / stage2Fixed="
                + result.getAdditionalFixedDamageTotal()
                + " / stage2Fire="
                + result.getAdditionalElementDamageTotal()
                + " / total="
                + result.getNormalTotalDamage()
        );
    }


    /*
     * =====================================================
     * 弱点特効【属性】Lv1
     * =====================================================
     *
     * 火肉質25なので発動。
     *
     * 徹甲榴弾Lv3の火5：
     *
     * 5 × 25%
     * = 1.25
     *
     * × 1.10
     * = 1.375
     */
    @Test
    void testStickyAmmoElementExploitLv1() {

        BowgunAmmo ammo
                = createAmmo(
                        "徹甲榴弾",
                        3
                );


        BowgunSpecialDamageResult specialDamageResult
                = createSpecialDamageResult(
                        ammo,
                        350
                );


        MonsterHitzone hitzone
                = createHitzone();


        SkillEffectState elementExploit
                = new SkillEffectState();

        elementExploit.setStateCode(
                "ACTIVE"
        );

        elementExploit.setElementDamageMul(
                new BigDecimal("1.10")
        );


        BowgunSpecialFinalDamageResult result
                = bowgunSpecialFinalDamageService.calculate(
                        ammo,
                        specialDamageResult,
                        hitzone,
                        0,
                        null,
                        Map.of(
                                34,
                                elementExploit
                        )
                );


        assertBigDecimalEquals(
                "1.3750",
                result.getSecondaryElementDamageTotal()
        );


        assertEquals(
                70,
                result.getNormalTotalDamage()
        );


        System.out.println(
                "徹甲榴弾Lv3＋弱点特効【属性】Lv1："
                + "fire="
                + result.getSecondaryElementDamageTotal()
                + " / total="
                + result.getNormalTotalDamage()
        );
    }


    /*
     * =====================================================
     * 共通
     * =====================================================
     */

    private BowgunSpecialDamageResult createSpecialDamageResult(
            BowgunAmmo ammo,
            Integer weaponAttack) {

        BowgunAmmoModifier ammoModifier
                = bowgunAmmoModifierService.calculate(
                        ammo
                );


        return bowgunSpecialDamageService.calculate(
                weaponAttack,
                ammoModifier,
                null
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


    /*
     * オオナズチ頭部と同じ値。
     *
     * 斬55
     * 弾45
     * 火25
     */
    private MonsterHitzone createHitzone() {

        MonsterHitzone hitzone
                = new MonsterHitzone();

        hitzone.setPartName(
                "頭部"
        );

        hitzone.setSever(
                55
        );

        hitzone.setBlunt(
                50
        );

        hitzone.setShot(
                45
        );

        hitzone.setFire(
                25
        );

        hitzone.setWater(
                0
        );

        hitzone.setThunder(
                10
        );

        hitzone.setIce(
                0
        );

        hitzone.setDragon(
                20
        );

        return hitzone;
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