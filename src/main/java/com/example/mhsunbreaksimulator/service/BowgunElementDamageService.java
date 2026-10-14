package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowgunAmmoModifier;
import com.example.mhsunbreaksimulator.dto.BowgunElementDamageResult;
import com.example.mhsunbreaksimulator.dto.BowgunRapidFireModifier;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;

@Service
public class BowgunElementDamageService {

    /*
     * 既存の3引数版。
     *
     * 速射補正なしとして
     * 4引数版を呼び出す。
     */
    public BowgunElementDamageResult calculate(
            Integer weaponAttack,
            BowgunAmmoModifier bowgunAmmoModifier,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal) {

        return calculate(
                weaponAttack,
                bowgunAmmoModifier,
                conditionalSkillModifierTotal,
                null
        );
    }


    /*
     * 速射補正対応版
     */
    public BowgunElementDamageResult calculate(
            Integer weaponAttack,
            BowgunAmmoModifier bowgunAmmoModifier,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal,
            BowgunRapidFireModifier rapidFireModifier) {

        BowgunElementDamageResult result
                = new BowgunElementDamageResult();


        if (weaponAttack == null
                || bowgunAmmoModifier == null
                || bowgunAmmoModifier.getElementMotionValuePerHit() == null
                || bowgunAmmoModifier.getElementMotionValuePerHit()
                        .compareTo(BigDecimal.ZERO) <= 0
                || bowgunAmmoModifier.getMaxHitCount() == null
                || bowgunAmmoModifier.getMaxHitCount() <= 0) {

            return result;
        }


        BigDecimal baseAttack
                = BigDecimal.valueOf(
                        weaponAttack
                );


        BigDecimal attackAdd
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal.getAttackAdd() == null
                                ? BigDecimal.ZERO
                                : conditionalSkillModifierTotal.getAttackAdd();


        BigDecimal attackMul
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal.getAttackMul() == null
                                ? BigDecimal.ONE
                                : conditionalSkillModifierTotal.getAttackMul();


        BigDecimal elementAdd
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal.getElementAdd() == null
                                ? BigDecimal.ZERO
                                : conditionalSkillModifierTotal.getElementAdd();


        BigDecimal elementMul
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal.getElementMul() == null
                                ? BigDecimal.ONE
                                : conditionalSkillModifierTotal.getElementMul();


        BigDecimal elementDamageMul
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal.getElementDamageMul() == null
                                ? BigDecimal.ONE
                                : conditionalSkillModifierTotal.getElementDamageMul();


        /*
         * 速射1発ごとの属性補正
         *
         * 非速射：1.00
         * 速射　：0.50
         */
        BigDecimal rapidFireElementMul
                = rapidFireModifier == null
                        || rapidFireModifier.getElementMulPerShot() == null
                                ? BigDecimal.ONE
                                : rapidFireModifier.getElementMulPerShot();


        /*
         * 1回の射撃操作で発射される弾数
         *
         * 非速射：1
         * 速射　：3
         */
        int shotCount
                = rapidFireModifier == null
                        || rapidFireModifier.getShotCount() == null
                        || rapidFireModifier.getShotCount() <= 0
                                ? 1
                                : rapidFireModifier.getShotCount();


        /*
         * 条件付き補正後攻撃力
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
         * 属性弾の補正後属性値
         *
         * 元属性値
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
         * 肉質補正前・1hit属性値
         *
         * 補正後攻撃力
         * × 補正後属性値
         * ÷ 100
         * × 属性ダメージ補正
         * × 速射属性補正
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
                        );


        /*
         * 1発の弾が最大ヒットした場合の属性値
         *
         * 通常属性弾なら1hit。
         * 貫通属性弾では複数hit。
         */
        BigDecimal maxElementDamage
                = elementDamagePerHit
                        .multiply(
                                BigDecimal.valueOf(
                                        bowgunAmmoModifier.getMaxHitCount()
                                )
                        );


        /*
         * 弾1個消費あたりの最大属性値
         *
         * 非速射：
         * maxElementDamage × 1
         *
         * 速射：
         * maxElementDamage × 3
         */
        BigDecimal maxElementDamagePerAmmo
                = maxElementDamage
                        .multiply(
                                BigDecimal.valueOf(
                                        shotCount
                                )
                        );


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