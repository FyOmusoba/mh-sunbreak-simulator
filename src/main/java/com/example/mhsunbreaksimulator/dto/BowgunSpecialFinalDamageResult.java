package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BowgunSpecialFinalDamageResult {

    /*
     * =====================================================
     * 着弾部分
     * =====================================================
     *
     * 斬裂弾
     * 徹甲榴弾
     * 拡散弾
     *
     * などの最初に当たる
     * MV1部分。
     *
     * 弾肉質を使用する。
     */

    private BigDecimal impactNormalPhysicalDamage
            = BigDecimal.ZERO;

    private BigDecimal impactPositiveCriticalPhysicalDamage
            = BigDecimal.ZERO;

    private BigDecimal impactNegativeCriticalPhysicalDamage
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * 二次物理部分
     * =====================================================
     *
     * 主に斬裂弾の炸裂部分。
     *
     * 斬肉質を使用する。
     */

    private BigDecimal secondaryNormalPhysicalDamagePerHit
            = BigDecimal.ZERO;

    private BigDecimal secondaryPositiveCriticalPhysicalDamagePerHit
            = BigDecimal.ZERO;

    private BigDecimal secondaryNegativeCriticalPhysicalDamagePerHit
            = BigDecimal.ZERO;


    private BigDecimal secondaryNormalPhysicalDamageTotal
            = BigDecimal.ZERO;

    private BigDecimal secondaryPositiveCriticalPhysicalDamageTotal
            = BigDecimal.ZERO;

    private BigDecimal secondaryNegativeCriticalPhysicalDamageTotal
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * 二次固定ダメージ
     * =====================================================
     *
     * 徹甲榴弾
     * 拡散弾
     * 竜撃弾1段目
     *
     * など。
     *
     * 物理肉質・会心を無視する。
     */

    private BigDecimal secondaryFixedDamageTotal
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * 二次火属性部分
     * =====================================================
     */

    private BigDecimal secondaryElementDamageTotal
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * 追加固定ダメージ
     * =====================================================
     *
     * 現在は主に
     * 竜撃弾2段目。
     */

    private BigDecimal additionalFixedDamageTotal
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * 追加火属性部分
     * =====================================================
     */

    private BigDecimal additionalElementDamageTotal
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * 四捨五入後
     * 物理・属性内訳
     * =====================================================
     *
     * 既存のFinalDamageResultへ接続するため、
     * 特殊弾についても
     *
     * 物理
     * 属性
     * 合計
     *
     * を分けて保持する。
     */


    /*
     * -----------------------------
     * 非会心
     * -----------------------------
     */

    private Integer normalPhysicalDamage
            = 0;

    private Integer normalElementDamage
            = 0;


    /*
     * -----------------------------
     * プラス会心
     * -----------------------------
     */

    private Integer positiveCriticalPhysicalDamage
            = 0;

    private Integer positiveCriticalElementDamage
            = 0;


    /*
     * -----------------------------
     * マイナス会心
     * -----------------------------
     */

    private Integer negativeCriticalPhysicalDamage
            = 0;

    private Integer negativeCriticalElementDamage
            = 0;


    /*
     * =====================================================
     * 四捨五入前合計
     * =====================================================
     */

    private BigDecimal normalTotalDamageBeforeRound
            = BigDecimal.ZERO;

    private BigDecimal positiveCriticalTotalDamageBeforeRound
            = BigDecimal.ZERO;

    private BigDecimal negativeCriticalTotalDamageBeforeRound
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * 最終ダメージ
     * =====================================================
     */

    private Integer normalTotalDamage
            = 0;

    private Integer positiveCriticalTotalDamage
            = 0;

    private Integer negativeCriticalTotalDamage
            = 0;
}