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
        BigDecimal normalPhysicalBeforeRound
                = physicalResult == null
                        ? BigDecimal.ZERO
                        : safeDamage(
                                physicalResult
                                        .getNormalPhysicalDamagePerHit()
                        );


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


        result.setNormalTotalDamage(
                normalPhysicalDamage
                        + normalElementDamage
        );


        /*
         * =================================================
         * プラス会心
         * =================================================
         */
        BigDecimal positiveCriticalPhysicalBeforeRound
                = physicalResult == null
                        ? BigDecimal.ZERO
                        : safeDamage(
                                physicalResult
                                        .getPositiveCriticalPhysicalDamagePerHit()
                        );


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


        result.setPositiveCriticalTotalDamage(
                positiveCriticalPhysicalDamage
                        + positiveCriticalElementDamage
        );


        /*
         * =================================================
         * マイナス会心
         * =================================================
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
     * 固定追加ダメージ
     * =====================================================
     *
     * 鬼火纏など。
     *
     * 肉質・会心の影響を受けない固定値を
     * 最終ダメージへ加算する。
     */
    public FinalDamageResult addFixedDamage(
            FinalDamageResult result,
            BigDecimal fixedDamageAdd) {

        if (result == null) {

            return null;
        }


        BigDecimal fixedDamage
                = safeDamage(
                        fixedDamageAdd
                );


        if (fixedDamage.compareTo(
                BigDecimal.ZERO
        ) == 0) {

            return result;
        }


        int roundedFixedDamage
                = roundDamage(
                        fixedDamage
                );


        /*
         * 非会心
         */
        result.setNormalPhysicalDamageBeforeRound(
                safeDamage(
                        result.getNormalPhysicalDamageBeforeRound()
                )
                        .add(
                                fixedDamage
                        )
        );


        result.setNormalPhysicalDamage(
                safeInteger(
                        result.getNormalPhysicalDamage()
                )
                        + roundedFixedDamage
        );


        result.setNormalTotalDamage(
                safeInteger(
                        result.getNormalTotalDamage()
                )
                        + roundedFixedDamage
        );


        /*
         * プラス会心
         */
        result.setPositiveCriticalPhysicalDamageBeforeRound(
                safeDamage(
                        result.getPositiveCriticalPhysicalDamageBeforeRound()
                )
                        .add(
                                fixedDamage
                        )
        );


        result.setPositiveCriticalPhysicalDamage(
                safeInteger(
                        result.getPositiveCriticalPhysicalDamage()
                )
                        + roundedFixedDamage
        );


        result.setPositiveCriticalTotalDamage(
                safeInteger(
                        result.getPositiveCriticalTotalDamage()
                )
                        + roundedFixedDamage
        );


        /*
         * マイナス会心
         */
        result.setNegativeCriticalPhysicalDamageBeforeRound(
                safeDamage(
                        result.getNegativeCriticalPhysicalDamageBeforeRound()
                )
                        .add(
                                fixedDamage
                        )
        );


        result.setNegativeCriticalPhysicalDamage(
                safeInteger(
                        result.getNegativeCriticalPhysicalDamage()
                )
                        + roundedFixedDamage
        );


        result.setNegativeCriticalTotalDamage(
                safeInteger(
                        result.getNegativeCriticalTotalDamage()
                )
                        + roundedFixedDamage
        );


        return result;
    }


    /*
     * =====================================================
     * 攻撃力参照固定追加ダメージ
     * =====================================================
     *
     * 粉塵纏など。
     *
     * 補正後攻撃力
     * ×
     * 追加ダメージ倍率
     *
     * で固定追加ダメージを計算する。
     *
     *
     * 例：
     *
     * 攻撃力350
     *
     * 赤粉塵
     * 350 × 0.30
     * = 105
     *
     * 青粉塵
     * 350 × 1.25
     * = 437.5
     * → 438
     *
     *
     * 計算した値は
     * 肉質・会心の影響を受けない。
     */
    public FinalDamageResult addAttackScaledFixedDamage(
            FinalDamageResult result,
            BigDecimal effectiveAttack,
            BigDecimal attackMultiplier) {

        if (result == null) {

            return null;
        }


        /*
         * 状態未発動時は
         * multiplierをnullで渡す。
         */
        if (effectiveAttack == null
                || attackMultiplier == null) {

            return result;
        }


        if (effectiveAttack.compareTo(
                BigDecimal.ZERO
        ) <= 0
                || attackMultiplier.compareTo(
                        BigDecimal.ZERO
                ) <= 0) {

            return result;
        }


        BigDecimal additionalDamage
                = effectiveAttack
                        .multiply(
                                attackMultiplier
                        );


        return addFixedDamage(
                result,
                additionalDamage
        );
    }


    /*
     * =====================================================
     * ダメージ四捨五入
     * =====================================================
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
     * BigDecimal null対策
     * =====================================================
     */
    private BigDecimal safeDamage(
            BigDecimal damage) {

        return damage == null
                ? BigDecimal.ZERO
                : damage;
    }


    /*
     * =====================================================
     * Integer null対策
     * =====================================================
     */
    private int safeInteger(
            Integer value) {

        return value == null
                ? 0
                : value;
    }
}