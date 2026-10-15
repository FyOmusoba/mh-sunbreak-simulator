package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowgunRapidFireModifier;
import com.example.mhsunbreaksimulator.dto.BowgunStatusModifier;
import com.example.mhsunbreaksimulator.dto.BowgunStatusResult;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;

@Service
public class BowgunStatusService {

    /*
     * 条件付きスキル補正なし版
     */
    public BowgunStatusResult calculate(
            BowgunStatusModifier statusModifier,
            BowgunRapidFireModifier rapidFireModifier) {

        return calculate(
                statusModifier,
                rapidFireModifier,
                null
        );
    }


    /*
     * 条件付きスキル補正あり版
     */
    public BowgunStatusResult calculate(
            BowgunStatusModifier statusModifier,
            BowgunRapidFireModifier rapidFireModifier,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal) {

        BowgunStatusResult result
                = new BowgunStatusResult();


        /*
         * 状態異常弾ではない場合
         */
        if (statusModifier == null
                || !statusModifier.isStatusAmmo()) {

            return result;
        }


        result.setStatusType(
                statusModifier.getStatusType()
        );

        result.setStatusAmmo(
                true
        );


        /*
         * 1発の弾自体のhit数
         */
        int maxHitCount
                = positiveOrOne(
                        statusModifier.getMaxHitCount()
                );


        /*
         * 1回の射撃で発射される弾数
         *
         * 非速射：1
         * 状態異常弾速射：2
         */
        int shotCount
                = rapidFireModifier == null
                        ? 1
                        : positiveOrOne(
                                rapidFireModifier.getShotCount()
                        );


        /*
         * =====================================================
         * 毒・麻痺・睡眠
         * =====================================================
         */

        BigDecimal baseStatusBuildupPerHit
                = valueOrZero(
                        statusModifier
                                .getStatusBuildupPerHit()
                );


        /*
         * ALL用補正。
         *
         * 災禍転福
         * 伏魔響命
         * 狂竜症【蝕】
         * など。
         */
        BigDecimal allStatusAdd
                = conditionalSkillModifierTotal == null
                        ? BigDecimal.ZERO
                        : valueOrZero(
                                conditionalSkillModifierTotal
                                        .getStatusAdd()
                        );


        BigDecimal allStatusMul
                = conditionalSkillModifierTotal == null
                        ? BigDecimal.ONE
                        : valueOrOne(
                                conditionalSkillModifierTotal
                                        .getStatusMul()
                        );


        /*
         * 対象専用補正。
         *
         * poison
         * paralysis
         * sleep
         *
         * に応じて取得する。
         */
        BigDecimal targetStatusAdd
                = getTargetStatusAdd(
                        statusModifier.getStatusType(),
                        conditionalSkillModifierTotal
                );


        BigDecimal targetStatusMul
                = getTargetStatusMul(
                        statusModifier.getStatusType(),
                        conditionalSkillModifierTotal
                );


        /*
         * ALLと対象専用を合成。
         *
         * 倍率：
         * ALL × 対象専用
         *
         * 加算：
         * ALL + 対象専用
         */
        BigDecimal totalStatusMul
                = allStatusMul.multiply(
                        targetStatusMul
                );


        BigDecimal totalStatusAdd
                = allStatusAdd.add(
                        targetStatusAdd
                );


        /*
         * 毒・麻痺・睡眠の場合だけ
         * 状態異常補正を適用。
         *
         * 基礎値
         * × 合成倍率
         * ＋ 合成加算
         */
        BigDecimal statusBuildupPerHit;

        if (isPoisonParalysisOrSleep(
                statusModifier.getStatusType())) {

            statusBuildupPerHit
                    = baseStatusBuildupPerHit
                            .multiply(
                                    totalStatusMul
                            )
                            .add(
                                    totalStatusAdd
                            );

        } else {

            statusBuildupPerHit
                    = baseStatusBuildupPerHit;
        }


        /*
         * 1回の射撃操作で入る
         * 毒・麻痺・睡眠蓄積値
         */
        BigDecimal statusBuildupPerShot
                = statusBuildupPerHit
                        .multiply(
                                BigDecimal.valueOf(
                                        maxHitCount
                                )
                        )
                        .multiply(
                                BigDecimal.valueOf(
                                        shotCount
                                )
                        );


        /*
         * =====================================================
         * 減気
         * =====================================================
         *
         * statusAdd / statusMul
         * および
         * POISON / PARALYSIS / SLEEP / BLAST
         *
         * は適用しない。
         */

        BigDecimal exhaustBuildupPerHit
                = valueOrZero(
                        statusModifier
                                .getExhaustBuildupPerHit()
                );


        BigDecimal exhaustBuildupPerShot
                = exhaustBuildupPerHit
                        .multiply(
                                BigDecimal.valueOf(
                                        maxHitCount
                                )
                        )
                        .multiply(
                                BigDecimal.valueOf(
                                        shotCount
                                )
                        );


        /*
         * =====================================================
         * 気絶
         * =====================================================
         */

        BigDecimal stunBuildupPerHit
                = valueOrZero(
                        statusModifier
                                .getStunBuildupPerHit()
                );


        BigDecimal stunBuildupPerShot
                = stunBuildupPerHit
                        .multiply(
                                BigDecimal.valueOf(
                                        maxHitCount
                                )
                        )
                        .multiply(
                                BigDecimal.valueOf(
                                        shotCount
                                )
                        );


        /*
         * =====================================================
         * Result
         * =====================================================
         */

        result.setStatusBuildupPerHit(
                statusBuildupPerHit
        );

        result.setShotCount(
                shotCount
        );

        result.setStatusBuildupPerShot(
                statusBuildupPerShot
        );

        result.setExhaustBuildupPerHit(
                exhaustBuildupPerHit
        );

        result.setExhaustBuildupPerShot(
                exhaustBuildupPerShot
        );

        result.setStunBuildupPerHit(
                stunBuildupPerHit
        );

        result.setStunBuildupPerShot(
                stunBuildupPerShot
        );

        result.setMaxHitCount(
                maxHitCount
        );


        return result;
    }


    /*
     * =====================================================
     * 対象専用の加算値
     * =====================================================
     */
    private BigDecimal getTargetStatusAdd(
            String statusType,
            ConditionalSkillModifierTotal modifier) {

        if (modifier == null
                || statusType == null) {

            return BigDecimal.ZERO;
        }


        switch (statusType) {

        case "poison":

            return valueOrZero(
                    modifier.getPoisonStatusAdd()
            );


        case "paralysis":

            return valueOrZero(
                    modifier.getParalysisStatusAdd()
            );


        case "sleep":

            return valueOrZero(
                    modifier.getSleepStatusAdd()
            );


        default:

            return BigDecimal.ZERO;
        }
    }


    /*
     * =====================================================
     * 対象専用の倍率
     * =====================================================
     */
    private BigDecimal getTargetStatusMul(
            String statusType,
            ConditionalSkillModifierTotal modifier) {

        if (modifier == null
                || statusType == null) {

            return BigDecimal.ONE;
        }


        switch (statusType) {

        case "poison":

            return valueOrOne(
                    modifier.getPoisonStatusMul()
            );


        case "paralysis":

            return valueOrOne(
                    modifier.getParalysisStatusMul()
            );


        case "sleep":

            return valueOrOne(
                    modifier.getSleepStatusMul()
            );


        default:

            return BigDecimal.ONE;
        }
    }


    /*
     * 毒・麻痺・睡眠かどうか
     */
    private boolean isPoisonParalysisOrSleep(
            String statusType) {

        if (statusType == null) {

            return false;
        }


        return statusType.equals(
                "poison"
        )
                || statusType.equals(
                        "paralysis"
                )
                || statusType.equals(
                        "sleep"
                );
    }


    /*
     * nullなら0
     */
    private BigDecimal valueOrZero(
            BigDecimal value) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }


    /*
     * nullなら1
     */
    private BigDecimal valueOrOne(
            BigDecimal value) {

        return value == null
                ? BigDecimal.ONE
                : value;
    }


    /*
     * nullまたは0以下なら1
     */
    private int positiveOrOne(
            Integer value) {

        if (value == null
                || value <= 0) {

            return 1;
        }

        return value;
    }
}