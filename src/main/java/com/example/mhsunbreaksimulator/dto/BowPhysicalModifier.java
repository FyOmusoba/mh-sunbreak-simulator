package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BowPhysicalModifier {

    /*
     * 1hitあたりの最終物理係数
     *
     * 例：
     * MV12なら 0.12 を基礎として、
     * 溜め・ビン・射撃補正を掛け合わせる
     */
    private BigDecimal physicalRatePerHit
            = BigDecimal.ZERO;

    /*
     * 最大ヒット時の物理係数合計
     */
    private BigDecimal maxPhysicalRate
            = BigDecimal.ZERO;

    /*
     * 最大ヒット数
     */
    private Integer maxHitCount
            = 0;
}