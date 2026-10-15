package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PhysicalHitzoneDamageResult {

    /*
     * =====================================================
     * 肉質適用前
     * =====================================================
     */


    /*
     * 肉質を掛ける前の
     * 1ヒット物理ダメージ。
     *
     * この段階では小数を保持する。
     */
    private BigDecimal physicalDamageBeforeHitzone
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * 物理肉質
     * =====================================================
     */


    /*
     * 使用する物理肉質値。
     *
     * 例：
     *
     * 45
     * 70
     */
    private Integer physicalHitzone
            = 0;


    /*
     * 計算に使用する肉質倍率。
     *
     * 45
     * ↓
     * 0.45
     */
    private BigDecimal physicalHitzoneRate
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * 非会心
     * =====================================================
     */


    /*
     * 非会心時の
     * 1ヒット物理ダメージ。
     *
     * まだ整数へ丸めない。
     */
    private BigDecimal normalPhysicalDamagePerHit
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * プラス会心
     * =====================================================
     */


    /*
     * プラス会心倍率。
     *
     * 通常
     * → 1.25
     *
     * 超会心
     * → 1.30 ～ 1.40
     */
    private BigDecimal positiveCriticalDamageMul
            = BigDecimal.ONE;


    /*
     * プラス会心時の
     * 1ヒット物理ダメージ。
     *
     * まだ整数へ丸めない。
     */
    private BigDecimal positiveCriticalPhysicalDamagePerHit
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * マイナス会心
     * =====================================================
     */


    /*
     * マイナス会心倍率。
     *
     * 通常
     * → 0.75
     */
    private BigDecimal negativeCriticalDamageMul
            = BigDecimal.ONE;


    /*
     * マイナス会心時の
     * 1ヒット物理ダメージ。
     *
     * まだ整数へ丸めない。
     */
    private BigDecimal negativeCriticalPhysicalDamagePerHit
            = BigDecimal.ZERO;
}