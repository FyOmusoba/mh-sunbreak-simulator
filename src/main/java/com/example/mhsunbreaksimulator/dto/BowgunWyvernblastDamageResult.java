package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BowgunWyvernblastDamageResult {

    /*
     * =====================================================
     * ライトボウガン
     * 起爆竜弾
     * ダメージ計算結果
     * =====================================================
     */

    /*
     * 条件付きスキル補正後の攻撃力。
     *
     * 計算：
     *
     * （武器攻撃力 + attackAdd）
     * × attackMul
     */
    private BigDecimal effectiveAttack
            = BigDecimal.ZERO;


    /*
     * 起爆竜弾の固定ダメージ。
     *
     * 計算：
     *
     * effectiveAttack
     * × fixedDamageMotionValue
     * ÷ 100
     * × specialShotMul
     *
     * 物理肉質の影響を受けない。
     */
    private BigDecimal fixedDamage
            = BigDecimal.ZERO;


    /*
     * 起爆竜弾に含まれる
     * 火属性値。
     *
     * SMALL  = 3
     * MEDIUM = 5
     * LARGE  = 7
     *
     * 特殊射撃強化は
     * この火属性部分には適用しない。
     *
     * この段階ではまだ
     * 火属性肉質を掛けない。
     */
    private BigDecimal fixedFireValue
            = BigDecimal.ZERO;


    /*
     * 1回の爆発で消費する
     * 起爆竜弾の耐久値。
     *
     * SMALL  = 1
     * MEDIUM = 2
     * LARGE  = 4
     */
    private Integer durabilityCost
            = 0;
}