package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.CriticalModifierResult;
import com.example.mhsunbreaksimulator.dto.PhysicalHitzoneDamageResult;

@Service
public class PhysicalHitzoneDamageService {

    /*
     * =====================================================
     * 物理肉質＋会心ダメージ計算
     * =====================================================
     *
     * physicalDamageBeforeHitzone
     *     肉質を掛ける前の物理ダメージ
     *
     * physicalHitzone
     *     使用する肉質値
     *
     *     例：
     *     斬70
     *     打72
     *     弾45
     *
     * criticalModifier
     *     通常会心
     *     超会心
     *     マイナス会心
     *
     *     の倍率情報
     */
    public PhysicalHitzoneDamageResult calculate(
            BigDecimal physicalDamageBeforeHitzone,
            Integer physicalHitzone,
            CriticalModifierResult criticalModifier) {

        PhysicalHitzoneDamageResult result
                = new PhysicalHitzoneDamageResult();


        /*
         * =================================================
         * 肉質適用前ダメージ
         * =================================================
         */
        BigDecimal beforeHitzone
                = safeDamage(
                        physicalDamageBeforeHitzone
                );


        result.setPhysicalDamageBeforeHitzone(
                beforeHitzone
        );


        /*
         * =================================================
         * 物理肉質
         * =================================================
         */
        int hitzone
                = safeHitzone(
                        physicalHitzone
                );


        result.setPhysicalHitzone(
                hitzone
        );


        /*
         * 45
         * ↓
         * 0.45
         *
         * 70
         * ↓
         * 0.70
         */
        BigDecimal hitzoneRate
                = BigDecimal.valueOf(
                        hitzone
                ).movePointLeft(
                        2
                );


        result.setPhysicalHitzoneRate(
                hitzoneRate
        );


        /*
         * =================================================
         * 非会心ダメージ
         * =================================================
         *
         * 肉質適用前物理ダメージ
         * ×
         * 物理肉質
         *
         * この段階では丸めない。
         */
        BigDecimal normalPhysicalDamage
                = beforeHitzone.multiply(
                        hitzoneRate
                );


        result.setNormalPhysicalDamagePerHit(
                normalPhysicalDamage
        );


        /*
         * =================================================
         * 会心倍率
         * =================================================
         */


        /*
         * プラス会心。
         *
         * CriticalModifierResultがない場合は
         * 通常会心の1.25を使用する。
         */
        BigDecimal positiveCriticalMul
                = criticalModifier == null
                        ? new BigDecimal("1.25")
                        : safePositiveCriticalMultiplier(
                                criticalModifier
                                        .getPositiveCriticalDamageMul()
                        );


        /*
         * マイナス会心。
         *
         * Resultがない場合は
         * 0.75を使用する。
         */
        BigDecimal negativeCriticalMul
                = criticalModifier == null
                        ? new BigDecimal("0.75")
                        : safeNegativeCriticalMultiplier(
                                criticalModifier
                                        .getNegativeCriticalDamageMul()
                        );


        result.setPositiveCriticalDamageMul(
                positiveCriticalMul
        );

        result.setNegativeCriticalDamageMul(
                negativeCriticalMul
        );


        /*
         * =================================================
         * プラス会心ダメージ
         * =================================================
         *
         * 非会心物理ダメージ
         * ×
         * 会心倍率
         *
         * 通常
         * → 1.25
         *
         * 超会心Lv1
         * → 1.30
         *
         * 超会心Lv2
         * → 1.35
         *
         * 超会心Lv3
         * → 1.40
         */
        BigDecimal positiveCriticalDamage
                = normalPhysicalDamage.multiply(
                        positiveCriticalMul
                );


        result.setPositiveCriticalPhysicalDamagePerHit(
                positiveCriticalDamage
        );


        /*
         * =================================================
         * マイナス会心ダメージ
         * =================================================
         *
         * 非会心物理ダメージ
         * ×
         * 0.75
         */
        BigDecimal negativeCriticalDamage
                = normalPhysicalDamage.multiply(
                        negativeCriticalMul
                );


        result.setNegativeCriticalPhysicalDamagePerHit(
                negativeCriticalDamage
        );


        return result;
    }


    /*
     * =====================================================
     * ダメージnull対策
     * =====================================================
     */
    private BigDecimal safeDamage(
            BigDecimal value) {

        if (value == null) {

            return BigDecimal.ZERO;
        }


        return value;
    }


    /*
     * =====================================================
     * 肉質null対策
     * =====================================================
     */
    private int safeHitzone(
            Integer value) {

        if (value == null) {

            return 0;
        }


        return value;
    }


    /*
     * =====================================================
     * プラス会心倍率null対策
     * =====================================================
     */
    private BigDecimal safePositiveCriticalMultiplier(
            BigDecimal value) {

        if (value == null) {

            return new BigDecimal("1.25");
        }


        return value;
    }


    /*
     * =====================================================
     * マイナス会心倍率null対策
     * =====================================================
     */
    private BigDecimal safeNegativeCriticalMultiplier(
            BigDecimal value) {

        if (value == null) {

            return new BigDecimal("0.75");
        }


        return value;
    }
}