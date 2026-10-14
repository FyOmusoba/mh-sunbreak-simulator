package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BowChargeModifier {

    /*
     * 溜め段階による物理倍率
     */
    private BigDecimal physicalMul
            = BigDecimal.ONE;

    /*
     * 溜め段階による属性倍率
     */
    private BigDecimal elementMul
            = BigDecimal.ONE;
}