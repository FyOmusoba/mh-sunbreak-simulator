package com.example.mhsunbreaksimulator;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.mhsunbreaksimulator.entity.SkillEffectState;
import com.example.mhsunbreaksimulator.service.SkillEffectStateService;

@SpringBootTest
public class SkillEffectStateServiceTest {

    @Autowired
    private SkillEffectStateService skillEffectStateService;


    @Test
    void testRengekiLevel3GreatSword() {

        List<SkillEffectState> states
                = skillEffectStateService.findBySkillIdAndLevel(
                        35,
                        3,
                        "大剣"
                );

        System.out.println("取得件数：" + states.size());

        for (SkillEffectState state : states) {

            System.out.println(
                    state.getStateCode()
                    + " / "
                    + state.getStateName()
                    + " / attackAdd="
                    + state.getAttackAdd()
                    + " / attackMul="
                    + state.getAttackMul()
                    + " / elementAdd="
                    + state.getElementAdd()
            );
        }
    }


    @Test
    void testRengekiLevel3GreatSwordHit5Plus() {

        SkillEffectState state
                = skillEffectStateService.findBySkillIdAndLevelAndStateCode(
                        35,
                        3,
                        "HIT_5_PLUS",
                        "大剣"
                );

        System.out.println(
                "指定状態："
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