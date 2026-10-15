package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ElementHitzoneDamageResult {

    /*
     * =====================================================
     * 肉質適用前
     * =====================================================
     */


    /*
     * 属性肉質を掛ける前の
     * 1ヒット属性ダメージ。
     *
     * この段階では小数を保持する。
     */
    private BigDecimal elementDamageBeforeHitzone
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * 属性肉質
     * =====================================================
     */


    /*
     * 使用する属性肉質値。
     *
     * 例：
     *
     * 火10
     * 雷20
     * 龍30
     */
    private Integer elementHitzone
            = 0;


    /*
     * 計算に使用する属性肉質倍率。
     *
     * 20
     * ↓
     * 0.20
     */
    private BigDecimal elementHitzoneRate
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * 弱点特効【属性】
     * =====================================================
     */


    /*
     * この属性側に適用する
     * 弱点特効【属性】倍率。
     *
     * 非発動
     * → 1.0
     *
     * Lv1
     * → 1.10
     *
     * Lv2
     * → 1.125
     *
     * Lv3
     * → 1.15
     *
     * 主属性と副属性で
     * 別々の値を渡せるようにする。
     */
    private BigDecimal weakpointElementDamageMul
            = BigDecimal.ONE;


    /*
     * =====================================================
     * 非会心
     * =====================================================
     */


    /*
     * 通常時の
     * 1ヒット属性ダメージ。
     *
     * 小数を保持する。
     */
    private BigDecimal normalElementDamagePerHit
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * 属性会心
     * =====================================================
     */


    /*
     * 会心撃【属性】による倍率。
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


    /*
     * プラス会心が発生した場合の
     * 1ヒット属性ダメージ。
     *
     * 会心撃【属性】がなければ
     * normalElementDamagePerHitと同じ。
     */
    private BigDecimal positiveCriticalElementDamagePerHit
            = BigDecimal.ZERO;
}