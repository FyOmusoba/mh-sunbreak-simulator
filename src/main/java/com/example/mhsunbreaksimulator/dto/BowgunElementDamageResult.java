package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BowgunElementDamageResult {

    /*
     * 条件付き補正後の攻撃力
     */
    private BigDecimal effectiveAttack
            = BigDecimal.ZERO;


    /*
     * 1hitあたりの肉質補正前属性値
     *
     * 速射の場合は
     * 速射属性補正後の1発・1hitの値。
     */
    private BigDecimal elementDamagePerHit
            = BigDecimal.ZERO;


    /*
     * 1発の弾が最大ヒットした場合の属性値
     *
     * 通常属性弾なら1hit。
     * 貫通属性弾では複数hitになる。
     *
     * ここには速射3発分は含めない。
     */
    private BigDecimal maxElementDamage
            = BigDecimal.ZERO;


    /*
     * 1回の射撃操作で発射される弾数
     *
     * 非速射：1
     * 速射　：3
     */
    private Integer shotCount = 1;


    /*
     * 弾1個消費あたりの最大属性値
     *
     * 非速射：
     * maxElementDamage × 1
     *
     * 速射：
     * maxElementDamage × 3
     */
    private BigDecimal maxElementDamagePerAmmo
            = BigDecimal.ZERO;
}