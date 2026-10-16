package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SharpnessModifier {

    /*
     * 現在使用する斬れ味色
     *
     * 赤
     * 橙
     * 黄
     * 緑
     * 青
     * 白
     * 紫
     */
    private String sharpnessColor;


    /*
     * 物理斬れ味補正
     *
     * 例：
     *
     * 緑 = 1.05
     * 青 = 1.20
     * 白 = 1.32
     * 紫 = 1.39
     */
    private BigDecimal physicalMultiplier
            = BigDecimal.ONE;


    /*
     * 属性斬れ味補正
     *
     * 例：
     *
     * 緑 = 1.00
     * 青 = 1.0625
     * 白 = 1.15
     * 紫 = 1.25
     */
    private BigDecimal elementMultiplier
            = BigDecimal.ONE;
}