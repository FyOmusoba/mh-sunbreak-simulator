package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowChargeModifier;
import com.example.mhsunbreaksimulator.dto.BowCoatingModifier;
import com.example.mhsunbreaksimulator.dto.BowPhysicalModifier;
import com.example.mhsunbreaksimulator.dto.BowShotModifier;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;

@Service
public class BowPhysicalModifierService {

    public BowPhysicalModifier calculate(
            BowChargeModifier bowChargeModifier,
            BowShotModifier bowShotModifier,
            BowCoatingModifier bowCoatingModifier,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal) {

        BowPhysicalModifier result
                = new BowPhysicalModifier();

        if (bowShotModifier == null
                || bowShotModifier.getMotionValuePerHit() == null
                || bowShotModifier.getMaxHitCount() == null
                || bowShotModifier.getMaxHitCount() <= 0) {

            return result;
        }


        /* ========================================== */
        /* 矢のモーション値を倍率へ変換 */
        /* ========================================== */

        BigDecimal motionValueRate
                = bowShotModifier.getMotionValuePerHit()
                        .divide(
                                new BigDecimal("100"),
                                10,
                                RoundingMode.HALF_UP
                        );


        /* ========================================== */
        /* 各補正 */
        /* ========================================== */

        BigDecimal chargePhysicalMul
                = bowChargeModifier == null
                        || bowChargeModifier.getPhysicalMul() == null
                                ? BigDecimal.ONE
                                : bowChargeModifier.getPhysicalMul();

        BigDecimal coatingPhysicalMul
                = bowCoatingModifier == null
                        || bowCoatingModifier.getPhysicalMul() == null
                                ? BigDecimal.ONE
                                : bowCoatingModifier.getPhysicalMul();

        BigDecimal shotMul
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal.getShotMul() == null
                                ? BigDecimal.ONE
                                : conditionalSkillModifierTotal.getShotMul();


        /* ========================================== */
        /* 1hitあたりの最終物理係数 */
        /* ========================================== */

        BigDecimal physicalRatePerHit
                = motionValueRate
                        .multiply(
                                chargePhysicalMul
                        )
                        .multiply(
                                coatingPhysicalMul
                        )
                        .multiply(
                                shotMul
                        );


        /* ========================================== */
        /* 最大ヒット時の合計係数 */
        /* ========================================== */

        BigDecimal maxPhysicalRate
                = physicalRatePerHit.multiply(
                        BigDecimal.valueOf(
                                bowShotModifier.getMaxHitCount()
                        )
                );


        result.setPhysicalRatePerHit(
                physicalRatePerHit
        );

        result.setMaxPhysicalRate(
                maxPhysicalRate
        );

        result.setMaxHitCount(
                bowShotModifier.getMaxHitCount()
        );

        return result;
    }
}