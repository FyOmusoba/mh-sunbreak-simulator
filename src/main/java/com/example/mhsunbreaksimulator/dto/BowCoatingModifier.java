package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BowCoatingModifier {

    /*
     * ビンによる物理ダメージ倍率
     *
     * 補正なしの場合は1倍
     */
    private BigDecimal physicalMul
            = BigDecimal.ONE;

    /*
     * 状態異常蓄積倍率
     *
     * 状態異常系ビンで使用する
     * 補正なしの場合は1倍
     */
    private BigDecimal statusMul
            = BigDecimal.ONE;
}