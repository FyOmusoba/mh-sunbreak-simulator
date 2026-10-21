package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowgunWyvernblastDamageResult;
import com.example.mhsunbreaksimulator.dto.FinalDamageResult;
import com.example.mhsunbreaksimulator.dto.MonsterHitzoneValueResult;
import com.example.mhsunbreaksimulator.dto.MonsterWeakpointResult;
import com.example.mhsunbreaksimulator.dto.WeakpointSkillModifier;
import com.example.mhsunbreaksimulator.entity.MonsterHitzone;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BowgunWyvernblastFinalDamageService {

    private final MonsterWeakpointService monsterWeakpointService;

    private final WeakpointSkillModifierService
            weakpointSkillModifierService;


    /*
     * =====================================================
     * ライトボウガン
     * 起爆竜弾
     * 最終ダメージ
     * =====================================================
     *
     * 起爆竜弾は
     *
     * ・肉質無視固定ダメージ
     * ・火属性ダメージ
     *
     * の2成分として扱う。
     *
     *
     * 固定ダメージ：
     *
     * BowgunWyvernblastDamageServiceで
     * specialShotMul適用済み。
     *
     * 物理肉質・会心の影響を受けない。
     *
     *
     * 火属性：
     *
     * fixedFireValue
     * × 火属性肉質
     * × 属性弱点倍率
     *
     * 特殊射撃強化・会心の影響を受けない。
     */
    public FinalDamageResult calculate(
            BowgunWyvernblastDamageResult wyvernblastDamageResult,
            MonsterHitzone hitzone,
            Map<Integer, SkillEffectState> resolvedSkillEffects) {

        FinalDamageResult result
                = new FinalDamageResult();


        /*
         * =================================================
         * 必須値
         * =================================================
         */
        if (wyvernblastDamageResult == null
                || hitzone == null) {

            return result;
        }


        /*
         * =================================================
         * 固定ダメージ
         * =================================================
         *
         * 物理肉質を無視。
         *
         * 会心もしないため、
         * 通常・プラス会心・マイナス会心で
         * 同じ値を使用する。
         */
        BigDecimal fixedDamage
                = safeDamage(
                        wyvernblastDamageResult
                                .getFixedDamage()
                );


        /*
         * =================================================
         * 火属性肉質
         * =================================================
         */
        int fireHitzone
                = valueOrZero(
                        hitzone.getFire()
                );


        /*
         * =================================================
         * 火属性弱点倍率
         * =================================================
         *
         * 弱点特効【属性】など、
         * 既存の属性弱点依存補正を反映する。
         */
        BigDecimal fireWeakpointMultiplier
                = createFireWeakpointMultiplier(
                        fireHitzone,
                        resolvedSkillEffects
                );


        /*
         * =================================================
         * 火属性ダメージ
         * =================================================
         *
         * fixedFireValue
         * × 火属性肉質
         * × 属性弱点倍率
         */
        BigDecimal fireDamage
                = applyHitzone(
                        safeDamage(
                                wyvernblastDamageResult
                                        .getFixedFireValue()
                        ),
                        fireHitzone
                )
                        .multiply(
                                fireWeakpointMultiplier
                        );


        /*
         * =================================================
         * 四捨五入
         * =================================================
         */
        int roundedFixedDamage
                = roundDamage(
                        fixedDamage
                );


        int roundedFireDamage
                = roundDamage(
                        fireDamage
                );


        /*
         * =================================================
         * 非会心
         * =================================================
         */
        result.setNormalPhysicalDamageBeforeRound(
                fixedDamage
        );

        result.setNormalElementDamageBeforeRound(
                fireDamage
        );

        result.setNormalPhysicalDamage(
                roundedFixedDamage
        );

        result.setNormalElementDamage(
                roundedFireDamage
        );

        result.setNormalTotalDamage(
                roundedFixedDamage
                        + roundedFireDamage
        );


        /*
         * =================================================
         * プラス会心
         * =================================================
         *
         * 起爆竜弾の固定ダメージ・火属性部分は
         * 会心で変化しない。
         */
        result.setPositiveCriticalPhysicalDamageBeforeRound(
                fixedDamage
        );

        result.setPositiveCriticalElementDamageBeforeRound(
                fireDamage
        );

        result.setPositiveCriticalPhysicalDamage(
                roundedFixedDamage
        );

        result.setPositiveCriticalElementDamage(
                roundedFireDamage
        );

        result.setPositiveCriticalTotalDamage(
                roundedFixedDamage
                        + roundedFireDamage
        );


        /*
         * =================================================
         * マイナス会心
         * =================================================
         *
         * こちらも会心率の影響を受けない。
         */
        result.setNegativeCriticalPhysicalDamageBeforeRound(
                fixedDamage
        );

        result.setNegativeCriticalPhysicalDamage(
                roundedFixedDamage
        );

        result.setNegativeCriticalElementDamage(
                roundedFireDamage
        );

        result.setNegativeCriticalTotalDamage(
                roundedFixedDamage
                        + roundedFireDamage
        );


        return result;
    }


    /*
     * =====================================================
     * 火属性弱点倍率
     * =====================================================
     */
    private BigDecimal createFireWeakpointMultiplier(
            int fireHitzone,
            Map<Integer, SkillEffectState> resolvedSkillEffects) {

        MonsterHitzoneValueResult hitzoneValueResult
                = new MonsterHitzoneValueResult();


        hitzoneValueResult.setElementType(
                "火"
        );

        hitzoneValueResult.setElemental(
                true
        );

        hitzoneValueResult.setElementHitzone(
                fireHitzone
        );


        MonsterWeakpointResult weakpointResult
                = monsterWeakpointService.calculate(
                        hitzoneValueResult
                );


        WeakpointSkillModifier weakpointModifier
                = weakpointSkillModifierService.calculate(
                        weakpointResult,
                        resolvedSkillEffects
                );


        if (weakpointModifier == null
                || weakpointModifier
                        .getMainElementDamageMul() == null) {

            return BigDecimal.ONE;
        }


        return weakpointModifier
                .getMainElementDamageMul();
    }


    /*
     * =====================================================
     * 属性肉質適用
     * =====================================================
     */
    private BigDecimal applyHitzone(
            BigDecimal damage,
            int hitzone) {

        if (damage == null
                || hitzone <= 0) {

            return BigDecimal.ZERO;
        }


        return damage
                .multiply(
                        BigDecimal.valueOf(
                                hitzone
                        )
                )
                .movePointLeft(
                        2
                );
    }


    /*
     * =====================================================
     * 四捨五入
     * =====================================================
     */
    private int roundDamage(
            BigDecimal damage) {

        if (damage == null) {

            return 0;
        }


        return damage
                .setScale(
                        0,
                        RoundingMode.HALF_UP
                )
                .intValue();
    }


    /*
     * =====================================================
     * null安全
     * =====================================================
     */
    private BigDecimal safeDamage(
            BigDecimal value) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }


    private int valueOrZero(
            Integer value) {

        return value == null
                ? 0
                : value;
    }
}