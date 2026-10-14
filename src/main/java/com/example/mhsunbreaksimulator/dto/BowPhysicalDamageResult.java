package com.example.mhsunbreaksimulator.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BowPhysicalDamageResult {

    /*
     * 条件付き攻撃力補正を反映した攻撃力
     */
    private BigDecimal effectiveAttack
            = BigDecimal.ZERO;

    /*
     * 肉質補正前の1hit物理値
     */
    private BigDecimal physicalDamagePerHit
            = BigDecimal.ZERO;

    /*
     * 肉質補正前の最大ヒット時物理値
     */
    private BigDecimal maxPhysicalDamage
            = BigDecimal.ZERO;
}