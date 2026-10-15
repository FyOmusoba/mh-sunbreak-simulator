package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;
import com.example.mhsunbreaksimulator.service.ConditionalSkillModifierService;

public class ConditionalSkillStatusModifierTest {

    private final ConditionalSkillModifierService
            conditionalSkillModifierService
            = new ConditionalSkillModifierService();


    /*
     * 各状態異常補正が
     * 正しい対象へ振り分けられることを確認。
     */
    @Test
    void testStatusTargetAggregation() {

        Map<Integer, SkillEffectState> selectedEffects
                = new HashMap<>();


        /*
         * 毒専用
         *
         * +5
         * ×1.20
         */
        selectedEffects.put(
                1,
                createStatusEffect(
                        "POISON",
                        "5",
                        "1.20"
                )
        );


        /*
         * 麻痺専用
         *
         * +2
         * ×1.10
         */
        selectedEffects.put(
                2,
                createStatusEffect(
                        "PARALYSIS",
                        "2",
                        "1.10"
                )
        );


        /*
         * 睡眠専用
         *
         * +1
         * ×1.05
         */
        selectedEffects.put(
                3,
                createStatusEffect(
                        "SLEEP",
                        "1",
                        "1.05"
                )
        );


        /*
         * 爆破専用
         *
         * +5
         * ×1.20
         */
        selectedEffects.put(
                4,
                createStatusEffect(
                        "BLAST",
                        "5",
                        "1.20"
                )
        );


        /*
         * 全状態異常共通
         *
         * +7
         * ×1.15
         */
        selectedEffects.put(
                5,
                createStatusEffect(
                        "ALL",
                        "7",
                        "1.15"
                )
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        selectedEffects
                );


        /*
         * ALL
         */
        assertBigDecimalEquals(
                "7",
                result.getStatusAdd()
        );

        assertBigDecimalEquals(
                "1.15",
                result.getStatusMul()
        );


        /*
         * POISON
         */
        assertBigDecimalEquals(
                "5",
                result.getPoisonStatusAdd()
        );

        assertBigDecimalEquals(
                "1.20",
                result.getPoisonStatusMul()
        );


        /*
         * PARALYSIS
         */
        assertBigDecimalEquals(
                "2",
                result.getParalysisStatusAdd()
        );

        assertBigDecimalEquals(
                "1.10",
                result.getParalysisStatusMul()
        );


        /*
         * SLEEP
         */
        assertBigDecimalEquals(
                "1",
                result.getSleepStatusAdd()
        );

        assertBigDecimalEquals(
                "1.05",
                result.getSleepStatusMul()
        );


        /*
         * BLAST
         */
        assertBigDecimalEquals(
                "5",
                result.getBlastStatusAdd()
        );

        assertBigDecimalEquals(
                "1.20",
                result.getBlastStatusMul()
        );


        System.out.println(
                "ALL：add="
                + result.getStatusAdd()
                + " / mul="
                + result.getStatusMul()
        );

        System.out.println(
                "POISON：add="
                + result.getPoisonStatusAdd()
                + " / mul="
                + result.getPoisonStatusMul()
        );

        System.out.println(
                "PARALYSIS：add="
                + result.getParalysisStatusAdd()
                + " / mul="
                + result.getParalysisStatusMul()
        );

        System.out.println(
                "SLEEP：add="
                + result.getSleepStatusAdd()
                + " / mul="
                + result.getSleepStatusMul()
        );

        System.out.println(
                "BLAST：add="
                + result.getBlastStatusAdd()
                + " / mul="
                + result.getBlastStatusMul()
        );
    }


    /*
     * 同じ対象の効果が複数ある場合。
     *
     * POISON
     *
     * +1
     * ×1.05
     *
     * +5
     * ×1.20
     *
     * ----------------
     *
     * add
     * = 1 + 5
     * = 6
     *
     * mul
     * = 1.05 × 1.20
     * = 1.26
     */
    @Test
    void testMultiplePoisonModifiers() {

        Map<Integer, SkillEffectState> selectedEffects
                = new HashMap<>();


        selectedEffects.put(
                1,
                createStatusEffect(
                        "POISON",
                        "1",
                        "1.05"
                )
        );

        selectedEffects.put(
                2,
                createStatusEffect(
                        "POISON",
                        "5",
                        "1.20"
                )
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        selectedEffects
                );


        assertBigDecimalEquals(
                "6",
                result.getPoisonStatusAdd()
        );

        assertBigDecimalEquals(
                "1.26",
                result.getPoisonStatusMul()
        );


        /*
         * 他の状態異常には混ざらない。
         */
        assertBigDecimalEquals(
                "0",
                result.getParalysisStatusAdd()
        );

        assertBigDecimalEquals(
                "1",
                result.getParalysisStatusMul()
        );

        assertBigDecimalEquals(
                "0",
                result.getSleepStatusAdd()
        );

        assertBigDecimalEquals(
                "1",
                result.getSleepStatusMul()
        );


        System.out.println(
                "POISON複数集計：add="
                + result.getPoisonStatusAdd()
                + " / mul="
                + result.getPoisonStatusMul()
        );
    }


    /*
     * statusTarget=nullなら、
     *
     * statusAdd / statusMul に値があっても
     * 状態異常補正として集計しない。
     */
    @Test
    void testNullStatusTargetIgnored() {

        SkillEffectState effect
                = new SkillEffectState();

        effect.setStatusTarget(
                null
        );

        effect.setStatusAdd(
                new BigDecimal("999")
        );

        effect.setStatusMul(
                new BigDecimal("9.99")
        );


        Map<Integer, SkillEffectState> selectedEffects
                = new HashMap<>();

        selectedEffects.put(
                1,
                effect
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        selectedEffects
                );


        assertBigDecimalEquals(
                "0",
                result.getStatusAdd()
        );

        assertBigDecimalEquals(
                "1",
                result.getStatusMul()
        );

        assertBigDecimalEquals(
                "0",
                result.getPoisonStatusAdd()
        );

        assertBigDecimalEquals(
                "1",
                result.getPoisonStatusMul()
        );

        assertBigDecimalEquals(
                "0",
                result.getParalysisStatusAdd()
        );

        assertBigDecimalEquals(
                "1",
                result.getParalysisStatusMul()
        );

        assertBigDecimalEquals(
                "0",
                result.getSleepStatusAdd()
        );

        assertBigDecimalEquals(
                "1",
                result.getSleepStatusMul()
        );

        assertBigDecimalEquals(
                "0",
                result.getBlastStatusAdd()
        );

        assertBigDecimalEquals(
                "1",
                result.getBlastStatusMul()
        );


        System.out.println(
                "statusTarget=null：状態異常補正を無視 正常"
        );
    }


    /*
     * 未知のstatusTargetも無視する。
     */
    @Test
    void testUnknownStatusTargetIgnored() {

        Map<Integer, SkillEffectState> selectedEffects
                = new HashMap<>();


        selectedEffects.put(
                1,
                createStatusEffect(
                        "UNKNOWN",
                        "100",
                        "5.00"
                )
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        selectedEffects
                );


        assertBigDecimalEquals(
                "0",
                result.getStatusAdd()
        );

        assertBigDecimalEquals(
                "1",
                result.getStatusMul()
        );

        assertBigDecimalEquals(
                "0",
                result.getPoisonStatusAdd()
        );

        assertBigDecimalEquals(
                "1",
                result.getPoisonStatusMul()
        );


        System.out.println(
                "未知statusTarget：無視 正常"
        );
    }


    /*
     * Map=null
     */
    @Test
    void testNullSelectedEffects() {

        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        null
                );


        assertBigDecimalEquals(
                "0",
                result.getStatusAdd()
        );

        assertBigDecimalEquals(
                "1",
                result.getStatusMul()
        );

        assertBigDecimalEquals(
                "0",
                result.getPoisonStatusAdd()
        );

        assertBigDecimalEquals(
                "1",
                result.getPoisonStatusMul()
        );


        System.out.println(
                "selectedEffects=null：正常"
        );
    }


    /*
     * 空Map
     */
    @Test
    void testEmptySelectedEffects() {

        Map<Integer, SkillEffectState> selectedEffects
                = new HashMap<>();


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        selectedEffects
                );


        assertBigDecimalEquals(
                "0",
                result.getStatusAdd()
        );

        assertBigDecimalEquals(
                "1",
                result.getStatusMul()
        );

        assertBigDecimalEquals(
                "0",
                result.getPoisonStatusAdd()
        );

        assertBigDecimalEquals(
                "1",
                result.getPoisonStatusMul()
        );


        System.out.println(
                "selectedEffects=empty：正常"
        );
    }


    /*
     * テスト用状態異常効果生成
     */
    private SkillEffectState createStatusEffect(
            String statusTarget,
            String statusAdd,
            String statusMul) {

        SkillEffectState effect
                = new SkillEffectState();

        effect.setStatusTarget(
                statusTarget
        );

        effect.setStatusAdd(
                statusAdd == null
                        ? null
                        : new BigDecimal(
                                statusAdd
                        )
        );

        effect.setStatusMul(
                statusMul == null
                        ? null
                        : new BigDecimal(
                                statusMul
                        )
        );

        return effect;
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