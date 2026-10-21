package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.BowgunWyvernblastDamageResult;
import com.example.mhsunbreaksimulator.dto.FinalDamageResult;
import com.example.mhsunbreaksimulator.entity.MonsterHitzone;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;
import com.example.mhsunbreaksimulator.service.BowgunWyvernblastFinalDamageService;
import com.example.mhsunbreaksimulator.service.MonsterWeakpointService;
import com.example.mhsunbreaksimulator.service.WeakpointSkillModifierService;

public class BowgunWyvernblastFinalDamageServiceTest {

    private final BowgunWyvernblastFinalDamageService service
            = new BowgunWyvernblastFinalDamageService(
                    new MonsterWeakpointService(),
                    new WeakpointSkillModifierService()
            );


    /*
     * =====================================================
     * 基準
     * =====================================================
     *
     * 固定ダメージ：
     *
     * 176.4
     *
     * ↓
     *
     * 物理肉質を無視してそのまま。
     *
     *
     * 火属性：
     *
     * 火7
     * × 火肉質25%
     *
     * = 1.75
     *
     *
     * 四捨五入：
     *
     * 固定176.4
     * → 176
     *
     * 火1.75
     * → 2
     *
     * 合計178
     *
     *
     * 起爆竜弾は会心で変化しないため、
     *
     * 通常
     * プラス会心
     * マイナス会心
     *
     * はすべて178。
     */
    @Test
    void testBaseline() {

        BowgunWyvernblastDamageResult damageResult
                = createDamageResult(
                        "176.4",
                        "7"
                );


        MonsterHitzone hitzone
                = createHitzone(
                        10,
                        25
                );


        FinalDamageResult result
                = service.calculate(
                        damageResult,
                        hitzone,
                        Map.of()
                );


        /*
         * 固定ダメージは
         * 弾肉質10でも減少しない。
         */
        assertDecimalEquals(
                "176.4",
                result.getNormalPhysicalDamageBeforeRound()
        );


        /*
         * 火7 × 火25%
         * = 1.75
         */
        assertDecimalEquals(
                "1.75",
                result.getNormalElementDamageBeforeRound()
        );


        assertEquals(
                176,
                result.getNormalPhysicalDamage()
        );

        assertEquals(
                2,
                result.getNormalElementDamage()
        );

        assertEquals(
                178,
                result.getNormalTotalDamage()
        );


        /*
         * 会心によって変化しない。
         */
        assertDecimalEquals(
                "176.4",
                result.getPositiveCriticalPhysicalDamageBeforeRound()
        );

        assertDecimalEquals(
                "1.75",
                result.getPositiveCriticalElementDamageBeforeRound()
        );

        assertEquals(
                176,
                result.getPositiveCriticalPhysicalDamage()
        );

        assertEquals(
                2,
                result.getPositiveCriticalElementDamage()
        );

        assertEquals(
                178,
                result.getPositiveCriticalTotalDamage()
        );


        assertDecimalEquals(
                "176.4",
                result.getNegativeCriticalPhysicalDamageBeforeRound()
        );

        assertEquals(
                176,
                result.getNegativeCriticalPhysicalDamage()
        );

        assertEquals(
                2,
                result.getNegativeCriticalElementDamage()
        );

        assertEquals(
                178,
                result.getNegativeCriticalTotalDamage()
        );
    }


    /*
     * =====================================================
     * 火肉質0
     * =====================================================
     *
     * 固定ダメージ176.4はそのまま。
     *
     * 火属性部分だけ0になる。
     */
    @Test
    void testFireHitzoneZero() {

        BowgunWyvernblastDamageResult damageResult
                = createDamageResult(
                        "176.4",
                        "7"
                );


        MonsterHitzone hitzone
                = createHitzone(
                        100,
                        0
                );


        FinalDamageResult result
                = service.calculate(
                        damageResult,
                        hitzone,
                        Map.of()
                );


        /*
         * 弾肉質100でも固定ダメージは
         * 176.4のまま。
         */
        assertDecimalEquals(
                "176.4",
                result.getNormalPhysicalDamageBeforeRound()
        );

        assertDecimalEquals(
                "0",
                result.getNormalElementDamageBeforeRound()
        );

        assertEquals(
                176,
                result.getNormalPhysicalDamage()
        );

        assertEquals(
                0,
                result.getNormalElementDamage()
        );

        assertEquals(
                176,
                result.getNormalTotalDamage()
        );
    }


    /*
     * =====================================================
     * 弱点特効【属性】Lv1
     * =====================================================
     *
     * 火肉質25なので属性弱点。
     *
     * 火7
     * × 25%
     * = 1.75
     *
     * × 1.10
     * = 1.925
     *
     *
     * 固定ダメージ176.4には
     * 属性弱点倍率を掛けない。
     */
    @Test
    void testElementExploitLevel1() {

        BowgunWyvernblastDamageResult damageResult
                = createDamageResult(
                        "176.4",
                        "7"
                );


        MonsterHitzone hitzone
                = createHitzone(
                        45,
                        25
                );


        SkillEffectState elementExploit
                = new SkillEffectState();

        elementExploit.setStateCode(
                "ACTIVE"
        );

        elementExploit.setElementDamageMul(
                new BigDecimal(
                        "1.10"
                )
        );


        FinalDamageResult result
                = service.calculate(
                        damageResult,
                        hitzone,
                        Map.of(
                                34,
                                elementExploit
                        )
                );


        /*
         * 固定部分は変化なし。
         */
        assertDecimalEquals(
                "176.4",
                result.getNormalPhysicalDamageBeforeRound()
        );


        /*
         * 7 × 0.25 × 1.10
         * = 1.925
         */
        assertDecimalEquals(
                "1.9250",
                result.getNormalElementDamageBeforeRound()
        );


        assertEquals(
                176,
                result.getNormalPhysicalDamage()
        );

        assertEquals(
                2,
                result.getNormalElementDamage()
        );

        assertEquals(
                178,
                result.getNormalTotalDamage()
        );
    }


    /*
     * =====================================================
     * DamageResult null
     * =====================================================
     */
    @Test
    void testNullDamageResult() {

        FinalDamageResult result
                = service.calculate(
                        null,
                        createHitzone(
                                45,
                                25
                        ),
                        Map.of()
                );


        assertDecimalEquals(
                "0",
                result.getNormalPhysicalDamageBeforeRound()
        );

        assertDecimalEquals(
                "0",
                result.getNormalElementDamageBeforeRound()
        );

        assertEquals(
                0,
                result.getNormalTotalDamage()
        );
    }


    /*
     * =====================================================
     * Hitzone null
     * =====================================================
     */
    @Test
    void testNullHitzone() {

        FinalDamageResult result
                = service.calculate(
                        createDamageResult(
                                "176.4",
                                "7"
                        ),
                        null,
                        Map.of()
                );


        assertDecimalEquals(
                "0",
                result.getNormalPhysicalDamageBeforeRound()
        );

        assertDecimalEquals(
                "0",
                result.getNormalElementDamageBeforeRound()
        );

        assertEquals(
                0,
                result.getNormalTotalDamage()
        );
    }


    /*
     * =====================================================
     * 共通
     * =====================================================
     */

    private BowgunWyvernblastDamageResult createDamageResult(
            String fixedDamage,
            String fixedFireValue) {

        BowgunWyvernblastDamageResult result
                = new BowgunWyvernblastDamageResult();

        result.setEffectiveAttack(
                new BigDecimal(
                        "300"
                )
        );

        result.setFixedDamage(
                new BigDecimal(
                        fixedDamage
                )
        );

        result.setFixedFireValue(
                new BigDecimal(
                        fixedFireValue
                )
        );

        result.setDurabilityCost(
                4
        );

        return result;
    }


    private MonsterHitzone createHitzone(
            Integer shot,
            Integer fire) {

        MonsterHitzone hitzone
                = new MonsterHitzone();

        hitzone.setPartName(
                "テスト部位"
        );

        hitzone.setSever(
                50
        );

        hitzone.setBlunt(
                50
        );

        hitzone.setShot(
                shot
        );

        hitzone.setFire(
                fire
        );

        hitzone.setWater(
                0
        );

        hitzone.setThunder(
                0
        );

        hitzone.setIce(
                0
        );

        hitzone.setDragon(
                0
        );

        return hitzone;
    }


    private void assertDecimalEquals(
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