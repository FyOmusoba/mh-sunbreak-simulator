package com.example.mhsunbreaksimulator;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.mhsunbreaksimulator.dto.SkillStateSelection;
import com.example.mhsunbreaksimulator.dto.SkillTotal;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;
import com.example.mhsunbreaksimulator.service.SkillStateSelectionService;

@SpringBootTest
public class SkillStateSelectionServiceTest {

    @Autowired
    private SkillStateSelectionService skillStateSelectionService;


    @Test
    void testRengekiSelection() {

        List<SkillTotal> skillTotals
                = List.of(
                        new SkillTotal(
                                35,
                                "連撃",
                                3,
                                3
                        )
                );

        List<SkillStateSelection> selections
                = skillStateSelectionService.createSelections(
                        skillTotals,
                        "大剣"
                );

        System.out.println(
                "条件付きスキル件数："
                + selections.size()
        );


        for (SkillStateSelection selection : selections) {

            System.out.println(
                    selection.getSkillName()
                    + " Lv"
                    + selection.getLevel()
            );

            System.out.println(
                    "条件："
                    + selection.getConditionText()
            );

            System.out.println(
                    "stateSelectRequired="
                    + selection.getStateSelectRequired()
            );

            System.out.println(
                    "stageSelectRequired="
                    + selection.getStageSelectRequired()
            );


            for (SkillEffectState state : selection.getStates()) {

                System.out.println(
                        "  "
                        + state.getStateCode()
                        + " / "
                        + state.getStateName()
                        + " / attackAdd="
                        + state.getAttackAdd()
                        + " / elementAdd="
                        + state.getElementAdd()
                );
            }
        }
    }
}