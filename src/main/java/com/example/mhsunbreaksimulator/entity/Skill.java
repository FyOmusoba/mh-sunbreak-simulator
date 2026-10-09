package com.example.mhsunbreaksimulator.entity;

import lombok.Data;

@Data
public class Skill {

    private Integer skillId;

    private String skillName;

    private Integer maxLevel;

    private String description;

    private String conditionText;

    private Boolean stateSelectRequired;

    private Boolean stageSelectRequired;

    private Integer talismanSkill1MaxLevel;

    private Integer talismanSkill2MaxLevel;
}