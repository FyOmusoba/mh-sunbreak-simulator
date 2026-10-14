package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BowgunPhysicalDamageResult {

    /*
     * 条件付きスキル補正後の攻撃力
     */
    private BigDecimal effectiveAttack
            = BigDecimal.ZERO;


    /*
     * 1hitあたりの肉質補正前物理値
     *
     * 速射の場合は
     * 速射補正後の「1発・1hit」の値。
     */
    private BigDecimal physicalDamagePerHit
            = BigDecimal.ZERO;


    /*
     * 1発の弾が最大ヒットした場合の物理値
     *
     * 例：
     * 貫通弾7hitなら
     *
     * physicalDamagePerHit × 7
     *
     * ここには速射3発分は含めない。
     */
    private BigDecimal maxPhysicalDamage
            = BigDecimal.ZERO;


    /*
     * 1回の射撃操作で発射される弾数
     *
     * 非速射：1
     * 速射　：3
     */
    private Integer shotCount = 1;


    /*
     * 弾1個消費あたりの最大物理値
     *
     * 非速射：
     * maxPhysicalDamage × 1
     *
     * 速射：
     * maxPhysicalDamage × 3
     */
    private BigDecimal maxPhysicalDamagePerAmmo
            = BigDecimal.ZERO;
}