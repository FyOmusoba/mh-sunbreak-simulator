package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowgunAmmoModifier;
import com.example.mhsunbreaksimulator.dto.BowgunPhysicalDamageResult;
import com.example.mhsunbreaksimulator.dto.BowgunRapidFireModifier;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;

@Service
public class BowgunPhysicalDamageService {

    /*
     * 既存の3引数版。
     *
     * 速射補正なしとして
     * 4引数版を呼び出す。
     */
    public BowgunPhysicalDamageResult calculate(
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
    public BowgunPhysicalDamageResult calculate(
            Integer weaponAttack,
            BowgunAmmoModifier bowgunAmmoModifier,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal,
            BowgunRapidFireModifier rapidFireModifier) {

        BowgunPhysicalDamageResult result
                = new BowgunPhysicalDamageResult();


        if (weaponAttack == null
                || bowgunAmmoModifier == null
                || bowgunAmmoModifier.getPhysicalMotionValuePerHit() == null
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


        BigDecimal shotMul
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal.getShotMul() == null
                                ? BigDecimal.ONE
                                : conditionalSkillModifierTotal.getShotMul();


        BigDecimal physicalDamageMul
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal.getPhysicalDamageMul() == null
                                ? BigDecimal.ONE
                                : conditionalSkillModifierTotal.getPhysicalDamageMul();


        /*
         * 速射1発ごとの物理補正
         *
         * 非速射：1.00
         * 速射　：0.70
         */
        BigDecimal rapidFirePhysicalMul
                = rapidFireModifier == null
                        || rapidFireModifier.getPhysicalMulPerShot() == null
                                ? BigDecimal.ONE
                                : rapidFireModifier.getPhysicalMulPerShot();


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
         * モーション値
         *
         * 例：
         * MV 7
         * ↓
         * 0.07
         */
        BigDecimal motionValueRate
                = bowgunAmmoModifier
                        .getPhysicalMotionValuePerHit()
                        .movePointLeft(
                                2
                        );


        /*
         * 肉質補正前・1hit物理値
         *
         * 補正後攻撃力
         * × モーション値
         * × 射撃補正
         * × 物理ダメージ補正
         * × 速射1発補正
         */
        BigDecimal physicalDamagePerHit
                = effectiveAttack
                        .multiply(
                                motionValueRate
                        )
                        .multiply(
                                shotMul
                        )
                        .multiply(
                                physicalDamageMul
                        )
                        .multiply(
                                rapidFirePhysicalMul
                        );


        /*
         * 1発の弾が最大ヒットした場合の物理値
         *
         * 貫通弾などの
         * 弾そのもののヒット数を掛ける。
         */
        BigDecimal maxPhysicalDamage
                = physicalDamagePerHit
                        .multiply(
                                BigDecimal.valueOf(
                                        bowgunAmmoModifier.getMaxHitCount()
                                )
                        );


        /*
         * 弾1個消費あたりの最大物理値
         *
         * 非速射：
         * maxPhysicalDamage × 1
         *
         * 速射：
         * maxPhysicalDamage × 3
         */
        BigDecimal maxPhysicalDamagePerAmmo
                = maxPhysicalDamage
                        .multiply(
                                BigDecimal.valueOf(
                                        shotCount
                                )
                        );


        result.setEffectiveAttack(
                effectiveAttack
        );

        result.setPhysicalDamagePerHit(
                physicalDamagePerHit
        );

        result.setMaxPhysicalDamage(
                maxPhysicalDamage
        );

        result.setShotCount(
                shotCount
        );

        result.setMaxPhysicalDamagePerAmmo(
                maxPhysicalDamagePerAmmo
        );


        return result;
    }
}