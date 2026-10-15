package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MonsterStatusProcResult {

    /*
     * Java内部の状態異常種別。
     *
     * poison
     * paralysis
     * sleep
     * blast
     */
    private String statusType;


    /*
     * モンスターが
     * この状態異常に完全耐性を持つか。
     */
    private boolean immune
            = false;


    /*
     * 1回の射撃操作で入る
     * 状態異常蓄積値。
     *
     * 速射なら速射全弾分を含む。
     */
    private BigDecimal buildupPerShot
            = BigDecimal.ZERO;


    /*
     * 初回発症に必要な蓄積値。
     */
    private Integer initialThreshold
            = 0;


    /*
     * 発症するたびに増加する
     * 必要蓄積値。
     */
    private Integer increasePerProc
            = 0;


    /*
     * 必要蓄積値の上限。
     */
    private Integer maxThreshold
            = 0;


    /*
     * 時間経過による
     * 蓄積値の減少量。
     */
    private BigDecimal decayValue
            = BigDecimal.ZERO;


    /*
     * 状態異常の効果時間。
     *
     * 毒・麻痺・睡眠などで使用。
     */
    private BigDecimal effectDurationSec
            = BigDecimal.ZERO;


    /*
     * 発症時ダメージ。
     *
     * 毒・爆破などで使用。
     *
     * ダメージを持たない状態異常ではnull。
     */
    private Integer procDamage;


    /*
     * 初回発症までに必要な射撃回数。
     *
     * 例：
     *
     * 必要蓄積250
     * 1射撃あたり80
     *
     * 250 / 80 = 3.125
     *
     * 切り上げて4回。
     *
     * 注意：
     * 現段階では時間経過による
     * decayValueは考慮しない。
     *
     * つまり
     * 「蓄積減少が発生しない前提での最短射撃回数」。
     */
    private Integer shotsToFirstProc;


    /*
     * 初回発症までに実際に投入される
     * 状態異常蓄積値。
     *
     * buildupPerShot
     * × shotsToFirstProc
     *
     * 例：
     *
     * 80 × 4
     * = 320
     */
    private BigDecimal buildupAtFirstProc
            = BigDecimal.ZERO;
}