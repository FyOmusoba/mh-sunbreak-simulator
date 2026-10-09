package com.example.mhsunbreaksimulator.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.mhsunbreaksimulator.entity.RampageDecoration;
import com.example.mhsunbreaksimulator.entity.Weapon;
import com.example.mhsunbreaksimulator.form.SimulatorForm;
import com.example.mhsunbreaksimulator.service.RampageDecorationService;
import com.example.mhsunbreaksimulator.service.WeaponService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RampageDecorationController {

    private final RampageDecorationService rampageDecorationService;
    private final WeaponService weaponService;


    /* ========================================== */
    /* 百竜装飾品選択画面 */
    /* ========================================== */

    @GetMapping("/rampage-decoration-select")
    public String showRampageDecorationSelect(
            @ModelAttribute SimulatorForm simulatorForm,
            Model model) {

        int slotSize
                = getRampageSlotSize(
                        simulatorForm
                );

        List<RampageDecoration> rampageDecorations;

        if (slotSize <= 0) {

            rampageDecorations = List.of();

        } else {

            rampageDecorations
                    = rampageDecorationService
                            .findUsableBySlotSize(
                                    slotSize
                            );
        }

        model.addAttribute(
                "rampageDecorations",
                rampageDecorations
        );

        model.addAttribute(
                "slotSize",
                slotSize
        );

        model.addAttribute(
                "simulatorForm",
                simulatorForm
        );

        return "rampage-decoration-select";
    }


    /* ========================================== */
    /* 百竜装飾品を適用 */
    /* ========================================== */

    @GetMapping("/rampage-decoration-apply")
    public String applyRampageDecoration(
            @RequestParam Integer rampageDecorationId,
            @ModelAttribute SimulatorForm simulatorForm,
            RedirectAttributes redirectAttributes) {

        RampageDecoration rampageDecoration
                = rampageDecorationService.findById(
                        rampageDecorationId
                );

        int actualSlotSize
                = getRampageSlotSize(
                        simulatorForm
                );

        if (rampageDecoration != null
                && actualSlotSize > 0
                && rampageDecoration.getRequiredSlotSize() != null
                && rampageDecoration.getRequiredSlotSize()
                        <= actualSlotSize) {

            simulatorForm.setRampageDecorationId(
                    rampageDecorationId
            );

        } else {

            simulatorForm.setRampageDecorationId(
                    null
            );
        }

        addSimulatorAttributes(
                simulatorForm,
                redirectAttributes
        );

        return "redirect:/simulator";
    }


    /* ========================================== */
    /* 武器の百竜スロットサイズ取得 */
    /* ========================================== */

    private int getRampageSlotSize(
            SimulatorForm simulatorForm) {

        if (simulatorForm.getWeaponId() == null) {
            return 0;
        }

        Weapon weapon
                = weaponService.findById(
                        simulatorForm.getWeaponId()
                );

        if (weapon == null) {
            return 0;
        }

        return safeRampageSlotSize(
                weapon.getRampageSlotSize()
        );
    }


    /* ========================================== */
    /* 百竜スロットサイズ安全化 */
    /* ========================================== */

    private int safeRampageSlotSize(
            Integer slotSize) {

        if (slotSize == null) {
            return 0;
        }

        if (slotSize < 1) {
            return 0;
        }

        if (slotSize > 3) {
            return 3;
        }

        return slotSize;
    }


    /* ========================================== */
    /* シミュレーター状態を維持 */
    /* ========================================== */

    private void addSimulatorAttributes(
            SimulatorForm simulatorForm,
            RedirectAttributes redirectAttributes) {

        /* 武器 */
        redirectAttributes.addAttribute(
                "weaponId",
                simulatorForm.getWeaponId()
        );

        /* 百竜装飾品 */
        redirectAttributes.addAttribute(
                "rampageDecorationId",
                simulatorForm.getRampageDecorationId()
        );


        /* ====================================== */
        /* 防具 */
        /* ====================================== */

        redirectAttributes.addAttribute(
                "headArmorId",
                simulatorForm.getHeadArmorId()
        );

        redirectAttributes.addAttribute(
                "chestArmorId",
                simulatorForm.getChestArmorId()
        );

        redirectAttributes.addAttribute(
                "armArmorId",
                simulatorForm.getArmArmorId()
        );

        redirectAttributes.addAttribute(
                "waistArmorId",
                simulatorForm.getWaistArmorId()
        );

        redirectAttributes.addAttribute(
                "legArmorId",
                simulatorForm.getLegArmorId()
        );


        /* ====================================== */
        /* 武器装飾品 */
        /* ====================================== */

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


        /* ====================================== */
        /* 頭装飾品 */
        /* ====================================== */

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


        /* ====================================== */
        /* 胴装飾品 */
        /* ====================================== */

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


        /* ====================================== */
        /* 腕装飾品 */
        /* ====================================== */

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


        /* ====================================== */
        /* 腰装飾品 */
        /* ====================================== */

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


        /* ====================================== */
        /* 脚装飾品 */
        /* ====================================== */

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


        /* ====================================== */
        /* 護石スキル */
        /* ====================================== */

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


        /* ====================================== */
        /* 護石スロット */
        /* ====================================== */

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


        /* ====================================== */
        /* 護石装飾品 */
        /* ====================================== */

        redirectAttributes.addAttribute(
                "talismanDecoration1Id",
                simulatorForm.getTalismanDecoration1Id()
        );

        redirectAttributes.addAttribute(
                "talismanDecoration2Id",
                simulatorForm.getTalismanDecoration2Id()
        );

        redirectAttributes.addAttribute(
                "talismanDecoration3Id",
                simulatorForm.getTalismanDecoration3Id()
        );
    }
}