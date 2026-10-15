package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.ElementHitzoneDamageResult;
import com.example.mhsunbreaksimulator.dto.FinalDamageResult;
import com.example.mhsunbreaksimulator.dto.PhysicalHitzoneDamageResult;

@Service
public class FinalDamageService {

    /*
     * =====================================================
     * 最終ダメージ計算
     * =====================================================
     *
     * 物理ダメージ
     * ＋
     * 属性ダメージ
     *
     * を最終的な表示値へ変換する。
     *
     *
     * 流れ：
     *
     * 1.
     * 物理を四捨五入
     *
     * 2.
     * 属性を四捨五入
     *
     * 3.
     * 両方を加算
     *
     *
     * 非会心
     * プラス会心
     * マイナス会心
     *
     * の3パターンをそれぞれ計算する。
     */
    public FinalDamageResult calculate(
            PhysicalHitzoneDamageResult physicalResult,
            ElementHitzoneDamageResult elementResult) {

        FinalDamageResult result
                = new FinalDamageResult();


        /*
         * =================================================
         * 非会心
         * =================================================
         */


        /*
         * 四捨五入前の物理。
         */
        BigDecimal normalPhysicalBeforeRound
                = physicalResult == null
                        ? BigDecimal.ZERO
                        : safeDamage(
                                physicalResult
                                        .getNormalPhysicalDamagePerHit()
                        );


        /*
         * 四捨五入前の属性。
         */
        BigDecimal normalElementBeforeRound
                = elementResult == null
                        ? BigDecimal.ZERO
                        : safeDamage(
                                elementResult
                                        .getNormalElementDamagePerHit()
                        );


        result.setNormalPhysicalDamageBeforeRound(
                normalPhysicalBeforeRound
        );

        result.setNormalElementDamageBeforeRound(
                normalElementBeforeRound
        );


        /*
         * 物理・属性を
         * それぞれ個別に四捨五入する。
         */
        int normalPhysicalDamage
                = roundDamage(
                        normalPhysicalBeforeRound
                );


        int normalElementDamage
                = roundDamage(
                        normalElementBeforeRound
                );


        result.setNormalPhysicalDamage(
                normalPhysicalDamage
        );

        result.setNormalElementDamage(
                normalElementDamage
        );


        /*
         * 四捨五入後に合算。
         */
        result.setNormalTotalDamage(
                normalPhysicalDamage
                        + normalElementDamage
        );


        /*
         * =================================================
         * プラス会心
         * =================================================
         */


        /*
         * 四捨五入前の
         * プラス会心物理ダメージ。
         */
        BigDecimal positiveCriticalPhysicalBeforeRound
                = physicalResult == null
                        ? BigDecimal.ZERO
                        : safeDamage(
                                physicalResult
                                        .getPositiveCriticalPhysicalDamagePerHit()
                        );


        /*
         * 四捨五入前の
         * プラス会心属性ダメージ。
         *
         * 会心撃【属性】がなければ、
         * 通常属性ダメージと同じ値になる。
         */
        BigDecimal positiveCriticalElementBeforeRound
                = elementResult == null
                        ? BigDecimal.ZERO
                        : safeDamage(
                                elementResult
                                        .getPositiveCriticalElementDamagePerHit()
                        );


        result.setPositiveCriticalPhysicalDamageBeforeRound(
                positiveCriticalPhysicalBeforeRound
        );

        result.setPositiveCriticalElementDamageBeforeRound(
                positiveCriticalElementBeforeRound
        );


        /*
         * 物理・属性を
         * 個別に四捨五入。
         */
        int positiveCriticalPhysicalDamage
                = roundDamage(
                        positiveCriticalPhysicalBeforeRound
                );


        int positiveCriticalElementDamage
                = roundDamage(
                        positiveCriticalElementBeforeRound
                );


        result.setPositiveCriticalPhysicalDamage(
                positiveCriticalPhysicalDamage
        );

        result.setPositiveCriticalElementDamage(
                positiveCriticalElementDamage
        );


        /*
         * 四捨五入後に合算。
         */
        result.setPositiveCriticalTotalDamage(
                positiveCriticalPhysicalDamage
                        + positiveCriticalElementDamage
        );


        /*
         * =================================================
         * マイナス会心
         * =================================================
         */


        /*
         * マイナス会心では
         * 物理側のみ0.75倍された値を使用する。
         */
        BigDecimal negativeCriticalPhysicalBeforeRound
                = physicalResult == null
                        ? BigDecimal.ZERO
                        : safeDamage(
                                physicalResult
                                        .getNegativeCriticalPhysicalDamagePerHit()
                        );


        result.setNegativeCriticalPhysicalDamageBeforeRound(
                negativeCriticalPhysicalBeforeRound
        );


        int negativeCriticalPhysicalDamage
                = roundDamage(
                        negativeCriticalPhysicalBeforeRound
                );


        /*
         * 属性部分はマイナス会心で減少しないため、
         * 通常属性ダメージを使用する。
         */
        int negativeCriticalElementDamage
                = normalElementDamage;


        result.setNegativeCriticalPhysicalDamage(
                negativeCriticalPhysicalDamage
        );

        result.setNegativeCriticalElementDamage(
                negativeCriticalElementDamage
        );


        result.setNegativeCriticalTotalDamage(
                negativeCriticalPhysicalDamage
                        + negativeCriticalElementDamage
        );


        return result;
    }


    /*
     * =====================================================
     * ダメージ四捨五入
     * =====================================================
     *
     * 55.49
     * → 55
     *
     * 55.50
     * → 56
     *
     * 55.5525
     * → 56
     */
    private int roundDamage(
            BigDecimal damage) {

        if (damage == null) {

            return 0;
        }


        return damage
                .setScale(
                        0,
                        RoundingMode.HALF_UP
                )
                .intValue();
    }


    /*
     * =====================================================
     * null対策
     * =====================================================
     */
    private BigDecimal safeDamage(
            BigDecimal damage) {

        return damage == null
                ? BigDecimal.ZERO
                : damage;
    }
}