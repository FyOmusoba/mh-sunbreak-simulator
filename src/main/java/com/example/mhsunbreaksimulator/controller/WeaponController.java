package com.example.mhsunbreaksimulator.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.mhsunbreaksimulator.entity.Weapon;
import com.example.mhsunbreaksimulator.form.SimulatorForm;
import com.example.mhsunbreaksimulator.service.WeaponService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WeaponController {

    private final WeaponService weaponService;

    // JSON確認用
    @GetMapping("/weapons")
    @ResponseBody
    public List<Weapon> findAll() {

        return weaponService.findAll();
    }

    // 武器一覧画面
    @GetMapping("/weapon-list")
    public String showWeaponList(
            @RequestParam(required = false) String weaponType,
            @ModelAttribute SimulatorForm simulatorForm,
            Model model) {

        List<Weapon> weapons;

        if (weaponType == null || weaponType.isBlank()) {

            weapons = weaponService.findAll();

        } else {

            weapons = weaponService.findByWeaponType(
                    weaponType
            );
        }

        model.addAttribute(
                "weapons",
                weapons
        );

        model.addAttribute(
                "selectedWeaponType",
                weaponType
        );

        model.addAttribute(
                "simulatorForm",
                simulatorForm
        );

        return "weapon-list";
    }

    // 武器詳細画面
    @GetMapping("/weapon/{weaponId}")
    public String showWeaponDetail(
            @PathVariable Integer weaponId,
            @ModelAttribute SimulatorForm simulatorForm,
            Model model) {

        Weapon weapon
                = weaponService.findById(
                        weaponId
                );

        model.addAttribute(
                "weapon",
                weapon
        );

        model.addAttribute(
                "simulatorForm",
                simulatorForm
        );

        return "weapon-detail";
    }
}