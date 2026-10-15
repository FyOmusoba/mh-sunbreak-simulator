package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BowgunAmmoModifier {

    /*
     * 通常の1hitあたり物理モーション値
     *
     * 通常弾・貫通弾・散弾・属性弾などで使用。
     */
    private BigDecimal physicalMotionValuePerHit
            = BigDecimal.ZERO;


    /*
     * 1hitあたり属性モーション値
     *
     * 通常属性弾・貫通属性弾などで使用。
     */
    private BigDecimal elementMotionValuePerHit
            = BigDecimal.ZERO;


    /*
     * 既存互換用の固定ダメージ値。
     */
    private BigDecimal fixedDamageValuePerHit
            = BigDecimal.ZERO;


    /*
     * 通常部分の最大ヒット数
     */
    private Integer maxHitCount
            = 0;


    /*
     * 特殊弾の着弾時物理MV。
     *
     * 例：
     * 斬裂弾
     * 徹甲榴弾
     * 拡散弾
     */
    private BigDecimal impactPhysicalMotionValue
            = BigDecimal.ZERO;


    /*
     * 特殊弾の二次攻撃部分の
     * 1hitあたり物理MV。
     *
     * 例：
     * 斬裂弾 = 14
     */
    private BigDecimal secondaryPhysicalMotionValuePerHit
            = BigDecimal.ZERO;


    /*
     * 特殊弾の二次攻撃部分の
     * 攻撃力依存属性MV。
     */
    private BigDecimal secondaryElementMotionValuePerHit
            = BigDecimal.ZERO;


    /*
     * 特殊弾の二次攻撃部分のヒット数。
     *
     * 例：
     * 斬裂弾 = 3
     * 徹甲榴弾 = 1
     * 拡散弾 = 3～5
     */
    private Integer secondaryHitCount
            = 0;


    /*
     * 二次攻撃部分が
     * 斬肉質を使用するか。
     */
    private boolean secondaryUsesSeveringHitzone
            = false;


    /*
     * 二次攻撃部分が
     * 物理肉質を無視するか。
     */
    private boolean secondaryIgnoresPhysicalHitzone
            = false;


    /*
     * 二次攻撃部分の
     * 攻撃力依存固定ダメージMV。
     *
     * 計算：
     *
     * 攻撃力 × MV ÷ 100
     *
     * 例：
     * 徹甲榴弾Lv1 = 12
     * 拡散弾Lv3 = 20
     */
    private BigDecimal secondaryFixedDamageMotionValuePerHit
            = BigDecimal.ZERO;


    /*
     * 二次攻撃部分の
     * 攻撃力非依存属性値。
     *
     * 例：
     * 徹甲榴弾Lv1 = 火3
     * 拡散弾Lv3 = 火10
     */
    private BigDecimal secondaryFixedElementValuePerHit
            = BigDecimal.ZERO;


    /*
     * さらに別の固定ダメージ成分。
     *
     * 値の異なる複数段攻撃を持つ
     * 特殊弾用。
     *
     * 竜撃弾の2段目などで使用する。
     */
    private BigDecimal additionalFixedDamageMotionValuePerHit
            = BigDecimal.ZERO;


    /*
     * additionalFixedDamageMotionValuePerHit
     * に対応する攻撃力非依存属性値。
     *
     * 竜撃弾2段目：
     * 火20
     */
    private BigDecimal additionalFixedElementValuePerHit
            = BigDecimal.ZERO;


    /*
     * 追加固定ダメージ成分のヒット数。
     *
     * 竜撃弾2段目 = 1
     */
    private Integer additionalFixedHitCount
            = 0;


    /*
     * 追加固定ダメージ部分が
     * 物理肉質を無視するか。
     *
     * 竜撃弾 = true
     */
    private boolean additionalIgnoresPhysicalHitzone
            = false;
}