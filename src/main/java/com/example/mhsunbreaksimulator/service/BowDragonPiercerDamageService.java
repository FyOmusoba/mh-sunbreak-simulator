package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowChargeModifier;
import com.example.mhsunbreaksimulator.dto.BowCoatingModifier;
import com.example.mhsunbreaksimulator.dto.BowDragonPiercerModifier;
import com.example.mhsunbreaksimulator.dto.BowPhysicalDamageResult;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;

@Service
public class BowDragonPiercerDamageService {

    /*
     * =====================================================
     * 竜の一矢 物理ダメージ
     * =====================================================
     *
     * 計算対象：
     *
     * ・武器攻撃力
     * ・条件付き攻撃力加算
     * ・条件付き攻撃力倍率
     * ・竜の一矢モーション値
     * ・弓の溜め物理倍率
     * ・ビン物理倍率
     * ・一般物理ダメージ倍率
     * ・特殊射撃強化倍率
     *
     *
     * 注意：
     *
     * 通常弾・連射矢強化
     * 貫通弾・貫通矢強化
     * 散弾・拡散矢強化
     *
     * などの通常射撃用倍率は
     * 竜の一矢には適用しない。
     */
    public BowPhysicalDamageResult calculate(
            Integer weaponAttack,
            BowDragonPiercerModifier dragonPiercerModifier,
            BowChargeModifier bowChargeModifier,
            BowCoatingModifier bowCoatingModifier,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal) {

        BowPhysicalDamageResult result
                = new BowPhysicalDamageResult();


        /*
         * =================================================
         * 必須値
         * =================================================
         */
        if (weaponAttack == null
                || dragonPiercerModifier == null
                || dragonPiercerModifier.getMotionValuePerHit() == null
                || dragonPiercerModifier.getMaxHitCount() == null
                || dragonPiercerModifier.getMaxHitCount() <= 0) {

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
         * 一般物理ダメージ倍率
         * =================================================
         */
        BigDecimal physicalDamageMul
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal
                                .getPhysicalDamageMul() == null

                                ? BigDecimal.ONE
                                : conditionalSkillModifierTotal
                                        .getPhysicalDamageMul();


        /*
         * =================================================
         * 特殊射撃強化倍率
         * =================================================
         *
         * Lv0 = 1.00
         * Lv1 = 1.10
         * Lv2 = 1.20
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
         * 溜め物理倍率
         * =================================================
         *
         * 溜め1 = 0.65
         * 溜め2 = 1.00
         * 溜め3 = 1.25
         * 溜め4 = 1.35
         */
        BigDecimal chargePhysicalMul
                = bowChargeModifier == null
                        || bowChargeModifier
                                .getPhysicalMul() == null

                                ? BigDecimal.ONE
                                : bowChargeModifier
                                        .getPhysicalMul();


        /*
         * =================================================
         * ビン物理倍率
         * =================================================
         *
         * 既存BowCoatingModifierServiceの値を使用。
         */
        BigDecimal coatingPhysicalMul
                = bowCoatingModifier == null
                        || bowCoatingModifier
                                .getPhysicalMul() == null

                                ? BigDecimal.ONE
                                : bowCoatingModifier
                                        .getPhysicalMul();


        /*
         * =================================================
         * 竜の一矢モーション値
         * =================================================
         *
         * 例：
         *
         * MV34
         *
         * ↓
         *
         * 0.34
         */
        BigDecimal motionValueRate
                = dragonPiercerModifier
                        .getMotionValuePerHit()
                        .divide(
                                new BigDecimal(
                                        "100"
                                ),
                                10,
                                RoundingMode.HALF_UP
                        );


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
         * 1hitあたり基礎物理係数
         * =================================================
         *
         * MV
         *
         * ×
         *
         * 溜め補正
         *
         * ×
         *
         * ビン補正
         */
        BigDecimal physicalRatePerHit
                = motionValueRate
                        .multiply(
                                chargePhysicalMul
                        )
                        .multiply(
                                coatingPhysicalMul
                        );


        /*
         * =================================================
         * 1hitあたり物理値
         * =================================================
         *
         * 補正後攻撃力
         *
         * ×
         *
         * 竜の一矢基礎物理係数
         *
         * ×
         *
         * 一般物理ダメージ倍率
         *
         * ×
         *
         * 特殊射撃強化倍率
         */
        BigDecimal physicalDamagePerHit
                = effectiveAttack
                        .multiply(
                                physicalRatePerHit
                        )
                        .multiply(
                                physicalDamageMul
                        )
                        .multiply(
                                specialShotMul
                        );


        /*
         * =================================================
         * 最大ヒット時物理値
         * =================================================
         */
        BigDecimal maxPhysicalDamage
                = physicalDamagePerHit
                        .multiply(
                                BigDecimal.valueOf(
                                        dragonPiercerModifier
                                                .getMaxHitCount()
                                )
                        );


        /*
         * =================================================
         * 結果
         * =================================================
         */
        result.setEffectiveAttack(
                effectiveAttack
        );

        result.setPhysicalDamagePerHit(
                physicalDamagePerHit
        );

        result.setMaxPhysicalDamage(
                maxPhysicalDamage
        );


        return result;
    }
}