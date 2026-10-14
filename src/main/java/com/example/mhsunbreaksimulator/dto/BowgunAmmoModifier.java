package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BowgunAmmoModifier {

    /*
     * 1hitあたりの物理モーション値
     *
     * 例：
     * 通常弾Lv1 → 16
     * 貫通弾Lv1 → 7
     */
    private BigDecimal physicalMotionValuePerHit
            = BigDecimal.ZERO;


    /*
     * 1hitあたりの属性モーション値
     *
     * 属性弾用。
     * 通常の物理弾では0。
     */
    private BigDecimal elementMotionValuePerHit
            = BigDecimal.ZERO;


    /*
     * 固定ダメージ系の計算用値
     *
     * 徹甲榴弾などで後から使用する。
     * 通常弾などでは0。
     */
    private BigDecimal fixedDamageValuePerHit
            = BigDecimal.ZERO;


    /*
     * 最大ヒット数
     *
     * 通常弾 → 1
     * 散弾 → 複数
     * 貫通弾 → 複数
     */
    private Integer maxHitCount = 0;
}