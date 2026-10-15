package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BowgunStatusModifier {

    /*
     * 状態異常の種類。
     *
     * 例：
     * poison
     * paralysis
     * sleep
     * exhaust
     */
    private String statusType;


    /*
     * 1hitあたりの
     * 毒・麻痺・睡眠などの蓄積値。
     *
     * 状態異常弾の基礎値を保持する。
     */
    private BigDecimal statusBuildupPerHit
            = BigDecimal.ZERO;


    /*
     * 1hitあたりの減気蓄積値。
     *
     * 減気弾用。
     *
     * 毒・麻痺・睡眠とは
     * 別枠で保持する。
     */
    private BigDecimal exhaustBuildupPerHit
            = BigDecimal.ZERO;


    /*
     * 1hitあたりの気絶値。
     *
     * 減気弾など、
     * 頭部へ当てた場合に
     * 気絶へ関係する弾用。
     */
    private BigDecimal stunBuildupPerHit
            = BigDecimal.ZERO;


    /*
     * 1発の弾そのものの最大hit数。
     *
     * 通常の状態異常弾は
     * 基本1hitとして扱う。
     *
     * 速射の2発分はここには含めない。
     */
    private Integer maxHitCount
            = 1;


    /*
     * 状態異常弾として
     * 有効なデータか。
     *
     * 通常弾などならfalse。
     */
    private boolean statusAmmo
            = false;
}