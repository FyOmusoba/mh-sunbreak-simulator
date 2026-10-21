package com.example.mhsunbreaksimulator.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.mhsunbreaksimulator.entity.Skill;
import com.example.mhsunbreaksimulator.form.SimulatorForm;
import com.example.mhsunbreaksimulator.service.SkillService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TalismanController {

    private final SkillService skillService;

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

    @GetMapping("/talisman-apply")
    public String applyTalisman(
            @ModelAttribute SimulatorForm simulatorForm,
            @RequestParam Map<String, String> requestParams,
            RedirectAttributes redirectAttributes) {

        validateSkill1(
                simulatorForm
        );

        validateSkill2(
                simulatorForm
        );

        validateDuplicateSkills(
                simulatorForm
        );

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

        validateSlotPattern(
                simulatorForm
        );

        preserveSkillStateParameters(
                requestParams,
                redirectAttributes
        );

        addSimulatorAttributes(
                simulatorForm,
                redirectAttributes
        );

        return "redirect:/simulator";
    }

    private void validateSkill1(
            SimulatorForm simulatorForm) {

        Integer skillId
                = simulatorForm.getTalismanSkill1Id();

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

        if (skill == null
                || skill.getTalismanSkill1MaxLevel() == null) {

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

        Integer maxLevel
                = skill.getTalismanSkill1MaxLevel();

        if (level == null || level < 1) {

            simulatorForm.setTalismanSkill1Level(
                    1
            );

            return;
        }

        if (level > maxLevel) {

            simulatorForm.setTalismanSkill1Level(
                    maxLevel
            );
        }
    }

    private void validateSkill2(
            SimulatorForm simulatorForm) {

        Integer skillId
                = simulatorForm.getTalismanSkill2Id();

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

        if (skill == null
                || skill.getTalismanSkill2MaxLevel() == null) {

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

        Integer maxLevel
                = skill.getTalismanSkill2MaxLevel();

        if (level == null || level < 1) {

            simulatorForm.setTalismanSkill2Level(
                    1
            );

            return;
        }

        if (level > maxLevel) {

            simulatorForm.setTalismanSkill2Level(
                    maxLevel
            );
        }
    }

    private void validateDuplicateSkills(
            SimulatorForm simulatorForm) {

        Integer skill1Id
                = simulatorForm.getTalismanSkill1Id();

        Integer skill2Id
                = simulatorForm.getTalismanSkill2Id();

        if (skill1Id != null
                && skill1Id.equals(skill2Id)) {

            simulatorForm.setTalismanSkill2Id(
                    null
            );

            simulatorForm.setTalismanSkill2Level(
                    null
            );
        }
    }

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

    private void validateSlotPattern(
            SimulatorForm simulatorForm) {

        int slot1
                = simulatorForm.getTalismanSlot1Size();

        int slot2
                = simulatorForm.getTalismanSlot2Size();

        int slot3
                = simulatorForm.getTalismanSlot3Size();

        if (!isValidSlotPattern(
                slot1,
                slot2,
                slot3
        )) {

            simulatorForm.setTalismanSlot1Size(
                    0
            );

            simulatorForm.setTalismanSlot2Size(
                    0
            );

            simulatorForm.setTalismanSlot3Size(
                    0
            );
        }
    }

    private boolean isValidSlotPattern(
            int slot1,
            int slot2,
            int slot3) {

        String pattern
                = slot1
                + "-"
                + slot2
                + "-"
                + slot3;

        return switch (pattern) {

            case "0-0-0",

                 "1-0-0",
                 "1-1-0",
                 "1-1-1",

                 "2-0-0",
                 "2-1-0",
                 "2-1-1",
                 "2-2-0",
                 "2-2-1",
                 "2-2-2",

                 "3-0-0",
                 "3-1-0",
                 "3-1-1",
                 "3-2-0",
                 "3-2-1",
                 "3-3-0",
                 "3-3-1",

                 "4-0-0",
                 "4-1-0",
                 "4-1-1"

                    -> true;

            default
                    -> false;
        };
    }

    private void preserveSkillStateParameters(
            Map<String, String> requestParams,
            RedirectAttributes redirectAttributes) {

        if (requestParams == null
                || requestParams.isEmpty()) {

            return;
        }

        for (Map.Entry<String, String> entry
                : requestParams.entrySet()) {

            String name
                    = entry.getKey();

            String value
                    = entry.getValue();

            if (name == null
                    || !name.startsWith(
                            "skillState_"
                    )) {

                continue;
            }

            if (value == null
                    || value.isBlank()) {

                continue;
            }

            redirectAttributes.addAttribute(
                    name,
                    value
            );
        }
    }

    private void addSimulatorAttributes(
            SimulatorForm simulatorForm,
            RedirectAttributes redirectAttributes) {

        /*
         * ==========================================
         * 武器
         * ==========================================
         */
        addAttributeIfNotNull(
                redirectAttributes,
                "weaponId",
                simulatorForm.getWeaponId()
        );

        /*
         * ==========================================
         * 武器傀異錬成
         * ==========================================
         */
        addAttributeIfNotNull(
                redirectAttributes,
                "weaponAugmentAttackLevel",
                simulatorForm.getWeaponAugmentAttackLevel()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "weaponAugmentElementLevel",
                simulatorForm.getWeaponAugmentElementLevel()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "weaponAugmentAffinityLevel",
                simulatorForm.getWeaponAugmentAffinityLevel()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "weaponAugmentSharpnessLevel",
                simulatorForm.getWeaponAugmentSharpnessLevel()
        );

        /*
         * ==========================================
         * モンスター
         * ==========================================
         */
        addAttributeIfNotNull(
                redirectAttributes,
                "monsterId",
                simulatorForm.getMonsterId()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "hitzoneId",
                simulatorForm.getHitzoneId()
        );

        /*
         * ==========================================
         * 百竜装飾品
         * ==========================================
         */
        addAttributeIfNotNull(
                redirectAttributes,
                "rampageDecorationId",
                simulatorForm.getRampageDecorationId()
        );

        /*
         * ==========================================
         * 弓
         * ==========================================
         */
        addAttributeIfNotNull(
                redirectAttributes,
                "bowAttackType",
                simulatorForm.getBowAttackType()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "bowChargeId",
                simulatorForm.getBowChargeId()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "bowCoatingId",
                simulatorForm.getBowCoatingId()
        );

        /*
         * ==========================================
         * ボウガン
         * ==========================================
         */
        addAttributeIfNotNull(
                redirectAttributes,
                "bowgunAmmoId",
                simulatorForm.getBowgunAmmoId()
        );

        /*
         * ==========================================
         * 武器装飾品
         * ==========================================
         */
        addAttributeIfNotNull(
                redirectAttributes,
                "weaponDecoration1Id",
                simulatorForm.getWeaponDecoration1Id()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "weaponDecoration2Id",
                simulatorForm.getWeaponDecoration2Id()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "weaponDecoration3Id",
                simulatorForm.getWeaponDecoration3Id()
        );

        /*
         * ==========================================
         * 頭
         * ==========================================
         */
        addAttributeIfNotNull(
                redirectAttributes,
                "headArmorId",
                simulatorForm.getHeadArmorId()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "headDecoration1Id",
                simulatorForm.getHeadDecoration1Id()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "headDecoration2Id",
                simulatorForm.getHeadDecoration2Id()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "headDecoration3Id",
                simulatorForm.getHeadDecoration3Id()
        );

        /*
         * ==========================================
         * 胴
         * ==========================================
         */
        addAttributeIfNotNull(
                redirectAttributes,
                "chestArmorId",
                simulatorForm.getChestArmorId()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "chestDecoration1Id",
                simulatorForm.getChestDecoration1Id()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "chestDecoration2Id",
                simulatorForm.getChestDecoration2Id()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "chestDecoration3Id",
                simulatorForm.getChestDecoration3Id()
        );

        /*
         * ==========================================
         * 腕
         * ==========================================
         */
        addAttributeIfNotNull(
                redirectAttributes,
                "armArmorId",
                simulatorForm.getArmArmorId()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "armDecoration1Id",
                simulatorForm.getArmDecoration1Id()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "armDecoration2Id",
                simulatorForm.getArmDecoration2Id()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "armDecoration3Id",
                simulatorForm.getArmDecoration3Id()
        );

        /*
         * ==========================================
         * 腰
         * ==========================================
         */
        addAttributeIfNotNull(
                redirectAttributes,
                "waistArmorId",
                simulatorForm.getWaistArmorId()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "waistDecoration1Id",
                simulatorForm.getWaistDecoration1Id()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "waistDecoration2Id",
                simulatorForm.getWaistDecoration2Id()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "waistDecoration3Id",
                simulatorForm.getWaistDecoration3Id()
        );

        /*
         * ==========================================
         * 脚
         * ==========================================
         */
        addAttributeIfNotNull(
                redirectAttributes,
                "legArmorId",
                simulatorForm.getLegArmorId()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "legDecoration1Id",
                simulatorForm.getLegDecoration1Id()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "legDecoration2Id",
                simulatorForm.getLegDecoration2Id()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "legDecoration3Id",
                simulatorForm.getLegDecoration3Id()
        );

        /*
         * ==========================================
         * 護石スキル
         * ==========================================
         */
        addAttributeIfNotNull(
                redirectAttributes,
                "talismanSkill1Id",
                simulatorForm.getTalismanSkill1Id()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "talismanSkill1Level",
                simulatorForm.getTalismanSkill1Level()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "talismanSkill2Id",
                simulatorForm.getTalismanSkill2Id()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "talismanSkill2Level",
                simulatorForm.getTalismanSkill2Level()
        );

        /*
         * ==========================================
         * 護石スロット
         * ==========================================
         */
        addAttributeIfNotNull(
                redirectAttributes,
                "talismanSlot1Size",
                simulatorForm.getTalismanSlot1Size()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "talismanSlot2Size",
                simulatorForm.getTalismanSlot2Size()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "talismanSlot3Size",
                simulatorForm.getTalismanSlot3Size()
        );

        /*
         * ==========================================
         * 護石装飾品
         * ==========================================
         */
        addAttributeIfNotNull(
                redirectAttributes,
                "talismanDecoration1Id",
                simulatorForm.getTalismanDecoration1Id()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "talismanDecoration2Id",
                simulatorForm.getTalismanDecoration2Id()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "talismanDecoration3Id",
                simulatorForm.getTalismanDecoration3Id()
        );
    }

    private void addAttributeIfNotNull(
            RedirectAttributes redirectAttributes,
            String name,
            Object value) {

        if (value == null) {
            return;
        }

        redirectAttributes.addAttribute(
                name,
                value
        );
    }
}