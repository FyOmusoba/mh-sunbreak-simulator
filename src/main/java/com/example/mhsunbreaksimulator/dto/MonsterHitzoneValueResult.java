package com.example.mhsunbreaksimulator.dto;

import lombok.Data;

@Data
public class MonsterHitzoneValueResult {

    /*
     * 選択部位名
     */
    private String partName;


    /*
     * =====================================================
     * 物理肉質
     * =====================================================
     */


    /*
     * 使用した物理肉質種別。
     *
     * SEVER
     * BLUNT
     * SHOT
     */
    private String physicalHitzoneType;


    /*
     * 実際に使用する物理肉質値。
     *
     * 例：
     *
     * ガイアデルム頭部
     *
     * 弾武器なら
     * shot = 45
     */
    private Integer physicalHitzone
            = 0;


    /*
     * =====================================================
     * 主属性
     * =====================================================
     */


    /*
     * 武器の主属性。
     *
     * 火
     * 水
     * 雷
     * 氷
     * 龍
     *
     * または
     *
     * 毒
     * 麻痺
     * 睡眠
     * 爆破
     * 無
     */
    private String elementType;


    /*
     * 主属性で使用する属性肉質。
     *
     * 火 → fire
     * 水 → water
     * 雷 → thunder
     * 氷 → ice
     * 龍 → dragon
     *
     * 毒・麻痺・睡眠・爆破・無は
     * 属性肉質を使用しないので0。
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
     * =====================================================
     * 副属性
     * =====================================================
     */


    /*
     * 武器の副属性。
     *
     * 現在DBでは
     *
     * 毒
     * 氷
     * 麻痺
     *
     * が存在する。
     */
    private String subElementType;


    /*
     * 副属性で使用する属性肉質。
     *
     * 例：
     *
     * subElementType = 氷
     * ↓
     * ice肉質
     *
     * 毒・麻痺などは0。
     */
    private Integer subElementHitzone
            = 0;


    /*
     * 副属性が
     * 五属性のどれかか。
     */
    private boolean subElemental
            = false;
}