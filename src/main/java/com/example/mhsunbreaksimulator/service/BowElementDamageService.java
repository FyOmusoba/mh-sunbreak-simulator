package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowChargeModifier;
import com.example.mhsunbreaksimulator.dto.BowElementDamageResult;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;

@Service
public class BowElementDamageService {

    public BowElementDamageResult calculate(
            Integer weaponElement,
            BowChargeModifier bowChargeModifier,
            Integer maxHitCount,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal) {

        BowElementDamageResult result
                = new BowElementDamageResult();


        if (weaponElement == null
                || bowChargeModifier == null
                || maxHitCount == null
                || maxHitCount <= 0) {

            return result;
        }


        /* ========================================== */
        /* 武器の基礎属性値 */
        /* ========================================== */

        BigDecimal baseElement
                = BigDecimal.valueOf(
                        weaponElement
                );


        /* ========================================== */
        /* 条件付き属性補正 */
        /* ========================================== */

        BigDecimal elementAdd
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal.getElementAdd() == null
                                ? BigDecimal.ZERO
                                : conditionalSkillModifierTotal.getElementAdd();

        BigDecimal elementMul
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal.getElementMul() == null
                                ? BigDecimal.ONE
                                : conditionalSkillModifierTotal.getElementMul();

        BigDecimal elementDamageMul
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal.getElementDamageMul() == null
                                ? BigDecimal.ONE
                                : conditionalSkillModifierTotal.getElementDamageMul();


        /* ========================================== */
        /* 条件付き補正反映後の属性値 */
        /* ========================================== */

        BigDecimal effectiveElement
                = baseElement
                        .add(
                                elementAdd
                        )
                        .multiply(
                                elementMul
                        );


        /* ========================================== */
        /* 溜め段階の属性倍率 */
        /* ========================================== */

        BigDecimal chargeElementMul
                = bowChargeModifier.getElementMul() == null
                        ? BigDecimal.ONE
                        : bowChargeModifier.getElementMul();


        /* ========================================== */
        /* 1hitあたり属性値 */
        /* ========================================== */

        BigDecimal elementDamagePerHit
                = effectiveElement
                        .multiply(
                                chargeElementMul
                        )
                        .multiply(
                                elementDamageMul
                        );


        /* ========================================== */
        /* 最大ヒット時属性値 */
        /* ========================================== */

        BigDecimal maxElementDamage
                = elementDamagePerHit.multiply(
                        BigDecimal.valueOf(
                                maxHitCount
                        )
                );


        result.setEffectiveElement(
                effectiveElement
        );

        result.setElementDamagePerHit(
                elementDamagePerHit
        );

        result.setMaxElementDamage(
                maxElementDamage
        );


        return result;
    }
}