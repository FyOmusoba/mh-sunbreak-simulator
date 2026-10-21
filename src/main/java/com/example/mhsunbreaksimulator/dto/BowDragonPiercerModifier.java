package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BowDragonPiercerModifier {

    /*
     * =====================================================
     * 竜の一矢
     * =====================================================
     */

    /*
     * 1ヒットあたりの物理モーション値。
     *
     * 溜め1 = 28
     * 溜め2 = 30
     * 溜め3 = 32
     * 溜め4 = 34
     */
    private BigDecimal motionValuePerHit
            = BigDecimal.ZERO;


    /*
     * 最大ヒット数。
     *
     * 竜の一矢は貫通攻撃なので、
     * モンスターの大きさ・当たり方によって
     * 実際のヒット数は変化する。
     *
     * シミュレーターでは最大7ヒットとして保持する。
     */
    private Integer maxHitCount
            = 0;
}