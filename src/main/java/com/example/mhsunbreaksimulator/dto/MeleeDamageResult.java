package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MeleeDamageResult {

    /*
     * =====================================================
     * 基準攻撃情報
     * =====================================================
     */


    /*
     * 基準攻撃名
     *
     * 例：
     * 通常溜め斬り（溜めなし）
     * 踏み込み斬り
     * 二段斬り
     */
    private String attackName;


    /*
     * 攻撃属性
     *
     * 斬
     * 打
     * 弾
     */
    private String attackProperty;


    /*
     * モーション値
     *
     * 複数ヒット技の場合は、
     * weapon_type_attack に登録されている
     * 合計モーション値。
     *
     * 例：
     * 双剣 二段斬り
     * 10 + 8 = 18
     */
    private BigDecimal motionValue
            = BigDecimal.ZERO;


    /*
     * 属性補正
     */
    private BigDecimal elementModifier
            = BigDecimal.ONE;


    /*
     * ヒット数
     */
    private Integer hitCount
            = 1;


    /*
     * =====================================================
     * 補正後ステータス
     * =====================================================
     */


    /*
     * 条件付きスキル補正反映後の攻撃力
     *
     * (武器攻撃力 + attackAdd)
     * × attackMul
     */
    private BigDecimal effectiveAttack
            = BigDecimal.ZERO;


    /*
     * 条件付きスキル補正反映後の属性値
     *
     * (武器属性値 + elementAdd)
     * × elementMul
     */
    private BigDecimal effectiveElement
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * 斬れ味
     * =====================================================
     */


    /*
     * 使用斬れ味色
     */
    private String sharpnessColor;


    /*
     * 物理斬れ味倍率
     */
    private BigDecimal physicalSharpnessMultiplier
            = BigDecimal.ONE;


    /*
     * 属性斬れ味倍率
     */
    private BigDecimal elementSharpnessMultiplier
            = BigDecimal.ONE;


    /*
     * =====================================================
     * 肉質適用前ダメージ
     * =====================================================
     */


    /*
     * 基準攻撃1回ぶんの物理値。
     *
     * 肉質・会心適用前。
     */
    private BigDecimal physicalDamageBeforeHitzone
            = BigDecimal.ZERO;


    /*
     * 基準攻撃1回ぶんの属性値。
     *
     * 複数ヒット技の場合は
     * hitCount を反映した合計値。
     *
     * 肉質・属性会心適用前。
     */
    private BigDecimal elementDamageBeforeHitzone
            = BigDecimal.ZERO;
}