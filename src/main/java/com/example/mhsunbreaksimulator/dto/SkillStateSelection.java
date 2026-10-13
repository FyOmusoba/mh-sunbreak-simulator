package com.example.mhsunbreaksimulator.dto;

import java.util.List;

import com.example.mhsunbreaksimulator.entity.SkillEffectState;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SkillStateSelection {

    private Integer skillId;

    private String skillName;

    private Integer level;

    private String conditionText;

    private Boolean stateSelectRequired;

    private Boolean stageSelectRequired;

    private List<SkillEffectState> states;
}