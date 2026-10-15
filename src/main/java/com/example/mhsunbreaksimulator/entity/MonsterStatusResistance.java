package com.example.mhsunbreaksimulator.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MonsterStatusResistance {

    /*
     * monster_status_resistance
     * 主キー
     */
    private Integer statusResistanceId;


    /*
     * 対象モンスター
     */
    private Integer monsterId;


    /*
     * 状態異常種別
     *
     * DB上では
     *
     * 毒
     * 麻痺
     * 睡眠
     * 爆破
     *
     * を使用する。
     */
    private String statusType;


    /*
     * 初回発症に必要な蓄積値
     */
    private Integer initialThreshold;


    /*
     * 発症するたびに増加する耐性値
     */
    private Integer increasePerProc;


    /*
     * 発症耐性の上限値
     */
    private Integer maxThreshold;


    /*
     * 時間経過による
     * 蓄積値の減少量
     */
    private BigDecimal decayValue;


    /*
     * 状態異常の効果時間。
     *
     * 毒・麻痺・睡眠などで使用。
     *
     * 爆破では0の場合がある。
     */
    private BigDecimal effectDurationSec;


    /*
     * 状態異常発症時のダメージ。
     *
     * 主に
     *
     * 毒
     * 爆破
     *
     * で使用。
     *
     * 麻痺・睡眠ではnull。
     */
    private Integer procDamage;


    /*
     * 完全耐性かどうか。
     *
     * trueなら
     * その状態異常は発症しない。
     */
    private Boolean isImmune;
}