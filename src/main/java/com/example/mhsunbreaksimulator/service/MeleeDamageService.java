package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.dto.MeleeDamageResult;
import com.example.mhsunbreaksimulator.dto.SharpnessModifier;
import com.example.mhsunbreaksimulator.entity.Weapon;
import com.example.mhsunbreaksimulator.entity.WeaponTypeAttack;

@Service
public class MeleeDamageService {

    /*
     * =====================================================
     * 近接武器
     * 肉質適用前ダメージ計算
     * =====================================================
     *
     * 現段階では、
     *
     * ・基準攻撃
     * ・物理
     * ・主属性
     * ・斬れ味
     * ・条件付きスキル補正
     *
     * を反映する。
     *
     *
     * 副属性を持つ双剣については、
     * 属性のヒット振り分け情報がDBにないため、
     * 現段階では属性計算対象外。
     *
     * 物理部分は計算可能。
     */
    public MeleeDamageResult calculate(
            Weapon weapon,
            WeaponTypeAttack weaponTypeAttack,
            SharpnessModifier sharpnessModifier,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal) {

        MeleeDamageResult result
                = new MeleeDamageResult();


        /*
         * 必須データがない場合。
         */
        if (weapon == null
                || weaponTypeAttack == null) {

            return result;
        }


        /*
         * =================================================
         * 基準攻撃情報
         * =================================================
         */

        result.setAttackName(
                weaponTypeAttack.getBaseAttackName()
        );

        result.setAttackProperty(
                weaponTypeAttack.getAttackProperty()
        );

        result.setMotionValue(
                safeDecimal(
                        weaponTypeAttack.getMotionValue(),
                        BigDecimal.ZERO
                )
        );

        result.setElementModifier(
                safeDecimal(
                        weaponTypeAttack.getElementModifier(),
                        BigDecimal.ONE
                )
        );

        result.setHitCount(
                weaponTypeAttack.getHitCount() == null
                        || weaponTypeAttack.getHitCount() <= 0
                                ? 1
                                : weaponTypeAttack.getHitCount()
        );


        /*
         * =================================================
         * 斬れ味
         * =================================================
         */

        BigDecimal physicalSharpnessMultiplier
                = sharpnessModifier == null
                        || sharpnessModifier.getPhysicalMultiplier() == null
                                ? BigDecimal.ONE
                                : sharpnessModifier.getPhysicalMultiplier();


        BigDecimal elementSharpnessMultiplier
                = sharpnessModifier == null
                        || sharpnessModifier.getElementMultiplier() == null
                                ? BigDecimal.ONE
                                : sharpnessModifier.getElementMultiplier();


        String sharpnessColor
                = sharpnessModifier == null
                        ? null
                        : sharpnessModifier.getSharpnessColor();


        result.setSharpnessColor(
                sharpnessColor
        );

        result.setPhysicalSharpnessMultiplier(
                physicalSharpnessMultiplier
        );

        result.setElementSharpnessMultiplier(
                elementSharpnessMultiplier
        );


        /*
         * =================================================
         * 条件付き物理補正
         * =================================================
         */

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


        /*
         * =================================================
         * 補正後攻撃力
         * =================================================
         */

        BigDecimal baseAttack
                = weapon.getAttack() == null
                        ? BigDecimal.ZERO
                        : BigDecimal.valueOf(
                                weapon.getAttack()
                        );


        BigDecimal effectiveAttack
                = baseAttack
                        .add(
                                attackAdd
                        )
                        .multiply(
                                attackMul
                        );


        result.setEffectiveAttack(
                effectiveAttack
        );


        /*
         * =================================================
         * 物理ダメージ
         * 肉質適用前
         * =================================================
         *
         * 補正後攻撃力
         * × モーション値 / 100
         * × 物理ダメージ倍率
         * × 斬れ味物理倍率
         */

        BigDecimal motionValueRate
                = safeDecimal(
                        weaponTypeAttack.getMotionValue(),
                        BigDecimal.ZERO
                )
                        .movePointLeft(
                                2
                        );


        BigDecimal physicalDamageBeforeHitzone
                = effectiveAttack
                        .multiply(
                                motionValueRate
                        )
                        .multiply(
                                physicalDamageMul
                        )
                        .multiply(
                                physicalSharpnessMultiplier
                        );


        result.setPhysicalDamageBeforeHitzone(
                physicalDamageBeforeHitzone
        );


        /*
         * =================================================
         * 条件付き属性補正
         * =================================================
         */

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


        /*
         * =================================================
         * 属性ダメージ
         * 肉質適用前
         * =================================================
         *
         * 属性として扱うのは
         *
         * 火
         * 水
         * 雷
         * 氷
         * 龍
         *
         * のみ。
         *
         * 毒・麻痺・睡眠・爆破・無は
         * 属性ダメージとして計算しない。
         *
         *
         * また、副属性持ち双剣については
         * 各ヒットへの属性割り当てが不明なので、
         * 現段階では属性計算を行わない。
         */

        BigDecimal effectiveElement
                = BigDecimal.ZERO;

        BigDecimal elementDamageBeforeHitzone
                = BigDecimal.ZERO;


        boolean canCalculateElement
                = isElementType(
                        weapon.getElementType()
                )
                && weapon.getSubElementType() == null;


        if (canCalculateElement) {

            BigDecimal baseElement
                    = weapon.getElementValue() == null
                            ? BigDecimal.ZERO
                            : BigDecimal.valueOf(
                                    weapon.getElementValue()
                            );


            effectiveElement
                    = baseElement
                            .add(
                                    elementAdd
                            )
                            .multiply(
                                    elementMul
                            );


            BigDecimal attackElementModifier
                    = safeDecimal(
                            weaponTypeAttack.getElementModifier(),
                            BigDecimal.ONE
                    );


            int hitCount
                    = weaponTypeAttack.getHitCount() == null
                            || weaponTypeAttack.getHitCount() <= 0
                                    ? 1
                                    : weaponTypeAttack.getHitCount();


            elementDamageBeforeHitzone
                    = effectiveElement
                            .multiply(
                                    attackElementModifier
                            )
                            .multiply(
                                    elementDamageMul
                            )
                            .multiply(
                                    elementSharpnessMultiplier
                            )
                            .multiply(
                                    BigDecimal.valueOf(
                                            hitCount
                                    )
                            );
        }


        result.setEffectiveElement(
                effectiveElement
        );

        result.setElementDamageBeforeHitzone(
                elementDamageBeforeHitzone
        );


        return result;
    }


    /*
     * =====================================================
     * 属性判定
     * =====================================================
     */
    private boolean isElementType(
            String elementType) {

        if (elementType == null) {

            return false;
        }


        return switch (elementType) {

        case "火",
             "水",
             "雷",
             "氷",
             "龍"

                -> true;


        default -> false;
        };
    }


    /*
     * =====================================================
     * null対策
     * =====================================================
     */
    private BigDecimal safeDecimal(
            BigDecimal value,
            BigDecimal defaultValue) {

        return value == null
                ? defaultValue
                : value;
    }
}