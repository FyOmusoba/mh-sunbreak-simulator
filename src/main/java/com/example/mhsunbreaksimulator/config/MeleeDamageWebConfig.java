package com.example.mhsunbreaksimulator.config;

import java.math.BigDecimal;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.dto.CriticalModifierResult;
import com.example.mhsunbreaksimulator.dto.ElementHitzoneDamageResult;
import com.example.mhsunbreaksimulator.dto.FinalDamageResult;
import com.example.mhsunbreaksimulator.dto.MeleeDamageResult;
import com.example.mhsunbreaksimulator.dto.MonsterHitzoneValueResult;
import com.example.mhsunbreaksimulator.dto.PhysicalHitzoneDamageResult;
import com.example.mhsunbreaksimulator.dto.SharpnessModifier;
import com.example.mhsunbreaksimulator.dto.WeakpointSkillModifier;
import com.example.mhsunbreaksimulator.dto.WeaponAugmentModifier;
import com.example.mhsunbreaksimulator.entity.MonsterHitzone;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;
import com.example.mhsunbreaksimulator.entity.Weapon;
import com.example.mhsunbreaksimulator.entity.WeaponTypeAttack;
import com.example.mhsunbreaksimulator.service.ElementHitzoneDamageService;
import com.example.mhsunbreaksimulator.service.FinalDamageService;
import com.example.mhsunbreaksimulator.service.MeleeDamageService;
import com.example.mhsunbreaksimulator.service.PhysicalHitzoneDamageService;
import com.example.mhsunbreaksimulator.service.SharpnessModifierService;
import com.example.mhsunbreaksimulator.service.WeaponTypeAttackService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MeleeDamageWebConfig
        implements WebMvcConfigurer {

    /*
     * =====================================================
     * スキルID
     * =====================================================
     */

    private static final int POWDER_MANTLE_SKILL_ID
            = 74;


    private final WeaponTypeAttackService weaponTypeAttackService;

    private final SharpnessModifierService sharpnessModifierService;

    private final MeleeDamageService meleeDamageService;

    private final PhysicalHitzoneDamageService physicalHitzoneDamageService;

    private final ElementHitzoneDamageService elementHitzoneDamageService;

    private final FinalDamageService finalDamageService;


    /*
     * =====================================================
     * インターセプター登録
     * =====================================================
     */
    @Override
    public void addInterceptors(
            InterceptorRegistry registry) {

        registry.addInterceptor(
                new HandlerInterceptor() {

                    /*
                     * =========================================
                     * SimulatorControllerの処理完了後に
                     * 近接ダメージを追加計算する。
                     * =========================================
                     */
                    @Override
                    public void postHandle(
                            HttpServletRequest request,
                            HttpServletResponse response,
                            Object handler,
                            ModelAndView modelAndView) {

                        applyMeleeDamage(
                                modelAndView
                        );
                    }
                }
        );
    }


    /*
     * =====================================================
     * 近接ダメージ計算
     * =====================================================
     */
    private void applyMeleeDamage(
            ModelAndView modelAndView) {

        /*
         * ModelAndViewがない場合。
         */
        if (modelAndView == null) {

            return;
        }


        /*
         * simulator画面以外では何もしない。
         */
        if (!"simulator".equals(
                modelAndView.getViewName()
        )) {

            return;
        }


        Map<String, Object> model
                = modelAndView.getModel();


        /*
         * =================================================
         * 武器
         * =================================================
         */
        Object weaponObject
                = model.get(
                        "weapon"
                );


        if (!(weaponObject
                instanceof Weapon weapon)) {

            return;
        }


        /*
         * 近接武器以外は、
         * SimulatorController側の
         * 弓・ボウガン計算をそのまま使用する。
         */
        if (!isMeleeWeapon(
                weapon.getWeaponType()
        )) {

            return;
        }


        /*
         * =================================================
         * モンスター部位
         * =================================================
         */
        Object hitzoneObject
                = model.get(
                        "selectedHitzone"
                );


        if (!(hitzoneObject
                instanceof MonsterHitzone selectedHitzone)) {

            return;
        }


        if (selectedHitzone == null) {

            return;
        }


        /*
         * =================================================
         * 条件付きスキル補正
         * =================================================
         */
        ConditionalSkillModifierTotal conditionalSkillModifierTotal
                = null;


        Object conditionalObject
                = model.get(
                        "conditionalSkillModifierTotal"
                );


        if (conditionalObject
                instanceof ConditionalSkillModifierTotal value) {

            conditionalSkillModifierTotal
                    = value;
        }


        /*
         * =================================================
         * 解決済みスキル効果
         * =================================================
         *
         * 粉塵纏は、
         * aggregate後のadditionalDamageAttackMulではなく
         * 選択されたSkillEffectStateを直接使用する。
         *
         * INACTIVE
         * → multiplier = null
         *
         * RED
         * → 0.30
         *
         * BLUE
         * → 1.25
         */
        Map<Integer, SkillEffectState> resolvedSkillEffects
                = getResolvedSkillEffects(
                        model.get(
                                "resolvedSkillEffects"
                        )
                );


        /*
         * =================================================
         * 使用肉質
         * =================================================
         */
        MonsterHitzoneValueResult monsterHitzoneValueResult
                = null;


        Object hitzoneValueObject
                = model.get(
                        "monsterHitzoneValueResult"
                );


        if (hitzoneValueObject
                instanceof MonsterHitzoneValueResult value) {

            monsterHitzoneValueResult
                    = value;
        }


        if (monsterHitzoneValueResult == null) {

            return;
        }


        /*
         * =================================================
         * 弱点スキル補正
         * =================================================
         */
        WeakpointSkillModifier weakpointSkillModifier
                = null;


        Object weakpointObject
                = model.get(
                        "weakpointSkillModifier"
                );


        if (weakpointObject
                instanceof WeakpointSkillModifier value) {

            weakpointSkillModifier
                    = value;
        }


        /*
         * =================================================
         * 会心補正
         * =================================================
         */
        CriticalModifierResult criticalModifierResult
                = null;


        Object criticalObject
                = model.get(
                        "criticalModifierResult"
                );


        if (criticalObject
                instanceof CriticalModifierResult value) {

            criticalModifierResult
                    = value;
        }


        if (criticalModifierResult == null) {

            return;
        }


        /*
         * =================================================
         * 武器種基準攻撃
         * =================================================
         */
        WeaponTypeAttack weaponTypeAttack
                = weaponTypeAttackService.findByWeaponType(
                        weapon.getWeaponType()
                );


        if (weaponTypeAttack == null) {

            return;
        }


        /*
         * =================================================
         * 武器傀異錬成反映後の近接武器
         * =================================================
         */
        Weapon damageWeapon
                = createAugmentedWeaponForDamage(
                        weapon,
                        model
                );


        /*
         * =================================================
         * 斬れ味
         * 武器傀異錬成反映
         * =================================================
         */

        WeaponAugmentModifier weaponAugmentModifier
                = null;


        Object weaponAugmentObject
                = model.get(
                        "weaponAugmentModifier"
                );


        if (weaponAugmentObject
                instanceof WeaponAugmentModifier value) {

            weaponAugmentModifier
                    = value;
        }


        Integer sharpnessAdd
                = weaponAugmentModifier == null
                        || weaponAugmentModifier.getSharpnessAdd() == null

                                ? 0
                                : weaponAugmentModifier.getSharpnessAdd();


        String augmentedSharpnessColor
                = sharpnessModifierService
                        .calculateAugmentedSharpnessColor(
                                weapon,
                                sharpnessAdd
                        );


        SharpnessModifier sharpnessModifier
                = sharpnessModifierService.calculate(
                        augmentedSharpnessColor
                );


        /*
         * =================================================
         * 近接
         * 肉質適用前ダメージ
         * =================================================
         */
        MeleeDamageResult meleeDamageResult
                = meleeDamageService.calculate(
                        damageWeapon,
                        weaponTypeAttack,
                        sharpnessModifier,
                        conditionalSkillModifierTotal
                );


        model.put(
                "meleeDamageResult",
                meleeDamageResult
        );


        /*
         * =================================================
         * 物理肉質
         * =================================================
         */
        PhysicalHitzoneDamageResult physicalHitzoneDamageResult
                = physicalHitzoneDamageService.calculate(
                        meleeDamageResult
                                .getPhysicalDamageBeforeHitzone(),

                        monsterHitzoneValueResult
                                .getPhysicalHitzone(),

                        criticalModifierResult
                );


        model.put(
                "physicalHitzoneDamageResult",
                physicalHitzoneDamageResult
        );


        /*
         * =================================================
         * 属性肉質
         * =================================================
         */
        ElementHitzoneDamageResult elementHitzoneDamageResult
                = null;


        /*
         * 五属性武器の場合のみ計算。
         *
         * 副属性持ち双剣については
         * MeleeDamageService側で
         * 属性計算対象外としている。
         */
        if (monsterHitzoneValueResult.isElemental()
                && weapon.getSubElementType() == null) {

            BigDecimal weakpointElementDamageMul
                    = BigDecimal.ONE;


            if (weakpointSkillModifier != null
                    && weakpointSkillModifier
                            .getMainElementDamageMul() != null) {

                weakpointElementDamageMul
                        = weakpointSkillModifier
                                .getMainElementDamageMul();
            }


            elementHitzoneDamageResult
                    = elementHitzoneDamageService.calculate(
                            meleeDamageResult
                                    .getElementDamageBeforeHitzone(),

                            monsterHitzoneValueResult
                                    .getElementHitzone(),

                            weakpointElementDamageMul,

                            criticalModifierResult
                    );
        }


        model.put(
                "elementHitzoneDamageResult",
                elementHitzoneDamageResult
        );


        /*
         * =================================================
         * 最終ダメージ
         * =================================================
         */
        FinalDamageResult finalDamageResult
                = finalDamageService.calculate(
                        physicalHitzoneDamageResult,
                        elementHitzoneDamageResult
                );


        /*
         * =================================================
         * 固定追加ダメージ
         * 鬼火纏など
         * =================================================
         */
        BigDecimal fixedDamageAdd
                = conditionalSkillModifierTotal == null
                        || conditionalSkillModifierTotal
                                .getFixedDamageAdd() == null

                                ? BigDecimal.ZERO
                                : conditionalSkillModifierTotal
                                        .getFixedDamageAdd();


        finalDamageResult
                = finalDamageService.addFixedDamage(
                        finalDamageResult,
                        fixedDamageAdd
                );


        /*
         * =================================================
         * 攻撃力参照固定追加ダメージ
         * 粉塵纏
         * =================================================
         */
        BigDecimal powderMantleAttackMultiplier
                = resolvePowderMantleAttackMultiplier(
                        resolvedSkillEffects
                );


        finalDamageResult
                = finalDamageService
                        .addAttackScaledFixedDamage(
                                finalDamageResult,
                                meleeDamageResult
                                        .getEffectiveAttack(),
                                powderMantleAttackMultiplier
                        );


        model.put(
                "finalDamageResult",
                finalDamageResult
        );
    }


    /*
     * =====================================================
     * 粉塵纏
     * 攻撃力参照倍率
     * =====================================================
     */
    private BigDecimal resolvePowderMantleAttackMultiplier(
            Map<Integer, SkillEffectState> resolvedSkillEffects) {

        if (resolvedSkillEffects == null
                || resolvedSkillEffects.isEmpty()) {

            return null;
        }


        SkillEffectState powderMantleEffect
                = resolvedSkillEffects.get(
                        POWDER_MANTLE_SKILL_ID
                );


        if (powderMantleEffect == null) {

            return null;
        }


        /*
         * INACTIVEではDB値がnull。
         *
         * RED
         * → 0.30
         *
         * BLUE
         * → 1.25
         */
        return powderMantleEffect
                .getAdditionalDamageAttackMul();
    }


    /*
     * =====================================================
     * resolvedSkillEffects
     * =====================================================
     */
    @SuppressWarnings("unchecked")
    private Map<Integer, SkillEffectState>
            getResolvedSkillEffects(
                    Object value) {

        if (!(value instanceof Map<?, ?> map)) {

            return Map.of();
        }


        for (Map.Entry<?, ?> entry
                : map.entrySet()) {

            if (!(entry.getKey()
                    instanceof Integer)) {

                return Map.of();
            }


            if (!(entry.getValue()
                    instanceof SkillEffectState)) {

                return Map.of();
            }
        }


        return (Map<Integer, SkillEffectState>) value;
    }


    /*
     * =====================================================
     * 武器傀異錬成反映後の近接計算用Weapon
     * =====================================================
     */
    private Weapon createAugmentedWeaponForDamage(
            Weapon weapon,
            Map<String, Object> model) {

        Weapon result
                = new Weapon();

        result.setWeaponId(
                weapon.getWeaponId()
        );

        result.setWeaponName(
                weapon.getWeaponName()
        );

        result.setWeaponType(
                weapon.getWeaponType()
        );

        result.setElementType(
                weapon.getElementType()
        );

        result.setSubElementType(
                weapon.getSubElementType()
        );

        result.setAttack(
                getIntegerModelValue(
                        model,
                        "augmentedWeaponAttack",
                        weapon.getAttack()
                )
        );

        result.setElementValue(
                getIntegerModelValue(
                        model,
                        "augmentedWeaponElementValue",
                        weapon.getElementValue()
                )
        );

        result.setSubElementValue(
                getIntegerModelValue(
                        model,
                        "augmentedWeaponSubElementValue",
                        weapon.getSubElementValue()
                )
        );

        result.setAffinity(
                getIntegerModelValue(
                        model,
                        "augmentedWeaponAffinity",
                        weapon.getAffinity()
                )
        );

        result.setMaxSharpnessColor(
                weapon.getMaxSharpnessColor()
        );

        return result;
    }


    private Integer getIntegerModelValue(
            Map<String, Object> model,
            String name,
            Integer defaultValue) {

        if (model == null
                || name == null) {

            return defaultValue;
        }


        Object value
                = model.get(
                        name
                );


        if (value instanceof Integer integerValue) {

            return integerValue;
        }


        return defaultValue;
    }


    /*
     * =====================================================
     * 近接武器判定
     * =====================================================
     */
    private boolean isMeleeWeapon(
            String weaponType) {

        if (weaponType == null) {

            return false;
        }


        return switch (weaponType) {

        case "大剣",
             "太刀",
             "片手剣",
             "双剣",
             "ハンマー",
             "狩猟笛",
             "ランス",
             "ガンランス",
             "スラッシュアックス",
             "チャージアックス",
             "操虫棍"

                -> true;


        default -> false;
        };
    }
}