package com.example.mhsunbreaksimulator.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.mhsunbreaksimulator.entity.Skill;
import com.example.mhsunbreaksimulator.form.SimulatorForm;
import com.example.mhsunbreaksimulator.service.SkillService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TalismanController {

    private final SkillService skillService;


    /*
     * =========================================
     * 護石編集画面
     * =========================================
     */
    @GetMapping("/talisman")
    public String showTalisman(
            @ModelAttribute SimulatorForm simulatorForm,
            Model model) {

        List<Skill> skills
                = skillService.findAll();

        model.addAttribute(
                "skills",
                skills
        );

        model.addAttribute(
                "simulatorForm",
                simulatorForm
        );

        return "talisman";
    }


    /*
     * =========================================
     * 護石設定
     * =========================================
     */
    @GetMapping("/talisman-apply")
    public String applyTalisman(
            @ModelAttribute SimulatorForm simulatorForm,
            RedirectAttributes redirectAttributes) {

        /*
         * =========================================
         * スキル1をチェック
         * =========================================
         */
        validateSkill1(
                simulatorForm
        );


        /*
         * =========================================
         * スキル2をチェック
         * =========================================
         */
        validateSkill2(
                simulatorForm
        );


        /*
         * =========================================
         * スロットサイズを0～4に制限
         * =========================================
         */
        simulatorForm.setTalismanSlot1Size(
                normalizeSlotSize(
                        simulatorForm.getTalismanSlot1Size()
                )
        );

        simulatorForm.setTalismanSlot2Size(
                normalizeSlotSize(
                        simulatorForm.getTalismanSlot2Size()
                )
        );

        simulatorForm.setTalismanSlot3Size(
                normalizeSlotSize(
                        simulatorForm.getTalismanSlot3Size()
                )
        );


        /*
         * =========================================
         * シミュレーターへ選択状態を返す
         * =========================================
         */
        addSimulatorAttributes(
                simulatorForm,
                redirectAttributes
        );

        return "redirect:/simulator";
    }


    /*
     * =========================================
     * 護石スキル1チェック
     * =========================================
     */
    private void validateSkill1(
            SimulatorForm simulatorForm) {

        Integer skillId
                = simulatorForm.getTalismanSkill1Id();

        /*
         * スキル未選択
         */
        if (skillId == null) {

            simulatorForm.setTalismanSkill1Level(
                    null
            );

            return;
        }


        Skill skill
                = skillService.findById(
                        skillId
                );

        /*
         * 存在しないスキルID
         */
        if (skill == null) {

            simulatorForm.setTalismanSkill1Id(
                    null
            );

            simulatorForm.setTalismanSkill1Level(
                    null
            );

            return;
        }


        Integer level
                = simulatorForm.getTalismanSkill1Level();

        /*
         * Lv未入力ならLv1
         */
        if (level == null || level < 1) {

            simulatorForm.setTalismanSkill1Level(
                    1
            );

            return;
        }


        /*
         * 最大Lv超過なら最大Lvまで
         */
        if (level > skill.getMaxLevel()) {

            simulatorForm.setTalismanSkill1Level(
                    skill.getMaxLevel()
            );
        }
    }


    /*
     * =========================================
     * 護石スキル2チェック
     * =========================================
     */
    private void validateSkill2(
            SimulatorForm simulatorForm) {

        Integer skillId
                = simulatorForm.getTalismanSkill2Id();

        /*
         * スキル未選択
         */
        if (skillId == null) {

            simulatorForm.setTalismanSkill2Level(
                    null
            );

            return;
        }


        Skill skill
                = skillService.findById(
                        skillId
                );

        /*
         * 存在しないスキルID
         */
        if (skill == null) {

            simulatorForm.setTalismanSkill2Id(
                    null
            );

            simulatorForm.setTalismanSkill2Level(
                    null
            );

            return;
        }


        Integer level
                = simulatorForm.getTalismanSkill2Level();

        /*
         * Lv未入力ならLv1
         */
        if (level == null || level < 1) {

            simulatorForm.setTalismanSkill2Level(
                    1
            );

            return;
        }


        /*
         * 最大Lv超過なら最大Lvまで
         */
        if (level > skill.getMaxLevel()) {

            simulatorForm.setTalismanSkill2Level(
                    skill.getMaxLevel()
            );
        }
    }


    /*
     * =========================================
     * スロットサイズを0～4へ正規化
     * =========================================
     */
    private Integer normalizeSlotSize(
            Integer slotSize) {

        if (slotSize == null) {
            return 0;
        }

        if (slotSize < 0) {
            return 0;
        }

        if (slotSize > 4) {
            return 4;
        }

        return slotSize;
    }


    /*
     * =========================================
     * 現在のSimulatorFormを
     * シミュレーターへ引き継ぐ
     * =========================================
     */
    private void addSimulatorAttributes(
            SimulatorForm simulatorForm,
            RedirectAttributes redirectAttributes) {

        /*
         * 武器
         */
        redirectAttributes.addAttribute(
                "weaponId",
                simulatorForm.getWeaponId()
        );

        redirectAttributes.addAttribute(
                "weaponDecoration1Id",
                simulatorForm.getWeaponDecoration1Id()
        );

        redirectAttributes.addAttribute(
                "weaponDecoration2Id",
                simulatorForm.getWeaponDecoration2Id()
        );

        redirectAttributes.addAttribute(
                "weaponDecoration3Id",
                simulatorForm.getWeaponDecoration3Id()
        );


        /*
         * 頭
         */
        redirectAttributes.addAttribute(
                "headArmorId",
                simulatorForm.getHeadArmorId()
        );

        redirectAttributes.addAttribute(
                "headDecoration1Id",
                simulatorForm.getHeadDecoration1Id()
        );

        redirectAttributes.addAttribute(
                "headDecoration2Id",
                simulatorForm.getHeadDecoration2Id()
        );

        redirectAttributes.addAttribute(
                "headDecoration3Id",
                simulatorForm.getHeadDecoration3Id()
        );


        /*
         * 胴
         */
        redirectAttributes.addAttribute(
                "chestArmorId",
                simulatorForm.getChestArmorId()
        );

        redirectAttributes.addAttribute(
                "chestDecoration1Id",
                simulatorForm.getChestDecoration1Id()
        );

        redirectAttributes.addAttribute(
                "chestDecoration2Id",
                simulatorForm.getChestDecoration2Id()
        );

        redirectAttributes.addAttribute(
                "chestDecoration3Id",
                simulatorForm.getChestDecoration3Id()
        );


        /*
         * 腕
         */
        redirectAttributes.addAttribute(
                "armArmorId",
                simulatorForm.getArmArmorId()
        );

        redirectAttributes.addAttribute(
                "armDecoration1Id",
                simulatorForm.getArmDecoration1Id()
        );

        redirectAttributes.addAttribute(
                "armDecoration2Id",
                simulatorForm.getArmDecoration2Id()
        );

        redirectAttributes.addAttribute(
                "armDecoration3Id",
                simulatorForm.getArmDecoration3Id()
        );


        /*
         * 腰
         */
        redirectAttributes.addAttribute(
                "waistArmorId",
                simulatorForm.getWaistArmorId()
        );

        redirectAttributes.addAttribute(
                "waistDecoration1Id",
                simulatorForm.getWaistDecoration1Id()
        );

        redirectAttributes.addAttribute(
                "waistDecoration2Id",
                simulatorForm.getWaistDecoration2Id()
        );

        redirectAttributes.addAttribute(
                "waistDecoration3Id",
                simulatorForm.getWaistDecoration3Id()
        );


        /*
         * 脚
         */
        redirectAttributes.addAttribute(
                "legArmorId",
                simulatorForm.getLegArmorId()
        );

        redirectAttributes.addAttribute(
                "legDecoration1Id",
                simulatorForm.getLegDecoration1Id()
        );

        redirectAttributes.addAttribute(
                "legDecoration2Id",
                simulatorForm.getLegDecoration2Id()
        );

        redirectAttributes.addAttribute(
                "legDecoration3Id",
                simulatorForm.getLegDecoration3Id()
        );


        /*
         * 護石
         */
        redirectAttributes.addAttribute(
                "talismanSkill1Id",
                simulatorForm.getTalismanSkill1Id()
        );

        redirectAttributes.addAttribute(
                "talismanSkill1Level",
                simulatorForm.getTalismanSkill1Level()
        );

        redirectAttributes.addAttribute(
                "talismanSkill2Id",
                simulatorForm.getTalismanSkill2Id()
        );

        redirectAttributes.addAttribute(
                "talismanSkill2Level",
                simulatorForm.getTalismanSkill2Level()
        );

        redirectAttributes.addAttribute(
                "talismanSlot1Size",
                simulatorForm.getTalismanSlot1Size()
        );

        redirectAttributes.addAttribute(
                "talismanSlot2Size",
                simulatorForm.getTalismanSlot2Size()
        );

        redirectAttributes.addAttribute(
                "talismanSlot3Size",
                simulatorForm.getTalismanSlot3Size()
        );
    }
}