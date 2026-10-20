package com.example.mhsunbreaksimulator.controller;

import java.util.List;
import java.util.Map;

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


    /*
     * =====================================================
     * 百竜装飾品選択画面
     * =====================================================
     */
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


    /*
     * =====================================================
     * 百竜装飾品を適用
     * =====================================================
     */
    @GetMapping("/rampage-decoration-apply")
    public String applyRampageDecoration(
            @RequestParam Integer rampageDecorationId,
            @RequestParam Map<String, String> requestParams,
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


        /*
         * 条件付きスキルはSimulatorFormに存在しないため、
         * requestParamsから別途引き継ぐ。
         */
        preserveSkillStateParameters(
                requestParams,
                redirectAttributes
        );


        /*
         * SimulatorFormで管理している状態を
         * すべてシミュレーターへ戻す。
         */
        addSimulatorAttributes(
                simulatorForm,
                redirectAttributes
        );

        return "redirect:/simulator";
    }


    /*
     * =====================================================
     * 武器の百竜スロットサイズ取得
     * =====================================================
     */
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


    /*
     * =====================================================
     * 百竜スロットサイズ安全化
     * =====================================================
     */
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


    /*
     * =====================================================
     * 条件付きスキル状態を維持
     * =====================================================
     */
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


    /*
     * =====================================================
     * シミュレーター状態を維持
     * =====================================================
     */
    private void addSimulatorAttributes(
            SimulatorForm simulatorForm,
            RedirectAttributes redirectAttributes) {

        /*
         * 武器
         */
        addAttributeIfNotNull(
                redirectAttributes,
                "weaponId",
                simulatorForm.getWeaponId()
        );


        /*
         * モンスター
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
         * 百竜装飾品
         */
        addAttributeIfNotNull(
                redirectAttributes,
                "rampageDecorationId",
                simulatorForm.getRampageDecorationId()
        );


        /*
         * 弓
         */
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
         * ボウガン
         */
        addAttributeIfNotNull(
                redirectAttributes,
                "bowgunAmmoId",
                simulatorForm.getBowgunAmmoId()
        );


        /*
         * 防具
         */
        addAttributeIfNotNull(
                redirectAttributes,
                "headArmorId",
                simulatorForm.getHeadArmorId()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "chestArmorId",
                simulatorForm.getChestArmorId()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "armArmorId",
                simulatorForm.getArmArmorId()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "waistArmorId",
                simulatorForm.getWaistArmorId()
        );

        addAttributeIfNotNull(
                redirectAttributes,
                "legArmorId",
                simulatorForm.getLegArmorId()
        );


        /*
         * 武器装飾品
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
         * 頭装飾品
         */
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
         * 胴装飾品
         */
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
         * 腕装飾品
         */
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
         * 腰装飾品
         */
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
         * 脚装飾品
         */
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
         * 護石スキル
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
         * 護石スロット
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
         * 護石装飾品
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


    /*
     * =====================================================
     * nullではない値だけURLへ追加
     * =====================================================
     */
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