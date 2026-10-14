package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;

@Service
public class ConditionalSkillModifierService {

    public ConditionalSkillModifierTotal calculate(
            Map<Integer, SkillEffectState> selectedSkillEffects) {

        ConditionalSkillModifierTotal total
                = new ConditionalSkillModifierTotal();

        if (selectedSkillEffects == null
                || selectedSkillEffects.isEmpty()) {

            return total;
        }

        for (SkillEffectState effect
                : selectedSkillEffects.values()) {

            if (effect == null) {
                continue;
            }


            /* ====================================== */
            /* 加算系 */
            /* ====================================== */

            total.setAttackAdd(
                    total.getAttackAdd().add(
                            safeAdd(
                                    effect.getAttackAdd()
                            )
                    )
            );

            total.setElementAdd(
                    total.getElementAdd().add(
                            safeAdd(
                                    effect.getElementAdd()
                            )
                    )
            );

            total.setAffinityAdd(
                    total.getAffinityAdd().add(
                            safeAdd(
                                    effect.getAffinityAdd()
                            )
                    )
            );

            total.setFixedDamageAdd(
                    total.getFixedDamageAdd().add(
                            safeAdd(
                                    effect.getFixedDamageAdd()
                            )
                    )
            );


            /* ====================================== */
            /* 倍率系 */
            /* ====================================== */

            total.setAttackMul(
                    total.getAttackMul().multiply(
                            safeMultiplier(
                                    effect.getAttackMul()
                            )
                    )
            );

            total.setElementMul(
                    total.getElementMul().multiply(
                            safeMultiplier(
                                    effect.getElementMul()
                            )
                    )
            );

            total.setCritDamageMul(
                    total.getCritDamageMul().multiply(
                            safeMultiplier(
                                    effect.getCritDamageMul()
                            )
                    )
            );

            total.setPhysicalDamageMul(
                    total.getPhysicalDamageMul().multiply(
                            safeMultiplier(
                                    effect.getPhysicalDamageMul()
                            )
                    )
            );

            total.setElementDamageMul(
                    total.getElementDamageMul().multiply(
                            safeMultiplier(
                                    effect.getElementDamageMul()
                            )
                    )
            );

            total.setElementCritDamageMul(
                    total.getElementCritDamageMul().multiply(
                            safeMultiplier(
                                    effect.getElementCritDamageMul()
                            )
                    )
            );

            total.setShotMul(
                    total.getShotMul().multiply(
                            safeMultiplier(
                                    effect.getShotMul()
                            )
                    )
            );

            total.setPhialMul(
                    total.getPhialMul().multiply(
                            safeMultiplier(
                                    effect.getPhialMul()
                            )
                    )
            );

            total.setArtilleryDamageMul(
                    total.getArtilleryDamageMul().multiply(
                            safeMultiplier(
                                    effect.getArtilleryDamageMul()
                            )
                    )
            );

            total.setAdditionalDamageAttackMul(
                    total.getAdditionalDamageAttackMul().multiply(
                            safeMultiplier(
                                    effect.getAdditionalDamageAttackMul()
                            )
                    )
            );
        }

        return total;
    }


    /* ========================================== */
    /* 加算値のnull対策 */
    /* ========================================== */

    private BigDecimal safeAdd(
            BigDecimal value) {

        if (value == null) {
            return BigDecimal.ZERO;
        }

        return value;
    }


    /* ========================================== */
    /* 倍率値のnull対策 */
    /* ========================================== */

    private BigDecimal safeMultiplier(
            BigDecimal value) {

        if (value == null) {
            return BigDecimal.ONE;
        }

        return value;
    }
}