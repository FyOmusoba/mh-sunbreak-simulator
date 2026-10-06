package com.example.mhsunbreaksimulator.entity;

import lombok.Data;

@Data
public class Decoration {

    private Integer decorationId;

    private Integer skillId;

    private String skillName;

    private Integer skillLevel;

    private Integer skillMaxLevel;

    private Integer requiredSlotSize;
}