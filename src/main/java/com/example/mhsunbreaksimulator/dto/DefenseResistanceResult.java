package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DefenseResistanceResult {

    /*
     * =====================================================
     * 防御力
     * =====================================================
     */

    /*
     * スキル補正前の防御力。
     *
     * 防具5部位などから算出した
     * 元の防御力を入れる。
     */
    private Integer baseDefense
            = 0;


    /*
     * スキルによる防御力加算。
     */
    private BigDecimal defenseAdd
            = BigDecimal.ZERO;


    /*
     * スキルによる防御力倍率。
     */
    private BigDecimal defenseMul
            = BigDecimal.ONE;


    /*
     * 最終整数化前の防御力。
     */
    private BigDecimal defenseBeforeRound
            = BigDecimal.ZERO;


    /*
     * 最終表示用防御力。
     */
    private Integer finalDefense
            = 0;


    /*
     * =====================================================
     * 元の属性耐性
     * =====================================================
     */

    private Integer baseFireRes
            = 0;

    private Integer baseWaterRes
            = 0;

    private Integer baseThunderRes
            = 0;

    private Integer baseIceRes
            = 0;

    private Integer baseDragonRes
            = 0;


    /*
     * =====================================================
     * 属性耐性加算
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
     * 通常加算後の属性耐性
     * =====================================================
     */

    private Integer fireResBeforeSet
            = 0;

    private Integer waterResBeforeSet
            = 0;

    private Integer thunderResBeforeSet
            = 0;

    private Integer iceResBeforeSet
            = 0;

    private Integer dragonResBeforeSet
            = 0;


    /*
     * =====================================================
     * 変換前の全耐性固定
     * =====================================================
     *
     * 龍気活性など。
     *
     * null
     * → 固定なし
     *
     * 30 / 50
     * → 全耐性をその値として扱う。
     */
    private Integer allElementResSet
            = null;


    /*
     * =====================================================
     * 龍気変換に使用する耐性
     * =====================================================
     *
     * マイナス値は0として扱う。
     *
     * 各属性の変換元耐性は
     * 最大50。
     */

    private Integer conversionFireRes
            = 0;

    private Integer conversionWaterRes
            = 0;

    private Integer conversionThunderRes
            = 0;

    private Integer conversionIceRes
            = 0;

    private Integer conversionDragonRes
            = 0;


    /*
     * 5属性の変換元耐性合計。
     *
     * 最大：
     *
     * 50 × 5
     * = 250
     */
    private Integer conversionResistanceTotal
            = 0;


    /*
     * =====================================================
     * 龍気変換レート
     * =====================================================
     */

    /*
     * 龍気変換なし
     * → 0
     *
     * 近接 Lv1・2
     * → 0.15
     *
     * 近接 Lv3
     * → 0.25
     *
     * ライト・ヘビィ・弓 Lv1・2
     * → 0.04
     *
     * ライト・ヘビィ・弓 Lv3
     * → 0.08
     */
    private BigDecimal resistanceToElementRate
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * 龍気変換による属性値加算
     * 計算直後
     * =====================================================
     *
     * 変換元耐性合計
     * ×
     * resistanceToElementRate
     *
     * の生値。
     *
     * 例：
     *
     * 250 × 0.25
     * = 62.50
     *
     * この時点では丸めない。
     */
    private BigDecimal convertedElementAddBeforeRound
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * 龍気変換による実際の属性値加算
     * =====================================================
     *
     * BigDecimalで保持する。
     *
     * 理由：
     *
     * 剣士・弓と
     * ボウガン属性弾では
     * 小数処理が異なるため。
     *
     *
     * 剣士・弓：
     * ゲーム側の特殊な小数処理を適用した値。
     *
     * ボウガン属性弾：
     * 必要に応じて小数を保持したまま
     * 後続の属性弾計算へ渡せるようにする。
     */
    private BigDecimal convertedElementAdd
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * 変換後の全耐性固定
     * =====================================================
     *
     * 龍気変換【朱】
     * → 0
     *
     * null
     * → 変換後固定なし
     */
    private Integer postConversionAllElementResSet
            = null;


    /*
     * =====================================================
     * 最終属性耐性
     * =====================================================
     */

    private Integer finalFireRes
            = 0;

    private Integer finalWaterRes
            = 0;

    private Integer finalThunderRes
            = 0;

    private Integer finalIceRes
            = 0;

    private Integer finalDragonRes
            = 0;


    /*
     * =====================================================
     * 龍気活性
     * =====================================================
     */

    /*
     * 龍属性以外の武器属性・状態異常を
     * 無効化する特殊効果。
     */
    private Boolean nullifyNonDragonElement
            = false;
}