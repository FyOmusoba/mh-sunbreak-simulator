package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowgunAmmoModifier;
import com.example.mhsunbreaksimulator.dto.BowgunSpecialDamageResult;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;

@Service
public class BowgunSpecialDamageService {

    public BowgunSpecialDamageResult calculate(
            Integer weaponAttack,
            BowgunAmmoModifier bowgunAmmoModifier,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal) {

        BowgunSpecialDamageResult result
                = new BowgunSpecialDamageResult();


        if (weaponAttack == null
                || bowgunAmmoModifier == null) {

            return result;
        }


        /*
         * 武器の素の攻撃力
         */
        BigDecimal baseAttack
                = BigDecimal.valueOf(
                        weaponAttack
                );


        /*
         * 条件付きスキルによる
         * 攻撃力加算
         */
        BigDecimal attackAdd
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal.getAttackAdd() == null
                                ? BigDecimal.ZERO
                                : conditionalSkillModifierTotal.getAttackAdd();


        /*
         * 条件付きスキルによる
         * 攻撃力倍率
         */
        BigDecimal attackMul
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal.getAttackMul() == null
                                ? BigDecimal.ONE
                                : conditionalSkillModifierTotal.getAttackMul();


        /*
         * 条件付き補正後攻撃力
         *
         * 現段階では既存の
         * BowgunPhysicalDamageService
         * と同じ順序。
         *
         * （基礎攻撃力 + 加算）
         * × 倍率
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
         * -----------------------------
         * 着弾部分
         * -----------------------------
         *
         * 斬裂弾
         * 徹甲榴弾
         * 拡散弾
         * などの着弾MV。
         *
         * ここではまだ肉質を掛けない。
         */
        BigDecimal impactPhysicalMotionValue
                = valueOrZero(
                        bowgunAmmoModifier
                                .getImpactPhysicalMotionValue()
                );


        /*
         * -----------------------------
         * 二次攻撃：
         * 通常の物理MV部分
         * -----------------------------
         *
         * 斬裂弾の炸裂MVなど。
         *
         * これもまだ肉質を掛けない。
         */
        BigDecimal secondaryPhysicalMotionValuePerHit
                = valueOrZero(
                        bowgunAmmoModifier
                                .getSecondaryPhysicalMotionValuePerHit()
                );


        int secondaryHitCount
                = positiveOrZero(
                        bowgunAmmoModifier
                                .getSecondaryHitCount()
                );


        /*
         * -----------------------------
         * 二次攻撃：
         * 固定ダメージ部分
         * -----------------------------
         *
         * 徹甲榴弾
         * 拡散弾
         * 竜撃弾1段目
         *
         * など。
         *
         * 攻撃力 × 固定MV ÷ 100
         */
        BigDecimal secondaryFixedDamageMotionValue
                = valueOrZero(
                        bowgunAmmoModifier
                                .getSecondaryFixedDamageMotionValuePerHit()
                );


        BigDecimal secondaryFixedDamagePerHit
                = effectiveAttack
                        .multiply(
                                secondaryFixedDamageMotionValue
                        )
                        .movePointLeft(
                                2
                        );


        BigDecimal secondaryFixedDamageTotal
                = secondaryFixedDamagePerHit
                        .multiply(
                                BigDecimal.valueOf(
                                        secondaryHitCount
                                )
                        );


        /*
         * 二次攻撃部分の
         * 攻撃力非依存属性値。
         *
         * 例：
         *
         * 徹甲榴弾Lv1
         * 火3
         *
         * 拡散弾Lv3
         * 火10
         *
         * 竜撃弾1段目
         * 火40
         *
         * ここではまだ属性肉質を掛けない。
         */
        BigDecimal secondaryFixedElementValuePerHit
                = valueOrZero(
                        bowgunAmmoModifier
                                .getSecondaryFixedElementValuePerHit()
                );


        BigDecimal secondaryFixedElementValueTotal
                = secondaryFixedElementValuePerHit
                        .multiply(
                                BigDecimal.valueOf(
                                        secondaryHitCount
                                )
                        );


        /*
         * -----------------------------
         * 値の異なる追加攻撃部分
         * -----------------------------
         *
         * 現在は主に
         * 竜撃弾2段目用。
         */
        BigDecimal additionalFixedDamageMotionValue
                = valueOrZero(
                        bowgunAmmoModifier
                                .getAdditionalFixedDamageMotionValuePerHit()
                );


        int additionalHitCount
                = positiveOrZero(
                        bowgunAmmoModifier
                                .getAdditionalFixedHitCount()
                );


        BigDecimal additionalFixedDamagePerHit
                = effectiveAttack
                        .multiply(
                                additionalFixedDamageMotionValue
                        )
                        .movePointLeft(
                                2
                        );


        BigDecimal additionalFixedDamageTotal
                = additionalFixedDamagePerHit
                        .multiply(
                                BigDecimal.valueOf(
                                        additionalHitCount
                                )
                        );


        BigDecimal additionalFixedElementValuePerHit
                = valueOrZero(
                        bowgunAmmoModifier
                                .getAdditionalFixedElementValuePerHit()
                );


        BigDecimal additionalFixedElementValueTotal
                = additionalFixedElementValuePerHit
                        .multiply(
                                BigDecimal.valueOf(
                                        additionalHitCount
                                )
                        );


        /*
         * -----------------------------
         * Resultへ格納
         * -----------------------------
         */

        result.setEffectiveAttack(
                effectiveAttack
        );


        result.setImpactPhysicalMotionValue(
                impactPhysicalMotionValue
        );


        result.setSecondaryPhysicalMotionValuePerHit(
                secondaryPhysicalMotionValuePerHit
        );


        result.setSecondaryHitCount(
                secondaryHitCount
        );


        result.setSecondaryFixedDamagePerHit(
                secondaryFixedDamagePerHit
        );


        result.setSecondaryFixedDamageTotal(
                secondaryFixedDamageTotal
        );


        result.setSecondaryFixedElementValuePerHit(
                secondaryFixedElementValuePerHit
        );


        result.setSecondaryFixedElementValueTotal(
                secondaryFixedElementValueTotal
        );


        result.setAdditionalFixedDamagePerHit(
                additionalFixedDamagePerHit
        );


        result.setAdditionalHitCount(
                additionalHitCount
        );


        result.setAdditionalFixedDamageTotal(
                additionalFixedDamageTotal
        );


        result.setAdditionalFixedElementValuePerHit(
                additionalFixedElementValuePerHit
        );


        result.setAdditionalFixedElementValueTotal(
                additionalFixedElementValueTotal
        );


        result.setSecondaryUsesSeveringHitzone(
                bowgunAmmoModifier
                        .isSecondaryUsesSeveringHitzone()
        );


        result.setSecondaryIgnoresPhysicalHitzone(
                bowgunAmmoModifier
                        .isSecondaryIgnoresPhysicalHitzone()
        );


        result.setAdditionalIgnoresPhysicalHitzone(
                bowgunAmmoModifier
                        .isAdditionalIgnoresPhysicalHitzone()
        );


        return result;
    }


    /*
     * BigDecimalがnullなら0
     */
    private BigDecimal valueOrZero(
            BigDecimal value) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }


    /*
     * Integerがnullまたは0以下なら0
     */
    private int positiveOrZero(
            Integer value) {

        if (value == null
                || value <= 0) {

            return 0;
        }

        return value;
    }
}