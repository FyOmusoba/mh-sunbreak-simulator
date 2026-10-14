package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowPhysicalDamageResult;
import com.example.mhsunbreaksimulator.dto.BowPhysicalModifier;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;

@Service
public class BowPhysicalDamageService {

    public BowPhysicalDamageResult calculate(
            Integer weaponAttack,
            BowPhysicalModifier bowPhysicalModifier,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal) {

        BowPhysicalDamageResult result
                = new BowPhysicalDamageResult();


        if (weaponAttack == null
                || bowPhysicalModifier == null) {

            return result;
        }


        /* ========================================== */
        /* 武器攻撃力 */
        /* ========================================== */

        BigDecimal baseAttack
                = BigDecimal.valueOf(
                        weaponAttack
                );


        /* ========================================== */
        /* 条件付き攻撃力補正 */
        /* ========================================== */

        BigDecimal attackAdd
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal.getAttackAdd() == null
                                ? BigDecimal.ZERO
                                : conditionalSkillModifierTotal.getAttackAdd();

        BigDecimal attackMul
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal.getAttackMul() == null
                                ? BigDecimal.ONE
                                : conditionalSkillModifierTotal.getAttackMul();

        BigDecimal physicalDamageMul
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal.getPhysicalDamageMul() == null
                                ? BigDecimal.ONE
                                : conditionalSkillModifierTotal.getPhysicalDamageMul();


        /* ========================================== */
        /* 条件付き補正反映後の攻撃力 */
        /* ========================================== */

        BigDecimal effectiveAttack
                = baseAttack
                        .add(
                                attackAdd
                        )
                        .multiply(
                                attackMul
                        );


        /* ========================================== */
        /* 1hitあたり物理値 */
        /* ========================================== */

        BigDecimal physicalDamagePerHit
                = effectiveAttack
                        .multiply(
                                bowPhysicalModifier.getPhysicalRatePerHit()
                        )
                        .multiply(
                                physicalDamageMul
                        );


        /* ========================================== */
        /* 最大ヒット時物理値 */
        /* ========================================== */

        BigDecimal maxPhysicalDamage
                = effectiveAttack
                        .multiply(
                                bowPhysicalModifier.getMaxPhysicalRate()
                        )
                        .multiply(
                                physicalDamageMul
                        );


        result.setEffectiveAttack(
                effectiveAttack
        );

        result.setPhysicalDamagePerHit(
                physicalDamagePerHit
        );

        result.setMaxPhysicalDamage(
                maxPhysicalDamage
        );


        return result;
    }
}