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

    /*
     * =====================================================
     * 弓の基礎物理係数
     * =====================================================
     *
     * ここでは、
     *
     * ・矢のモーション値
     * ・溜め段階
     * ・ビン
     *
     * だけをまとめる。
     *
     *
     * 通常弾・連射矢強化
     * 貫通弾・貫通矢強化
     * 散弾・拡散矢強化
     *
     * などのshotMulは、
     *
     * ShotSkillModifierService
     *
     * で現在の矢種を判定した後、
     *
     * BowPhysicalDamageService
     *
     * 側で1回だけ適用する。
     */
    public BowPhysicalModifier calculate(
            BowChargeModifier bowChargeModifier,
            BowShotModifier bowShotModifier,
            BowCoatingModifier bowCoatingModifier,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal) {

        BowPhysicalModifier result
                = new BowPhysicalModifier();


        /*
         * =================================================
         * 必須値確認
         * =================================================
         */
        if (bowShotModifier == null
                || bowShotModifier
                        .getMotionValuePerHit() == null
                || bowShotModifier
                        .getMaxHitCount() == null
                || bowShotModifier
                        .getMaxHitCount() <= 0) {

            return result;
        }


        /*
         * =================================================
         * 矢モーション値
         * =================================================
         *
         * 例：
         *
         * MV 12
         *
         * ↓
         *
         * 0.12
         */
        BigDecimal motionValueRate
                = bowShotModifier
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
         * 溜め物理倍率
         * =================================================
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
         * 1hitあたり基礎物理係数
         * =================================================
         *
         * モーション値
         *
         * ×
         *
         * 溜め倍率
         *
         * ×
         *
         * ビン倍率
         *
         *
         * ★ shotMulはここでは掛けない。
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
         * 最大ヒット時物理係数
         * =================================================
         */
        BigDecimal maxPhysicalRate
                = physicalRatePerHit
                        .multiply(
                                BigDecimal.valueOf(
                                        bowShotModifier
                                                .getMaxHitCount()
                                )
                        );


        /*
         * =================================================
         * 結果
         * =================================================
         */
        result.setPhysicalRatePerHit(
                physicalRatePerHit
        );


        result.setMaxPhysicalRate(
                maxPhysicalRate
        );


        result.setMaxHitCount(
                bowShotModifier
                        .getMaxHitCount()
        );


        return result;
    }
}