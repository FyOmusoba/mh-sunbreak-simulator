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

import com.example.mhsunbreaksimulator.dto.BowPhysicalDamageResult;
import com.example.mhsunbreaksimulator.dto.BowPhysicalModifier;
import com.example.mhsunbreaksimulator.dto.BowgunAmmoModifier;
import com.example.mhsunbreaksimulator.dto.BowgunElementDamageResult;
import com.example.mhsunbreaksimulator.dto.BowgunPhysicalDamageResult;
import com.example.mhsunbreaksimulator.dto.BowgunRapidFireModifier;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.dto.CriticalModifierResult;
import com.example.mhsunbreaksimulator.dto.ElementHitzoneDamageResult;
import com.example.mhsunbreaksimulator.dto.FinalDamageResult;
import com.example.mhsunbreaksimulator.dto.MonsterHitzoneValueResult;
import com.example.mhsunbreaksimulator.dto.PhysicalHitzoneDamageResult;
import com.example.mhsunbreaksimulator.dto.ShotSkillModifier;
import com.example.mhsunbreaksimulator.dto.WeakpointSkillModifier;
import com.example.mhsunbreaksimulator.entity.BowCharge;
import com.example.mhsunbreaksimulator.entity.BowgunAmmo;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;
import com.example.mhsunbreaksimulator.entity.Weapon;
import com.example.mhsunbreaksimulator.service.BowPhysicalDamageService;
import com.example.mhsunbreaksimulator.service.BowgunElementDamageService;
import com.example.mhsunbreaksimulator.service.BowgunPhysicalDamageService;
import com.example.mhsunbreaksimulator.service.ElementHitzoneDamageService;
import com.example.mhsunbreaksimulator.service.FinalDamageService;
import com.example.mhsunbreaksimulator.service.PhysicalHitzoneDamageService;
import com.example.mhsunbreaksimulator.service.ShotSkillModifierService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ShotDamageWebConfig
        implements WebMvcConfigurer {

    private final ShotSkillModifierService shotSkillModifierService;

    private final BowPhysicalDamageService bowPhysicalDamageService;

    private final BowgunPhysicalDamageService bowgunPhysicalDamageService;

    private final BowgunElementDamageService bowgunElementDamageService;

    private final PhysicalHitzoneDamageService physicalHitzoneDamageService;

    private final ElementHitzoneDamageService elementHitzoneDamageService;

    private final FinalDamageService finalDamageService;


    @Override
    public void addInterceptors(
            InterceptorRegistry registry) {

        registry.addInterceptor(
                new HandlerInterceptor() {

                    @Override
                    public void postHandle(
                            HttpServletRequest request,
                            HttpServletResponse response,
                            Object handler,
                            ModelAndView modelAndView) {

                        applyShotSkillDamage(
                                modelAndView
                        );
                    }
                }
        );
    }


    /*
     * =====================================================
     * 射撃系スキル補正
     * =====================================================
     */
    private void applyShotSkillDamage(
            ModelAndView modelAndView) {

        if (modelAndView == null) {

            return;
        }


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
         * 弓・ライト・ヘビィ以外は対象外
         */
        if (!isShotWeapon(
                weapon.getWeaponType()
        )) {

            return;
        }


        /*
         * =================================================
         * 解決済みスキル効果
         * =================================================
         */
        Map<Integer, SkillEffectState> resolvedSkillEffects
                = getResolvedSkillEffects(
                        model.get(
                                "resolvedSkillEffects"
                        )
                );


        /*
         * =================================================
         * 条件付きスキル補正合計
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
         * 弓
         * =================================================
         */
        if ("弓".equals(
                weapon.getWeaponType()
        )) {

            applyBow(
                    model,
                    weapon,
                    resolvedSkillEffects,
                    conditionalSkillModifierTotal
            );
        }


        /*
         * =================================================
         * ライト / ヘビィ
         * =================================================
         */
        else {

            applyBowgun(
                    model,
                    weapon,
                    resolvedSkillEffects,
                    conditionalSkillModifierTotal
            );
        }


        /*
         * =================================================
         * 肉質・最終ダメージを再計算
         * =================================================
         */
        recalculateFinalDamage(
                model,
                weapon
        );
    }


    /*
     * =====================================================
     * 弓
     * =====================================================
     */
    private void applyBow(
            Map<String, Object> model,
            Weapon weapon,
            Map<Integer, SkillEffectState> resolvedSkillEffects,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal) {

        Object bowChargeObject
                = model.get(
                        "selectedBowCharge"
                );


        if (!(bowChargeObject
                instanceof BowCharge selectedBowCharge)) {

            return;
        }


        Object bowPhysicalModifierObject
                = model.get(
                        "bowPhysicalModifier"
                );


        if (!(bowPhysicalModifierObject
                instanceof BowPhysicalModifier bowPhysicalModifier)) {

            return;
        }


        /*
         * 現在の矢種に対応したスキル倍率
         */
        ShotSkillModifier shotSkillModifier
                = shotSkillModifierService.calculateBow(
                        selectedBowCharge,
                        resolvedSkillEffects
                );


        model.put(
                "shotSkillModifier",
                shotSkillModifier
        );


        /*
         * 弓物理を再計算
         */
        BowPhysicalDamageResult bowPhysicalDamageResult
                = bowPhysicalDamageService.calculate(
                        weapon.getAttack(),
                        bowPhysicalModifier,
                        conditionalSkillModifierTotal,
                        shotSkillModifier
                );


        model.put(
                "bowPhysicalDamageResult",
                bowPhysicalDamageResult
        );
    }


    /*
     * =====================================================
     * ライト / ヘビィ
     * =====================================================
     */
    private void applyBowgun(
            Map<String, Object> model,
            Weapon weapon,
            Map<Integer, SkillEffectState> resolvedSkillEffects,
            ConditionalSkillModifierTotal conditionalSkillModifierTotal) {

        Object bowgunAmmoObject
                = model.get(
                        "selectedBowgunAmmo"
                );


        if (!(bowgunAmmoObject
                instanceof BowgunAmmo selectedBowgunAmmo)) {

            return;
        }


        Object ammoModifierObject
                = model.get(
                        "bowgunAmmoModifier"
                );


        if (!(ammoModifierObject
                instanceof BowgunAmmoModifier bowgunAmmoModifier)) {

            return;
        }


        BowgunRapidFireModifier rapidFireModifier
                = null;


        Object rapidFireObject
                = model.get(
                        "bowgunRapidFireModifier"
                );


        if (rapidFireObject
                instanceof BowgunRapidFireModifier value) {

            rapidFireModifier
                    = value;
        }


        /*
         * 現在の弾種・速射状態に対応した倍率
         */
        ShotSkillModifier shotSkillModifier
                = shotSkillModifierService.calculateBowgun(
                        selectedBowgunAmmo,
                        resolvedSkillEffects
                );


        model.put(
                "shotSkillModifier",
                shotSkillModifier
        );


        /*
         * =================================================
         * 物理
         * =================================================
         */
        BowgunPhysicalDamageResult bowgunPhysicalDamageResult
                = bowgunPhysicalDamageService.calculate(
                        weapon.getAttack(),
                        bowgunAmmoModifier,
                        conditionalSkillModifierTotal,
                        rapidFireModifier,
                        shotSkillModifier
                );


        model.put(
                "bowgunPhysicalDamageResult",
                bowgunPhysicalDamageResult
        );


        /*
         * =================================================
         * 属性
         * =================================================
         */
        BowgunElementDamageResult bowgunElementDamageResult
                = bowgunElementDamageService.calculate(
                        weapon.getAttack(),
                        bowgunAmmoModifier,
                        conditionalSkillModifierTotal,
                        rapidFireModifier,
                        shotSkillModifier
                );


        model.put(
                "bowgunElementDamageResult",
                bowgunElementDamageResult
        );
    }


    /*
     * =====================================================
     * 肉質適用後・最終ダメージ
     * =====================================================
     */
    private void recalculateFinalDamage(
            Map<String, Object> model,
            Weapon weapon) {

        MonsterHitzoneValueResult monsterHitzoneValueResult
                = null;


        Object hitzoneObject
                = model.get(
                        "monsterHitzoneValueResult"
                );


        if (hitzoneObject
                instanceof MonsterHitzoneValueResult value) {

            monsterHitzoneValueResult
                    = value;
        }


        /*
         * 部位未選択なら、
         * 肉質後計算はしない。
         */
        if (monsterHitzoneValueResult == null) {

            return;
        }


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
         * 肉質前物理
         * =================================================
         */
        BigDecimal physicalDamageBeforeHitzone
                = null;


        if ("弓".equals(
                weapon.getWeaponType()
        )) {

            Object resultObject
                    = model.get(
                            "bowPhysicalDamageResult"
                    );


            if (resultObject
                    instanceof BowPhysicalDamageResult result) {

                physicalDamageBeforeHitzone
                        = result.getPhysicalDamagePerHit();
            }
        }


        else if ("ライトボウガン".equals(
                weapon.getWeaponType()
        )
                || "ヘビィボウガン".equals(
                        weapon.getWeaponType()
                )) {

            Object resultObject
                    = model.get(
                            "bowgunPhysicalDamageResult"
                    );


            if (resultObject
                    instanceof BowgunPhysicalDamageResult result) {

                physicalDamageBeforeHitzone
                        = result.getPhysicalDamagePerHit();
            }
        }


        /*
         * =================================================
         * 物理肉質
         * =================================================
         */
        PhysicalHitzoneDamageResult physicalHitzoneDamageResult
                = null;


        if (physicalDamageBeforeHitzone != null) {

            physicalHitzoneDamageResult
                    = physicalHitzoneDamageService.calculate(
                            physicalDamageBeforeHitzone,
                            monsterHitzoneValueResult
                                    .getPhysicalHitzone(),
                            criticalModifierResult
                    );
        }


        model.put(
                "physicalHitzoneDamageResult",
                physicalHitzoneDamageResult
        );


        /*
         * =================================================
         * 肉質前属性
         * =================================================
         */
        BigDecimal elementDamageBeforeHitzone
                = null;


        /*
         * 弓属性は今回変更していないため、
         * Controllerで計算済みの値をそのまま使用。
         */
        if ("弓".equals(
                weapon.getWeaponType()
        )) {

            Object resultObject
                    = model.get(
                            "bowElementDamageResult"
                    );


            if (resultObject
                    instanceof com.example.mhsunbreaksimulator.dto.BowElementDamageResult result) {

                elementDamageBeforeHitzone
                        = result.getElementDamagePerHit();
            }
        }


        /*
         * ボウガン属性は
         * ShotSkillModifier反映後の結果を使用。
         */
        else {

            Object resultObject
                    = model.get(
                            "bowgunElementDamageResult"
                    );


            if (resultObject
                    instanceof BowgunElementDamageResult result) {

                elementDamageBeforeHitzone
                        = result.getElementDamagePerHit();
            }
        }


        /*
         * =================================================
         * 属性肉質
         * =================================================
         */
        ElementHitzoneDamageResult elementHitzoneDamageResult
                = null;


        if (elementDamageBeforeHitzone != null
                && monsterHitzoneValueResult.isElemental()) {

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
                            elementDamageBeforeHitzone,
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
                = null;


        if (physicalHitzoneDamageResult != null
                || elementHitzoneDamageResult != null) {

            finalDamageResult
                    = finalDamageService.calculate(
                            physicalHitzoneDamageResult,
                            elementHitzoneDamageResult
                    );
        }


        model.put(
                "finalDamageResult",
                finalDamageResult
        );
    }


    /*
     * =====================================================
     * resolvedSkillEffects取得
     * =====================================================
     */
    @SuppressWarnings("unchecked")
    private Map<Integer, SkillEffectState> getResolvedSkillEffects(
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
     * 射撃武器判定
     * =====================================================
     */
    private boolean isShotWeapon(
            String weaponType) {

        if (weaponType == null) {

            return false;
        }


        return "弓".equals(
                weaponType
        )
                || "ライトボウガン".equals(
                        weaponType
                )
                || "ヘビィボウガン".equals(
                        weaponType
                );
    }
}