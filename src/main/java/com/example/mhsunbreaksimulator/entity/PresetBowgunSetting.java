package com.example.mhsunbreaksimulator.entity;

import lombok.Data;

@Data
public class PresetBowgunSetting {

    private Integer presetId;

    private String bowgunAttackType;

    private String wyvernblastExplosionSize;

    private String ammoType;

    private Integer ammoLevel;

    private Integer customId;
}