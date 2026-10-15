package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.MonsterWeakpointResult;
import com.example.mhsunbreaksimulator.dto.WeakpointSkillModifier;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;

@Service
public class WeakpointSkillModifierService {

    /*
     * =====================================================
     * スキルID
     * =====================================================
     */

    /*
     * 弱点特効
     */
    private static final int WEAKNESS_EXPLOIT_SKILL_ID
            = 8;


    /*
     * 弱点特効【属性】
     */
    private static final int ELEMENT_EXPLOIT_SKILL_ID
            = 34;


    /*
     * =====================================================
     * 弱点系スキル補正計算
     * =====================================================
     */
    public WeakpointSkillModifier calculate(
            MonsterWeakpointResult weakpointResult,
            Map<Integer, SkillEffectState> selectedSkillEffects) {

        WeakpointSkillModifier result
                = new WeakpointSkillModifier();


        /*
         * 肉質情報がない場合。
         */
        if (weakpointResult == null) {

            return result;
        }


        /*
         * 発動スキルがない場合。
         */
        if (selectedSkillEffects == null
                || selectedSkillEffects.isEmpty()) {

            return result;
        }


        /*
         * =====================================================
         * 弱点特効
         * =====================================================
         *
         * skill_id = 8
         *
         * Lv1 → 会心率 +15
         * Lv2 → 会心率 +30
         * Lv3 → 会心率 +50
         *
         * ただし、
         * 使用する物理肉質が45以上の場合のみ適用。
         */

        SkillEffectState weaknessExploitEffect
                = selectedSkillEffects.get(
                        WEAKNESS_EXPLOIT_SKILL_ID
                );


        if (weaknessExploitEffect != null
                && isActiveEffect(
                        weaknessExploitEffect
                )
                && weakpointResult
                        .isPhysicalWeakpoint()) {

            result.setPhysicalWeakpointActive(
                    true
            );

            result.setAffinityAdd(
                    safeAdd(
                            weaknessExploitEffect
                                    .getAffinityAdd()
                    )
            );
        }


        /*
         * =====================================================
         * 弱点特効【属性】
         * =====================================================
         *
         * skill_id = 34
         *
         * Lv1 → ×1.10
         * Lv2 → ×1.125
         * Lv3 → ×1.15
         *
         * 属性肉質20以上の場合のみ適用。
         *
         * 主属性と副属性は
         * 別々に判定する。
         */

        SkillEffectState elementExploitEffect
                = selectedSkillEffects.get(
                        ELEMENT_EXPLOIT_SKILL_ID
                );


        if (elementExploitEffect != null
                && isActiveEffect(
                        elementExploitEffect
                )) {

            BigDecimal elementDamageMul
                    = safeMultiplier(
                            elementExploitEffect
                                    .getElementDamageMul()
                    );


            /*
             * 主属性
             */
            if (weakpointResult
                    .isElementWeakpoint()) {

                result.setMainElementWeakpointActive(
                        true
                );

                result.setMainElementDamageMul(
                        elementDamageMul
                );
            }


            /*
             * 副属性
             */
            if (weakpointResult
                    .isSubElementWeakpoint()) {

                result.setSubElementWeakpointActive(
                        true
                );

                result.setSubElementDamageMul(
                        elementDamageMul
                );
            }
        }


        return result;
    }


    /*
     * =====================================================
     * ACTIVE状態か
     * =====================================================
     *
     * 現在DBの弱点特効系は
     * state_code = ACTIVE。
     *
     * 万一INACTIVEなどが追加された場合にも、
     * 誤って補正を適用しないようにする。
     */
    private boolean isActiveEffect(
            SkillEffectState effect) {

        if (effect == null) {

            return false;
        }


        return "ACTIVE".equals(
                effect.getStateCode()
        );
    }


    /*
     * =====================================================
     * 加算値のnull対策
     * =====================================================
     */
    private BigDecimal safeAdd(
            BigDecimal value) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }


    /*
     * =====================================================
     * 倍率値のnull対策
     * =====================================================
     */
    private BigDecimal safeMultiplier(
            BigDecimal value) {

        return value == null
                ? BigDecimal.ONE
                : value;
    }
}