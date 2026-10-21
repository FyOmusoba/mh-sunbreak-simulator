package com.example.mhsunbreaksimulator.dto;

import lombok.Data;

@Data
public class WeaponAugmentModifier {

    /*
     * =====================================================
     * 武器傀異錬成による補正値
     * =====================================================
     */

    /*
     * 攻撃力加算
     *
     * 例：
     * Lv1 → +5
     * Lv4 → +20
     */
    private Integer attackAdd = 0;


    /*
     * 主属性・主状態異常値加算
     *
     * 属性強化は武器種によって
     * 上昇値が異なる。
     */
    private Integer elementAdd = 0;


    /*
     * 副属性・副状態異常値加算
     *
     * 双剣など、
     * 副属性を持つ武器用。
     */
    private Integer subElementAdd = 0;


    /*
     * 会心率加算
     *
     * Lv1 → +5%
     * Lv2 → +10%
     * Lv3 → +15%
     */
    private Integer affinityAdd = 0;


    /*
     * 斬れ味ゲージ加算
     *
     * Lv1 → +10
     * Lv2 → +20
     * Lv3 → +30
     */
    private Integer sharpnessAdd = 0;
}