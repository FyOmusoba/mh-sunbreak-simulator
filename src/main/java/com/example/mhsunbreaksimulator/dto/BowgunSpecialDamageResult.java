package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BowgunSpecialDamageResult {

    /*
     * 条件付きスキル補正後の攻撃力
     */
    private BigDecimal effectiveAttack
            = BigDecimal.ZERO;


    /*
     * 着弾部分の物理MV。
     *
     * 斬裂弾・徹甲榴弾・拡散弾などで使用。
     *
     * この段階ではまだ肉質を掛けない。
     */
    private BigDecimal impactPhysicalMotionValue
            = BigDecimal.ZERO;


    /*
     * 二次攻撃部分の
     * 肉質計算を行う物理MV。
     *
     * 例：
     * 斬裂弾 = 14
     */
    private BigDecimal secondaryPhysicalMotionValuePerHit
            = BigDecimal.ZERO;


    /*
     * 二次攻撃部分のヒット数。
     */
    private Integer secondaryHitCount
            = 0;


    /*
     * 二次攻撃部分の
     * 肉質無視固定ダメージ。
     *
     * 攻撃力 × 固定MV ÷ 100
     *
     * を計算した値。
     *
     * 例：
     * 徹甲榴弾
     * 拡散弾
     * 竜撃弾
     */
    private BigDecimal secondaryFixedDamagePerHit
            = BigDecimal.ZERO;


    /*
     * 二次攻撃部分の
     * 固定ダメージ合計。
     *
     * secondaryFixedDamagePerHit
     * × secondaryHitCount
     */
    private BigDecimal secondaryFixedDamageTotal
            = BigDecimal.ZERO;


    /*
     * 二次攻撃部分に含まれる
     * 属性値。
     *
     * 徹甲榴弾・拡散弾・竜撃弾の
     * 火属性成分を保持する。
     *
     * ここではまだ属性肉質を掛けない。
     */
    private BigDecimal secondaryFixedElementValuePerHit
            = BigDecimal.ZERO;


    /*
     * 二次攻撃部分の属性値合計。
     *
     * secondaryFixedElementValuePerHit
     * × secondaryHitCount
     *
     * ※まだ属性肉質補正前。
     */
    private BigDecimal secondaryFixedElementValueTotal
            = BigDecimal.ZERO;


    /*
     * 値の異なる追加固定ダメージ。
     *
     * 竜撃弾2段目などで使用。
     */
    private BigDecimal additionalFixedDamagePerHit
            = BigDecimal.ZERO;


    /*
     * 追加固定ダメージ部分のヒット数。
     */
    private Integer additionalHitCount
            = 0;


    /*
     * 追加固定ダメージ合計。
     */
    private BigDecimal additionalFixedDamageTotal
            = BigDecimal.ZERO;


    /*
     * 追加攻撃部分の属性値。
     *
     * 竜撃弾2段目の火20など。
     *
     * まだ属性肉質補正前。
     */
    private BigDecimal additionalFixedElementValuePerHit
            = BigDecimal.ZERO;


    /*
     * 追加攻撃部分の属性値合計。
     *
     * まだ属性肉質補正前。
     */
    private BigDecimal additionalFixedElementValueTotal
            = BigDecimal.ZERO;


    /*
     * 二次攻撃部分が
     * 斬肉質を使用するか。
     */
    private boolean secondaryUsesSeveringHitzone
            = false;


    /*
     * 二次攻撃部分の物理値が
     * 肉質を無視するか。
     */
    private boolean secondaryIgnoresPhysicalHitzone
            = false;


    /*
     * 追加攻撃部分の物理値が
     * 肉質を無視するか。
     */
    private boolean additionalIgnoresPhysicalHitzone
            = false;
}