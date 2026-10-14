package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BowgunRapidFireModifier {

    /*
     * 速射かどうか
     */
    private boolean rapidFire = false;


    /*
     * 1回の射撃で発射される弾数
     *
     * 非速射：1発
     * 速射　：3発
     */
    private Integer shotCount = 1;


    /*
     * 速射1発ごとの物理補正
     *
     * 非速射：1.00
     * 速射　：0.70
     */
    private BigDecimal physicalMulPerShot
            = BigDecimal.ONE;


    /*
     * 速射1発ごとの属性補正
     *
     * 非速射：1.00
     * 速射　：0.50
     */
    private BigDecimal elementMulPerShot
            = BigDecimal.ONE;
}