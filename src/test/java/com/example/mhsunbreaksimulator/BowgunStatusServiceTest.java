package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.BowgunRapidFireModifier;
import com.example.mhsunbreaksimulator.dto.BowgunStatusModifier;
import com.example.mhsunbreaksimulator.dto.BowgunStatusResult;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.entity.BowgunAmmo;
import com.example.mhsunbreaksimulator.service.BowgunRapidFireModifierService;
import com.example.mhsunbreaksimulator.service.BowgunStatusModifierService;
import com.example.mhsunbreaksimulator.service.BowgunStatusService;

public class BowgunStatusServiceTest {

    private final BowgunStatusModifierService
            bowgunStatusModifierService
            = new BowgunStatusModifierService();

    private final BowgunRapidFireModifierService
            bowgunRapidFireModifierService
            = new BowgunRapidFireModifierService();

    private final BowgunStatusService
            bowgunStatusService
            = new BowgunStatusService();


    /*
     * 毒弾Lv2
     * 非速射
     *
     * 80 × 1
     * = 80
     */
    @Test
    void testPoisonLv2NonRapidFire() {

        BowgunAmmo ammo
                = createAmmo(
                        "毒弾",
                        2,
                        false
                );


        BowgunStatusResult result
                = calculate(
                        ammo,
                        null
                );


        assertTrue(
                result.isStatusAmmo()
        );

        assertEquals(
                "poison",
                result.getStatusType()
        );

        assertBigDecimalEquals(
                "80",
                result.getStatusBuildupPerHit()
        );

        assertEquals(
                1,
                result.getShotCount()
        );

        assertBigDecimalEquals(
                "80",
                result.getStatusBuildupPerShot()
        );


        System.out.println(
                "毒弾Lv2・非速射："
                + "perHit="
                + result.getStatusBuildupPerHit()
                + " / shotCount="
                + result.getShotCount()
                + " / total="
                + result.getStatusBuildupPerShot()
        );
    }


    /*
     * 毒弾Lv2
     * 2発速射
     *
     * 80 × 2
     * = 160
     */
    @Test
    void testPoisonLv2RapidFire() {

        BowgunAmmo ammo
                = createAmmo(
                        "毒弾",
                        2,
                        true
                );


        BowgunStatusResult result
                = calculate(
                        ammo,
                        null
                );


        assertBigDecimalEquals(
                "80",
                result.getStatusBuildupPerHit()
        );

        assertEquals(
                2,
                result.getShotCount()
        );

        assertBigDecimalEquals(
                "160",
                result.getStatusBuildupPerShot()
        );


        System.out.println(
                "毒弾Lv2・速射："
                + "perHit="
                + result.getStatusBuildupPerHit()
                + " / shotCount="
                + result.getShotCount()
                + " / total="
                + result.getStatusBuildupPerShot()
        );
    }


    /*
     * =====================================================
     * 毒専用補正
     * =====================================================
     *
     * POISON
     *
     * statusMul = 1.20
     * statusAdd = 5
     *
     * 毒Lv2：
     *
     * 80 × 1.20 + 5
     * = 101
     *
     * 2発速射
     *
     * 101 × 2
     * = 202
     */
    @Test
    void testPoisonSpecificModifier() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setPoisonStatusMul(
                new BigDecimal("1.20")
        );

        modifier.setPoisonStatusAdd(
                new BigDecimal("5")
        );


        BowgunStatusResult result
                = calculate(
                        createAmmo(
                                "毒弾",
                                2,
                                true
                        ),
                        modifier
                );


        assertBigDecimalEquals(
                "101",
                result.getStatusBuildupPerHit()
        );

        assertBigDecimalEquals(
                "202",
                result.getStatusBuildupPerShot()
        );


        System.out.println(
                "毒専用補正・毒弾Lv2："
                + "perHit="
                + result.getStatusBuildupPerHit()
                + " / total="
                + result.getStatusBuildupPerShot()
        );
    }


    /*
     * 毒専用補正が
     * 麻痺弾へ漏れないことを確認。
     *
     * POISON
     * ×1.20
     * +5
     *
     * が設定されていても、
     *
     * 麻痺Lv2は
     * 80のまま。
     *
     * 2発速射
     * = 160
     */
    @Test
    void testPoisonModifierDoesNotAffectParalysis() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setPoisonStatusMul(
                new BigDecimal("1.20")
        );

        modifier.setPoisonStatusAdd(
                new BigDecimal("5")
        );


        BowgunStatusResult result
                = calculate(
                        createAmmo(
                                "麻痺弾",
                                2,
                                true
                        ),
                        modifier
                );


        assertEquals(
                "paralysis",
                result.getStatusType()
        );

        assertBigDecimalEquals(
                "80",
                result.getStatusBuildupPerHit()
        );

        assertBigDecimalEquals(
                "160",
                result.getStatusBuildupPerShot()
        );


        System.out.println(
                "毒専用補正→麻痺弾：影響なし "
                + "perHit="
                + result.getStatusBuildupPerHit()
                + " / total="
                + result.getStatusBuildupPerShot()
        );
    }


    /*
     * 毒専用補正が
     * 睡眠弾へ漏れないことを確認。
     */
    @Test
    void testPoisonModifierDoesNotAffectSleep() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setPoisonStatusMul(
                new BigDecimal("1.20")
        );

        modifier.setPoisonStatusAdd(
                new BigDecimal("5")
        );


        BowgunStatusResult result
                = calculate(
                        createAmmo(
                                "睡眠弾",
                                2,
                                true
                        ),
                        modifier
                );


        assertEquals(
                "sleep",
                result.getStatusType()
        );

        assertBigDecimalEquals(
                "80",
                result.getStatusBuildupPerHit()
        );

        assertBigDecimalEquals(
                "160",
                result.getStatusBuildupPerShot()
        );


        System.out.println(
                "毒専用補正→睡眠弾：影響なし "
                + "perHit="
                + result.getStatusBuildupPerHit()
                + " / total="
                + result.getStatusBuildupPerShot()
        );
    }


    /*
     * =====================================================
     * ALL補正
     * =====================================================
     *
     * ALL倍率は
     * 毒・麻痺・睡眠すべてへ適用される。
     *
     * 基礎80
     * ×1.10
     * =88
     *
     * 2発速射
     * =176
     */
    @Test
    void testAllModifierAffectsAllStatusAmmo() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setStatusMul(
                new BigDecimal("1.10")
        );


        BowgunStatusResult poison
                = calculate(
                        createAmmo(
                                "毒弾",
                                2,
                                true
                        ),
                        modifier
                );

        BowgunStatusResult paralysis
                = calculate(
                        createAmmo(
                                "麻痺弾",
                                2,
                                true
                        ),
                        modifier
                );

        BowgunStatusResult sleep
                = calculate(
                        createAmmo(
                                "睡眠弾",
                                2,
                                true
                        ),
                        modifier
                );


        assertBigDecimalEquals(
                "88",
                poison.getStatusBuildupPerHit()
        );

        assertBigDecimalEquals(
                "176",
                poison.getStatusBuildupPerShot()
        );


        assertBigDecimalEquals(
                "88",
                paralysis.getStatusBuildupPerHit()
        );

        assertBigDecimalEquals(
                "176",
                paralysis.getStatusBuildupPerShot()
        );


        assertBigDecimalEquals(
                "88",
                sleep.getStatusBuildupPerHit()
        );

        assertBigDecimalEquals(
                "176",
                sleep.getStatusBuildupPerShot()
        );


        System.out.println(
                "ALL補正："
                + "毒="
                + poison.getStatusBuildupPerShot()
                + " / 麻痺="
                + paralysis.getStatusBuildupPerShot()
                + " / 睡眠="
                + sleep.getStatusBuildupPerShot()
        );
    }


    /*
     * =====================================================
     * ALL + POISON
     * =====================================================
     *
     * ALL
     * ×1.10
     *
     * POISON
     * ×1.20
     * +5
     *
     * 合成倍率
     * =1.10×1.20
     * =1.32
     *
     * 毒Lv2
     *
     * 80×1.32+5
     * =110.6
     *
     * 2発速射
     * =221.2
     */
    @Test
    void testAllAndPoisonModifier() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setStatusMul(
                new BigDecimal("1.10")
        );

        modifier.setPoisonStatusMul(
                new BigDecimal("1.20")
        );

        modifier.setPoisonStatusAdd(
                new BigDecimal("5")
        );


        BowgunStatusResult poison
                = calculate(
                        createAmmo(
                                "毒弾",
                                2,
                                true
                        ),
                        modifier
                );


        BowgunStatusResult paralysis
                = calculate(
                        createAmmo(
                                "麻痺弾",
                                2,
                                true
                        ),
                        modifier
                );


        assertBigDecimalEquals(
                "110.6",
                poison.getStatusBuildupPerHit()
        );

        assertBigDecimalEquals(
                "221.2",
                poison.getStatusBuildupPerShot()
        );


        /*
         * 麻痺にはPOISON専用補正が入らず、
         * ALLの×1.10だけ。
         *
         * 80×1.10
         * =88
         *
         * 88×2
         * =176
         */
        assertBigDecimalEquals(
                "88",
                paralysis.getStatusBuildupPerHit()
        );

        assertBigDecimalEquals(
                "176",
                paralysis.getStatusBuildupPerShot()
        );


        System.out.println(
                "ALL+POISON："
                + "毒perHit="
                + poison.getStatusBuildupPerHit()
                + " / 毒total="
                + poison.getStatusBuildupPerShot()
                + " / 麻痺total="
                + paralysis.getStatusBuildupPerShot()
        );
    }


    /*
     * 麻痺専用補正
     */
    @Test
    void testParalysisSpecificModifier() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setParalysisStatusMul(
                new BigDecimal("1.10")
        );

        modifier.setParalysisStatusAdd(
                new BigDecimal("2")
        );


        BowgunStatusResult result
                = calculate(
                        createAmmo(
                                "麻痺弾",
                                2,
                                true
                        ),
                        modifier
                );


        /*
         * 80×1.10+2
         * =90
         *
         * 2発
         * =180
         */
        assertBigDecimalEquals(
                "90",
                result.getStatusBuildupPerHit()
        );

        assertBigDecimalEquals(
                "180",
                result.getStatusBuildupPerShot()
        );


        System.out.println(
                "麻痺専用補正：total="
                + result.getStatusBuildupPerShot()
        );
    }


    /*
     * 睡眠専用補正
     */
    @Test
    void testSleepSpecificModifier() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setSleepStatusMul(
                new BigDecimal("1.05")
        );

        modifier.setSleepStatusAdd(
                new BigDecimal("1")
        );


        BowgunStatusResult result
                = calculate(
                        createAmmo(
                                "睡眠弾",
                                2,
                                true
                        ),
                        modifier
                );


        /*
         * 80×1.05+1
         * =85
         *
         * 2発
         * =170
         */
        assertBigDecimalEquals(
                "85",
                result.getStatusBuildupPerHit()
        );

        assertBigDecimalEquals(
                "170",
                result.getStatusBuildupPerShot()
        );


        System.out.println(
                "睡眠専用補正：total="
                + result.getStatusBuildupPerShot()
        );
    }


    /*
     * 減気弾には
     *
     * ALL
     * POISON
     * PARALYSIS
     * SLEEP
     *
     * の状態異常補正を適用しない。
     */
    @Test
    void testExhaustIgnoresStatusModifiers() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setStatusMul(
                new BigDecimal("9.99")
        );

        modifier.setStatusAdd(
                new BigDecimal("999")
        );

        modifier.setPoisonStatusMul(
                new BigDecimal("9.99")
        );

        modifier.setPoisonStatusAdd(
                new BigDecimal("999")
        );


        BowgunStatusResult result
                = calculate(
                        createAmmo(
                                "減気弾",
                                2,
                                true
                        ),
                        modifier
                );


        assertBigDecimalEquals(
                "0",
                result.getStatusBuildupPerShot()
        );

        assertBigDecimalEquals(
                "100",
                result.getExhaustBuildupPerHit()
        );

        assertBigDecimalEquals(
                "200",
                result.getExhaustBuildupPerShot()
        );

        assertBigDecimalEquals(
                "30",
                result.getStunBuildupPerHit()
        );

        assertBigDecimalEquals(
                "60",
                result.getStunBuildupPerShot()
        );


        System.out.println(
                "減気弾・状態異常補正無視："
                + "exhaust="
                + result.getExhaustBuildupPerShot()
                + " / stun="
                + result.getStunBuildupPerShot()
        );
    }


    /*
     * 通常弾は状態異常計算なし。
     */
    @Test
    void testNormalAmmo() {

        BowgunStatusResult result
                = calculate(
                        createAmmo(
                                "通常弾",
                                3,
                                true
                        ),
                        null
                );


        assertFalse(
                result.isStatusAmmo()
        );

        assertEquals(
                null,
                result.getStatusType()
        );

        assertBigDecimalEquals(
                "0",
                result.getStatusBuildupPerShot()
        );

        assertBigDecimalEquals(
                "0",
                result.getExhaustBuildupPerShot()
        );

        assertBigDecimalEquals(
                "0",
                result.getStunBuildupPerShot()
        );


        System.out.println(
                "通常弾：状態異常計算なし 正常"
        );
    }


    /*
     * statusModifier=null
     */
    @Test
    void testNullStatusModifier() {

        BowgunStatusResult result
                = bowgunStatusService.calculate(
                        null,
                        null,
                        new ConditionalSkillModifierTotal()
                );


        assertFalse(
                result.isStatusAmmo()
        );

        assertBigDecimalEquals(
                "0",
                result.getStatusBuildupPerShot()
        );

        assertBigDecimalEquals(
                "0",
                result.getExhaustBuildupPerShot()
        );

        assertBigDecimalEquals(
                "0",
                result.getStunBuildupPerShot()
        );


        System.out.println(
                "statusModifier=null：正常"
        );
    }


    /*
     * 弾データから
     *
     * StatusModifier
     * RapidFireModifier
     *
     * を生成して計算する。
     */
    private BowgunStatusResult calculate(
            BowgunAmmo ammo,
            ConditionalSkillModifierTotal conditionalModifier) {

        BowgunStatusModifier statusModifier
                = bowgunStatusModifierService.calculate(
                        ammo
                );


        BowgunRapidFireModifier rapidFireModifier
                = bowgunRapidFireModifierService.calculate(
                        ammo
                );


        return bowgunStatusService.calculate(
                statusModifier,
                rapidFireModifier,
                conditionalModifier
        );
    }


    private BowgunAmmo createAmmo(
            String ammoType,
            Integer ammoLevel,
            Boolean rapidFire) {

        BowgunAmmo ammo
                = new BowgunAmmo();

        ammo.setAmmoType(
                ammoType
        );

        ammo.setAmmoLevel(
                ammoLevel
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