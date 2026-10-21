package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowgunWyvernblastDamageResult;
import com.example.mhsunbreaksimulator.dto.BowgunWyvernblastModifier;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;

@Service
public class BowgunWyvernblastDamageService {

    /*
     * =====================================================
     * ライトボウガン
     * 起爆竜弾
     * =====================================================
     *
     * 固定ダメージ：
     *
     * （武器攻撃力 + attackAdd）
     * × attackMul
     * × fixedDamageMotionValue
     * ÷ 100
     * × specialShotMul
     *
     *
     * 火属性部分：
     *
     * modifier.fixedFireValue
     *
     *
     * 注意：
     *
     * ・起爆竜弾の固定ダメージは物理肉質を無視する
     *
     * ・特殊射撃強化は固定ダメージ部分だけに適用する
     *
     * ・火属性部分には特殊射撃強化を適用しない
     *
     * ・通常弾・貫通弾・散弾などの
     *   shotMul は起爆竜弾には適用しない
     */
    public BowgunWyvernblastDamageResult calculate(
            Integer weaponAttack,
            BowgunWyvernblastModifier wyvernblastModifier,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal) {

        BowgunWyvernblastDamageResult result
                = new BowgunWyvernblastDamageResult();


        /*
         * =================================================
         * 必須値
         * =================================================
         */
        if (weaponAttack == null
                || wyvernblastModifier == null
                || wyvernblastModifier
                        .getFixedDamageMotionValue() == null
                || wyvernblastModifier
                        .getFixedFireValue() == null
                || wyvernblastModifier
                        .getDurabilityCost() == null) {

            return result;
        }


        /*
         * =================================================
         * 武器攻撃力
         * =================================================
         */
        BigDecimal baseAttack
                = BigDecimal.valueOf(
                        weaponAttack
                );


        /*
         * =================================================
         * 条件付き攻撃力加算
         * =================================================
         */
        BigDecimal attackAdd
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal
                                .getAttackAdd() == null

                                ? BigDecimal.ZERO
                                : conditionalSkillModifierTotal
                                        .getAttackAdd();


        /*
         * =================================================
         * 条件付き攻撃力倍率
         * =================================================
         */
        BigDecimal attackMul
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal
                                .getAttackMul() == null

                                ? BigDecimal.ONE
                                : conditionalSkillModifierTotal
                                        .getAttackMul();


        /*
         * =================================================
         * 特殊射撃強化倍率
         * =================================================
         *
         * Lv0 = 1.00
         * Lv1 = 1.10
         * Lv2 = 1.20
         *
         * 起爆竜弾では
         * 固定ダメージ部分だけに適用する。
         */
        BigDecimal specialShotMul
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal
                                .getSpecialShotMul() == null

                                ? BigDecimal.ONE
                                : conditionalSkillModifierTotal
                                        .getSpecialShotMul();


        /*
         * =================================================
         * 条件付き補正後攻撃力
         * =================================================
         */
        BigDecimal effectiveAttack
                = baseAttack
                        .add(
                                attackAdd
                        )
                        .multiply(
                                attackMul
                        );


        /*
         * =================================================
         * 固定ダメージ
         * =================================================
         *
         * effectiveAttack
         *
         * ×
         *
         * fixedDamageMotionValue
         *
         * ÷100
         *
         * ×
         *
         * specialShotMul
         */
        BigDecimal fixedDamage
                = effectiveAttack
                        .multiply(
                                wyvernblastModifier
                                        .getFixedDamageMotionValue()
                        )
                        .movePointLeft(
                                2
                        )
                        .multiply(
                                specialShotMul
                        );


        /*
         * =================================================
         * 火属性部分
         * =================================================
         *
         * 特殊射撃強化は掛けない。
         *
         * 属性肉質の適用は
         * Web計算側で行う。
         */
        BigDecimal fixedFireValue
                = wyvernblastModifier
                        .getFixedFireValue();


        /*
         * =================================================
         * 耐久消費
         * =================================================
         */
        Integer durabilityCost
                = wyvernblastModifier
                        .getDurabilityCost();


        /*
         * =================================================
         * 結果
         * =================================================
         */
        result.setEffectiveAttack(
                effectiveAttack
        );


        result.setFixedDamage(
                fixedDamage
        );


        result.setFixedFireValue(
                fixedFireValue
        );


        result.setDurabilityCost(
                durabilityCost
        );


        return result;
    }
}