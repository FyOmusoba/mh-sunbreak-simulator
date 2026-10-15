package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.dto.CriticalModifierResult;
import com.example.mhsunbreaksimulator.dto.WeakpointSkillModifier;

@Service
public class CriticalModifierService {

    /*
     * =====================================================
     * 会心率の上下限
     * =====================================================
     */

    private static final BigDecimal MIN_AFFINITY
            = new BigDecimal("-100");

    private static final BigDecimal MAX_AFFINITY
            = new BigDecimal("100");


    /*
     * =====================================================
     * 通常会心倍率
     * =====================================================
     *
     * プラス会心
     * → 1.25倍
     *
     * マイナス会心
     * → 0.75倍
     */
    private static final BigDecimal DEFAULT_POSITIVE_CRITICAL_MUL
            = new BigDecimal("1.25");

    private static final BigDecimal NEGATIVE_CRITICAL_MUL
            = new BigDecimal("0.75");


    /*
     * =====================================================
     * 会心補正計算
     * =====================================================
     */
    public CriticalModifierResult calculate(
            Integer weaponAffinity,
            ConditionalSkillModifierTotal conditionalModifier,
            WeakpointSkillModifier weakpointModifier) {

        CriticalModifierResult result
                = new CriticalModifierResult();


        /*
         * =================================================
         * 武器自身の会心率
         * =================================================
         */
        BigDecimal weaponAffinityValue
                = weaponAffinity == null
                        ? BigDecimal.ZERO
                        : BigDecimal.valueOf(
                                weaponAffinity
                        );


        result.setWeaponAffinity(
                weaponAffinityValue
        );


        /*
         * =================================================
         * 通常スキルの会心率
         * =================================================
         *
         * 見切り
         * 挑戦者
         * 力の解放
         * 渾身
         * 攻勢
         * 狂竜症【蝕】
         * など。
         *
         * 弱点特効は
         * ConditionalSkillModifierService側から
         * 除外済み。
         */
        BigDecimal conditionalAffinityAdd
                = conditionalModifier == null
                        ? BigDecimal.ZERO
                        : safeAdd(
                                conditionalModifier
                                        .getAffinityAdd()
                        );


        result.setConditionalAffinityAdd(
                conditionalAffinityAdd
        );


        /*
         * =================================================
         * 弱点特効
         * =================================================
         *
         * 肉質条件を満たしている場合だけ、
         * WeakpointSkillModifierServiceによって
         * 15 / 30 / 50 が入っている。
         */
        BigDecimal weakpointAffinityAdd
                = weakpointModifier == null
                        ? BigDecimal.ZERO
                        : safeAdd(
                                weakpointModifier
                                        .getAffinityAdd()
                        );


        result.setWeakpointAffinityAdd(
                weakpointAffinityAdd
        );


        /*
         * =================================================
         * 上下限補正前の会心率
         * =================================================
         */
        BigDecimal rawAffinity
                = weaponAffinityValue
                        .add(
                                conditionalAffinityAdd
                        )
                        .add(
                                weakpointAffinityAdd
                        );


        result.setRawAffinity(
                rawAffinity
        );


        /*
         * =================================================
         * 最終会心率
         * =================================================
         *
         * -100 ～ 100 に制限。
         */
        BigDecimal finalAffinity
                = clampAffinity(
                        rawAffinity
                );


        result.setFinalAffinity(
                finalAffinity
        );


        /*
         * =================================================
         * プラス会心 / マイナス会心
         * =================================================
         */

        if (finalAffinity.compareTo(
                BigDecimal.ZERO
        ) > 0) {

            /*
             * 正の会心率。
             */
            result.setPositiveCriticalRate(
                    finalAffinity
            );

            result.setNegativeCriticalRate(
                    BigDecimal.ZERO
            );

        } else if (finalAffinity.compareTo(
                BigDecimal.ZERO
        ) < 0) {

            /*
             * 負の会心率。
             *
             * Resultでは発生率として
             * 絶対値を保持する。
             *
             * -30
             * ↓
             * negativeCriticalRate = 30
             */
            result.setPositiveCriticalRate(
                    BigDecimal.ZERO
            );

            result.setNegativeCriticalRate(
                    finalAffinity.abs()
            );

        } else {

            /*
             * 会心率0。
             */
            result.setPositiveCriticalRate(
                    BigDecimal.ZERO
            );

            result.setNegativeCriticalRate(
                    BigDecimal.ZERO
            );
        }


        /*
         * =================================================
         * プラス会心の物理倍率
         * =================================================
         *
         * 超会心なし
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
        BigDecimal positiveCriticalDamageMul
                = DEFAULT_POSITIVE_CRITICAL_MUL;


        if (conditionalModifier != null) {

            BigDecimal skillCriticalMul
                    = conditionalModifier
                            .getCritDamageMul();


            /*
             * ConditionalSkillModifierTotalの
             * 初期値は1.0。
             *
             * 1.0より大きい値がある場合だけ
             * 超会心などの倍率として採用する。
             */
            if (skillCriticalMul != null
                    && skillCriticalMul.compareTo(
                            BigDecimal.ONE
                    ) > 0) {

                positiveCriticalDamageMul
                        = skillCriticalMul;
            }
        }


        result.setPositiveCriticalDamageMul(
                positiveCriticalDamageMul
        );


        /*
         * =================================================
         * マイナス会心倍率
         * =================================================
         */
        result.setNegativeCriticalDamageMul(
                NEGATIVE_CRITICAL_MUL
        );


        /*
         * =================================================
         * 属性会心倍率
         * =================================================
         *
         * 通常会心だけでは
         * 属性ダメージ倍率は1.0のまま。
         *
         * 会心撃【属性】がある場合のみ
         *
         * Lv1 → 1.05
         * Lv2 → 1.10
         * Lv3 → 1.15
         *
         * を使用する。
         */
        BigDecimal positiveElementCriticalDamageMul
                = BigDecimal.ONE;


        if (conditionalModifier != null) {

            BigDecimal skillElementCriticalMul
                    = conditionalModifier
                            .getElementCritDamageMul();


            if (skillElementCriticalMul != null
                    && skillElementCriticalMul.compareTo(
                            BigDecimal.ONE
                    ) > 0) {

                positiveElementCriticalDamageMul
                        = skillElementCriticalMul;
            }
        }


        result.setNormalElementCriticalDamageMul(
                BigDecimal.ONE
        );

        result.setPositiveElementCriticalDamageMul(
                positiveElementCriticalDamageMul
        );


        return result;
    }


    /*
     * =====================================================
     * 会心率を -100 ～ 100 に制限
     * =====================================================
     */
    private BigDecimal clampAffinity(
            BigDecimal affinity) {

        if (affinity == null) {

            return BigDecimal.ZERO;
        }


        if (affinity.compareTo(
                MAX_AFFINITY
        ) > 0) {

            return MAX_AFFINITY;
        }


        if (affinity.compareTo(
                MIN_AFFINITY
        ) < 0) {

            return MIN_AFFINITY;
        }


        return affinity;
    }


    /*
     * =====================================================
     * 加算値のnull対策
     * =====================================================
     */
    private BigDecimal safeAdd(
            BigDecimal value) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }
}