package com.example.mhsunbreaksimulator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SkillTotal {

    private Integer skillId;

    private String skillName;

    private Integer level;

    private Integer maxLevel;
}