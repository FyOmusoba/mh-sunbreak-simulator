package com.example.mhsunbreaksimulator.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SkillEffectState {

    private Integer effectStateId;

    private Integer skillLevelId;

    private String stateCode;

    private String stateName;

    private BigDecimal attackAdd;

    private BigDecimal attackMul;

    private BigDecimal elementAdd;

    private BigDecimal elementMul;

    private BigDecimal affinityAdd;

    private BigDecimal critDamageMul;

    private BigDecimal defenseAdd;

    private BigDecimal defenseMul;

    private BigDecimal fireResAdd;

    private BigDecimal waterResAdd;

    private BigDecimal thunderResAdd;

    private BigDecimal iceResAdd;

    private BigDecimal dragonResAdd;

    private BigDecimal shotMul;

    /*
     * 特殊射撃強化倍率。
     *
     * 対象：
     * ・ライトボウガン特殊弾
     * ・ヘビィボウガン特殊弾
     * ・弓 竜の一矢
     *
     * Lv1 = 1.10
     * Lv2 = 1.20
     *
     * 通常弾・貫通弾・散弾などには使用しない。
     */
    private BigDecimal specialShotMul;

    private BigDecimal phialMul;

    private BigDecimal fixedDamageAdd;

    private BigDecimal statusAdd;

    private BigDecimal statusMul;

    /*
     * 状態異常補正の対象。
     *
     * POISON
     * PARALYSIS
     * SLEEP
     * BLAST
     * ALL
     *
     * 状態異常補正と無関係な効果はnull。
     */
    private String statusTarget;

    private Integer sharpnessAdd;

    private BigDecimal recoveryMul;

    private BigDecimal elementDamageMul;

    private BigDecimal elementCritDamageMul;

    private String weaponType;

    private Integer armorSkillLevelAdd;

    private BigDecimal physicalDamageMul;

    private BigDecimal activationChance;

    private BigDecimal bouncePreventionChance;

    private BigDecimal artilleryDamageMul;

    private Integer allElementResSet;

    private Boolean nullifyNonDragonElement;

    private BigDecimal activationHpThresholdPct;

    private Boolean nullifyStatus;

    private BigDecimal resistanceToElementRate;

    private Boolean guaranteeStatusAccumulation;

    private Integer effectDurationSeconds;

    private Integer recoveryThreshold;

    private Integer requiredSharpnessRecoveryStages;

    private BigDecimal poisonDurationMul;

    private BigDecimal closeRangeCoatingMulOverride;

    private Boolean unlockBowChargeStage;

    private BigDecimal bowgunCustomAttackMulOverride;

    private BigDecimal additionalDamageAttackMul;

    private Boolean additionalDamageIgnoreHitzone;

    private BigDecimal damageHealRatio;
}