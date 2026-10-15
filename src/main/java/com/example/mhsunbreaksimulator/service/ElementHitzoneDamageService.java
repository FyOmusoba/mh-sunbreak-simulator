package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.CriticalModifierResult;
import com.example.mhsunbreaksimulator.dto.ElementHitzoneDamageResult;

@Service
public class ElementHitzoneDamageService {

    /*
     * =====================================================
     * 属性肉質＋属性会心ダメージ計算
     * =====================================================
     *
     * elementDamageBeforeHitzone
     *     属性肉質を掛ける前の属性ダメージ
     *
     * elementHitzone
     *     使用する属性肉質
     *
     * weakpointElementDamageMul
     *     弱点特効【属性】の倍率
     *
     *     非発動
     *     → 1.0
     *
     *     Lv1
     *     → 1.10
     *
     *     Lv2
     *     → 1.125
     *
     *     Lv3
     *     → 1.15
     *
     * criticalModifier
     *     会心撃【属性】の倍率情報
     */
    public ElementHitzoneDamageResult calculate(
            BigDecimal elementDamageBeforeHitzone,
            Integer elementHitzone,
            BigDecimal weakpointElementDamageMul,
            CriticalModifierResult criticalModifier) {

        ElementHitzoneDamageResult result
                = new ElementHitzoneDamageResult();


        /*
         * =================================================
         * 肉質適用前属性ダメージ
         * =================================================
         */
        BigDecimal beforeHitzone
                = safeDamage(
                        elementDamageBeforeHitzone
                );


        result.setElementDamageBeforeHitzone(
                beforeHitzone
        );


        /*
         * =================================================
         * 属性肉質
         * =================================================
         */
        int hitzone
                = safeHitzone(
                        elementHitzone
                );


        result.setElementHitzone(
                hitzone
        );


        /*
         * 20
         * ↓
         * 0.20
         *
         * 30
         * ↓
         * 0.30
         */
        BigDecimal hitzoneRate
                = BigDecimal.valueOf(
                        hitzone
                ).movePointLeft(
                        2
                );


        result.setElementHitzoneRate(
                hitzoneRate
        );


        /*
         * =================================================
         * 弱点特効【属性】
         * =================================================
         *
         * 主属性と副属性で
         * 別々の倍率を渡す。
         *
         * nullの場合は1.0。
         */
        BigDecimal weakpointMul
                = safeMultiplier(
                        weakpointElementDamageMul
                );


        result.setWeakpointElementDamageMul(
                weakpointMul
        );


        /*
         * =================================================
         * 通常属性ダメージ
         * =================================================
         *
         * 肉質適用前属性ダメージ
         * ×
         * 属性肉質
         * ×
         * 弱点特効【属性】
         *
         * この段階では整数へ丸めない。
         */
        BigDecimal normalElementDamage
                = beforeHitzone
                        .multiply(
                                hitzoneRate
                        )
                        .multiply(
                                weakpointMul
                        );


        result.setNormalElementDamagePerHit(
                normalElementDamage
        );


        /*
         * =================================================
         * 会心撃【属性】
         * =================================================
         *
         * 会心撃【属性】なし
         * → 1.0
         *
         * Lv1
         * → 1.05
         *
         * Lv2
         * → 1.10
         *
         * Lv3
         * → 1.15
         *
         * CriticalModifierResultがnullの場合も
         * 1.0として扱う。
         */
        BigDecimal positiveElementCriticalMul
                = criticalModifier == null
                        ? BigDecimal.ONE
                        : safeMultiplier(
                                criticalModifier
                                        .getPositiveElementCriticalDamageMul()
                        );


        result.setPositiveElementCriticalDamageMul(
                positiveElementCriticalMul
        );


        /*
         * =================================================
         * プラス会心時の属性ダメージ
         * =================================================
         *
         * 通常属性ダメージ
         * ×
         * 会心撃【属性】倍率
         *
         * 会心撃【属性】がなければ
         * ×1.0なので通常時と同じ。
         */
        BigDecimal positiveCriticalElementDamage
                = normalElementDamage.multiply(
                        positiveElementCriticalMul
                );


        result.setPositiveCriticalElementDamagePerHit(
                positiveCriticalElementDamage
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
     * 属性肉質null対策
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
     * 倍率null対策
     * =====================================================
     */
    private BigDecimal safeMultiplier(
            BigDecimal value) {

        if (value == null) {

            return BigDecimal.ONE;
        }


        return value;
    }
}