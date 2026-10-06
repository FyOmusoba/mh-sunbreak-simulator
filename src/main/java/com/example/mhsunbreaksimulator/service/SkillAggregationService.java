package com.example.mhsunbreaksimulator.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.SkillTotal;
import com.example.mhsunbreaksimulator.entity.Armor;
import com.example.mhsunbreaksimulator.entity.Decoration;

@Service
public class SkillAggregationService {

    /*
     * 防具だけを合算する場合
     */
    public List<SkillTotal> calculateSkillTotals(
            List<Armor> armors) {

        return calculateSkillTotals(
                armors,
                List.of()
        );
    }


    /*
     * 防具 + 装飾品を合算する場合
     */
    public List<SkillTotal> calculateSkillTotals(
            List<Armor> armors,
            List<Decoration> decorations) {

        Map<Integer, SkillTotal> skillMap
                = new LinkedHashMap<>();


        /*
         * =========================
         * 防具スキル
         * =========================
         */
        for (Armor armor : armors) {

            if (armor == null) {
                continue;
            }

            addSkill(
                    skillMap,
                    armor.getSkill1Id(),
                    armor.getSkill1Name(),
                    armor.getSkill1Level(),
                    armor.getSkill1MaxLevel()
            );

            addSkill(
                    skillMap,
                    armor.getSkill2Id(),
                    armor.getSkill2Name(),
                    armor.getSkill2Level(),
                    armor.getSkill2MaxLevel()
            );

            addSkill(
                    skillMap,
                    armor.getSkill3Id(),
                    armor.getSkill3Name(),
                    armor.getSkill3Level(),
                    armor.getSkill3MaxLevel()
            );

            addSkill(
                    skillMap,
                    armor.getSkill4Id(),
                    armor.getSkill4Name(),
                    armor.getSkill4Level(),
                    armor.getSkill4MaxLevel()
            );
        }


        /*
         * =========================
         * 装飾品スキル
         * =========================
         */
        for (Decoration decoration : decorations) {

            if (decoration == null) {
                continue;
            }

            addSkill(
                    skillMap,
                    decoration.getSkillId(),
                    decoration.getSkillName(),
                    decoration.getSkillLevel(),
                    decoration.getSkillMaxLevel()
            );
        }


        return skillMap.values()
                .stream()
                .toList();
    }


    /*
     * 同じスキルIDならLvを加算する。
     * ただし最大Lvを超えない。
     */
    private void addSkill(
            Map<Integer, SkillTotal> skillMap,
            Integer skillId,
            String skillName,
            Integer level,
            Integer maxLevel) {

        if (skillId == null
                || skillName == null
                || level == null
                || maxLevel == null) {
            return;
        }

        if (skillMap.containsKey(skillId)) {

            SkillTotal existing
                    = skillMap.get(skillId);

            int totalLevel
                    = existing.getLevel() + level;

            int cappedLevel
                    = Math.min(
                            totalLevel,
                            existing.getMaxLevel()
                    );

            existing.setLevel(cappedLevel);

        } else {

            int cappedLevel
                    = Math.min(
                            level,
                            maxLevel
                    );

            skillMap.put(
                    skillId,
                    new SkillTotal(
                            skillId,
                            skillName,
                            cappedLevel,
                            maxLevel
                    )
            );
        }
    }
}