package com.example.mhsunbreaksimulator.entity;

import lombok.Data;

@Data
public class PresetBowSetting {

    private Integer presetId;

    private String coatingType;

    private Integer bowChargeId;

    private String bowAttackType;
}