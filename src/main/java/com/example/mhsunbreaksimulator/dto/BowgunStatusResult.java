package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BowgunStatusResult {

    /*
     * 状態異常の種類。
     *
     * poison
     * paralysis
     * sleep
     * exhaust
     */
    private String statusType;


    /*
     * 状態異常弾として
     * 有効かどうか。
     */
    private boolean statusAmmo
            = false;


    /*
     * 1発・1hitあたりの
     * 毒 / 麻痺 / 睡眠蓄積値。
     */
    private BigDecimal statusBuildupPerHit
            = BigDecimal.ZERO;


    /*
     * 1回の射撃で発射される弾数。
     *
     * 非速射：1
     *
     * 状態異常弾の速射：
     * 現在のRapidFireModifierに従う。
     */
    private Integer shotCount
            = 1;


    /*
     * 1回の射撃操作で入る
     * 毒 / 麻痺 / 睡眠の合計蓄積値。
     *
     * statusBuildupPerHit
     * × maxHitCount
     * × shotCount
     */
    private BigDecimal statusBuildupPerShot
            = BigDecimal.ZERO;


    /*
     * 1発・1hitあたりの
     * 減気蓄積値。
     */
    private BigDecimal exhaustBuildupPerHit
            = BigDecimal.ZERO;


    /*
     * 1回の射撃操作で入る
     * 減気蓄積値の合計。
     */
    private BigDecimal exhaustBuildupPerShot
            = BigDecimal.ZERO;


    /*
     * 1発・1hitあたりの
     * 気絶値。
     */
    private BigDecimal stunBuildupPerHit
            = BigDecimal.ZERO;


    /*
     * 1回の射撃操作で入る
     * 気絶値の合計。
     */
    private BigDecimal stunBuildupPerShot
            = BigDecimal.ZERO;


    /*
     * 1発の弾自体の最大hit数。
     *
     * 通常の状態異常弾なら1。
     *
     * 速射数とは別。
     */
    private Integer maxHitCount
            = 1;
}