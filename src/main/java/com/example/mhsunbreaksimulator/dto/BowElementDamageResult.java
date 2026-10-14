package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BowElementDamageResult {

    /*
     * 条件付き補正を反映した属性値
     */
    private BigDecimal effectiveElement
            = BigDecimal.ZERO;

    /*
     * 肉質補正前の1hit属性値
     */
    private BigDecimal elementDamagePerHit
            = BigDecimal.ZERO;

    /*
     * 肉質補正前の最大ヒット時属性値
     */
    private BigDecimal maxElementDamage
            = BigDecimal.ZERO;
}