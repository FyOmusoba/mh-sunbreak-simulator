package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.BowgunStatusResult;
import com.example.mhsunbreaksimulator.dto.MonsterStatusProcResult;
import com.example.mhsunbreaksimulator.entity.MonsterStatusResistance;
import com.example.mhsunbreaksimulator.service.MonsterStatusProcService;

public class MonsterStatusProcServiceTest {

    private final MonsterStatusProcService
            monsterStatusProcService
            = new MonsterStatusProcService();


    /*
     * =====================================================
     * 250 ÷ 80
     * = 3.125
     *
     * 切り上げ
     * = 4射
     *
     * 80 × 4
     * = 320
     * =====================================================
     */
    @Test
    void testCeilingShotsToFirstProc() {

        MonsterStatusResistance resistance
                = createResistance(
                        250,
                        150,
                        700,
                        false
                );


        MonsterStatusProcResult result
                = monsterStatusProcService.calculate(
                        "poison",
                        new BigDecimal("80"),
                        resistance
                );


        assertEquals(
                "poison",
                result.getStatusType()
        );

        assertFalse(
                result.isImmune()
        );

        assertEquals(
                250,
                result.getInitialThreshold()
        );

        assertEquals(
                4,
                result.getShotsToFirstProc()
        );

        assertBigDecimalEquals(
                "80",
                result.getBuildupPerShot()
        );

        assertBigDecimalEquals(
                "320",
                result.getBuildupAtFirstProc()
        );


        System.out.println(
                "250÷80："
                + "shots="
                + result.getShotsToFirstProc()
                + " / buildup="
                + result.getBuildupAtFirstProc()
        );
    }


    /*
     * =====================================================
     * 割り切れる場合
     *
     * 240 ÷ 80
     * = 3射
     * =====================================================
     */
    @Test
    void testExactShotsToFirstProc() {

        MonsterStatusResistance resistance
                = createResistance(
                        240,
                        100,
                        600,
                        false
                );


        MonsterStatusProcResult result
                = monsterStatusProcService.calculate(
                        "paralysis",
                        new BigDecimal("80"),
                        resistance
                );


        assertEquals(
                3,
                result.getShotsToFirstProc()
        );

        assertBigDecimalEquals(
                "240",
                result.getBuildupAtFirstProc()
        );


        System.out.println(
                "240÷80："
                + "shots="
                + result.getShotsToFirstProc()
                + " / buildup="
                + result.getBuildupAtFirstProc()
        );
    }


    /*
     * =====================================================
     * BowgunStatusResult版
     *
     * 毒弾Lv2速射などを想定。
     *
     * 1射撃あたり160
     * 閾値250
     *
     * 250 ÷ 160
     * = 1.5625
     *
     * 切り上げ
     * = 2射
     *
     * 160 × 2
     * = 320
     * =====================================================
     */
    @Test
    void testBowgunStatusResult() {

        BowgunStatusResult bowgunStatusResult
                = new BowgunStatusResult();

        bowgunStatusResult.setStatusType(
                "poison"
        );

        bowgunStatusResult.setStatusAmmo(
                true
        );

        bowgunStatusResult.setStatusBuildupPerShot(
                new BigDecimal("160")
        );


        MonsterStatusResistance resistance
                = createResistance(
                        250,
                        150,
                        700,
                        false
                );


        MonsterStatusProcResult result
                = monsterStatusProcService.calculate(
                        bowgunStatusResult,
                        resistance
                );


        assertEquals(
                "poison",
                result.getStatusType()
        );

        assertEquals(
                2,
                result.getShotsToFirstProc()
        );

        assertBigDecimalEquals(
                "160",
                result.getBuildupPerShot()
        );

        assertBigDecimalEquals(
                "320",
                result.getBuildupAtFirstProc()
        );


        System.out.println(
                "ボウガン毒160 / 閾値250："
                + "shots="
                + result.getShotsToFirstProc()
                + " / buildup="
                + result.getBuildupAtFirstProc()
        );
    }


    /*
     * =====================================================
     * 完全耐性
     *
     * 何回撃っても発症しないので
     * shotsToFirstProc = null
     * =====================================================
     */
    @Test
    void testImmune() {

        MonsterStatusResistance resistance
                = createResistance(
                        250,
                        150,
                        700,
                        true
                );


        MonsterStatusProcResult result
                = monsterStatusProcService.calculate(
                        "sleep",
                        new BigDecimal("80"),
                        resistance
                );


        assertTrue(
                result.isImmune()
        );

        assertNull(
                result.getShotsToFirstProc()
        );

        assertBigDecimalEquals(
                "0",
                result.getBuildupAtFirstProc()
        );


        System.out.println(
                "完全耐性：発症不能 正常"
        );
    }


    /*
     * =====================================================
     * 1射撃あたり蓄積0
     *
     * 発症回数は計算不能。
     * =====================================================
     */
    @Test
    void testZeroBuildup() {

        MonsterStatusResistance resistance
                = createResistance(
                        250,
                        150,
                        700,
                        false
                );


        MonsterStatusProcResult result
                = monsterStatusProcService.calculate(
                        "poison",
                        BigDecimal.ZERO,
                        resistance
                );


        assertFalse(
                result.isImmune()
        );

        assertNull(
                result.getShotsToFirstProc()
        );

        assertBigDecimalEquals(
                "0",
                result.getBuildupAtFirstProc()
        );


        System.out.println(
                "蓄積0：発症回数計算なし 正常"
        );
    }


    /*
     * =====================================================
     * resistance=null
     *
     * モンスター耐性が取得できていない場合。
     * =====================================================
     */
    @Test
    void testNullResistance() {

        MonsterStatusProcResult result
                = monsterStatusProcService.calculate(
                        "poison",
                        new BigDecimal("80"),
                        null
                );


        assertEquals(
                "poison",
                result.getStatusType()
        );

        assertBigDecimalEquals(
                "80",
                result.getBuildupPerShot()
        );

        assertNull(
                result.getShotsToFirstProc()
        );


        System.out.println(
                "resistance=null：正常"
        );
    }


    /*
     * =====================================================
     * BowgunStatusResult=null
     * =====================================================
     */
    @Test
    void testNullBowgunStatusResult() {

        MonsterStatusProcResult result
                = monsterStatusProcService.calculate(
                        (BowgunStatusResult) null,
                        null
                );


        assertNull(
                result.getStatusType()
        );

        assertNull(
                result.getShotsToFirstProc()
        );

        assertBigDecimalEquals(
                "0",
                result.getBuildupPerShot()
        );

        assertBigDecimalEquals(
                "0",
                result.getBuildupAtFirstProc()
        );


        System.out.println(
                "BowgunStatusResult=null：正常"
        );
    }


    /*
     * =====================================================
     * テスト用耐性データ
     * =====================================================
     */
    private MonsterStatusResistance createResistance(
            Integer initialThreshold,
            Integer increasePerProc,
            Integer maxThreshold,
            Boolean immune) {

        MonsterStatusResistance resistance
                = new MonsterStatusResistance();

        resistance.setInitialThreshold(
                initialThreshold
        );

        resistance.setIncreasePerProc(
                increasePerProc
        );

        resistance.setMaxThreshold(
                maxThreshold
        );

        resistance.setDecayValue(
                new BigDecimal("5.00")
        );

        resistance.setEffectDurationSec(
                new BigDecimal("15.00")
        );

        resistance.setProcDamage(
                100
        );

        resistance.setIsImmune(
                immune
        );

        return resistance;
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