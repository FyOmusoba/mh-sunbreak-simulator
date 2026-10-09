package com.example.mhsunbreaksimulator.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.mhsunbreaksimulator.entity.Armor;
import com.example.mhsunbreaksimulator.entity.Decoration;
import com.example.mhsunbreaksimulator.entity.Weapon;
import com.example.mhsunbreaksimulator.form.SimulatorForm;
import com.example.mhsunbreaksimulator.service.ArmorService;
import com.example.mhsunbreaksimulator.service.DecorationService;
import com.example.mhsunbreaksimulator.service.WeaponService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DecorationController {

    private final DecorationService decorationService;
    private final WeaponService weaponService;
    private final ArmorService armorService;

    @GetMapping("/decoration-list")
    public String showDecorationList(
            @RequestParam(required = false) Integer requiredSlotSize,
            Model model) {

        List<Decoration> decorations;

        if (requiredSlotSize == null) {
            decorations = decorationService.findAll();
        } else {
            decorations = decorationService.findByRequiredSlotSize(requiredSlotSize);
        }

        model.addAttribute("decorations", decorations);
        model.addAttribute("selectedRequiredSlotSize", requiredSlotSize);

        return "decoration-list";
    }

    @GetMapping("/decoration-select")
    public String showDecorationSelect(
            @RequestParam Integer slotSize,
            @RequestParam String target,
            @ModelAttribute SimulatorForm simulatorForm,
            Model model) {

        List<Decoration> decorations
                = decorationService.findUsableBySlotSize(slotSize);

        model.addAttribute("decorations", decorations);
        model.addAttribute("slotSize", slotSize);
        model.addAttribute("target", target);
        model.addAttribute("simulatorForm", simulatorForm);

        return "decoration-select";
    }

    @GetMapping("/decoration-apply")
    public String applyDecoration(
            @RequestParam Integer decorationId,
            @RequestParam String target,
            @ModelAttribute SimulatorForm simulatorForm,
            RedirectAttributes redirectAttributes) {

        Decoration decoration
                = decorationService.findById(decorationId);

        int actualSlotSize
                = getActualSlotSize(
                        target,
                        simulatorForm
                );

        if (decoration != null
                && actualSlotSize > 0
                && decoration.getRequiredSlotSize() != null
                && decoration.getRequiredSlotSize() <= actualSlotSize) {

            applyDecorationToTarget(
                    decorationId,
                    target,
                    simulatorForm
            );
        }

        addSimulatorAttributes(
                simulatorForm,
                redirectAttributes
        );

        return "redirect:/simulator";
    }

    private void applyDecorationToTarget(
            Integer decorationId,
            String target,
            SimulatorForm simulatorForm) {

        switch (target) {

        case "weaponDecoration1Id":
            simulatorForm.setWeaponDecoration1Id(decorationId);
            break;

        case "weaponDecoration2Id":
            simulatorForm.setWeaponDecoration2Id(decorationId);
            break;

        case "weaponDecoration3Id":
            simulatorForm.setWeaponDecoration3Id(decorationId);
            break;

        case "headDecoration1Id":
            simulatorForm.setHeadDecoration1Id(decorationId);
            break;

        case "headDecoration2Id":
            simulatorForm.setHeadDecoration2Id(decorationId);
            break;

        case "headDecoration3Id":
            simulatorForm.setHeadDecoration3Id(decorationId);
            break;

        case "chestDecoration1Id":
            simulatorForm.setChestDecoration1Id(decorationId);
            break;

        case "chestDecoration2Id":
            simulatorForm.setChestDecoration2Id(decorationId);
            break;

        case "chestDecoration3Id":
            simulatorForm.setChestDecoration3Id(decorationId);
            break;

        case "armDecoration1Id":
            simulatorForm.setArmDecoration1Id(decorationId);
            break;

        case "armDecoration2Id":
            simulatorForm.setArmDecoration2Id(decorationId);
            break;

        case "armDecoration3Id":
            simulatorForm.setArmDecoration3Id(decorationId);
            break;

        case "waistDecoration1Id":
            simulatorForm.setWaistDecoration1Id(decorationId);
            break;

        case "waistDecoration2Id":
            simulatorForm.setWaistDecoration2Id(decorationId);
            break;

        case "waistDecoration3Id":
            simulatorForm.setWaistDecoration3Id(decorationId);
            break;

        case "legDecoration1Id":
            simulatorForm.setLegDecoration1Id(decorationId);
            break;

        case "legDecoration2Id":
            simulatorForm.setLegDecoration2Id(decorationId);
            break;

        case "legDecoration3Id":
            simulatorForm.setLegDecoration3Id(decorationId);
            break;

        case "talismanDecoration1Id":
            simulatorForm.setTalismanDecoration1Id(decorationId);
            break;

        case "talismanDecoration2Id":
            simulatorForm.setTalismanDecoration2Id(decorationId);
            break;

        case "talismanDecoration3Id":
            simulatorForm.setTalismanDecoration3Id(decorationId);
            break;

        default:
            break;
        }
    }

    private int getActualSlotSize(
            String target,
            SimulatorForm simulatorForm) {

        if (target.startsWith("weaponDecoration")) {

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

            return switch (target) {

            case "weaponDecoration1Id"
                    -> safeSlotSize(
                            weapon.getSlot1Size()
                    );

            case "weaponDecoration2Id"
                    -> safeSlotSize(
                            weapon.getSlot2Size()
                    );

            case "weaponDecoration3Id"
                    -> safeSlotSize(
                            weapon.getSlot3Size()
                    );

            default -> 0;
            };
        }

        if (target.startsWith("headDecoration")) {

            Armor armor
                    = findArmor(
                            simulatorForm.getHeadArmorId()
                    );

            return getArmorSlotSize(
                    armor,
                    target,
                    "headDecoration"
            );
        }

        if (target.startsWith("chestDecoration")) {

            Armor armor
                    = findArmor(
                            simulatorForm.getChestArmorId()
                    );

            return getArmorSlotSize(
                    armor,
                    target,
                    "chestDecoration"
            );
        }

        if (target.startsWith("armDecoration")) {

            Armor armor
                    = findArmor(
                            simulatorForm.getArmArmorId()
                    );

            return getArmorSlotSize(
                    armor,
                    target,
                    "armDecoration"
            );
        }

        if (target.startsWith("waistDecoration")) {

            Armor armor
                    = findArmor(
                            simulatorForm.getWaistArmorId()
                    );

            return getArmorSlotSize(
                    armor,
                    target,
                    "waistDecoration"
            );
        }

        if (target.startsWith("legDecoration")) {

            Armor armor
                    = findArmor(
                            simulatorForm.getLegArmorId()
                    );

            return getArmorSlotSize(
                    armor,
                    target,
                    "legDecoration"
            );
        }

        if (target.startsWith("talismanDecoration")) {

            return switch (target) {

            case "talismanDecoration1Id"
                    -> normalizeSlotSize(
                            simulatorForm.getTalismanSlot1Size()
                    );

            case "talismanDecoration2Id"
                    -> normalizeSlotSize(
                            simulatorForm.getTalismanSlot2Size()
                    );

            case "talismanDecoration3Id"
                    -> normalizeSlotSize(
                            simulatorForm.getTalismanSlot3Size()
                    );

            default -> 0;
            };
        }

        return 0;
    }

    private Armor findArmor(Integer armorId) {

        if (armorId == null) {
            return null;
        }

        return armorService.findById(armorId);
    }

    private int getArmorSlotSize(
            Armor armor,
            String target,
            String prefix) {

        if (armor == null) {
            return 0;
        }

        if (target.equals(prefix + "1Id")) {
            return safeSlotSize(
                    armor.getSlot1Size()
            );
        }

        if (target.equals(prefix + "2Id")) {
            return safeSlotSize(
                    armor.getSlot2Size()
            );
        }

        if (target.equals(prefix + "3Id")) {
            return safeSlotSize(
                    armor.getSlot3Size()
            );
        }

        return 0;
    }

    private int safeSlotSize(Integer slotSize) {

        if (slotSize == null) {
            return 0;
        }

        return slotSize;
    }

    private int normalizeSlotSize(Integer slotSize) {

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

    private void addSimulatorAttributes(
            SimulatorForm simulatorForm,
            RedirectAttributes redirectAttributes) {

        /* 武器 */
        redirectAttributes.addAttribute(
                "weaponId",
                simulatorForm.getWeaponId()
        );

        /* 防具 */
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

        /* 武器装飾品 */
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

        /* 頭装飾品 */
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

        /* 胴装飾品 */
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

        /* 腕装飾品 */
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

        /* 腰装飾品 */
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

        /* 脚装飾品 */
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

        /* 護石スキル */
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

        /* 護石スロット */
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

        /* 護石装飾品 */
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
