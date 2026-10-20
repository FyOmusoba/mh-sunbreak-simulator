package com.example.mhsunbreaksimulator.entity;

import lombok.Data;

@Data
public class Preset {

    private Integer presetId;

    private Integer userId;

    private String presetName;

    private Integer weaponId;

    private Integer rampageDecorationId;
}