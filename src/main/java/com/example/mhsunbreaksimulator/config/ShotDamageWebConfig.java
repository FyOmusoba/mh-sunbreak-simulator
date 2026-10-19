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

import com.example.mhsunbreaksimulator.dto.BowElementDamageResult;
import com.example.mhsunbreaksimulator.dto.BowPhysicalDamageResult;
import com.example.mhsunbreaksimulator.dto.BowPhysicalModifier;
import com.example.mhsunbreaksimulator.dto.BowgunAmmoModifier;
import com.example.mhsunbreaksimulator.dto.BowgunElementDamageResult;
import com.example.mhsunbreaksimulator.dto.BowgunPhysicalDamageResult;
import com.example.mhsunbreaksimulator.dto.BowgunRapidFireModifier;
import com.example.mhsunbreaksimulator.dto.BowgunSpecialDamageResult;
import com.example.mhsunbreaksimulator.dto.BowgunSpecialFinalDamageResult;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.dto.CriticalModifierResult;
import com.example.mhsunbreaksimulator.dto.ElementHitzoneDamageResult;
import com.example.mhsunbreaksimulator.dto.FinalDamageResult;
import com.example.mhsunbreaksimulator.dto.MonsterHitzoneValueResult;
import com.example.mhsunbreaksimulator.dto.MonsterWeakpointResult;
import com.example.mhsunbreaksimulator.dto.PhysicalHitzoneDamageResult;
import com.example.mhsunbreaksimulator.dto.ShotSkillModifier;
import com.example.mhsunbreaksimulator.dto.WeakpointSkillModifier;
import com.example.mhsunbreaksimulator.entity.BowCharge;
import com.example.mhsunbreaksimulator.entity.BowgunAmmo;
import com.example.mhsunbreaksimulator.entity.MonsterHitzone;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;
import com.example.mhsunbreaksimulator.entity.Weapon;
import com.example.mhsunbreaksimulator.service.BowPhysicalDamageService;
import com.example.mhsunbreaksimulator.service.BowgunElementDamageService;
import com.example.mhsunbreaksimulator.service.BowgunPhysicalDamageService;
import com.example.mhsunbreaksimulator.service.BowgunSpecialDamageService;
import com.example.mhsunbreaksimulator.service.BowgunSpecialFinalDamageService;
import com.example.mhsunbreaksimulator.service.CriticalModifierService;
import com.example.mhsunbreaksimulator.service.ElementHitzoneDamageService;
import com.example.mhsunbreaksimulator.service.FinalDamageService;
import com.example.mhsunbreaksimulator.service.MonsterWeakpointService;
import com.example.mhsunbreaksimulator.service.PhysicalHitzoneDamageService;
import com.example.mhsunbreaksimulator.service.ShotSkillModifierService;
import com.example.mhsunbreaksimulator.service.WeakpointSkillModifierService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ShotDamageWebConfig
        implements WebMvcConfigurer {

    private final ShotSkillModifierService shotSkillModifierService;

    private final BowPhysicalDamageService bowPhysicalDamageService;

    private final BowgunPhysicalDamageService bowgunPhysicalDamageService;

    private final BowgunElementDamageService bowgunElementDamageService;

    private final BowgunSpecialDamageService bowgunSpecialDamageService;

    private final BowgunSpecialFinalDamageService
            bowgunSpecialFinalDamageService;

    private final PhysicalHitzoneDamageService
            physicalHitzoneDamageService;

    private final ElementHitzoneDamageService
            elementHitzoneDamageService;

    private final FinalDamageService finalDamageService;

    private final MonsterWeakpointService monsterWeakpointService;

    private final WeakpointSkillModifierService
            weakpointSkillModifierService;

    private final CriticalModifierService criticalModifierService;


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


        Object weaponObject
                = model.get(
                        "weapon"
                );


        if (!(weaponObject
                instanceof Weapon weapon)) {

            return;
        }


        if (!isShotWeapon(
                weapon.getWeaponType()
        )) {

            return;
        }


        Map<Integer, SkillEffectState> resolvedSkillEffects
                = getResolvedSkillEffects(
                        model.get(
                                "resolvedSkillEffects"
                        )
                );


        ConditionalSkillModifierTotal
                conditionalSkillModifierTotal
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


        else {

            applyBowgun(
                    model,
                    weapon,
                    resolvedSkillEffects,
                    conditionalSkillModifierTotal
            );


            applyBowgunElementContext(
                    model,
                    weapon,
                    resolvedSkillEffects,
                    conditionalSkillModifierTotal
            );
        }


        /*
         * まず通常ルートを計算。
         */
        recalculateFinalDamage(
                model,
                weapon
        );


        /*
         * ボウガン特殊弾の場合は
         * 最後に特殊弾結果へ差し替える。
         */
        if ("ライトボウガン".equals(
                weapon.getWeaponType()
        )
                || "ヘビィボウガン".equals(
                        weapon.getWeaponType()
                )) {

            applyBowgunSpecialFinalDamage(
                    model,
                    weapon,
                    resolvedSkillEffects,
                    conditionalSkillModifierTotal
            );
        }
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
            ConditionalSkillModifierTotal
                    conditionalSkillModifierTotal) {

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


        ShotSkillModifier shotSkillModifier
                = shotSkillModifierService.calculateBow(
                        selectedBowCharge,
                        resolvedSkillEffects
                );


        model.put(
                "shotSkillModifier",
                shotSkillModifier
        );


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
     * ボウガン
     * =====================================================
     */
    private void applyBowgun(
            Map<String, Object> model,
            Weapon weapon,
            Map<Integer, SkillEffectState> resolvedSkillEffects,
            ConditionalSkillModifierTotal
                    conditionalSkillModifierTotal) {

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
         * 通常物理
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
         * 通常属性
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


        /*
         * 特殊弾
         */
        BowgunSpecialDamageResult bowgunSpecialDamageResult
                = bowgunSpecialDamageService.calculate(
                        weapon.getAttack(),
                        bowgunAmmoModifier,
                        conditionalSkillModifierTotal,
                        selectedBowgunAmmo.getAmmoType()
                );


        model.put(
                "bowgunSpecialDamageResult",
                bowgunSpecialDamageResult
        );
    }


    /*
     * =====================================================
     * ボウガン特殊弾
     * 最終ダメージ
     * =====================================================
     */
    private void applyBowgunSpecialFinalDamage(
            Map<String, Object> model,
            Weapon weapon,
            Map<Integer, SkillEffectState> resolvedSkillEffects,
            ConditionalSkillModifierTotal
                    conditionalSkillModifierTotal) {

        Object ammoObject
                = model.get(
                        "selectedBowgunAmmo"
                );


        if (!(ammoObject
                instanceof BowgunAmmo selectedBowgunAmmo)) {

            model.put(
                    "bowgunSpecialFinalDamageResult",
                    null
            );

            return;
        }


        /*
         * 通常弾・属性弾なら
         * 特殊弾処理はしない。
         */
        if (!isBowgunSpecialDamageAmmo(
                selectedBowgunAmmo.getAmmoType()
        )) {

            model.put(
                    "bowgunSpecialFinalDamageResult",
                    null
            );

            return;
        }


        Object specialDamageObject
                = model.get(
                        "bowgunSpecialDamageResult"
                );


        if (!(specialDamageObject
                instanceof BowgunSpecialDamageResult
                        bowgunSpecialDamageResult)) {

            model.put(
                    "bowgunSpecialFinalDamageResult",
                    null
            );

            return;
        }


        Object hitzoneObject
                = model.get(
                        "selectedHitzone"
                );


        if (!(hitzoneObject
                instanceof MonsterHitzone selectedHitzone)) {

            model.put(
                    "bowgunSpecialFinalDamageResult",
                    null
            );

            return;
        }


        /*
         * 特殊弾専用計算
         */
        BowgunSpecialFinalDamageResult
                bowgunSpecialFinalDamageResult
                = bowgunSpecialFinalDamageService.calculate(
                        selectedBowgunAmmo,
                        bowgunSpecialDamageResult,
                        selectedHitzone,
                        weapon.getAffinity(),
                        conditionalSkillModifierTotal,
                        resolvedSkillEffects
                );


        model.put(
                "bowgunSpecialFinalDamageResult",
                bowgunSpecialFinalDamageResult
        );


        /*
         * 既存の最終ダメージ表示用DTOへ変換。
         */
        FinalDamageResult specialFinalDamageResult
                = convertSpecialFinalDamageResult(
                        bowgunSpecialFinalDamageResult
                );


        model.put(
                "finalDamageResult",
                specialFinalDamageResult
        );


        /*
         * =================================================
         * 特殊弾選択中は
         * 通常弾・属性弾用の途中表示を隠す
         * =================================================
         */
        hideNormalBowgunDamageBlocksForSpecialAmmo(
                model
        );
    }


    /*
     * =====================================================
     * 特殊弾選択中
     * 通常弾用表示を非表示
     * =====================================================
     *
     * simulator.html側には既に
     *
     * th:if="${... != null}"
     *
     * が入っているので、
     * Modelをnullにするだけで
     * HTML変更なしで非表示にできる。
     */
    private void hideNormalBowgunDamageBlocksForSpecialAmmo(
            Map<String, Object> model) {

        /*
         * 「ボウガン弾・計算用データ」
         */
        model.put(
                "bowgunAmmoModifier",
                null
        );


        /*
         * 「ボウガン物理ダメージ計算途中値」
         */
        model.put(
                "bowgunPhysicalDamageResult",
                null
        );


        /*
         * 「ボウガン属性ダメージ計算途中値」
         */
        model.put(
                "bowgunElementDamageResult",
                null
        );


        /*
         * 「物理肉質適用後」
         */
        model.put(
                "physicalHitzoneDamageResult",
                null
        );


        /*
         * 「属性肉質適用後」
         */
        model.put(
                "elementHitzoneDamageResult",
                null
        );


        /*
         * 特殊弾では速射補正表示も不要。
         */
        model.put(
                "bowgunRapidFireModifier",
                null
        );
    }


    /*
     * =====================================================
     * 特殊弾
     * ↓
     * FinalDamageResult
     * =====================================================
     */
    private FinalDamageResult convertSpecialFinalDamageResult(
            BowgunSpecialFinalDamageResult specialResult) {

        FinalDamageResult result
                = new FinalDamageResult();


        if (specialResult == null) {

            return result;
        }


        BigDecimal normalPhysicalBeforeRound
                = safeBigDecimal(
                        specialResult
                                .getImpactNormalPhysicalDamage()
                )
                        .add(
                                safeBigDecimal(
                                        specialResult
                                                .getSecondaryNormalPhysicalDamageTotal()
                                )
                        )
                        .add(
                                safeBigDecimal(
                                        specialResult
                                                .getSecondaryFixedDamageTotal()
                                )
                        )
                        .add(
                                safeBigDecimal(
                                        specialResult
                                                .getAdditionalFixedDamageTotal()
                                )
                        );


        BigDecimal normalElementBeforeRound
                = safeBigDecimal(
                        specialResult
                                .getSecondaryElementDamageTotal()
                )
                        .add(
                                safeBigDecimal(
                                        specialResult
                                                .getAdditionalElementDamageTotal()
                                )
                        );


        result.setNormalPhysicalDamageBeforeRound(
                normalPhysicalBeforeRound
        );

        result.setNormalElementDamageBeforeRound(
                normalElementBeforeRound
        );


        BigDecimal positivePhysicalBeforeRound
                = safeBigDecimal(
                        specialResult
                                .getImpactPositiveCriticalPhysicalDamage()
                )
                        .add(
                                safeBigDecimal(
                                        specialResult
                                                .getSecondaryPositiveCriticalPhysicalDamageTotal()
                                )
                        )
                        .add(
                                safeBigDecimal(
                                        specialResult
                                                .getSecondaryFixedDamageTotal()
                                )
                        )
                        .add(
                                safeBigDecimal(
                                        specialResult
                                                .getAdditionalFixedDamageTotal()
                                )
                        );


        result.setPositiveCriticalPhysicalDamageBeforeRound(
                positivePhysicalBeforeRound
        );

        result.setPositiveCriticalElementDamageBeforeRound(
                normalElementBeforeRound
        );


        BigDecimal negativePhysicalBeforeRound
                = safeBigDecimal(
                        specialResult
                                .getImpactNegativeCriticalPhysicalDamage()
                )
                        .add(
                                safeBigDecimal(
                                        specialResult
                                                .getSecondaryNegativeCriticalPhysicalDamageTotal()
                                )
                        )
                        .add(
                                safeBigDecimal(
                                        specialResult
                                                .getSecondaryFixedDamageTotal()
                                )
                        )
                        .add(
                                safeBigDecimal(
                                        specialResult
                                                .getAdditionalFixedDamageTotal()
                                )
                        );


        result.setNegativeCriticalPhysicalDamageBeforeRound(
                negativePhysicalBeforeRound
        );


        result.setNormalPhysicalDamage(
                safeInteger(
                        specialResult
                                .getNormalPhysicalDamage()
                )
        );

        result.setNormalElementDamage(
                safeInteger(
                        specialResult
                                .getNormalElementDamage()
                )
        );

        result.setNormalTotalDamage(
                safeInteger(
                        specialResult
                                .getNormalTotalDamage()
                )
        );


        result.setPositiveCriticalPhysicalDamage(
                safeInteger(
                        specialResult
                                .getPositiveCriticalPhysicalDamage()
                )
        );

        result.setPositiveCriticalElementDamage(
                safeInteger(
                        specialResult
                                .getPositiveCriticalElementDamage()
                )
        );

        result.setPositiveCriticalTotalDamage(
                safeInteger(
                        specialResult
                                .getPositiveCriticalTotalDamage()
                )
        );


        result.setNegativeCriticalPhysicalDamage(
                safeInteger(
                        specialResult
                                .getNegativeCriticalPhysicalDamage()
                )
        );

        result.setNegativeCriticalElementDamage(
                safeInteger(
                        specialResult
                                .getNegativeCriticalElementDamage()
                )
        );

        result.setNegativeCriticalTotalDamage(
                safeInteger(
                        specialResult
                                .getNegativeCriticalTotalDamage()
                )
        );


        return result;
    }


    /*
     * =====================================================
     * ボウガン属性弾
     * 属性コンテキスト
     * =====================================================
     */
    private void applyBowgunElementContext(
            Map<String, Object> model,
            Weapon weapon,
            Map<Integer, SkillEffectState> resolvedSkillEffects,
            ConditionalSkillModifierTotal
                    conditionalSkillModifierTotal) {

        Object ammoObject
                = model.get(
                        "selectedBowgunAmmo"
                );


        if (!(ammoObject
                instanceof BowgunAmmo selectedBowgunAmmo)) {

            return;
        }


        String ammoElementType
                = resolveBowgunAmmoElementType(
                        selectedBowgunAmmo
                                .getAmmoType()
                );


        if (ammoElementType == null) {

            return;
        }


        Object selectedHitzoneObject
                = model.get(
                        "selectedHitzone"
                );


        if (!(selectedHitzoneObject
                instanceof MonsterHitzone selectedHitzone)) {

            return;
        }


        Object hitzoneValueObject
                = model.get(
                        "monsterHitzoneValueResult"
                );


        if (!(hitzoneValueObject
                instanceof MonsterHitzoneValueResult
                        hitzoneValueResult)) {

            return;
        }


        hitzoneValueResult.setElementType(
                ammoElementType
        );


        hitzoneValueResult.setElemental(
                true
        );


        hitzoneValueResult.setElementHitzone(
                resolveElementHitzone(
                        ammoElementType,
                        selectedHitzone
                )
        );


        model.put(
                "monsterHitzoneValueResult",
                hitzoneValueResult
        );


        MonsterWeakpointResult monsterWeakpointResult
                = monsterWeakpointService.calculate(
                        hitzoneValueResult
                );


        model.put(
                "monsterWeakpointResult",
                monsterWeakpointResult
        );


        WeakpointSkillModifier weakpointSkillModifier
                = weakpointSkillModifierService.calculate(
                        monsterWeakpointResult,
                        resolvedSkillEffects
                );


        model.put(
                "weakpointSkillModifier",
                weakpointSkillModifier
        );


        CriticalModifierResult criticalModifierResult
                = criticalModifierService.calculate(
                        weapon.getAffinity(),
                        conditionalSkillModifierTotal,
                        weakpointSkillModifier
                );


        model.put(
                "criticalModifierResult",
                criticalModifierResult
        );
    }


    /*
     * =====================================================
     * ボウガン弾 → 属性
     * =====================================================
     */
    private String resolveBowgunAmmoElementType(
            String ammoType) {

        if (ammoType == null) {

            return null;
        }


        switch (ammoType) {

        case "火炎弾":
        case "貫通火炎弾":

            return "火";


        case "水冷弾":
        case "貫通水冷弾":

            return "水";


        case "電撃弾":
        case "貫通電撃弾":

            return "雷";


        case "氷結弾":
        case "貫通氷結弾":

            return "氷";


        case "滅龍弾":
        case "貫通滅龍弾":

            return "龍";


        default:

            return null;
        }
    }


    /*
     * =====================================================
     * 特殊弾判定
     * =====================================================
     */
    private boolean isBowgunSpecialDamageAmmo(
            String ammoType) {

        if (ammoType == null) {

            return false;
        }


        return "斬裂弾".equals(
                ammoType
        )
                || "徹甲榴弾".equals(
                        ammoType
                )
                || "拡散弾".equals(
                        ammoType
                )
                || "竜撃弾".equals(
                        ammoType
                );
    }


    /*
     * =====================================================
     * 属性肉質
     * =====================================================
     */
    private int resolveElementHitzone(
            String elementType,
            MonsterHitzone hitzone) {

        if (elementType == null
                || hitzone == null) {

            return 0;
        }


        Integer value;


        switch (elementType) {

        case "火":

            value = hitzone.getFire();

            break;


        case "水":

            value = hitzone.getWater();

            break;


        case "雷":

            value = hitzone.getThunder();

            break;


        case "氷":

            value = hitzone.getIce();

            break;


        case "龍":

            value = hitzone.getDragon();

            break;


        default:

            value = 0;

            break;
        }


        return value == null
                ? 0
                : value;
    }


    /*
     * =====================================================
     * 通常射撃
     * 最終ダメージ再計算
     * =====================================================
     */
    private void recalculateFinalDamage(
            Map<String, Object> model,
            Weapon weapon) {

        MonsterHitzoneValueResult
                monsterHitzoneValueResult
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


        PhysicalHitzoneDamageResult
                physicalHitzoneDamageResult
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


        if ("弓".equals(
                weapon.getWeaponType()
        )) {

            Object resultObject
                    = model.get(
                            "bowElementDamageResult"
                    );


            if (resultObject
                    instanceof BowElementDamageResult result) {

                elementDamageBeforeHitzone
                        = result.getElementDamagePerHit();
            }
        }


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


        ElementHitzoneDamageResult
                elementHitzoneDamageResult
                = null;


        if (elementDamageBeforeHitzone != null
                && monsterHitzoneValueResult
                        .isElemental()) {

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
         * 最終
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


    private BigDecimal safeBigDecimal(
            BigDecimal value) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }


    private int safeInteger(
            Integer value) {

        return value == null
                ? 0
                : value;
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