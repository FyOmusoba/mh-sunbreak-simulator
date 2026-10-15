package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CriticalModifierResult {

    /*
     * =====================================================
     * 会心率
     * =====================================================
     */


    /*
     * 武器自身の会心率。
     *
     * 例：
     * 10
     * -20
     */
    private BigDecimal weaponAffinity
            = BigDecimal.ZERO;


    /*
     * 見切り・挑戦者など、
     * 通常のスキルから得た会心率。
     *
     * 弱点特効はここには含めない。
     */
    private BigDecimal conditionalAffinityAdd
            = BigDecimal.ZERO;


    /*
     * 弱点特効による会心率。
     *
     * 弱点でなければ0。
     */
    private BigDecimal weakpointAffinityAdd
            = BigDecimal.ZERO;


    /*
     * 上限・下限補正前の会心率。
     *
     * weaponAffinity
     * +
     * conditionalAffinityAdd
     * +
     * weakpointAffinityAdd
     */
    private BigDecimal rawAffinity
            = BigDecimal.ZERO;


    /*
     * 最終会心率。
     *
     * -100 ～ 100
     * の範囲に制限する。
     */
    private BigDecimal finalAffinity
            = BigDecimal.ZERO;


    /*
     * プラス会心の発生率。
     *
     * finalAffinityが正の場合のみ値を持つ。
     */
    private BigDecimal positiveCriticalRate
            = BigDecimal.ZERO;


    /*
     * マイナス会心の発生率。
     *
     * finalAffinityが負の場合、
     * 絶対値を保持する。
     *
     * 例：
     *
     * finalAffinity = -30
     * ↓
     * negativeCriticalRate = 30
     */
    private BigDecimal negativeCriticalRate
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * 物理会心倍率
     * =====================================================
     */


    /*
     * プラス会心時の物理倍率。
     *
     * 通常
     * → 1.25
     *
     * 超会心Lv1
     * → 1.30
     *
     * 超会心Lv2
     * → 1.35
     *
     * 超会心Lv3
     * → 1.40
     */
    private BigDecimal positiveCriticalDamageMul
            = new BigDecimal("1.25");


    /*
     * マイナス会心時の物理倍率。
     *
     * 通常は0.75。
     */
    private BigDecimal negativeCriticalDamageMul
            = new BigDecimal("0.75");


    /*
     * =====================================================
     * 属性会心倍率
     * =====================================================
     */


    /*
     * 属性の通常時倍率。
     *
     * 通常会心だけでは
     * 属性ダメージは増加しないので1.0。
     */
    private BigDecimal normalElementCriticalDamageMul
            = BigDecimal.ONE;


    /*
     * 会心撃【属性】が発動している場合の
     * 属性会心倍率。
     *
     * 未発動
     * → 1.0
     *
     * Lv1
     * → 1.05
     *
     * Lv2
     * → 1.10
     *
     * Lv3
     * → 1.15
     */
    private BigDecimal positiveElementCriticalDamageMul
            = BigDecimal.ONE;
}