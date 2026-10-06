package com.example.mhsunbreaksimulator.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mhsunbreaksimulator.dto.SkillTotal;
import com.example.mhsunbreaksimulator.entity.Armor;
import com.example.mhsunbreaksimulator.entity.Decoration;
import com.example.mhsunbreaksimulator.entity.Weapon;
import com.example.mhsunbreaksimulator.form.SimulatorForm;
import com.example.mhsunbreaksimulator.service.ArmorService;
import com.example.mhsunbreaksimulator.service.DecorationService;
import com.example.mhsunbreaksimulator.service.SkillAggregationService;
import com.example.mhsunbreaksimulator.service.WeaponService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SimulatorController {

    private final WeaponService weaponService;
    private final ArmorService armorService;
    private final DecorationService decorationService;
    private final SkillAggregationService skillAggregationService;

    @GetMapping("/simulator")
    public String showSimulator(
            @ModelAttribute SimulatorForm simulatorForm,
            @RequestParam(required = false) Integer armorId,
            Model model) {

        /*
         * =========================
         * 武器
         * =========================
         */
        Weapon weapon = weaponService.findById(
                simulatorForm.getWeaponId()
        );

        model.addAttribute("weapon", weapon);


        /*
         * =========================
         * 新しく選択された防具を
         * 正しい部位へ設定
         * =========================
         */
        if (armorId != null) {

            Armor selectedArmor = armorService.findById(armorId);

            if (selectedArmor != null) {

                switch (selectedArmor.getPart()) {

                case "頭":
                    simulatorForm.setHeadArmorId(armorId);
                    break;

                case "胴":
                    simulatorForm.setChestArmorId(armorId);
                    break;

                case "腕":
                    simulatorForm.setArmArmorId(armorId);
                    break;

                case "腰":
                    simulatorForm.setWaistArmorId(armorId);
                    break;

                case "脚":
                    simulatorForm.setLegArmorId(armorId);
                    break;

                default:
                    break;
                }
            }
        }


        /*
         * =========================
         * 防具5部位
         * =========================
         */
        Armor headArmor = null;
        Armor chestArmor = null;
        Armor armArmor = null;
        Armor waistArmor = null;
        Armor legArmor = null;

        if (simulatorForm.getHeadArmorId() != null) {
            headArmor = armorService.findById(
                    simulatorForm.getHeadArmorId()
            );
        }

        if (simulatorForm.getChestArmorId() != null) {
            chestArmor = armorService.findById(
                    simulatorForm.getChestArmorId()
            );
        }

        if (simulatorForm.getArmArmorId() != null) {
            armArmor = armorService.findById(
                    simulatorForm.getArmArmorId()
            );
        }

        if (simulatorForm.getWaistArmorId() != null) {
            waistArmor = armorService.findById(
                    simulatorForm.getWaistArmorId()
            );
        }

        if (simulatorForm.getLegArmorId() != null) {
            legArmor = armorService.findById(
                    simulatorForm.getLegArmorId()
            );
        }

        model.addAttribute("headArmor", headArmor);
        model.addAttribute("chestArmor", chestArmor);
        model.addAttribute("armArmor", armArmor);
        model.addAttribute("waistArmor", waistArmor);
        model.addAttribute("legArmor", legArmor);


        /*
         * =========================
         * 武器の装飾品3個
         * =========================
         */
        Decoration weaponDecoration1 = null;
        Decoration weaponDecoration2 = null;
        Decoration weaponDecoration3 = null;

        if (simulatorForm.getWeaponDecoration1Id() != null) {
            weaponDecoration1 = decorationService.findById(
                    simulatorForm.getWeaponDecoration1Id()
            );
        }

        if (simulatorForm.getWeaponDecoration2Id() != null) {
            weaponDecoration2 = decorationService.findById(
                    simulatorForm.getWeaponDecoration2Id()
            );
        }

        if (simulatorForm.getWeaponDecoration3Id() != null) {
            weaponDecoration3 = decorationService.findById(
                    simulatorForm.getWeaponDecoration3Id()
            );
        }

        model.addAttribute("weaponDecoration1", weaponDecoration1);
        model.addAttribute("weaponDecoration2", weaponDecoration2);
        model.addAttribute("weaponDecoration3", weaponDecoration3);


        /*
         * =========================
         * 頭防具の装飾品3個
         * =========================
         */
        Decoration headDecoration1 = null;
        Decoration headDecoration2 = null;
        Decoration headDecoration3 = null;

        if (simulatorForm.getHeadDecoration1Id() != null) {
            headDecoration1 = decorationService.findById(
                    simulatorForm.getHeadDecoration1Id()
            );
        }

        if (simulatorForm.getHeadDecoration2Id() != null) {
            headDecoration2 = decorationService.findById(
                    simulatorForm.getHeadDecoration2Id()
            );
        }

        if (simulatorForm.getHeadDecoration3Id() != null) {
            headDecoration3 = decorationService.findById(
                    simulatorForm.getHeadDecoration3Id()
            );
        }

        model.addAttribute("headDecoration1", headDecoration1);
        model.addAttribute("headDecoration2", headDecoration2);
        model.addAttribute("headDecoration3", headDecoration3);


        /*
         * =========================
         * 胴防具の装飾品3個
         * =========================
         */
        Decoration chestDecoration1 = null;
        Decoration chestDecoration2 = null;
        Decoration chestDecoration3 = null;

        if (simulatorForm.getChestDecoration1Id() != null) {
            chestDecoration1 = decorationService.findById(
                    simulatorForm.getChestDecoration1Id()
            );
        }

        if (simulatorForm.getChestDecoration2Id() != null) {
            chestDecoration2 = decorationService.findById(
                    simulatorForm.getChestDecoration2Id()
            );
        }

        if (simulatorForm.getChestDecoration3Id() != null) {
            chestDecoration3 = decorationService.findById(
                    simulatorForm.getChestDecoration3Id()
            );
        }

        model.addAttribute("chestDecoration1", chestDecoration1);
        model.addAttribute("chestDecoration2", chestDecoration2);
        model.addAttribute("chestDecoration3", chestDecoration3);


        /*
         * =========================
         * 腕防具の装飾品3個
         * =========================
         */
        Decoration armDecoration1 = null;
        Decoration armDecoration2 = null;
        Decoration armDecoration3 = null;

        if (simulatorForm.getArmDecoration1Id() != null) {
            armDecoration1 = decorationService.findById(
                    simulatorForm.getArmDecoration1Id()
            );
        }

        if (simulatorForm.getArmDecoration2Id() != null) {
            armDecoration2 = decorationService.findById(
                    simulatorForm.getArmDecoration2Id()
            );
        }

        if (simulatorForm.getArmDecoration3Id() != null) {
            armDecoration3 = decorationService.findById(
                    simulatorForm.getArmDecoration3Id()
            );
        }

        model.addAttribute("armDecoration1", armDecoration1);
        model.addAttribute("armDecoration2", armDecoration2);
        model.addAttribute("armDecoration3", armDecoration3);


        /*
         * =========================
         * 腰防具の装飾品3個
         * =========================
         */
        Decoration waistDecoration1 = null;
        Decoration waistDecoration2 = null;
        Decoration waistDecoration3 = null;

        if (simulatorForm.getWaistDecoration1Id() != null) {
            waistDecoration1 = decorationService.findById(
                    simulatorForm.getWaistDecoration1Id()
            );
        }

        if (simulatorForm.getWaistDecoration2Id() != null) {
            waistDecoration2 = decorationService.findById(
                    simulatorForm.getWaistDecoration2Id()
            );
        }

        if (simulatorForm.getWaistDecoration3Id() != null) {
            waistDecoration3 = decorationService.findById(
                    simulatorForm.getWaistDecoration3Id()
            );
        }

        model.addAttribute("waistDecoration1", waistDecoration1);
        model.addAttribute("waistDecoration2", waistDecoration2);
        model.addAttribute("waistDecoration3", waistDecoration3);


        /*
         * =========================
         * 脚防具の装飾品3個
         * =========================
         */
        Decoration legDecoration1 = null;
        Decoration legDecoration2 = null;
        Decoration legDecoration3 = null;

        if (simulatorForm.getLegDecoration1Id() != null) {
            legDecoration1 = decorationService.findById(
                    simulatorForm.getLegDecoration1Id()
            );
        }

        if (simulatorForm.getLegDecoration2Id() != null) {
            legDecoration2 = decorationService.findById(
                    simulatorForm.getLegDecoration2Id()
            );
        }

        if (simulatorForm.getLegDecoration3Id() != null) {
            legDecoration3 = decorationService.findById(
                    simulatorForm.getLegDecoration3Id()
            );
        }

        model.addAttribute("legDecoration1", legDecoration1);
        model.addAttribute("legDecoration2", legDecoration2);
        model.addAttribute("legDecoration3", legDecoration3);


        /*
         * =========================
         * 選択中の防具
         * =========================
         */
        List<Armor> selectedArmors = new ArrayList<>();

        selectedArmors.add(headArmor);
        selectedArmors.add(chestArmor);
        selectedArmors.add(armArmor);
        selectedArmors.add(waistArmor);
        selectedArmors.add(legArmor);


        /*
         * =========================
         * 選択中の装飾品
         * =========================
         */
        List<Decoration> selectedDecorations
                = new ArrayList<>();

        /*
         * 武器
         */
        selectedDecorations.add(weaponDecoration1);
        selectedDecorations.add(weaponDecoration2);
        selectedDecorations.add(weaponDecoration3);

        /*
         * 頭
         */
        selectedDecorations.add(headDecoration1);
        selectedDecorations.add(headDecoration2);
        selectedDecorations.add(headDecoration3);

        /*
         * 胴
         */
        selectedDecorations.add(chestDecoration1);
        selectedDecorations.add(chestDecoration2);
        selectedDecorations.add(chestDecoration3);

        /*
         * 腕
         */
        selectedDecorations.add(armDecoration1);
        selectedDecorations.add(armDecoration2);
        selectedDecorations.add(armDecoration3);

        /*
         * 腰
         */
        selectedDecorations.add(waistDecoration1);
        selectedDecorations.add(waistDecoration2);
        selectedDecorations.add(waistDecoration3);

        /*
         * 脚
         */
        selectedDecorations.add(legDecoration1);
        selectedDecorations.add(legDecoration2);
        selectedDecorations.add(legDecoration3);


        /*
         * =========================
         * 防具 + 装飾品
         * スキル合算
         * =========================
         */
        List<SkillTotal> skillTotals
                = skillAggregationService
                        .calculateSkillTotals(
                                selectedArmors,
                                selectedDecorations
                        );

        model.addAttribute("skillTotals", skillTotals);


        /*
         * =========================
         * 現在の選択状態
         * =========================
         */
        model.addAttribute(
                "simulatorForm",
                simulatorForm
        );

        return "simulator";
    }
}