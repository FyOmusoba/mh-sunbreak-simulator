package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ConditionalSkillModifierTotal {

    /*
     * 攻撃力加算
     */
    private BigDecimal attackAdd
            = BigDecimal.ZERO;

    /*
     * 攻撃力倍率
     */
    private BigDecimal attackMul
            = BigDecimal.ONE;

    /*
     * 属性値加算
     */
    private BigDecimal elementAdd
            = BigDecimal.ZERO;

    /*
     * 属性値倍率
     */
    private BigDecimal elementMul
            = BigDecimal.ONE;

    /*
     * 会心率加算
     */
    private BigDecimal affinityAdd
            = BigDecimal.ZERO;

    /*
     * 会心ダメージ倍率
     */
    private BigDecimal critDamageMul
            = BigDecimal.ONE;

    /*
     * 物理ダメージ倍率
     */
    private BigDecimal physicalDamageMul
            = BigDecimal.ONE;

    /*
     * 属性ダメージ倍率
     */
    private BigDecimal elementDamageMul
            = BigDecimal.ONE;

    /*
     * 属性会心ダメージ倍率
     */
    private BigDecimal elementCritDamageMul
            = BigDecimal.ONE;

    /*
     * 弾・矢ダメージ倍率
     */
    private BigDecimal shotMul
            = BigDecimal.ONE;

    /*
     * ビンダメージ倍率
     */
    private BigDecimal phialMul
            = BigDecimal.ONE;

    /*
     * 固定ダメージ加算
     */
    private BigDecimal fixedDamageAdd
            = BigDecimal.ZERO;

    /*
     * 砲撃系ダメージ倍率
     */
    private BigDecimal artilleryDamageMul
            = BigDecimal.ONE;

    /*
     * 追加ダメージ用攻撃力倍率
     */
    private BigDecimal additionalDamageAttackMul
            = BigDecimal.ONE;
}