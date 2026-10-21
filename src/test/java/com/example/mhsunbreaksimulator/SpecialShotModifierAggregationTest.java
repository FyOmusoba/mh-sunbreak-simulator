package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;
import com.example.mhsunbreaksimulator.service.ConditionalSkillModifierService;

public class SpecialShotModifierAggregationTest {

    private final ConditionalSkillModifierService
            conditionalSkillModifierService
            = new ConditionalSkillModifierService();


    /*
     * =====================================================
     * 特殊射撃強化 Lv2
     * =====================================================
     *
     * specialShotMul = 1.20
     *
     * 通常射撃用 shotMul には影響しないことも確認する。
     */
    @Test
    void testSpecialShotBoostLevel2Aggregation() {

        SkillEffectState effect
                = new SkillEffectState();

        effect.setStateCode(
                "DEFAULT"
        );

        effect.setSpecialShotMul(
                new BigDecimal("1.20")
        );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                95,
                effect
        );


        ConditionalSkillModifierTotal result
                = conditionalSkillModifierService.calculate(
                        effects
                );


        assertDecimalEquals(
                "1.20",
                result.getSpecialShotMul()
        );


        /*
         * 特殊射撃強化が
         * 通常のshotMulへ混入していないことを確認。
         */
        assertDecimalEquals(
                "1",
                result.getShotMul()
        );


        System.out.println(
                "特殊射撃強化Lv2集計："
                + "specialShotMul="
                + result.getSpecialShotMul()
                + " / shotMul="
                + result.getShotMul()
        );
    }


    /*
     * =====================================================
     * BigDecimal比較
     * =====================================================
     */
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