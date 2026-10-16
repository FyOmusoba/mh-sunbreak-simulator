package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.ShotSkillModifier;
import com.example.mhsunbreaksimulator.entity.BowCharge;
import com.example.mhsunbreaksimulator.entity.BowgunAmmo;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;

@Service
public class ShotSkillModifierService {

    /*
     * =====================================================
     * スキルID
     * =====================================================
     */

    /*
     * 通常弾・連射矢強化
     */
    private static final int NORMAL_RAPID_SKILL_ID
            = 22;


    /*
     * 貫通弾・貫通矢強化
     */
    private static final int PIERCE_SKILL_ID
            = 23;


    /*
     * 散弾・拡散矢強化
     */
    private static final int SPREAD_SKILL_ID
            = 24;


    /*
     * 速射強化
     */
    private static final int RAPID_FIRE_SKILL_ID
            = 25;


    /*
     * =====================================================
     * 弓
     * =====================================================
     */
    public ShotSkillModifier calculateBow(
            BowCharge bowCharge,
            Map<Integer, SkillEffectState> resolvedSkillEffects) {

        ShotSkillModifier result
                = new ShotSkillModifier();


        if (bowCharge == null
                || bowCharge.getShotType() == null
                || resolvedSkillEffects == null
                || resolvedSkillEffects.isEmpty()) {

            return result;
        }


        String shotType
                = bowCharge.getShotType();


        /*
         * =================================================
         * 連射矢
         * =================================================
         */
        if ("連射".equals(
                shotType
        )) {

            applyPhysicalShotMultiplier(
                    result,
                    resolvedSkillEffects.get(
                            NORMAL_RAPID_SKILL_ID
                    )
            );
        }


        /*
         * =================================================
         * 貫通矢
         * =================================================
         */
        else if ("貫通".equals(
                shotType
        )) {

            applyPhysicalShotMultiplier(
                    result,
                    resolvedSkillEffects.get(
                            PIERCE_SKILL_ID
                    )
            );
        }


        /*
         * =================================================
         * 拡散矢
         * =================================================
         */
        else if ("拡散".equals(
                shotType
        )) {

            applyPhysicalShotMultiplier(
                    result,
                    resolvedSkillEffects.get(
                            SPREAD_SKILL_ID
                    )
            );
        }


        return result;
    }


    /*
     * =====================================================
     * ライト / ヘビィ
     * =====================================================
     */
    public ShotSkillModifier calculateBowgun(
            BowgunAmmo bowgunAmmo,
            Map<Integer, SkillEffectState> resolvedSkillEffects) {

        ShotSkillModifier result
                = new ShotSkillModifier();


        if (bowgunAmmo == null
                || bowgunAmmo.getAmmoType() == null
                || resolvedSkillEffects == null
                || resolvedSkillEffects.isEmpty()) {

            return result;
        }


        String ammoType
                = bowgunAmmo.getAmmoType();


        /*
         * =================================================
         * 通常弾
         * =================================================
         */
        if ("通常弾".equals(
                ammoType
        )) {

            applyPhysicalShotMultiplier(
                    result,
                    resolvedSkillEffects.get(
                            NORMAL_RAPID_SKILL_ID
                    )
            );
        }


        /*
         * =================================================
         * 貫通弾
         * =================================================
         */
        else if ("貫通弾".equals(
                ammoType
        )) {

            applyPhysicalShotMultiplier(
                    result,
                    resolvedSkillEffects.get(
                            PIERCE_SKILL_ID
                    )
            );
        }


        /*
         * =================================================
         * 散弾
         * =================================================
         */
        else if ("散弾".equals(
                ammoType
        )) {

            applyPhysicalShotMultiplier(
                    result,
                    resolvedSkillEffects.get(
                            SPREAD_SKILL_ID
                    )
            );
        }


        /*
         * =================================================
         * 速射強化
         * =================================================
         *
         * rapidFire = true
         *
         * の弾だけ対象。
         *
         * 速射強化は、
         *
         * ・物理弾
         * ・属性弾
         *
         * の両方で使用できるよう、
         *
         * physicalMultiplier
         * elementMultiplier
         *
         * の両方へ倍率を入れる。
         */
        if (Boolean.TRUE.equals(
                bowgunAmmo.getRapidFire()
        )) {

            applyRapidFireMultiplier(
                    result,
                    resolvedSkillEffects.get(
                            RAPID_FIRE_SKILL_ID
                    )
            );
        }


        return result;
    }


    /*
     * =====================================================
     * 通常弾・貫通弾・散弾
     * 連射矢・貫通矢・拡散矢
     * =====================================================
     */
    private void applyPhysicalShotMultiplier(
            ShotSkillModifier result,
            SkillEffectState effect) {

        if (result == null
                || effect == null) {

            return;
        }


        BigDecimal shotMul
                = safeMultiplier(
                        effect.getShotMul()
                );


        result.setPhysicalMultiplier(
                result.getPhysicalMultiplier()
                        .multiply(
                                shotMul
                        )
        );
    }


    /*
     * =====================================================
     * 速射強化
     * =====================================================
     */
    private void applyRapidFireMultiplier(
            ShotSkillModifier result,
            SkillEffectState effect) {

        if (result == null
                || effect == null) {

            return;
        }


        BigDecimal shotMul
                = safeMultiplier(
                        effect.getShotMul()
                );


        /*
         * 物理側
         */
        result.setPhysicalMultiplier(
                result.getPhysicalMultiplier()
                        .multiply(
                                shotMul
                        )
        );


        /*
         * 属性側
         */
        result.setElementMultiplier(
                result.getElementMultiplier()
                        .multiply(
                                shotMul
                        )
        );
    }


    /*
     * =====================================================
     * null対策
     * =====================================================
     */
    private BigDecimal safeMultiplier(
            BigDecimal value) {

        return value == null
                ? BigDecimal.ONE
                : value;
    }
}