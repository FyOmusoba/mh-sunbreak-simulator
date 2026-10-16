package com.example.mhsunbreaksimulator.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class WeaponTypeAttack {

    /*
     * 武器種
     *
     * weapon_type_attack の主キー。
     *
     * 例：
     * 大剣
     * 太刀
     * 片手剣
     */
    private String weaponType;


    /*
     * 基準攻撃名
     *
     * 例：
     * 通常溜め斬り（溜めなし）
     * 踏み込み斬り
     * 斬り下ろし
     */
    private String baseAttackName;


    /*
     * モーション値
     *
     * 例：
     * 48
     * 26
     * 18
     */
    private BigDecimal motionValue;


    /*
     * 属性補正
     *
     * 通常は 1.00。
     */
    private BigDecimal elementModifier;


    /*
     * ヒット数
     *
     * 単発なら1。
     * 双剣・操虫棍など
     * 複数ヒット技では2以上。
     */
    private Integer hitCount;


    /*
     * 攻撃属性
     *
     * 斬
     * 打
     * 弾
     */
    private String attackProperty;


    /*
     * 補足説明
     */
    private String note;
}