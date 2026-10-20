package com.example.mhsunbreaksimulator.entity;

import lombok.Data;

@Data
public class PresetWeaponAugment {

    private Integer presetId;

    private Integer attackLevel;

    private Integer elementLevel;

    private Integer affinityLevel;

    private Integer sharpnessLevel;
}