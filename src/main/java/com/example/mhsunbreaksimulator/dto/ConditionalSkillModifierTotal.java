package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ConditionalSkillModifierTotal {

    /*
     * =====================================================
     * 攻撃
     * =====================================================
     */

    private BigDecimal attackAdd
            = BigDecimal.ZERO;

    private BigDecimal attackMul
            = BigDecimal.ONE;


    /*
     * =====================================================
     * 属性
     * =====================================================
     */

    private BigDecimal elementAdd
            = BigDecimal.ZERO;

    private BigDecimal elementMul
            = BigDecimal.ONE;


    /*
     * =====================================================
     * 会心
     * =====================================================
     */

    private BigDecimal affinityAdd
            = BigDecimal.ZERO;

    private BigDecimal critDamageMul
            = BigDecimal.ONE;


    /*
     * =====================================================
     * ダメージ倍率
     * =====================================================
     */

    private BigDecimal physicalDamageMul
            = BigDecimal.ONE;

    private BigDecimal elementDamageMul
            = BigDecimal.ONE;

    private BigDecimal elementCritDamageMul
            = BigDecimal.ONE;

    private BigDecimal shotMul
            = BigDecimal.ONE;

    private BigDecimal phialMul
            = BigDecimal.ONE;


    /*
     * =====================================================
     * 固定・砲術・追加ダメージ
     * =====================================================
     */

    private BigDecimal fixedDamageAdd
            = BigDecimal.ZERO;

    private BigDecimal artilleryDamageMul
            = BigDecimal.ONE;

    private BigDecimal additionalDamageAttackMul
            = BigDecimal.ONE;


    /*
     * =====================================================
     * 防御力
     * =====================================================
     */

    private BigDecimal defenseAdd
            = BigDecimal.ZERO;

    private BigDecimal defenseMul
            = BigDecimal.ONE;


    /*
     * =====================================================
     * 属性耐性
     * 通常加算
     * =====================================================
     */

    private BigDecimal fireResAdd
            = BigDecimal.ZERO;

    private BigDecimal waterResAdd
            = BigDecimal.ZERO;

    private BigDecimal thunderResAdd
            = BigDecimal.ZERO;

    private BigDecimal iceResAdd
            = BigDecimal.ZERO;

    private BigDecimal dragonResAdd
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * 属性耐性
     * 変換前の固定値
     * =====================================================
     */


    /*
     * 龍気活性などによって、
     * 変換前の全属性耐性を指定値へ固定する。
     *
     * null
     * → 固定処理なし
     *
     * 龍気活性Lv1
     * → 30
     *
     * 龍気活性Lv2～5
     * → 50
     *
     *
     * 龍気変換と同時発動している場合、
     * この値は属性攻撃への変換元として使用する。
     */
    private Integer allElementResSet
            = null;


    /*
     * =====================================================
     * 龍気変換後の属性耐性固定値
     * =====================================================
     */


    /*
     * 龍気変換【朱】などによって、
     * 属性攻撃への変換処理が終わった後の
     * 表示・被ダメージ計算用耐性を固定する。
     *
     * null
     * → 変換後固定なし
     *
     * 龍気変換【朱】
     * → 0
     *
     *
     * 重要：
     *
     * allElementResSet = 50
     * postConversionAllElementResSet = 0
     *
     * が同時に存在できる。
     *
     * 例：
     * 龍気活性＋龍気変換【朱】
     *
     * 変換計算には耐性50を使用し、
     * 最終表示耐性は0にする。
     */
    private Integer postConversionAllElementResSet
            = null;


    /*
     * =====================================================
     * 龍気変換
     * 属性耐性 → 属性攻撃
     * =====================================================
     */


    /*
     * 属性耐性を属性攻撃へ変換する倍率。
     *
     * 近接
     *
     * Lv1・2
     * → 0.15
     *
     * Lv3
     * → 0.25
     *
     *
     * ライト・ヘビィ・弓
     *
     * Lv1・2
     * → 0.04
     *
     * Lv3
     * → 0.08
     *
     *
     * 龍気変換が発動していなければ0。
     */
    private BigDecimal resistanceToElementRate
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * 非龍属性無効化
     * =====================================================
     */


    /*
     * 龍気活性などによる
     * 龍属性以外の属性・状態異常無効化。
     *
     * false
     * → 無効化なし
     *
     * true
     * → 龍属性以外を無効化
     */
    private Boolean nullifyNonDragonElement
            = false;


    /*
     * =====================================================
     * 状態異常
     * 全体
     * =====================================================
     */

    private BigDecimal statusAdd
            = BigDecimal.ZERO;

    private BigDecimal statusMul
            = BigDecimal.ONE;


    /*
     * =====================================================
     * 毒
     * =====================================================
     */

    private BigDecimal poisonStatusAdd
            = BigDecimal.ZERO;

    private BigDecimal poisonStatusMul
            = BigDecimal.ONE;


    /*
     * =====================================================
     * 麻痺
     * =====================================================
     */

    private BigDecimal paralysisStatusAdd
            = BigDecimal.ZERO;

    private BigDecimal paralysisStatusMul
            = BigDecimal.ONE;


    /*
     * =====================================================
     * 睡眠
     * =====================================================
     */

    private BigDecimal sleepStatusAdd
            = BigDecimal.ZERO;

    private BigDecimal sleepStatusMul
            = BigDecimal.ONE;


    /*
     * =====================================================
     * 爆破
     * =====================================================
     */

    private BigDecimal blastStatusAdd
            = BigDecimal.ZERO;

    private BigDecimal blastStatusMul
            = BigDecimal.ONE;
}