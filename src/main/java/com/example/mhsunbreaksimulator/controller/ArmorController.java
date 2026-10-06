package com.example.mhsunbreaksimulator.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mhsunbreaksimulator.entity.Armor;
import com.example.mhsunbreaksimulator.form.SimulatorForm;
import com.example.mhsunbreaksimulator.service.ArmorService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ArmorController {

    private final ArmorService armorService;

    @GetMapping("/armor-list")
    public String showArmorList(
            @RequestParam(required = false) String part,
            @ModelAttribute SimulatorForm simulatorForm,
            Model model) {

        List<Armor> armors;

        if (part == null || part.isBlank()) {
            armors = armorService.findAll();
        } else {
            armors = armorService.findByPart(part);
        }

        model.addAttribute("armors", armors);
        model.addAttribute("selectedPart", part);
        model.addAttribute("simulatorForm", simulatorForm);

        return "armor-list";
    }

    @GetMapping("/armor/{armorId}")
    public String showArmorDetail(
            @PathVariable Integer armorId,
            @ModelAttribute SimulatorForm simulatorForm,
            Model model) {

        Armor armor = armorService.findById(armorId);

        model.addAttribute("armor", armor);
        model.addAttribute("simulatorForm", simulatorForm);

        return "armor-detail";
    }
}