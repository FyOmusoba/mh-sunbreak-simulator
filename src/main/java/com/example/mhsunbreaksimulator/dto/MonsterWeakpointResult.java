package com.example.mhsunbreaksimulator.dto;

import lombok.Data;

@Data
public class MonsterWeakpointResult {

    /*
     * 選択部位名
     */
    private String partName;


    /*
     * =====================================================
     * 物理弱点
     * =====================================================
     */


    /*
     * 使用している物理肉質種別。
     *
     * SEVER
     * BLUNT
     * SHOT
     */
    private String physicalHitzoneType;


    /*
     * 実際に参照する物理肉質値。
     */
    private Integer physicalHitzone
            = 0;


    /*
     * 弱点特効が発動する物理弱点か。
     *
     * 物理肉質45以上ならtrue。
     */
    private boolean physicalWeakpoint
            = false;


    /*
     * 弱点特効判定の基準値。
     */
    private Integer physicalWeakpointThreshold
            = 45;


    /*
     * =====================================================
     * 主属性弱点
     * =====================================================
     */


    /*
     * 主属性の肉質値。
     *
     * 五属性ではない場合は0。
     */
    private Integer elementHitzone
            = 0;


    /*
     * 主属性が
     *
     * 火
     * 水
     * 雷
     * 氷
     * 龍
     *
     * のいずれかか。
     */
    private boolean elemental
            = false;


    /*
     * 弱点特効【属性】が発動する
     * 属性有効部位か。
     *
     * 五属性であり、
     * 属性肉質20以上ならtrue。
     */
    private boolean elementWeakpoint
            = false;


    /*
     * 弱点特効【属性】判定の基準値。
     */
    private Integer elementWeakpointThreshold
            = 20;


    /*
     * =====================================================
     * 副属性弱点
     * =====================================================
     */


    /*
     * 副属性の肉質値。
     *
     * 五属性ではない場合は0。
     */
    private Integer subElementHitzone
            = 0;


    /*
     * 副属性が五属性か。
     */
    private boolean subElemental
            = false;


    /*
     * 副属性側で
     * 弱点特効【属性】の条件を満たすか。
     *
     * 副属性が五属性であり、
     * 属性肉質20以上ならtrue。
     */
    private boolean subElementWeakpoint
            = false;
}