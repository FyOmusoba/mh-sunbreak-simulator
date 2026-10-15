package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class WeakpointSkillModifier {

    /*
     * =====================================================
     * 弱点特効
     * =====================================================
     */


    /*
     * 物理弱点部位か。
     *
     * 物理肉質45以上でtrue。
     */
    private boolean physicalWeakpointActive
            = false;


    /*
     * 弱点特効による会心率加算。
     *
     * Lv1 → +15
     * Lv2 → +30
     * Lv3 → +50
     *
     * 非発動時は0。
     */
    private BigDecimal affinityAdd
            = BigDecimal.ZERO;


    /*
     * =====================================================
     * 弱点特効【属性】
     * =====================================================
     */


    /*
     * 主属性側で
     * 弱点特効【属性】が発動するか。
     */
    private boolean mainElementWeakpointActive
            = false;


    /*
     * 主属性側の属性ダメージ倍率。
     *
     * 非発動時 → 1.0
     *
     * Lv1 → 1.10
     * Lv2 → 1.125
     * Lv3 → 1.15
     */
    private BigDecimal mainElementDamageMul
            = BigDecimal.ONE;


    /*
     * 副属性側で
     * 弱点特効【属性】が発動するか。
     */
    private boolean subElementWeakpointActive
            = false;


    /*
     * 副属性側の属性ダメージ倍率。
     *
     * 非発動時 → 1.0
     */
    private BigDecimal subElementDamageMul
            = BigDecimal.ONE;
}