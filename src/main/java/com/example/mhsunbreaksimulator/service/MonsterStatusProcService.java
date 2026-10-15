package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowgunStatusResult;
import com.example.mhsunbreaksimulator.dto.MonsterStatusProcResult;
import com.example.mhsunbreaksimulator.entity.MonsterStatusResistance;

@Service
public class MonsterStatusProcService {

    /*
     * ボウガンの状態異常計算結果と
     * モンスター耐性から
     *
     * 初回発症までの最短射撃回数を計算する。
     */
    public MonsterStatusProcResult calculate(
            BowgunStatusResult bowgunStatusResult,
            MonsterStatusResistance resistance) {

        /*
         * BowgunStatusResultから
         * 汎用計算版へ渡す。
         */
        if (bowgunStatusResult == null) {

            return new MonsterStatusProcResult();
        }


        return calculate(
                bowgunStatusResult.getStatusType(),
                bowgunStatusResult.getStatusBuildupPerShot(),
                resistance
        );
    }


    /*
     * 汎用版。
     *
     * 将来的に
     *
     * 毒武器
     * 麻痺武器
     * 睡眠武器
     * 爆破武器
     *
     * などからも使えるよう、
     *
     * statusType
     * buildupPerShot
     *
     * を直接受け取れる形も用意しておく。
     */
    public MonsterStatusProcResult calculate(
            String statusType,
            BigDecimal buildupPerShot,
            MonsterStatusResistance resistance) {

        MonsterStatusProcResult result
                = new MonsterStatusProcResult();


        /*
         * 状態異常タイプ
         */
        result.setStatusType(
                statusType
        );


        /*
         * 1射撃あたりの蓄積値
         */
        BigDecimal safeBuildupPerShot
                = valueOrZero(
                        buildupPerShot
                );

        result.setBuildupPerShot(
                safeBuildupPerShot
        );


        /*
         * モンスター耐性データがない場合は
         * ここまでで終了。
         */
        if (resistance == null) {

            return result;
        }


        /*
         * モンスター耐性データをResultへコピー。
         */
        result.setInitialThreshold(
                intOrZero(
                        resistance.getInitialThreshold()
                )
        );

        result.setIncreasePerProc(
                intOrZero(
                        resistance.getIncreasePerProc()
                )
        );

        result.setMaxThreshold(
                intOrZero(
                        resistance.getMaxThreshold()
                )
        );

        result.setDecayValue(
                valueOrZero(
                        resistance.getDecayValue()
                )
        );

        result.setEffectDurationSec(
                valueOrZero(
                        resistance.getEffectDurationSec()
                )
        );

        result.setProcDamage(
                resistance.getProcDamage()
        );


        /*
         * 完全耐性
         */
        boolean immune
                = Boolean.TRUE.equals(
                        resistance.getIsImmune()
                );

        result.setImmune(
                immune
        );


        /*
         * 完全耐性の場合、
         * 何回撃っても発症しない。
         *
         * shotsToFirstProcはnullのまま。
         */
        if (immune) {

            return result;
        }


        /*
         * 1射撃あたりの蓄積値が
         * 0以下なら発症回数を計算できない。
         */
        if (safeBuildupPerShot.compareTo(
                BigDecimal.ZERO
        ) <= 0) {

            return result;
        }


        int initialThreshold
                = intOrZero(
                        resistance.getInitialThreshold()
                );


        /*
         * 閾値が0以下なら
         * 有効な耐性データとして扱わない。
         */
        if (initialThreshold <= 0) {

            return result;
        }


        /*
         * =====================================================
         * 初回発症までの最短射撃回数
         * =====================================================
         *
         * 必要蓄積値 ÷ 1射撃あたり蓄積値
         *
         * を切り上げる。
         *
         * 例：
         *
         * threshold = 250
         * buildup    = 80
         *
         * 250 ÷ 80
         * = 3.125
         *
         * CEILING
         * = 4射
         */
        BigDecimal shots
                = BigDecimal.valueOf(
                        initialThreshold
                ).divide(
                        safeBuildupPerShot,
                        0,
                        RoundingMode.CEILING
                );


        int shotsToFirstProc
                = shots.intValueExact();


        result.setShotsToFirstProc(
                shotsToFirstProc
        );


        /*
         * 発症する射撃までに
         * 実際に投入される総蓄積値。
         *
         * 例：
         *
         * 80 × 4
         * = 320
         */
        result.setBuildupAtFirstProc(
                safeBuildupPerShot.multiply(
                        BigDecimal.valueOf(
                                shotsToFirstProc
                        )
                )
        );


        return result;
    }


    /*
     * BigDecimal null
     * ↓
     * 0
     */
    private BigDecimal valueOrZero(
            BigDecimal value) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }


    /*
     * Integer null
     * ↓
     * 0
     */
    private int intOrZero(
            Integer value) {

        return value == null
                ? 0
                : value;
    }
}