package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowgunAmmoModifier;
import com.example.mhsunbreaksimulator.dto.BowgunPhysicalDamageResult;
import com.example.mhsunbreaksimulator.dto.BowgunRapidFireModifier;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.dto.ShotSkillModifier;

@Service
public class BowgunPhysicalDamageService {

    /*
     * =====================================================
     * 3引数版
     * =====================================================
     *
     * 既存互換用。
     */
    public BowgunPhysicalDamageResult calculate(
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
     * 4引数版
     * =====================================================
     *
     * 既存互換用。
     *
     * 速射補正は使用するが、
     * ShotSkillModifierがまだ渡されない場合。
     */
    public BowgunPhysicalDamageResult calculate(
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
     * 5引数版
     * =====================================================
     *
     * 新しい正式版。
     *
     * ShotSkillModifierによって、
     *
     * 通常弾
     * 貫通弾
     * 散弾
     * 速射
     *
     * などの「現在選択中の弾にだけ有効な倍率」を
     * 適用できる。
     */
    public BowgunPhysicalDamageResult calculate(
            Integer weaponAttack,
            BowgunAmmoModifier bowgunAmmoModifier,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal,
            BowgunRapidFireModifier rapidFireModifier,
            ShotSkillModifier shotSkillModifier) {

        BowgunPhysicalDamageResult result
                = new BowgunPhysicalDamageResult();


        /*
         * =================================================
         * 必須値確認
         * =================================================
         */
        if (weaponAttack == null
                || bowgunAmmoModifier == null
                || bowgunAmmoModifier
                        .getPhysicalMotionValuePerHit() == null
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
         * 射撃系スキル倍率
         * =================================================
         *
         * 新方式：
         *
         * ShotSkillModifierが存在する場合、
         * 現在選択している弾に対応した倍率だけを使う。
         *
         *
         * 例：
         *
         * 通常弾
         * ＋
         * 通常弾・連射矢強化Lv3
         *
         * → 1.20
         *
         *
         * 散弾
         * ＋
         * 通常弾・連射矢強化Lv3
         *
         * → 1.00
         *
         *
         * 既存Controllerとの互換性のため、
         * ShotSkillModifierがnullなら
         * 従来のConditionalSkillModifierTotalの
         * shotMulを使用する。
         */
        BigDecimal shotMul;


        if (shotSkillModifier != null
                && shotSkillModifier
                        .getPhysicalMultiplier() != null) {

            shotMul
                    = shotSkillModifier
                            .getPhysicalMultiplier();

        } else {

            shotMul
                    = conditionalSkillModifierTotal == null
                            || conditionalSkillModifierTotal
                                    .getShotMul() == null

                                    ? BigDecimal.ONE
                                    : conditionalSkillModifierTotal
                                            .getShotMul();
        }


        /*
         * =================================================
         * 物理ダメージ倍率
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
         * 速射1発補正
         * =================================================
         *
         * 非速射
         * → 1.00
         *
         * 速射
         * → 0.70
         */
        BigDecimal rapidFirePhysicalMul
                = rapidFireModifier == null
                        || rapidFireModifier
                                .getPhysicalMulPerShot() == null

                                ? BigDecimal.ONE
                                : rapidFireModifier
                                        .getPhysicalMulPerShot();


        /*
         * =================================================
         * 1回の射撃操作で発射される弾数
         * =================================================
         *
         * 非速射
         * → 1
         *
         * 速射
         * → 3
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
         * モーション値
         * =================================================
         *
         * MV34
         *
         * ↓
         *
         * 0.34
         */
        BigDecimal motionValueRate
                = bowgunAmmoModifier
                        .getPhysicalMotionValuePerHit()
                        .movePointLeft(
                                2
                        );


        /*
         * =================================================
         * 肉質適用前
         * 1hit物理値
         * =================================================
         *
         * 補正後攻撃力
         *
         * ×
         *
         * モーション値
         *
         * ×
         *
         * 現在の弾に有効な射撃スキル倍率
         *
         * ×
         *
         * 物理ダメージ倍率
         *
         * ×
         *
         * 速射1発補正
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
         * =================================================
         * 1発の最大ヒット物理値
         * =================================================
         *
         * 貫通弾など。
         */
        BigDecimal maxPhysicalDamage
                = physicalDamagePerHit
                        .multiply(
                                BigDecimal.valueOf(
                                        bowgunAmmoModifier
                                                .getMaxHitCount()
                                )
                        );


        /*
         * =================================================
         * 弾1個消費あたり最大物理値
         * =================================================
         *
         * 非速射
         *
         * maxPhysicalDamage × 1
         *
         *
         * 速射
         *
         * maxPhysicalDamage × 3
         */
        BigDecimal maxPhysicalDamagePerAmmo
                = maxPhysicalDamage
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