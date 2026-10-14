package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BowShotModifier {

    /*
     * 1ヒットあたりのモーション値
     */
    private BigDecimal motionValuePerHit
            = BigDecimal.ZERO;

    /*
     * 最大ヒット数
     *
     * 連射・拡散：
     * 全ての矢が当たった場合の本数
     *
     * 貫通：
     * 1本の矢が最大まで貫通した場合のヒット数
     */
    private Integer maxHitCount
            = 0;
}