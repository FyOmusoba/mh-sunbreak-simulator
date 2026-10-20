package com.example.mhsunbreaksimulator.entity;

import lombok.Data;

@Data
public class PresetArmor {

    private Integer presetArmorId;

    private Integer presetId;

    private String part;

    private Integer armorId;

    private Integer slot1SizeFinal;

    private Integer slot2SizeFinal;

    private Integer slot3SizeFinal;

    private Integer defenseDelta;

    private Integer fireResDelta;

    private Integer waterResDelta;

    private Integer thunderResDelta;

    private Integer iceResDelta;

    private Integer dragonResDelta;
}