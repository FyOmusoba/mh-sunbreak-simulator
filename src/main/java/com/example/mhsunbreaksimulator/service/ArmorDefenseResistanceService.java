package com.example.mhsunbreaksimulator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.dto.DefenseResistanceResult;
import com.example.mhsunbreaksimulator.entity.Armor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArmorDefenseResistanceService {

    private final DefenseResistanceService
            defenseResistanceService;


    /*
     * =====================================================
     * 防具5部位
     * ＋
     * スキル補正
     *
     * から最終防御・耐性を計算
     * =====================================================
     */
    public DefenseResistanceResult calculate(
            List<Armor> armors,
            String weaponType,
            ConditionalSkillModifierTotal modifier) {

        int baseDefense = 0;

        int baseFireRes = 0;

        int baseWaterRes = 0;

        int baseThunderRes = 0;

        int baseIceRes = 0;

        int baseDragonRes = 0;


        /*
         * =================================================
         * 防具5部位集計
         * =================================================
         */
        if (armors != null) {

            for (Armor armor : armors) {

                if (armor == null) {

                    continue;
                }


                baseDefense
                        += safeInteger(
                                armor.getDefense()
                        );

                baseFireRes
                        += safeInteger(
                                armor.getFireRes()
                        );

                baseWaterRes
                        += safeInteger(
                                armor.getWaterRes()
                        );

                baseThunderRes
                        += safeInteger(
                                armor.getThunderRes()
                        );

                baseIceRes
                        += safeInteger(
                                armor.getIceRes()
                        );

                baseDragonRes
                        += safeInteger(
                                armor.getDragonRes()
                        );
            }
        }


        /*
         * =================================================
         * 最終計算
         * =================================================
         */
        return defenseResistanceService.calculate(
                baseDefense,
                baseFireRes,
                baseWaterRes,
                baseThunderRes,
                baseIceRes,
                baseDragonRes,
                weaponType,
                modifier
        );
    }


    /*
     * =====================================================
     * null対策
     * =====================================================
     */
    private int safeInteger(
            Integer value) {

        return value == null
                ? 0
                : value;
    }
}