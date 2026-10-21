package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BowgunWyvernblastModifier {

    /*
     * =====================================================
     * ライトボウガン
     * 起爆竜弾
     * =====================================================
     */

    /*
     * 1回の爆発に使用する
     * 攻撃力依存の固定ダメージMV。
     *
     * 計算：
     *
     * 攻撃力
     * × fixedDamageMotionValue
     * ÷ 100
     *
     * 小爆発 = 26
     * 中爆発 = 32
     * 大爆発 = 49
     */
    private BigDecimal fixedDamageMotionValue
            = BigDecimal.ZERO;


    /*
     * 1回の爆発に含まれる
     * 攻撃力非依存の火属性値。
     *
     * 小爆発 = 火3
     * 中爆発 = 火5
     * 大爆発 = 火7
     */
    private BigDecimal fixedFireValue
            = BigDecimal.ZERO;


    /*
     * 1回の爆発で消費する
     * 起爆竜弾の耐久値。
     *
     * 起爆竜弾の初期耐久値は5。
     *
     * 小爆発 = 1
     * 中爆発 = 2
     * 大爆発 = 4
     */
    private Integer durabilityCost
            = 0;
}