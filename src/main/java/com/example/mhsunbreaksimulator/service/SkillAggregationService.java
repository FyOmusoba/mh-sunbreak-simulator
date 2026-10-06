package com.example.mhsunbreaksimulator.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.SkillTotal;
import com.example.mhsunbreaksimulator.dto.TalismanSkill;
import com.example.mhsunbreaksimulator.entity.Armor;
import com.example.mhsunbreaksimulator.entity.Decoration;

@Service
public class SkillAggregationService {

    /*
     * =========================================
     * 防具のみ
     * =========================================
     */
    public List<SkillTotal> calculateSkillTotals(
            List<Armor> armors) {

        return calculateSkillTotals(
                armors,
                List.of(),
                List.of()
        );
    }


    /*
     * =========================================
     * 防具 + 装飾品
     * =========================================
     */
    public List<SkillTotal> calculateSkillTotals(
            List<Armor> armors,
            List<Decoration> decorations) {

        return calculateSkillTotals(
                armors,
                decorations,
                List.of()
        );
    }


    /*
     * =========================================
     * 防具 + 装飾品 + 護石
     * =========================================
     */
    public List<SkillTotal> calculateSkillTotals(
            List<Armor> armors,
            List<Decoration> decorations,
            List<TalismanSkill> talismanSkills) {

        Map<Integer, SkillTotal> skillMap
                = new LinkedHashMap<>();


        /*
         * =========================================
         * 防具スキル
         * =========================================
         */
        if (armors != null) {

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
        }


        /*
         * =========================================
         * 装飾品スキル
         * =========================================
         */
        if (decorations != null) {

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
        }


        /*
         * =========================================
         * 護石スキル
         * =========================================
         */
        if (talismanSkills != null) {

            for (TalismanSkill talismanSkill : talismanSkills) {

                if (talismanSkill == null) {
                    continue;
                }

                addSkill(
                        skillMap,
                        talismanSkill.getSkillId(),
                        talismanSkill.getSkillName(),
                        talismanSkill.getLevel(),
                        talismanSkill.getMaxLevel()
                );
            }
        }


        return skillMap
                .values()
                .stream()
                .toList();
    }


    /*
     * =========================================
     * スキル加算
     * =========================================
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


        /*
         * すでに同じスキルがある場合
         */
        if (skillMap.containsKey(skillId)) {

            SkillTotal existing
                    = skillMap.get(skillId);

            int totalLevel
                    = existing.getLevel()
                    + level;

            int cappedLevel
                    = Math.min(
                            totalLevel,
                            existing.getMaxLevel()
                    );

            existing.setLevel(
                    cappedLevel
            );
        }


        /*
         * 初めて出てきたスキル
         */
        else {

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