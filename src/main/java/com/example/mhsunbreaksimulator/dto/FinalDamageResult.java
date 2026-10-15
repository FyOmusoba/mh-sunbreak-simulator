package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FinalDamageResult {

    /*
     * =====================================================
     * 非会心
     * =====================================================
     */


    /*
     * 四捨五入前の物理ダメージ。
     */
    private BigDecimal normalPhysicalDamageBeforeRound
            = BigDecimal.ZERO;


    /*
     * 四捨五入前の属性ダメージ。
     */
    private BigDecimal normalElementDamageBeforeRound
            = BigDecimal.ZERO;


    /*
     * 四捨五入後の物理ダメージ。
     */
    private Integer normalPhysicalDamage
            = 0;


    /*
     * 四捨五入後の属性ダメージ。
     */
    private Integer normalElementDamage
            = 0;


    /*
     * 非会心時の最終ダメージ。
     *
     * roundedPhysical
     * +
     * roundedElement
     */
    private Integer normalTotalDamage
            = 0;


    /*
     * =====================================================
     * プラス会心
     * =====================================================
     */


    /*
     * 四捨五入前の
     * プラス会心物理ダメージ。
     */
    private BigDecimal positiveCriticalPhysicalDamageBeforeRound
            = BigDecimal.ZERO;


    /*
     * 四捨五入前の
     * プラス会心属性ダメージ。
     *
     * 会心撃【属性】がなければ、
     * 通常属性ダメージと同値。
     */
    private BigDecimal positiveCriticalElementDamageBeforeRound
            = BigDecimal.ZERO;


    /*
     * 四捨五入後の
     * プラス会心物理ダメージ。
     */
    private Integer positiveCriticalPhysicalDamage
            = 0;


    /*
     * 四捨五入後の
     * プラス会心属性ダメージ。
     */
    private Integer positiveCriticalElementDamage
            = 0;


    /*
     * プラス会心時の最終ダメージ。
     */
    private Integer positiveCriticalTotalDamage
            = 0;


    /*
     * =====================================================
     * マイナス会心
     * =====================================================
     */


    /*
     * 四捨五入前の
     * マイナス会心物理ダメージ。
     */
    private BigDecimal negativeCriticalPhysicalDamageBeforeRound
            = BigDecimal.ZERO;


    /*
     * 四捨五入後の
     * マイナス会心物理ダメージ。
     */
    private Integer negativeCriticalPhysicalDamage
            = 0;


    /*
     * マイナス会心では
     * 属性部分に0.75倍は掛からない。
     *
     * そのため属性部分は
     * 通常属性ダメージを使用する。
     */
    private Integer negativeCriticalElementDamage
            = 0;


    /*
     * マイナス会心時の最終ダメージ。
     */
    private Integer negativeCriticalTotalDamage
            = 0;
}