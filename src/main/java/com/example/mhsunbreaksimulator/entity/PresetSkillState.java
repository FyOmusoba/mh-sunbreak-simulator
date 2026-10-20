package com.example.mhsunbreaksimulator.entity;

import lombok.Data;

@Data
public class PresetSkillState {

    private Integer presetSkillStateId;

    private Integer presetId;

    private Integer skillId;

    private String stateCode;
}