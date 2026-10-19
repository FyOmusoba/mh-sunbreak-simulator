package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowgunSpecialDamageResult;
import com.example.mhsunbreaksimulator.dto.BowgunSpecialFinalDamageResult;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.dto.CriticalModifierResult;
import com.example.mhsunbreaksimulator.dto.MonsterHitzoneValueResult;
import com.example.mhsunbreaksimulator.dto.MonsterWeakpointResult;
import com.example.mhsunbreaksimulator.dto.WeakpointSkillModifier;
import com.example.mhsunbreaksimulator.entity.BowgunAmmo;
import com.example.mhsunbreaksimulator.entity.MonsterHitzone;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BowgunSpecialFinalDamageService {

    private final MonsterWeakpointService monsterWeakpointService;

    private final WeakpointSkillModifierService
            weakpointSkillModifierService;

    private final CriticalModifierService criticalModifierService;


    /*
     * =====================================================
     * ボウガン特殊弾
     * 最終ダメージ計算
     * =====================================================
     */
    public BowgunSpecialFinalDamageResult calculate(
            BowgunAmmo bowgunAmmo,
            BowgunSpecialDamageResult specialDamageResult,
            MonsterHitzone hitzone,
            Integer weaponAffinity,
            ConditionalSkillModifierTotal conditionalModifier,
            Map<Integer, SkillEffectState> resolvedSkillEffects) {

        BowgunSpecialFinalDamageResult result
                = new BowgunSpecialFinalDamageResult();


        if (bowgunAmmo == null
                || specialDamageResult == null
                || hitzone == null) {

            return result;
        }


        String ammoType
                = bowgunAmmo.getAmmoType();


        if (ammoType == null) {

            return result;
        }


        BigDecimal effectiveAttack
                = safeDamage(
                        specialDamageResult
                                .getEffectiveAttack()
                );


        BigDecimal physicalDamageMul
                = conditionalModifier == null
                        || conditionalModifier
                                .getPhysicalDamageMul() == null
                                ? BigDecimal.ONE
                                : conditionalModifier
                                        .getPhysicalDamageMul();


        /*
         * =================================================
         * 着弾部分
         * =================================================
         */
        BigDecimal impactMotionValue
                = safeDamage(
                        specialDamageResult
                                .getImpactPhysicalMotionValue()
                );


        BigDecimal impactBeforeHitzone
                = effectiveAttack
                        .multiply(
                                impactMotionValue
                        )
                        .movePointLeft(
                                2
                        )
                        .multiply(
                                physicalDamageMul
                        );


        int shotHitzone
                = valueOrZero(
                        hitzone.getShot()
                );


        BigDecimal impactNormalDamage
                = applyHitzone(
                        impactBeforeHitzone,
                        shotHitzone
                );


        /*
         * 着弾部分用
         * 弱点特効・会心判定
         */
        CriticalModifierResult impactCriticalModifier
                = createPhysicalCriticalModifier(
                        "SHOT",
                        shotHitzone,
                        weaponAffinity,
                        conditionalModifier,
                        resolvedSkillEffects
                );


        BigDecimal impactPositiveCriticalDamage;
        BigDecimal impactNegativeCriticalDamage;


        /*
         * 拡散弾の着弾部分は
         * 会心なしとして扱う。
         */
        if ("拡散弾".equals(
                ammoType
        )) {

            impactPositiveCriticalDamage
                    = impactNormalDamage;

            impactNegativeCriticalDamage
                    = impactNormalDamage;

        } else {

            impactPositiveCriticalDamage
                    = impactNormalDamage
                            .multiply(
                                    safeMultiplier(
                                            impactCriticalModifier
                                                    .getPositiveCriticalDamageMul()
                                    )
                            );


            impactNegativeCriticalDamage
                    = impactNormalDamage
                            .multiply(
                                    safeMultiplier(
                                            impactCriticalModifier
                                                    .getNegativeCriticalDamageMul()
                                    )
                            );
        }


        result.setImpactNormalPhysicalDamage(
                impactNormalDamage
        );

        result.setImpactPositiveCriticalPhysicalDamage(
                impactPositiveCriticalDamage
        );

        result.setImpactNegativeCriticalPhysicalDamage(
                impactNegativeCriticalDamage
        );


        /*
         * =================================================
         * 二次物理部分
         * =================================================
         *
         * 主に斬裂弾。
         */
        BigDecimal secondaryPhysicalMotionValue
                = safeDamage(
                        specialDamageResult
                                .getSecondaryPhysicalMotionValuePerHit()
                );


        int secondaryHitCount
                = positiveOrZero(
                        specialDamageResult
                                .getSecondaryHitCount()
                );


        BigDecimal secondaryPhysicalBeforeHitzone
                = effectiveAttack
                        .multiply(
                                secondaryPhysicalMotionValue
                        )
                        .movePointLeft(
                                2
                        )
                        .multiply(
                                physicalDamageMul
                        );


        int secondaryPhysicalHitzone;
        String secondaryHitzoneType;


        if (specialDamageResult
                .isSecondaryUsesSeveringHitzone()) {

            secondaryPhysicalHitzone
                    = valueOrZero(
                            hitzone.getSever()
                    );

            secondaryHitzoneType
                    = "SEVER";

        } else {

            secondaryPhysicalHitzone
                    = shotHitzone;

            secondaryHitzoneType
                    = "SHOT";
        }


        BigDecimal secondaryNormalPerHit
                = applyHitzone(
                        secondaryPhysicalBeforeHitzone,
                        secondaryPhysicalHitzone
                );


        /*
         * 斬裂弾では
         * 斬肉質基準で弱点特効を判定。
         */
        CriticalModifierResult secondaryCriticalModifier
                = createPhysicalCriticalModifier(
                        secondaryHitzoneType,
                        secondaryPhysicalHitzone,
                        weaponAffinity,
                        conditionalModifier,
                        resolvedSkillEffects
                );


        BigDecimal secondaryPositivePerHit
                = secondaryNormalPerHit
                        .multiply(
                                safeMultiplier(
                                        secondaryCriticalModifier
                                                .getPositiveCriticalDamageMul()
                                )
                        );


        BigDecimal secondaryNegativePerHit
                = secondaryNormalPerHit
                        .multiply(
                                safeMultiplier(
                                        secondaryCriticalModifier
                                                .getNegativeCriticalDamageMul()
                                )
                        );


        BigDecimal secondaryNormalTotal
                = secondaryNormalPerHit
                        .multiply(
                                BigDecimal.valueOf(
                                        secondaryHitCount
                                )
                        );


        BigDecimal secondaryPositiveTotal
                = secondaryPositivePerHit
                        .multiply(
                                BigDecimal.valueOf(
                                        secondaryHitCount
                                )
                        );


        BigDecimal secondaryNegativeTotal
                = secondaryNegativePerHit
                        .multiply(
                                BigDecimal.valueOf(
                                        secondaryHitCount
                                )
                        );


        result.setSecondaryNormalPhysicalDamagePerHit(
                secondaryNormalPerHit
        );

        result.setSecondaryPositiveCriticalPhysicalDamagePerHit(
                secondaryPositivePerHit
        );

        result.setSecondaryNegativeCriticalPhysicalDamagePerHit(
                secondaryNegativePerHit
        );


        result.setSecondaryNormalPhysicalDamageTotal(
                secondaryNormalTotal
        );

        result.setSecondaryPositiveCriticalPhysicalDamageTotal(
                secondaryPositiveTotal
        );

        result.setSecondaryNegativeCriticalPhysicalDamageTotal(
                secondaryNegativeTotal
        );


        /*
         * =================================================
         * 固定物理部分
         * =================================================
         */
        BigDecimal secondaryFixedDamageTotal
                = safeDamage(
                        specialDamageResult
                                .getSecondaryFixedDamageTotal()
                );


        BigDecimal additionalFixedDamageTotal
                = safeDamage(
                        specialDamageResult
                                .getAdditionalFixedDamageTotal()
                );


        result.setSecondaryFixedDamageTotal(
                secondaryFixedDamageTotal
        );

        result.setAdditionalFixedDamageTotal(
                additionalFixedDamageTotal
        );


        /*
         * =================================================
         * 火属性部分
         * =================================================
         */
        int fireHitzone
                = valueOrZero(
                        hitzone.getFire()
                );


        BigDecimal fireWeakpointMultiplier
                = createFireWeakpointMultiplier(
                        fireHitzone,
                        resolvedSkillEffects
                );


        BigDecimal secondaryElementPerHit
                = applyHitzone(
                        safeDamage(
                                specialDamageResult
                                        .getSecondaryFixedElementValuePerHit()
                        ),
                        fireHitzone
                )
                        .multiply(
                                fireWeakpointMultiplier
                        );


        BigDecimal secondaryElementTotal
                = secondaryElementPerHit
                        .multiply(
                                BigDecimal.valueOf(
                                        secondaryHitCount
                                )
                        );


        int additionalHitCount
                = positiveOrZero(
                        specialDamageResult
                                .getAdditionalHitCount()
                );


        BigDecimal additionalElementPerHit
                = applyHitzone(
                        safeDamage(
                                specialDamageResult
                                        .getAdditionalFixedElementValuePerHit()
                        ),
                        fireHitzone
                )
                        .multiply(
                                fireWeakpointMultiplier
                        );


        BigDecimal additionalElementTotal
                = additionalElementPerHit
                        .multiply(
                                BigDecimal.valueOf(
                                        additionalHitCount
                                )
                        );


        result.setSecondaryElementDamageTotal(
                secondaryElementTotal
        );

        result.setAdditionalElementDamageTotal(
                additionalElementTotal
        );


        /*
         * =================================================
         * 四捨五入前合計
         * =================================================
         */
        BigDecimal normalBeforeRound
                = impactNormalDamage
                        .add(
                                secondaryNormalTotal
                        )
                        .add(
                                secondaryFixedDamageTotal
                        )
                        .add(
                                secondaryElementTotal
                        )
                        .add(
                                additionalFixedDamageTotal
                        )
                        .add(
                                additionalElementTotal
                        );


        BigDecimal positiveBeforeRound
                = impactPositiveCriticalDamage
                        .add(
                                secondaryPositiveTotal
                        )
                        .add(
                                secondaryFixedDamageTotal
                        )
                        .add(
                                secondaryElementTotal
                        )
                        .add(
                                additionalFixedDamageTotal
                        )
                        .add(
                                additionalElementTotal
                        );


        BigDecimal negativeBeforeRound
                = impactNegativeCriticalDamage
                        .add(
                                secondaryNegativeTotal
                        )
                        .add(
                                secondaryFixedDamageTotal
                        )
                        .add(
                                secondaryElementTotal
                        )
                        .add(
                                additionalFixedDamageTotal
                        )
                        .add(
                                additionalElementTotal
                        );


        result.setNormalTotalDamageBeforeRound(
                normalBeforeRound
        );

        result.setPositiveCriticalTotalDamageBeforeRound(
                positiveBeforeRound
        );

        result.setNegativeCriticalTotalDamageBeforeRound(
                negativeBeforeRound
        );


        /*
         * =================================================
         * 四捨五入後
         * 物理内訳
         * =================================================
         *
         * 着弾
         * +
         * 斬裂などの二次物理
         * +
         * 肉質無視固定ダメージ
         */
        int normalPhysicalDamage
                = roundDamage(
                        impactNormalDamage
                )
                        + (
                                roundDamage(
                                        secondaryNormalPerHit
                                )
                                * secondaryHitCount
                        )
                        + (
                                roundDamage(
                                        safeDamage(
                                                specialDamageResult
                                                        .getSecondaryFixedDamagePerHit()
                                        )
                                )
                                * secondaryHitCount
                        )
                        + (
                                roundDamage(
                                        safeDamage(
                                                specialDamageResult
                                                        .getAdditionalFixedDamagePerHit()
                                        )
                                )
                                * additionalHitCount
                        );


        int positiveCriticalPhysicalDamage
                = roundDamage(
                        impactPositiveCriticalDamage
                )
                        + (
                                roundDamage(
                                        secondaryPositivePerHit
                                )
                                * secondaryHitCount
                        )
                        + (
                                roundDamage(
                                        safeDamage(
                                                specialDamageResult
                                                        .getSecondaryFixedDamagePerHit()
                                        )
                                )
                                * secondaryHitCount
                        )
                        + (
                                roundDamage(
                                        safeDamage(
                                                specialDamageResult
                                                        .getAdditionalFixedDamagePerHit()
                                        )
                                )
                                * additionalHitCount
                        );


        int negativeCriticalPhysicalDamage
                = roundDamage(
                        impactNegativeCriticalDamage
                )
                        + (
                                roundDamage(
                                        secondaryNegativePerHit
                                )
                                * secondaryHitCount
                        )
                        + (
                                roundDamage(
                                        safeDamage(
                                                specialDamageResult
                                                        .getSecondaryFixedDamagePerHit()
                                        )
                                )
                                * secondaryHitCount
                        )
                        + (
                                roundDamage(
                                        safeDamage(
                                                specialDamageResult
                                                        .getAdditionalFixedDamagePerHit()
                                        )
                                )
                                * additionalHitCount
                        );


        /*
         * =================================================
         * 四捨五入後
         * 属性内訳
         * =================================================
         */
        int normalElementDamage
                = (
                        roundDamage(
                                secondaryElementPerHit
                        )
                        * secondaryHitCount
                )
                        + (
                                roundDamage(
                                        additionalElementPerHit
                                )
                                * additionalHitCount
                        );


        /*
         * 現在の特殊弾火属性部分は
         * 会心による変化なし。
         */
        int positiveCriticalElementDamage
                = normalElementDamage;


        int negativeCriticalElementDamage
                = normalElementDamage;


        /*
         * DTOへ格納
         */
        result.setNormalPhysicalDamage(
                normalPhysicalDamage
        );

        result.setNormalElementDamage(
                normalElementDamage
        );


        result.setPositiveCriticalPhysicalDamage(
                positiveCriticalPhysicalDamage
        );

        result.setPositiveCriticalElementDamage(
                positiveCriticalElementDamage
        );


        result.setNegativeCriticalPhysicalDamage(
                negativeCriticalPhysicalDamage
        );

        result.setNegativeCriticalElementDamage(
                negativeCriticalElementDamage
        );


        /*
         * =================================================
         * 最終ダメージ
         * =================================================
         */
        int normalTotalDamage
                = normalPhysicalDamage
                        + normalElementDamage;


        int positiveCriticalTotalDamage
                = positiveCriticalPhysicalDamage
                        + positiveCriticalElementDamage;


        int negativeCriticalTotalDamage
                = negativeCriticalPhysicalDamage
                        + negativeCriticalElementDamage;


        result.setNormalTotalDamage(
                normalTotalDamage
        );

        result.setPositiveCriticalTotalDamage(
                positiveCriticalTotalDamage
        );

        result.setNegativeCriticalTotalDamage(
                negativeCriticalTotalDamage
        );


        return result;
    }


    /*
     * =====================================================
     * 物理肉質ごとの会心判定作成
     * =====================================================
     */
    private CriticalModifierResult createPhysicalCriticalModifier(
            String hitzoneType,
            int physicalHitzone,
            Integer weaponAffinity,
            ConditionalSkillModifierTotal conditionalModifier,
            Map<Integer, SkillEffectState> resolvedSkillEffects) {

        MonsterHitzoneValueResult hitzoneValueResult
                = new MonsterHitzoneValueResult();


        hitzoneValueResult.setPhysicalHitzoneType(
                hitzoneType
        );

        hitzoneValueResult.setPhysicalHitzone(
                physicalHitzone
        );


        MonsterWeakpointResult weakpointResult
                = monsterWeakpointService.calculate(
                        hitzoneValueResult
                );


        WeakpointSkillModifier weakpointModifier
                = weakpointSkillModifierService.calculate(
                        weakpointResult,
                        resolvedSkillEffects
                );


        return criticalModifierService.calculate(
                weaponAffinity,
                conditionalModifier,
                weakpointModifier
        );
    }


    /*
     * =====================================================
     * 火属性弱点倍率
     * =====================================================
     */
    private BigDecimal createFireWeakpointMultiplier(
            int fireHitzone,
            Map<Integer, SkillEffectState> resolvedSkillEffects) {

        MonsterHitzoneValueResult hitzoneValueResult
                = new MonsterHitzoneValueResult();


        hitzoneValueResult.setElementType(
                "火"
        );

        hitzoneValueResult.setElemental(
                true
        );

        hitzoneValueResult.setElementHitzone(
                fireHitzone
        );


        MonsterWeakpointResult weakpointResult
                = monsterWeakpointService.calculate(
                        hitzoneValueResult
                );


        WeakpointSkillModifier weakpointModifier
                = weakpointSkillModifierService.calculate(
                        weakpointResult,
                        resolvedSkillEffects
                );


        return safeMultiplier(
                weakpointModifier
                        .getMainElementDamageMul()
        );
    }


    /*
     * =====================================================
     * 肉質適用
     * =====================================================
     */
    private BigDecimal applyHitzone(
            BigDecimal damage,
            int hitzone) {

        if (damage == null
                || hitzone <= 0) {

            return BigDecimal.ZERO;
        }


        return damage
                .multiply(
                        BigDecimal.valueOf(
                                hitzone
                        )
                )
                .movePointLeft(
                        2
                );
    }


    /*
     * =====================================================
     * 四捨五入
     * =====================================================
     */
    private int roundDamage(
            BigDecimal damage) {

        if (damage == null) {

            return 0;
        }


        return damage
                .setScale(
                        0,
                        RoundingMode.HALF_UP
                )
                .intValue();
    }


    /*
     * =====================================================
     * null安全
     * =====================================================
     */
    private BigDecimal safeDamage(
            BigDecimal value) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }


    private BigDecimal safeMultiplier(
            BigDecimal value) {

        return value == null
                ? BigDecimal.ONE
                : value;
    }


    private int valueOrZero(
            Integer value) {

        return value == null
                ? 0
                : value;
    }


    private int positiveOrZero(
            Integer value) {

        if (value == null
                || value <= 0) {

            return 0;
        }


        return value;
    }
}