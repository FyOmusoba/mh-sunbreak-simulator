package com.example.mhsunbreaksimulator.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.SkillStateSelection;
import com.example.mhsunbreaksimulator.dto.SkillTotal;
import com.example.mhsunbreaksimulator.entity.Skill;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillStateSelectionService {

    private final SkillService skillService;

    private final SkillEffectStateService skillEffectStateService;


    /*
     * =========================================
     * 発動スキル一覧から
     * 条件付きスキルの状態選択情報を作成
     * =========================================
     */
    public List<SkillStateSelection> createSelections(
            List<SkillTotal> skillTotals,
            String weaponType) {

        List<SkillStateSelection> selections
                = new ArrayList<>();

        if (skillTotals == null) {
            return selections;
        }


        for (SkillTotal skillTotal : skillTotals) {

            if (skillTotal == null
                    || skillTotal.getSkillId() == null
                    || skillTotal.getLevel() == null
                    || skillTotal.getLevel() < 1) {

                continue;
            }


            Skill skill
                    = skillService.findById(
                            skillTotal.getSkillId()
                    );

            if (skill == null) {
                continue;
            }


            /*
             * 条件付き・状態選択・段階選択の
             * いずれにも該当しない通常スキルは除外
             */
            boolean hasConditionText
                    = skill.getConditionText() != null
                    && !skill.getConditionText().isBlank();

            boolean stateSelectRequired
                    = Boolean.TRUE.equals(
                            skill.getStateSelectRequired()
                    );

            boolean stageSelectRequired
                    = Boolean.TRUE.equals(
                            skill.getStageSelectRequired()
                    );

            if (!hasConditionText
                    && !stateSelectRequired
                    && !stageSelectRequired) {

                continue;
            }


            List<SkillEffectState> states
                    = skillEffectStateService.findBySkillIdAndLevel(
                            skillTotal.getSkillId(),
                            skillTotal.getLevel(),
                            weaponType
                    );


            if (states.isEmpty()) {
                continue;
            }


            selections.add(
                    new SkillStateSelection(
                            skill.getSkillId(),
                            skill.getSkillName(),
                            skillTotal.getLevel(),
                            skill.getConditionText(),
                            skill.getStateSelectRequired(),
                            skill.getStageSelectRequired(),
                            states
                    )
            );
        }


        return selections;
    }
}