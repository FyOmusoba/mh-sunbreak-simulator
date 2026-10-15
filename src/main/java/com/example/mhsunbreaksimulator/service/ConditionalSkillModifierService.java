package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;

@Service
public class ConditionalSkillModifierService {

    /*
     * =====================================================
     * 特殊スキルID
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
     * 龍気変換
     *
     * skill_id = 45
     *
     * 疾替えの書【朱】では
     *
     * all_element_res_set = 0
     *
     * だが、
     *
     * これは
     *
     * 「変換元耐性を0にする」
     *
     * という意味ではなく、
     *
     * 属性攻撃への変換後に
     * 表示・被ダメージ用耐性を0へ固定する
     *
     * という扱いにする。
     */
    private static final int DRAGON_CONVERSION_SKILL_ID
            = 45;


    /*
     * =====================================================
     * 条件付きスキル補正集計
     * =====================================================
     */
    public ConditionalSkillModifierTotal calculate(
            Map<Integer, SkillEffectState> selectedSkillEffects) {

        ConditionalSkillModifierTotal total
                = new ConditionalSkillModifierTotal();


        /*
         * null / empty
         */
        if (selectedSkillEffects == null
                || selectedSkillEffects.isEmpty()) {

            return total;
        }


        /*
         * skillIdも必要なので
         * entrySet()で処理する。
         */
        for (Map.Entry<Integer, SkillEffectState> entry
                : selectedSkillEffects.entrySet()) {

            Integer skillId
                    = entry.getKey();

            SkillEffectState effect
                    = entry.getValue();


            if (effect == null) {

                continue;
            }


            /*
             * =================================================
             * 弱点系スキル
             * =================================================
             *
             * 弱点特効
             * 弱点特効【属性】
             *
             * は肉質から自動判定するため、
             *
             * WeakpointSkillModifierService
             *
             * 側で処理する。
             */
            if (isWeakpointAutoSkill(
                    skillId
            )) {

                continue;
            }


            /*
             * =================================================
             * 攻撃
             * =================================================
             */

            total.setAttackAdd(
                    total.getAttackAdd().add(
                            safeAdd(
                                    effect.getAttackAdd()
                            )
                    )
            );


            total.setAttackMul(
                    total.getAttackMul().multiply(
                            safeMultiplier(
                                    effect.getAttackMul()
                            )
                    )
            );


            /*
             * =================================================
             * 属性
             * =================================================
             */

            total.setElementAdd(
                    total.getElementAdd().add(
                            safeAdd(
                                    effect.getElementAdd()
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


            /*
             * =================================================
             * 会心
             * =================================================
             */

            total.setAffinityAdd(
                    total.getAffinityAdd().add(
                            safeAdd(
                                    effect.getAffinityAdd()
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


            /*
             * =================================================
             * ダメージ倍率
             * =================================================
             */

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


            /*
             * =================================================
             * 固定・砲術・追加ダメージ
             * =================================================
             */

            total.setFixedDamageAdd(
                    total.getFixedDamageAdd().add(
                            safeAdd(
                                    effect.getFixedDamageAdd()
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


            /*
             * =================================================
             * 防御力
             * =================================================
             */

            total.setDefenseAdd(
                    total.getDefenseAdd().add(
                            safeAdd(
                                    effect.getDefenseAdd()
                            )
                    )
            );


            total.setDefenseMul(
                    total.getDefenseMul().multiply(
                            safeMultiplier(
                                    effect.getDefenseMul()
                            )
                    )
            );


            /*
             * =================================================
             * 通常属性耐性加算
             * =================================================
             *
             * 防御
             * 激昂
             * 業鎧【修羅】
             * 龍気変換
             *
             * などの加算値。
             */

            total.setFireResAdd(
                    total.getFireResAdd().add(
                            safeAdd(
                                    effect.getFireResAdd()
                            )
                    )
            );


            total.setWaterResAdd(
                    total.getWaterResAdd().add(
                            safeAdd(
                                    effect.getWaterResAdd()
                            )
                    )
            );


            total.setThunderResAdd(
                    total.getThunderResAdd().add(
                            safeAdd(
                                    effect.getThunderResAdd()
                            )
                    )
            );


            total.setIceResAdd(
                    total.getIceResAdd().add(
                            safeAdd(
                                    effect.getIceResAdd()
                            )
                    )
            );


            total.setDragonResAdd(
                    total.getDragonResAdd().add(
                            safeAdd(
                                    effect.getDragonResAdd()
                            )
                    )
            );


            /*
             * =================================================
             * 属性耐性の特殊処理
             * =================================================
             *
             * 龍気活性
             * 龍気変換
             *
             * など。
             */
            aggregateSpecialResistanceModifier(
                    total,
                    skillId,
                    effect
            );


            /*
             * =================================================
             * 状態異常
             * =================================================
             */
            aggregateStatusModifier(
                    total,
                    effect
            );
        }


        return total;
    }


    /*
     * =====================================================
     * 弱点系自動判定スキル
     * =====================================================
     */
    private boolean isWeakpointAutoSkill(
            Integer skillId) {

        if (skillId == null) {

            return false;
        }


        return skillId
                == WEAKNESS_EXPLOIT_SKILL_ID
                || skillId
                == ELEMENT_EXPLOIT_SKILL_ID;
    }


    /*
     * =====================================================
     * 属性耐性特殊補正
     * =====================================================
     */
    private void aggregateSpecialResistanceModifier(
            ConditionalSkillModifierTotal total,
            Integer skillId,
            SkillEffectState effect) {

        if (total == null
                || effect == null) {

            return;
        }


        /*
         * =================================================
         * 全属性耐性固定
         * =================================================
         *
         * 龍気活性など：
         *
         * allElementResSet
         * =
         * 30 / 50
         *
         *
         * 龍気変換【朱】：
         *
         * allElementResSet
         * =
         * 0
         *
         *
         * この2つは意味が異なるため、
         * 別フィールドへ分ける。
         */
        Integer allElementResSet
                = effect.getAllElementResSet();


        if (allElementResSet != null) {

            /*
             * =============================================
             * 龍気変換【朱】
             * =============================================
             *
             * skill_id = 45
             * state_code = RED
             *
             * の all_element_res_set = 0 は、
             *
             * 属性変換後の最終耐性0固定
             *
             * として保持する。
             */
            if (isDragonConversionRed(
                    skillId,
                    effect
            )) {

                total.setPostConversionAllElementResSet(
                        allElementResSet
                );
            }

            /*
             * =============================================
             * その他
             * =============================================
             *
             * 龍気活性など。
             *
             * 変換前の耐性固定値として保持する。
             */
            else {

                total.setAllElementResSet(
                        allElementResSet
                );
            }
        }


        /*
         * =================================================
         * 耐性 → 属性攻撃変換率
         * =================================================
         *
         * 龍気変換
         *
         * 近接
         * Lv1・2 = 0.15
         * Lv3   = 0.25
         *
         * ライト / ヘビィ / 弓
         * Lv1・2 = 0.04
         * Lv3   = 0.08
         *
         *
         * これは倍率同士を掛けない。
         *
         * 1つの選択済み効果が持つ
         * 変換率そのものを保持する。
         */
        BigDecimal resistanceToElementRate
                = effect.getResistanceToElementRate();


        if (resistanceToElementRate != null) {

            total.setResistanceToElementRate(
                    resistanceToElementRate
            );
        }


        /*
         * =================================================
         * 非龍属性無効化
         * =================================================
         *
         * 龍気活性ACTIVEなど。
         *
         * trueが1つでもあればtrue。
         */
        if (Boolean.TRUE.equals(
                effect.getNullifyNonDragonElement()
        )) {

            total.setNullifyNonDragonElement(
                    true
            );
        }
    }


    /*
     * =====================================================
     * 龍気変換【朱】か
     * =====================================================
     */
    private boolean isDragonConversionRed(
            Integer skillId,
            SkillEffectState effect) {

        if (skillId == null
                || effect == null) {

            return false;
        }


        if (skillId
                != DRAGON_CONVERSION_SKILL_ID) {

            return false;
        }


        return "RED".equals(
                effect.getStateCode()
        );
    }


    /*
     * =====================================================
     * 状態異常補正
     * =====================================================
     */
    private void aggregateStatusModifier(
            ConditionalSkillModifierTotal total,
            SkillEffectState effect) {

        if (total == null
                || effect == null) {

            return;
        }


        String statusTarget
                = effect.getStatusTarget();


        /*
         * statusTargetなしなら
         * 状態異常補正として扱わない。
         */
        if (statusTarget == null
                || statusTarget.isBlank()) {

            return;
        }


        switch (statusTarget) {

        /*
         * =========================================
         * 全状態異常
         * =========================================
         */
        case "ALL":

            total.setStatusAdd(
                    total.getStatusAdd().add(
                            safeAdd(
                                    effect.getStatusAdd()
                            )
                    )
            );

            total.setStatusMul(
                    total.getStatusMul().multiply(
                            safeMultiplier(
                                    effect.getStatusMul()
                            )
                    )
            );

            break;


        /*
         * =========================================
         * 毒
         * =========================================
         */
        case "POISON":

            total.setPoisonStatusAdd(
                    total.getPoisonStatusAdd().add(
                            safeAdd(
                                    effect.getStatusAdd()
                            )
                    )
            );

            total.setPoisonStatusMul(
                    total.getPoisonStatusMul().multiply(
                            safeMultiplier(
                                    effect.getStatusMul()
                            )
                    )
            );

            break;


        /*
         * =========================================
         * 麻痺
         * =========================================
         */
        case "PARALYSIS":

            total.setParalysisStatusAdd(
                    total.getParalysisStatusAdd().add(
                            safeAdd(
                                    effect.getStatusAdd()
                            )
                    )
            );

            total.setParalysisStatusMul(
                    total.getParalysisStatusMul().multiply(
                            safeMultiplier(
                                    effect.getStatusMul()
                            )
                    )
            );

            break;


        /*
         * =========================================
         * 睡眠
         * =========================================
         */
        case "SLEEP":

            total.setSleepStatusAdd(
                    total.getSleepStatusAdd().add(
                            safeAdd(
                                    effect.getStatusAdd()
                            )
                    )
            );

            total.setSleepStatusMul(
                    total.getSleepStatusMul().multiply(
                            safeMultiplier(
                                    effect.getStatusMul()
                            )
                    )
            );

            break;


        /*
         * =========================================
         * 爆破
         * =========================================
         */
        case "BLAST":

            total.setBlastStatusAdd(
                    total.getBlastStatusAdd().add(
                            safeAdd(
                                    effect.getStatusAdd()
                            )
                    )
            );

            total.setBlastStatusMul(
                    total.getBlastStatusMul().multiply(
                            safeMultiplier(
                                    effect.getStatusMul()
                            )
                    )
            );

            break;


        /*
         * =========================================
         * 未知
         * =========================================
         */
        default:

            break;
        }
    }


    /*
     * =====================================================
     * 加算値null対策
     * =====================================================
     */
    private BigDecimal safeAdd(
            BigDecimal value) {

        if (value == null) {

            return BigDecimal.ZERO;
        }


        return value;
    }


    /*
     * =====================================================
     * 倍率null対策
     * =====================================================
     */
    private BigDecimal safeMultiplier(
            BigDecimal value) {

        if (value == null) {

            return BigDecimal.ONE;
        }


        return value;
    }
}