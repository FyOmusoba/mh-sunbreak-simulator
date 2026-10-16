package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowPhysicalDamageResult;
import com.example.mhsunbreaksimulator.dto.BowPhysicalModifier;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.dto.ShotSkillModifier;

@Service
public class BowPhysicalDamageService {

    /*
     * =====================================================
     * 既存3引数版
     * =====================================================
     *
     * 既存Controllerとの互換性用。
     */
    public BowPhysicalDamageResult calculate(
            Integer weaponAttack,
            BowPhysicalModifier bowPhysicalModifier,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal) {

        return calculate(
                weaponAttack,
                bowPhysicalModifier,
                conditionalSkillModifierTotal,
                null
        );
    }


    /*
     * =====================================================
     * 新4引数版
     * =====================================================
     *
     * ShotSkillModifierによって、
     *
     * 連射矢
     * 貫通矢
     * 拡散矢
     *
     * に対応する強化スキルだけを
     * 物理ダメージへ反映する。
     */
    public BowPhysicalDamageResult calculate(
            Integer weaponAttack,
            BowPhysicalModifier bowPhysicalModifier,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal,
            ShotSkillModifier shotSkillModifier) {

        BowPhysicalDamageResult result
                = new BowPhysicalDamageResult();


        /*
         * =================================================
         * 必須値
         * =================================================
         */
        if (weaponAttack == null
                || bowPhysicalModifier == null) {

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
         * 矢種強化倍率
         * =================================================
         *
         * 連射矢
         * → 通常弾・連射矢強化
         *
         * 貫通矢
         * → 貫通弾・貫通矢強化
         *
         * 拡散矢
         * → 散弾・拡散矢強化
         *
         * 対象外なら1.00。
         */
        BigDecimal shotPhysicalMultiplier
                = shotSkillModifier == null
                        || shotSkillModifier
                                .getPhysicalMultiplier() == null

                                ? BigDecimal.ONE
                                : shotSkillModifier
                                        .getPhysicalMultiplier();


        /*
         * =================================================
         * 条件付き補正反映後攻撃力
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
         * 1hitあたり物理値
         * =================================================
         *
         * 補正後攻撃力
         *
         * ×
         *
         * 弓固有物理係数
         *
         * ×
         *
         * 一般物理ダメージ倍率
         *
         * ×
         *
         * 矢種強化倍率
         */
        BigDecimal physicalDamagePerHit
                = effectiveAttack
                        .multiply(
                                bowPhysicalModifier
                                        .getPhysicalRatePerHit()
                        )
                        .multiply(
                                physicalDamageMul
                        )
                        .multiply(
                                shotPhysicalMultiplier
                        );


        /*
         * =================================================
         * 最大ヒット時物理値
         * =================================================
         */
        BigDecimal maxPhysicalDamage
                = effectiveAttack
                        .multiply(
                                bowPhysicalModifier
                                        .getMaxPhysicalRate()
                        )
                        .multiply(
                                physicalDamageMul
                        )
                        .multiply(
                                shotPhysicalMultiplier
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