package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowgunAmmoModifier;
import com.example.mhsunbreaksimulator.dto.BowgunElementDamageResult;
import com.example.mhsunbreaksimulator.dto.BowgunRapidFireModifier;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.dto.ShotSkillModifier;

@Service
public class BowgunElementDamageService {

    /*
     * =====================================================
     * 既存3引数版
     * =====================================================
     */
    public BowgunElementDamageResult calculate(
            Integer weaponAttack,
            BowgunAmmoModifier bowgunAmmoModifier,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal) {

        return calculate(
                weaponAttack,
                bowgunAmmoModifier,
                conditionalSkillModifierTotal,
                null,
                null
        );
    }


    /*
     * =====================================================
     * 既存4引数版
     * =====================================================
     */
    public BowgunElementDamageResult calculate(
            Integer weaponAttack,
            BowgunAmmoModifier bowgunAmmoModifier,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal,
            BowgunRapidFireModifier rapidFireModifier) {

        return calculate(
                weaponAttack,
                bowgunAmmoModifier,
                conditionalSkillModifierTotal,
                rapidFireModifier,
                null
        );
    }


    /*
     * =====================================================
     * 新5引数版
     * =====================================================
     *
     * ShotSkillModifierによって、
     * 速射強化などの属性側倍率を適用する。
     */
    public BowgunElementDamageResult calculate(
            Integer weaponAttack,
            BowgunAmmoModifier bowgunAmmoModifier,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal,
            BowgunRapidFireModifier rapidFireModifier,
            ShotSkillModifier shotSkillModifier) {

        BowgunElementDamageResult result
                = new BowgunElementDamageResult();


        /*
         * =================================================
         * 必須値確認
         * =================================================
         */
        if (weaponAttack == null
                || bowgunAmmoModifier == null
                || bowgunAmmoModifier
                        .getElementMotionValuePerHit() == null
                || bowgunAmmoModifier
                        .getElementMotionValuePerHit()
                        .compareTo(
                                BigDecimal.ZERO
                        ) <= 0
                || bowgunAmmoModifier
                        .getMaxHitCount() == null
                || bowgunAmmoModifier
                        .getMaxHitCount() <= 0) {

            return result;
        }


        /*
         * =================================================
         * 基礎攻撃力
         * =================================================
         */
        BigDecimal baseAttack
                = BigDecimal.valueOf(
                        weaponAttack
                );


        /*
         * =================================================
         * 攻撃力加算
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
         * 攻撃力倍率
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
         * 属性値加算
         * =================================================
         */
        BigDecimal elementAdd
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal
                                .getElementAdd() == null

                                ? BigDecimal.ZERO
                                : conditionalSkillModifierTotal
                                        .getElementAdd();


        /*
         * =================================================
         * 属性値倍率
         * =================================================
         */
        BigDecimal elementMul
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal
                                .getElementMul() == null

                                ? BigDecimal.ONE
                                : conditionalSkillModifierTotal
                                        .getElementMul();


        /*
         * =================================================
         * 一般属性ダメージ倍率
         * =================================================
         */
        BigDecimal elementDamageMul
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal
                                .getElementDamageMul() == null

                                ? BigDecimal.ONE
                                : conditionalSkillModifierTotal
                                        .getElementDamageMul();


        /*
         * =================================================
         * 射撃スキル
         * 属性倍率
         * =================================================
         *
         * 現段階では主に速射強化用。
         *
         * 非対象
         * → 1.00
         */
        BigDecimal shotElementMultiplier
                = shotSkillModifier == null
                        || shotSkillModifier
                                .getElementMultiplier() == null

                                ? BigDecimal.ONE
                                : shotSkillModifier
                                        .getElementMultiplier();


        /*
         * =================================================
         * 速射1発ごとの属性補正
         * =================================================
         *
         * 非速射
         * → 1.00
         *
         * 速射
         * → 0.50
         */
        BigDecimal rapidFireElementMul
                = rapidFireModifier == null
                        || rapidFireModifier
                                .getElementMulPerShot() == null

                                ? BigDecimal.ONE
                                : rapidFireModifier
                                        .getElementMulPerShot();


        /*
         * =================================================
         * 1回の射撃操作で発射される弾数
         * =================================================
         */
        int shotCount
                = rapidFireModifier == null
                        || rapidFireModifier
                                .getShotCount() == null
                        || rapidFireModifier
                                .getShotCount() <= 0

                                ? 1
                                : rapidFireModifier
                                        .getShotCount();


        /*
         * =================================================
         * 補正後攻撃力
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
         * 属性弾の補正後属性値
         * =================================================
         *
         * 元属性MV
         * × 属性倍率
         * ＋ 属性加算
         */
        BigDecimal effectiveElementValue
                = bowgunAmmoModifier
                        .getElementMotionValuePerHit()
                        .multiply(
                                elementMul
                        )
                        .add(
                                elementAdd
                        );


        /*
         * =================================================
         * 肉質適用前
         * 1hit属性値
         * =================================================
         *
         * 補正後攻撃力
         *
         * ×
         *
         * 補正後属性値
         *
         * ÷100
         *
         * ×
         *
         * 一般属性ダメージ倍率
         *
         * ×
         *
         * 速射1発補正
         *
         * ×
         *
         * 射撃スキル属性倍率
         */
        BigDecimal elementDamagePerHit
                = effectiveAttack
                        .multiply(
                                effectiveElementValue
                        )
                        .movePointLeft(
                                2
                        )
                        .multiply(
                                elementDamageMul
                        )
                        .multiply(
                                rapidFireElementMul
                        )
                        .multiply(
                                shotElementMultiplier
                        );


        /*
         * =================================================
         * 1発最大属性値
         * =================================================
         */
        BigDecimal maxElementDamage
                = elementDamagePerHit
                        .multiply(
                                BigDecimal.valueOf(
                                        bowgunAmmoModifier
                                                .getMaxHitCount()
                                )
                        );


        /*
         * =================================================
         * 弾1個消費あたり最大属性値
         * =================================================
         */
        BigDecimal maxElementDamagePerAmmo
                = maxElementDamage
                        .multiply(
                                BigDecimal.valueOf(
                                        shotCount
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


        result.setElementDamagePerHit(
                elementDamagePerHit
        );


        result.setMaxElementDamage(
                maxElementDamage
        );


        result.setShotCount(
                shotCount
        );


        result.setMaxElementDamagePerAmmo(
                maxElementDamagePerAmmo
        );


        return result;
    }
}