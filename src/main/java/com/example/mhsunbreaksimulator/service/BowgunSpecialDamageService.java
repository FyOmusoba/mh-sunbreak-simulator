package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowgunAmmoModifier;
import com.example.mhsunbreaksimulator.dto.BowgunSpecialDamageResult;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;

@Service
public class BowgunSpecialDamageService {

    /*
     * =====================================================
     * 従来互換用
     * =====================================================
     *
     * 既存テストや既存処理を壊さないため、
     * ammoTypeを受け取らない従来版も残す。
     *
     * ammoTypeが分からない場合は
     * 砲術補正を適用しない。
     */
    public BowgunSpecialDamageResult calculate(
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
     * =====================================================
     * ボウガン特殊弾
     * =====================================================
     *
     * ammoTypeを受け取る版。
     *
     * 徹甲榴弾
     * 竜撃弾
     *
     * の固定ダメージ部分には
     * 砲術倍率を適用する。
     *
     * 拡散弾には砲術を適用しない。
     *
     * 火属性部分にも砲術を適用しない。
     */
    public BowgunSpecialDamageResult calculate(
            Integer weaponAttack,
            BowgunAmmoModifier bowgunAmmoModifier,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal,
            String ammoType) {

        BowgunSpecialDamageResult result
                = new BowgunSpecialDamageResult();


        if (weaponAttack == null
                || bowgunAmmoModifier == null) {

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
         * 条件付きスキル
         * 攻撃力加算
         */
        BigDecimal attackAdd
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal
                                .getAttackAdd() == null
                                ? BigDecimal.ZERO
                                : conditionalSkillModifierTotal
                                        .getAttackAdd();


        /*
         * 条件付きスキル
         * 攻撃力倍率
         */
        BigDecimal attackMul
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal
                                .getAttackMul() == null
                                ? BigDecimal.ONE
                                : conditionalSkillModifierTotal
                                        .getAttackMul();


        /*
         * 条件付き補正後攻撃力
         *
         * （基礎攻撃力 + 加算）
         * × 攻撃力倍率
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
         * 砲術倍率
         * =================================================
         *
         * 対象：
         *
         * 徹甲榴弾
         * 竜撃弾
         *
         * 非対象：
         *
         * 斬裂弾
         * 拡散弾
         *
         * など。
         */
        BigDecimal artilleryDamageMul
                = resolveArtilleryDamageMultiplier(
                        ammoType,
                        conditionalSkillModifierTotal
                );


        /*
         * =================================================
         * 着弾部分
         * =================================================
         *
         * 斬裂弾
         * 徹甲榴弾
         * 拡散弾
         *
         * などの着弾MV。
         *
         * 砲術はここには掛けない。
         */
        BigDecimal impactPhysicalMotionValue
                = valueOrZero(
                        bowgunAmmoModifier
                                .getImpactPhysicalMotionValue()
                );


        /*
         * =================================================
         * 二次物理MV
         * =================================================
         *
         * 主に斬裂弾。
         *
         * 砲術は掛けない。
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
         * =================================================
         * 二次固定ダメージ
         * =================================================
         *
         * 徹甲榴弾
         * 拡散弾
         * 竜撃弾1段目
         *
         * など。
         */
        BigDecimal secondaryFixedDamageMotionValue
                = valueOrZero(
                        bowgunAmmoModifier
                                .getSecondaryFixedDamageMotionValuePerHit()
                );


        /*
         * 固定ダメージ
         *
         * 攻撃力
         * × 固定MV
         * ÷ 100
         * × 砲術倍率
         */
        BigDecimal secondaryFixedDamagePerHit
                = effectiveAttack
                        .multiply(
                                secondaryFixedDamageMotionValue
                        )
                        .movePointLeft(
                                2
                        )
                        .multiply(
                                artilleryDamageMul
                        );


        BigDecimal secondaryFixedDamageTotal
                = secondaryFixedDamagePerHit
                        .multiply(
                                BigDecimal.valueOf(
                                        secondaryHitCount
                                )
                        );


        /*
         * =================================================
         * 二次火属性部分
         * =================================================
         *
         * 徹甲榴弾Lv3なら火5など。
         *
         * 砲術では増加しない。
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
         * =================================================
         * 追加固定ダメージ
         * =================================================
         *
         * 現在は主に
         * 竜撃弾2段目。
         *
         * 竜撃弾なので
         * ここにも砲術倍率を適用する。
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
                        )
                        .multiply(
                                artilleryDamageMul
                        );


        BigDecimal additionalFixedDamageTotal
                = additionalFixedDamagePerHit
                        .multiply(
                                BigDecimal.valueOf(
                                        additionalHitCount
                                )
                        );


        /*
         * =================================================
         * 追加火属性
         * =================================================
         *
         * 竜撃弾2段目など。
         *
         * 砲術では増加しない。
         */
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
         * =================================================
         * Result
         * =================================================
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
     * =====================================================
     * 砲術倍率
     * =====================================================
     */
    private BigDecimal resolveArtilleryDamageMultiplier(
            String ammoType,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal) {

        /*
         * 徹甲榴弾・竜撃弾以外は
         * 砲術対象外。
         */
        if (!isArtilleryAmmo(
                ammoType
        )) {

            return BigDecimal.ONE;
        }


        /*
         * スキル補正なし
         */
        if (conditionalSkillModifierTotal == null
                || conditionalSkillModifierTotal
                        .getArtilleryDamageMul() == null) {

            return BigDecimal.ONE;
        }


        return conditionalSkillModifierTotal
                .getArtilleryDamageMul();
    }


    /*
     * =====================================================
     * 砲術対象弾
     * =====================================================
     */
    private boolean isArtilleryAmmo(
            String ammoType) {

        if (ammoType == null) {

            return false;
        }


        return "徹甲榴弾".equals(
                ammoType
        )
                || "竜撃弾".equals(
                        ammoType
                );
    }


    /*
     * =====================================================
     * null安全
     * =====================================================
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