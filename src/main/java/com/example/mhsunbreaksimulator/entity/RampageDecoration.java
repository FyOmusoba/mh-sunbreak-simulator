package com.example.mhsunbreaksimulator.entity;

import lombok.Data;

@Data
public class RampageDecoration {

    private Integer rampageDecorationId;

    private Integer rampageSkillId;

    private String rampageSkillName;

    private String decorationName;

    private Integer requiredSlotSize;

    private String description;
}