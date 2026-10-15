package com.example.mhsunbreaksimulator.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.mhsunbreaksimulator.dto.BowChargeModifier;
import com.example.mhsunbreaksimulator.dto.BowCoatingModifier;
import com.example.mhsunbreaksimulator.dto.BowElementDamageResult;
import com.example.mhsunbreaksimulator.dto.BowPhysicalDamageResult;
import com.example.mhsunbreaksimulator.dto.BowPhysicalModifier;
import com.example.mhsunbreaksimulator.dto.BowShotModifier;
import com.example.mhsunbreaksimulator.dto.BowgunAmmoModifier;
import com.example.mhsunbreaksimulator.dto.BowgunPhysicalDamageResult;
import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.dto.DefenseResistanceResult;
import com.example.mhsunbreaksimulator.dto.SkillStateSelection;
import com.example.mhsunbreaksimulator.dto.SkillTotal;
import com.example.mhsunbreaksimulator.dto.TalismanSkill;
import com.example.mhsunbreaksimulator.entity.Armor;
import com.example.mhsunbreaksimulator.entity.BowCharge;
import com.example.mhsunbreaksimulator.entity.BowCoating;
import com.example.mhsunbreaksimulator.entity.BowgunAmmo;
import com.example.mhsunbreaksimulator.entity.Decoration;
import com.example.mhsunbreaksimulator.entity.Monster;
import com.example.mhsunbreaksimulator.entity.MonsterHitzone;
import com.example.mhsunbreaksimulator.entity.RampageDecoration;
import com.example.mhsunbreaksimulator.entity.Skill;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;
import com.example.mhsunbreaksimulator.entity.Weapon;
import com.example.mhsunbreaksimulator.form.SimulatorForm;
import com.example.mhsunbreaksimulator.service.ArmorDefenseResistanceService;
import com.example.mhsunbreaksimulator.service.ArmorService;
import com.example.mhsunbreaksimulator.service.BowChargeModifierService;
import com.example.mhsunbreaksimulator.service.BowChargeService;
import com.example.mhsunbreaksimulator.service.BowCoatingModifierService;
import com.example.mhsunbreaksimulator.service.BowCoatingService;
import com.example.mhsunbreaksimulator.service.BowElementDamageService;
import com.example.mhsunbreaksimulator.service.BowPhysicalDamageService;
import com.example.mhsunbreaksimulator.service.BowPhysicalModifierService;
import com.example.mhsunbreaksimulator.service.BowShotModifierService;
import com.example.mhsunbreaksimulator.service.BowgunAmmoModifierService;
import com.example.mhsunbreaksimulator.service.BowgunAmmoService;
import com.example.mhsunbreaksimulator.service.BowgunPhysicalDamageService;
import com.example.mhsunbreaksimulator.service.ConditionalSkillModifierService;
import com.example.mhsunbreaksimulator.service.DecorationService;
import com.example.mhsunbreaksimulator.service.MonsterHitzoneService;
import com.example.mhsunbreaksimulator.service.MonsterService;
import com.example.mhsunbreaksimulator.service.RampageDecorationService;
import com.example.mhsunbreaksimulator.service.ResolvedSkillEffectService;
import com.example.mhsunbreaksimulator.service.SelectedSkillEffectService;
import com.example.mhsunbreaksimulator.service.SkillAggregationService;
import com.example.mhsunbreaksimulator.service.SkillService;
import com.example.mhsunbreaksimulator.service.SkillStateSelectionService;
import com.example.mhsunbreaksimulator.service.WeaponService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SimulatorController {

    private final WeaponService weaponService;

    private final BowChargeService bowChargeService;

    private final BowChargeModifierService bowChargeModifierService;

    private final BowShotModifierService bowShotModifierService;

    private final BowCoatingService bowCoatingService;

    private final BowCoatingModifierService bowCoatingModifierService;

    private final BowElementDamageService bowElementDamageService;

    private final BowgunAmmoService bowgunAmmoService;

    private final BowgunAmmoModifierService bowgunAmmoModifierService;

    private final BowgunPhysicalDamageService bowgunPhysicalDamageService;

    private final BowPhysicalModifierService bowPhysicalModifierService;

    private final BowPhysicalDamageService bowPhysicalDamageService;

    private final ArmorService armorService;

    private final ArmorDefenseResistanceService armorDefenseResistanceService;

    private final MonsterService monsterService;

    private final MonsterHitzoneService monsterHitzoneService;

    private final DecorationService decorationService;

    private final RampageDecorationService rampageDecorationService;

    private final SkillAggregationService skillAggregationService;

    private final SkillService skillService;

    private final SkillStateSelectionService skillStateSelectionService;

    private final SelectedSkillEffectService selectedSkillEffectService;

    private final ResolvedSkillEffectService resolvedSkillEffectService;

    private final ConditionalSkillModifierService conditionalSkillModifierService;


    @GetMapping("/simulator")
    public String showSimulator(
            @ModelAttribute SimulatorForm simulatorForm,
            @RequestParam(required = false) Integer armorId,
            @RequestParam Map<String, String> requestParams,
            Model model) {

        /* ========================================== */
        /* 武器 */
        /* ========================================== */

        Weapon weapon = null;

        if (simulatorForm.getWeaponId() != null) {

            weapon = weaponService.findById(
                    simulatorForm.getWeaponId()
            );
        }

        model.addAttribute(
                "weapon",
                weapon
        );


        /* ========================================== */
        /* モンスター・部位肉質 */
        /* ========================================== */

        List<Monster> monsters
                = monsterService.findAll();

        Monster selectedMonster = null;

        List<MonsterHitzone> monsterHitzones
                = new ArrayList<>();

        MonsterHitzone selectedHitzone = null;


        if (simulatorForm.getMonsterId() != null) {

            selectedMonster
                    = monsterService.findById(
                            simulatorForm.getMonsterId()
                    );


            if (selectedMonster == null) {

                simulatorForm.setMonsterId(
                        null
                );

                simulatorForm.setHitzoneId(
                        null
                );

            } else {

                monsterHitzones
                        = monsterHitzoneService.findByMonsterId(
                                selectedMonster.getMonsterId()
                        );


                if (simulatorForm.getHitzoneId() != null) {

                    selectedHitzone
                            = monsterHitzoneService.findByIdAndMonsterId(
                                    simulatorForm.getHitzoneId(),
                                    selectedMonster.getMonsterId()
                            );


                    if (selectedHitzone == null) {

                        simulatorForm.setHitzoneId(
                                null
                        );
                    }
                }
            }

        } else {

            simulatorForm.setHitzoneId(
                    null
            );
        }


        model.addAttribute(
                "monsters",
                monsters
        );

        model.addAttribute(
                "selectedMonster",
                selectedMonster
        );

        model.addAttribute(
                "monsterHitzones",
                monsterHitzones
        );

        model.addAttribute(
                "selectedHitzone",
                selectedHitzone
        );


        /* ========================================== */
        /* ボウガンの弾 */
        /* ========================================== */

        List<BowgunAmmo> bowgunAmmos
                = new ArrayList<>();

        BowgunAmmo selectedBowgunAmmo = null;


        if (weapon != null
                && ("ライトボウガン".equals(
                        weapon.getWeaponType()
                )
                || "ヘビィボウガン".equals(
                        weapon.getWeaponType()
                ))) {

            bowgunAmmos
                    = bowgunAmmoService.findByWeaponId(
                            weapon.getWeaponId()
                    );


            if (simulatorForm.getBowgunAmmoId() != null) {

                BowgunAmmo candidateBowgunAmmo
                        = bowgunAmmoService.findById(
                                simulatorForm.getBowgunAmmoId()
                        );


                if (candidateBowgunAmmo != null
                        && weapon.getWeaponId().equals(
                                candidateBowgunAmmo.getWeaponId()
                        )) {

                    selectedBowgunAmmo
                            = candidateBowgunAmmo;

                } else {

                    simulatorForm.setBowgunAmmoId(
                            null
                    );
                }
            }

        } else {

            simulatorForm.setBowgunAmmoId(
                    null
            );
        }


        model.addAttribute(
                "bowgunAmmos",
                bowgunAmmos
        );

        model.addAttribute(
                "selectedBowgunAmmo",
                selectedBowgunAmmo
        );


        BowgunAmmoModifier bowgunAmmoModifier
                = bowgunAmmoModifierService.calculate(
                        selectedBowgunAmmo
                );


        model.addAttribute(
                "bowgunAmmoModifier",
                bowgunAmmoModifier
        );


        /* ========================================== */
        /* 弓の溜め段階 */
        /* ========================================== */

        List<BowCharge> bowCharges
                = new ArrayList<>();

        BowCharge selectedBowCharge = null;


        if (weapon != null
                && "弓".equals(
                        weapon.getWeaponType()
                )) {

            bowCharges
                    = bowChargeService.findByWeaponId(
                            weapon.getWeaponId()
                    );


            if (simulatorForm.getBowChargeId() != null) {

                BowCharge candidateBowCharge
                        = bowChargeService.findById(
                                simulatorForm.getBowChargeId()
                        );


                if (candidateBowCharge != null
                        && weapon.getWeaponId().equals(
                                candidateBowCharge.getWeaponId()
                        )) {

                    selectedBowCharge
                            = candidateBowCharge;

                } else {

                    simulatorForm.setBowChargeId(
                            null
                    );
                }
            }

        } else {

            simulatorForm.setBowChargeId(
                    null
            );
        }


        model.addAttribute(
                "bowCharges",
                bowCharges
        );

        model.addAttribute(
                "selectedBowCharge",
                selectedBowCharge
        );


        BowChargeModifier bowChargeModifier
                = bowChargeModifierService.calculate(
                        selectedBowCharge == null
                                ? null
                                : selectedBowCharge.getChargeLevel()
                );


        model.addAttribute(
                "bowChargeModifier",
                bowChargeModifier
        );


        BowShotModifier bowShotModifier
                = bowShotModifierService.calculate(
                        selectedBowCharge == null
                                ? null
                                : selectedBowCharge.getShotType(),
                        selectedBowCharge == null
                                ? null
                                : selectedBowCharge.getShotLevel()
                );


        model.addAttribute(
                "bowShotModifier",
                bowShotModifier
        );


        /* ========================================== */
        /* 弓のビン */
        /* ========================================== */

        List<BowCoating> bowCoatings
                = new ArrayList<>();

        BowCoating selectedBowCoating = null;


        if (weapon != null
                && "弓".equals(
                        weapon.getWeaponType()
                )) {

            bowCoatings
                    = bowCoatingService.findByWeaponId(
                            weapon.getWeaponId()
                    );


            if (simulatorForm.getBowCoatingId() != null) {

                BowCoating candidateBowCoating
                        = bowCoatingService.findById(
                                simulatorForm.getBowCoatingId()
                        );


                if (candidateBowCoating != null
                        && weapon.getWeaponId().equals(
                                candidateBowCoating.getWeaponId()
                        )) {

                    selectedBowCoating
                            = candidateBowCoating;

                } else {

                    simulatorForm.setBowCoatingId(
                            null
                    );
                }
            }

        } else {

            simulatorForm.setBowCoatingId(
                    null
            );
        }


        model.addAttribute(
                "bowCoatings",
                bowCoatings
        );

        model.addAttribute(
                "selectedBowCoating",
                selectedBowCoating
        );


        BowCoatingModifier bowCoatingModifier
                = bowCoatingModifierService.calculate(
                        selectedBowCoating == null
                                ? null
                                : selectedBowCoating.getCoatingType()
                );


        model.addAttribute(
                "bowCoatingModifier",
                bowCoatingModifier
        );


        /* ========================================== */
        /* 百竜装飾品 */
        /* ========================================== */

        RampageDecoration rampageDecoration
                = findUsableRampageDecoration(
                        simulatorForm.getRampageDecorationId(),
                        weapon == null
                                ? 0
                                : safeSlotSize(
                                        weapon.getRampageSlotSize()
                                )
                );


        if (simulatorForm.getRampageDecorationId() != null
                && rampageDecoration == null) {

            simulatorForm.setRampageDecorationId(
                    null
            );
        }


        model.addAttribute(
                "rampageDecoration",
                rampageDecoration
        );


        /* ========================================== */
        /* 新しく選択した防具を反映 */
        /* ========================================== */

        if (armorId != null) {

            Armor selectedArmor
                    = armorService.findById(
                            armorId
                    );


            if (selectedArmor != null) {

                switch (selectedArmor.getPart()) {

                case "頭":

                    simulatorForm.setHeadArmorId(
                            armorId
                    );

                    break;


                case "胴":

                    simulatorForm.setChestArmorId(
                            armorId
                    );

                    break;


                case "腕":

                    simulatorForm.setArmArmorId(
                            armorId
                    );

                    break;


                case "腰":

                    simulatorForm.setWaistArmorId(
                            armorId
                    );

                    break;


                case "脚":

                    simulatorForm.setLegArmorId(
                            armorId
                    );

                    break;


                default:

                    break;
                }
            }
        }


        /* ========================================== */
        /* 防具取得 */
        /* ========================================== */

        Armor headArmor
                = findArmor(
                        simulatorForm.getHeadArmorId()
                );

        Armor chestArmor
                = findArmor(
                        simulatorForm.getChestArmorId()
                );

        Armor armArmor
                = findArmor(
                        simulatorForm.getArmArmorId()
                );

        Armor waistArmor
                = findArmor(
                        simulatorForm.getWaistArmorId()
                );

        Armor legArmor
                = findArmor(
                        simulatorForm.getLegArmorId()
                );


        model.addAttribute(
                "headArmor",
                headArmor
        );

        model.addAttribute(
                "chestArmor",
                chestArmor
        );

        model.addAttribute(
                "armArmor",
                armArmor
        );

        model.addAttribute(
                "waistArmor",
                waistArmor
        );

        model.addAttribute(
                "legArmor",
                legArmor
        );


        /* ========================================== */
        /* 武器装飾品 */
        /* ========================================== */

        Decoration weaponDecoration1
                = findUsableDecoration(
                        simulatorForm.getWeaponDecoration1Id(),
                        weapon == null
                                ? 0
                                : safeSlotSize(
                                        weapon.getSlot1Size()
                                )
                );


        if (simulatorForm.getWeaponDecoration1Id() != null
                && weaponDecoration1 == null) {

            simulatorForm.setWeaponDecoration1Id(
                    null
            );
        }


        Decoration weaponDecoration2
                = findUsableDecoration(
                        simulatorForm.getWeaponDecoration2Id(),
                        weapon == null
                                ? 0
                                : safeSlotSize(
                                        weapon.getSlot2Size()
                                )
                );


        if (simulatorForm.getWeaponDecoration2Id() != null
                && weaponDecoration2 == null) {

            simulatorForm.setWeaponDecoration2Id(
                    null
            );
        }


        Decoration weaponDecoration3
                = findUsableDecoration(
                        simulatorForm.getWeaponDecoration3Id(),
                        weapon == null
                                ? 0
                                : safeSlotSize(
                                        weapon.getSlot3Size()
                                )
                );


        if (simulatorForm.getWeaponDecoration3Id() != null
                && weaponDecoration3 == null) {

            simulatorForm.setWeaponDecoration3Id(
                    null
            );
        }


        model.addAttribute(
                "weaponDecoration1",
                weaponDecoration1
        );

        model.addAttribute(
                "weaponDecoration2",
                weaponDecoration2
        );

        model.addAttribute(
                "weaponDecoration3",
                weaponDecoration3
        );


        /* ========================================== */
        /* 頭装飾品 */
        /* ========================================== */

        Decoration headDecoration1
                = findUsableDecoration(
                        simulatorForm.getHeadDecoration1Id(),
                        getArmorSlotSize(
                                headArmor,
                                1
                        )
                );


        if (simulatorForm.getHeadDecoration1Id() != null
                && headDecoration1 == null) {

            simulatorForm.setHeadDecoration1Id(
                    null
            );
        }


        Decoration headDecoration2
                = findUsableDecoration(
                        simulatorForm.getHeadDecoration2Id(),
                        getArmorSlotSize(
                                headArmor,
                                2
                        )
                );


        if (simulatorForm.getHeadDecoration2Id() != null
                && headDecoration2 == null) {

            simulatorForm.setHeadDecoration2Id(
                    null
            );
        }


        Decoration headDecoration3
                = findUsableDecoration(
                        simulatorForm.getHeadDecoration3Id(),
                        getArmorSlotSize(
                                headArmor,
                                3
                        )
                );


        if (simulatorForm.getHeadDecoration3Id() != null
                && headDecoration3 == null) {

            simulatorForm.setHeadDecoration3Id(
                    null
            );
        }


        model.addAttribute(
                "headDecoration1",
                headDecoration1
        );

        model.addAttribute(
                "headDecoration2",
                headDecoration2
        );

        model.addAttribute(
                "headDecoration3",
                headDecoration3
        );


        /* ========================================== */
        /* 胴装飾品 */
        /* ========================================== */

        Decoration chestDecoration1
                = findUsableDecoration(
                        simulatorForm.getChestDecoration1Id(),
                        getArmorSlotSize(
                                chestArmor,
                                1
                        )
                );


        if (simulatorForm.getChestDecoration1Id() != null
                && chestDecoration1 == null) {

            simulatorForm.setChestDecoration1Id(
                    null
            );
        }


        Decoration chestDecoration2
                = findUsableDecoration(
                        simulatorForm.getChestDecoration2Id(),
                        getArmorSlotSize(
                                chestArmor,
                                2
                        )
                );


        if (simulatorForm.getChestDecoration2Id() != null
                && chestDecoration2 == null) {

            simulatorForm.setChestDecoration2Id(
                    null
            );
        }


        Decoration chestDecoration3
                = findUsableDecoration(
                        simulatorForm.getChestDecoration3Id(),
                        getArmorSlotSize(
                                chestArmor,
                                3
                        )
                );


        if (simulatorForm.getChestDecoration3Id() != null
                && chestDecoration3 == null) {

            simulatorForm.setChestDecoration3Id(
                    null
            );
        }


        model.addAttribute(
                "chestDecoration1",
                chestDecoration1
        );

        model.addAttribute(
                "chestDecoration2",
                chestDecoration2
        );

        model.addAttribute(
                "chestDecoration3",
                chestDecoration3
        );


        /* ========================================== */
        /* 腕装飾品 */
        /* ========================================== */

        Decoration armDecoration1
                = findUsableDecoration(
                        simulatorForm.getArmDecoration1Id(),
                        getArmorSlotSize(
                                armArmor,
                                1
                        )
                );


        if (simulatorForm.getArmDecoration1Id() != null
                && armDecoration1 == null) {

            simulatorForm.setArmDecoration1Id(
                    null
            );
        }


        Decoration armDecoration2
                = findUsableDecoration(
                        simulatorForm.getArmDecoration2Id(),
                        getArmorSlotSize(
                                armArmor,
                                2
                        )
                );


        if (simulatorForm.getArmDecoration2Id() != null
                && armDecoration2 == null) {

            simulatorForm.setArmDecoration2Id(
                    null
            );
        }


        Decoration armDecoration3
                = findUsableDecoration(
                        simulatorForm.getArmDecoration3Id(),
                        getArmorSlotSize(
                                armArmor,
                                3
                        )
                );


        if (simulatorForm.getArmDecoration3Id() != null
                && armDecoration3 == null) {

            simulatorForm.setArmDecoration3Id(
                    null
            );
        }


        model.addAttribute(
                "armDecoration1",
                armDecoration1
        );

        model.addAttribute(
                "armDecoration2",
                armDecoration2
        );

        model.addAttribute(
                "armDecoration3",
                armDecoration3
        );


        /* ========================================== */
        /* 腰装飾品 */
        /* ========================================== */

        Decoration waistDecoration1
                = findUsableDecoration(
                        simulatorForm.getWaistDecoration1Id(),
                        getArmorSlotSize(
                                waistArmor,
                                1
                        )
                );


        if (simulatorForm.getWaistDecoration1Id() != null
                && waistDecoration1 == null) {

            simulatorForm.setWaistDecoration1Id(
                    null
            );
        }


        Decoration waistDecoration2
                = findUsableDecoration(
                        simulatorForm.getWaistDecoration2Id(),
                        getArmorSlotSize(
                                waistArmor,
                                2
                        )
                );


        if (simulatorForm.getWaistDecoration2Id() != null
                && waistDecoration2 == null) {

            simulatorForm.setWaistDecoration2Id(
                    null
            );
        }


        Decoration waistDecoration3
                = findUsableDecoration(
                        simulatorForm.getWaistDecoration3Id(),
                        getArmorSlotSize(
                                waistArmor,
                                3
                        )
                );


        if (simulatorForm.getWaistDecoration3Id() != null
                && waistDecoration3 == null) {

            simulatorForm.setWaistDecoration3Id(
                    null
            );
        }


        model.addAttribute(
                "waistDecoration1",
                waistDecoration1
        );

        model.addAttribute(
                "waistDecoration2",
                waistDecoration2
        );

        model.addAttribute(
                "waistDecoration3",
                waistDecoration3
        );


        /* ========================================== */
        /* 脚装飾品 */
        /* ========================================== */

        Decoration legDecoration1
                = findUsableDecoration(
                        simulatorForm.getLegDecoration1Id(),
                        getArmorSlotSize(
                                legArmor,
                                1
                        )
                );


        if (simulatorForm.getLegDecoration1Id() != null
                && legDecoration1 == null) {

            simulatorForm.setLegDecoration1Id(
                    null
            );
        }


        Decoration legDecoration2
                = findUsableDecoration(
                        simulatorForm.getLegDecoration2Id(),
                        getArmorSlotSize(
                                legArmor,
                                2
                        )
                );


        if (simulatorForm.getLegDecoration2Id() != null
                && legDecoration2 == null) {

            simulatorForm.setLegDecoration2Id(
                    null
            );
        }


        Decoration legDecoration3
                = findUsableDecoration(
                        simulatorForm.getLegDecoration3Id(),
                        getArmorSlotSize(
                                legArmor,
                                3
                        )
                );


        if (simulatorForm.getLegDecoration3Id() != null
                && legDecoration3 == null) {

            simulatorForm.setLegDecoration3Id(
                    null
            );
        }


        model.addAttribute(
                "legDecoration1",
                legDecoration1
        );

        model.addAttribute(
                "legDecoration2",
                legDecoration2
        );

        model.addAttribute(
                "legDecoration3",
                legDecoration3
        );


        /* ========================================== */
        /* 護石スロットサイズを正規化 */
        /* ========================================== */

        int talismanSlot1Size
                = normalizeSlotSize(
                        simulatorForm.getTalismanSlot1Size()
                );

        int talismanSlot2Size
                = normalizeSlotSize(
                        simulatorForm.getTalismanSlot2Size()
                );

        int talismanSlot3Size
                = normalizeSlotSize(
                        simulatorForm.getTalismanSlot3Size()
                );


        simulatorForm.setTalismanSlot1Size(
                talismanSlot1Size
        );

        simulatorForm.setTalismanSlot2Size(
                talismanSlot2Size
        );

        simulatorForm.setTalismanSlot3Size(
                talismanSlot3Size
        );


        /* ========================================== */
        /* 護石装飾品 */
        /* ========================================== */

        Decoration talismanDecoration1
                = findUsableDecoration(
                        simulatorForm.getTalismanDecoration1Id(),
                        talismanSlot1Size
                );


        if (simulatorForm.getTalismanDecoration1Id() != null
                && talismanDecoration1 == null) {

            simulatorForm.setTalismanDecoration1Id(
                    null
            );
        }


        Decoration talismanDecoration2
                = findUsableDecoration(
                        simulatorForm.getTalismanDecoration2Id(),
                        talismanSlot2Size
                );


        if (simulatorForm.getTalismanDecoration2Id() != null
                && talismanDecoration2 == null) {

            simulatorForm.setTalismanDecoration2Id(
                    null
            );
        }


        Decoration talismanDecoration3
                = findUsableDecoration(
                        simulatorForm.getTalismanDecoration3Id(),
                        talismanSlot3Size
                );


        if (simulatorForm.getTalismanDecoration3Id() != null
                && talismanDecoration3 == null) {

            simulatorForm.setTalismanDecoration3Id(
                    null
            );
        }


        model.addAttribute(
                "talismanDecoration1",
                talismanDecoration1
        );

        model.addAttribute(
                "talismanDecoration2",
                talismanDecoration2
        );

        model.addAttribute(
                "talismanDecoration3",
                talismanDecoration3
        );


        /* ========================================== */
        /* 選択防具一覧 */
        /* ========================================== */

        List<Armor> selectedArmors
                = new ArrayList<>();

        selectedArmors.add(
                headArmor
        );

        selectedArmors.add(
                chestArmor
        );

        selectedArmors.add(
                armArmor
        );

        selectedArmors.add(
                waistArmor
        );

        selectedArmors.add(
                legArmor
        );


        /* ========================================== */
        /* 選択装飾品一覧 */
        /* ========================================== */

        List<Decoration> selectedDecorations
                = new ArrayList<>();

        selectedDecorations.add(
                weaponDecoration1
        );

        selectedDecorations.add(
                weaponDecoration2
        );

        selectedDecorations.add(
                weaponDecoration3
        );

        selectedDecorations.add(
                headDecoration1
        );

        selectedDecorations.add(
                headDecoration2
        );

        selectedDecorations.add(
                headDecoration3
        );

        selectedDecorations.add(
                chestDecoration1
        );

        selectedDecorations.add(
                chestDecoration2
        );

        selectedDecorations.add(
                chestDecoration3
        );

        selectedDecorations.add(
                armDecoration1
        );

        selectedDecorations.add(
                armDecoration2
        );

        selectedDecorations.add(
                armDecoration3
        );

        selectedDecorations.add(
                waistDecoration1
        );

        selectedDecorations.add(
                waistDecoration2
        );

        selectedDecorations.add(
                waistDecoration3
        );

        selectedDecorations.add(
                legDecoration1
        );

        selectedDecorations.add(
                legDecoration2
        );

        selectedDecorations.add(
                legDecoration3
        );

        selectedDecorations.add(
                talismanDecoration1
        );

        selectedDecorations.add(
                talismanDecoration2
        );

        selectedDecorations.add(
                talismanDecoration3
        );


        /* ========================================== */
        /* 護石スキル */
        /* ========================================== */

        List<TalismanSkill> talismanSkills
                = new ArrayList<>();


        TalismanSkill talismanSkill1
                = createTalismanSkill(
                        simulatorForm.getTalismanSkill1Id(),
                        simulatorForm.getTalismanSkill1Level()
                );


        if (simulatorForm.getTalismanSkill1Id() != null
                && talismanSkill1 == null) {

            simulatorForm.setTalismanSkill1Id(
                    null
            );

            simulatorForm.setTalismanSkill1Level(
                    null
            );
        }


        if (talismanSkill1 != null) {

            simulatorForm.setTalismanSkill1Level(
                    talismanSkill1.getLevel()
            );

            talismanSkills.add(
                    talismanSkill1
            );
        }


        TalismanSkill talismanSkill2
                = createTalismanSkill(
                        simulatorForm.getTalismanSkill2Id(),
                        simulatorForm.getTalismanSkill2Level()
                );


        if (simulatorForm.getTalismanSkill2Id() != null
                && talismanSkill2 == null) {

            simulatorForm.setTalismanSkill2Id(
                    null
            );

            simulatorForm.setTalismanSkill2Level(
                    null
            );
        }


        if (talismanSkill2 != null) {

            simulatorForm.setTalismanSkill2Level(
                    talismanSkill2.getLevel()
            );

            talismanSkills.add(
                    talismanSkill2
            );
        }


        model.addAttribute(
                "talismanSkill1",
                talismanSkill1
        );

        model.addAttribute(
                "talismanSkill2",
                talismanSkill2
        );


        /* ========================================== */
        /* スキル合計 */
        /* ========================================== */

        List<SkillTotal> skillTotals
                = skillAggregationService.calculateSkillTotals(
                        selectedArmors,
                        selectedDecorations,
                        talismanSkills
                );


        model.addAttribute(
                "skillTotals",
                skillTotals
        );


        /* ========================================== */
        /* 条件付きスキル状態選択 */
        /* ========================================== */

        List<SkillStateSelection> skillStateSelections
                = skillStateSelectionService.createSelections(
                        skillTotals,
                        weapon == null
                                ? null
                                : weapon.getWeaponType()
                );


        model.addAttribute(
                "skillStateSelections",
                skillStateSelections
        );


        /* ========================================== */
        /* 選択中の条件付きスキル効果 */
        /* ========================================== */

        Map<Integer, SkillEffectState> selectedSkillEffects
                = selectedSkillEffectService.resolveSelectedEffects(
                        skillStateSelections,
                        requestParams
                );


        model.addAttribute(
                "selectedSkillEffects",
                selectedSkillEffects
        );


        /* ========================================== */
        /* DEFAULT + 条件付きスキル効果を統合 */
        /* ========================================== */

        Map<Integer, SkillEffectState> resolvedSkillEffects
                = resolvedSkillEffectService.resolveAllEffects(
                        skillTotals,
                        selectedSkillEffects,
                        weapon == null
                                ? null
                                : weapon.getWeaponType()
                );


        model.addAttribute(
                "resolvedSkillEffects",
                resolvedSkillEffects
        );


        /* ========================================== */
        /* 最終スキル補正合計 */
        /* ========================================== */

        ConditionalSkillModifierTotal conditionalSkillModifierTotal
                = conditionalSkillModifierService.calculate(
                        resolvedSkillEffects
                );


        model.addAttribute(
                "conditionalSkillModifierTotal",
                conditionalSkillModifierTotal
        );


        /* ========================================== */
        /* 防具5部位 + スキル補正の最終防御・耐性 */
        /* ========================================== */

        DefenseResistanceResult defenseResistanceResult
                = armorDefenseResistanceService.calculate(
                        selectedArmors,
                        weapon == null
                                ? null
                                : weapon.getWeaponType(),
                        conditionalSkillModifierTotal
                );


        model.addAttribute(
                "defenseResistanceResult",
                defenseResistanceResult
        );


        /* ========================================== */
        /* 弓溜め段階解放の適用 */
        /* ========================================== */

        boolean bowChargeLevelUnlocked
                = hasSkill(
                        skillTotals,
                        "弓溜め段階解放"
                );


        if (selectedBowCharge != null
                && Boolean.FALSE.equals(
                        selectedBowCharge.getInitiallyAvailable()
                )
                && !bowChargeLevelUnlocked) {

            simulatorForm.setBowChargeId(
                    null
            );

            selectedBowCharge = null;
        }


        model.addAttribute(
                "bowChargeLevelUnlocked",
                bowChargeLevelUnlocked
        );

        model.addAttribute(
                "selectedBowCharge",
                selectedBowCharge
        );


        /* ========================================== */
        /* 弓の最終物理係数 */
        /* ========================================== */

        BowPhysicalModifier bowPhysicalModifier
                = bowPhysicalModifierService.calculate(
                        selectedBowCharge == null
                                ? null
                                : bowChargeModifier,
                        selectedBowCharge == null
                                ? null
                                : bowShotModifier,
                        bowCoatingModifier,
                        conditionalSkillModifierTotal
                );


        model.addAttribute(
                "bowPhysicalModifier",
                bowPhysicalModifier
        );


        /* ========================================== */
        /* 弓の物理ダメージ計算途中値 */
        /* ========================================== */

        BowPhysicalDamageResult bowPhysicalDamageResult
                = bowPhysicalDamageService.calculate(
                        weapon == null
                                ? null
                                : weapon.getAttack(),
                        bowPhysicalModifier,
                        conditionalSkillModifierTotal
                );


        model.addAttribute(
                "bowPhysicalDamageResult",
                bowPhysicalDamageResult
        );


        /* ========================================== */
        /* 弓の属性ダメージ計算途中値 */
        /* ========================================== */

        BowElementDamageResult bowElementDamageResult
                = bowElementDamageService.calculate(
                        weapon == null
                                ? null
                                : weapon.getElementValue(),
                        selectedBowCharge == null
                                ? null
                                : bowChargeModifier,
                        bowShotModifier == null
                                ? null
                                : bowShotModifier.getMaxHitCount(),
                        conditionalSkillModifierTotal
                );


        model.addAttribute(
                "bowElementDamageResult",
                bowElementDamageResult
        );


        /* ========================================== */
        /* ボウガンの物理ダメージ計算途中値 */
        /* ========================================== */

        BowgunPhysicalDamageResult bowgunPhysicalDamageResult
                = bowgunPhysicalDamageService.calculate(
                        weapon == null
                                ? null
                                : weapon.getAttack(),
                        selectedBowgunAmmo == null
                                ? null
                                : bowgunAmmoModifier,
                        conditionalSkillModifierTotal
                );


        model.addAttribute(
                "bowgunPhysicalDamageResult",
                bowgunPhysicalDamageResult
        );


        /* ========================================== */
        /* 護石編集URL */
        /* ========================================== */

        String talismanUrl
                = buildTalismanUrl(
                        simulatorForm
                );


        model.addAttribute(
                "talismanUrl",
                talismanUrl
        );


        model.addAttribute(
                "simulatorForm",
                simulatorForm
        );


        return "simulator";
    }


    private boolean hasSkill(
            List<SkillTotal> skillTotals,
            String skillName) {

        if (skillTotals == null
                || skillName == null) {

            return false;
        }


        for (SkillTotal skillTotal : skillTotals) {

            if (skillTotal != null
                    && skillName.equals(
                            skillTotal.getSkillName()
                    )
                    && skillTotal.getLevel() != null
                    && skillTotal.getLevel() > 0) {

                return true;
            }
        }


        return false;
    }


    private Armor findArmor(
            Integer armorId) {

        if (armorId == null) {

            return null;
        }


        return armorService.findById(
                armorId
        );
    }


    private RampageDecoration findUsableRampageDecoration(
            Integer rampageDecorationId,
            int slotSize) {

        if (rampageDecorationId == null) {

            return null;
        }


        if (slotSize <= 0) {

            return null;
        }


        RampageDecoration rampageDecoration
                = rampageDecorationService.findById(
                        rampageDecorationId
                );


        if (rampageDecoration == null) {

            return null;
        }


        if (rampageDecoration.getRequiredSlotSize() == null) {

            return null;
        }


        if (rampageDecoration.getRequiredSlotSize() > slotSize) {

            return null;
        }


        return rampageDecoration;
    }


    private Decoration findUsableDecoration(
            Integer decorationId,
            int slotSize) {

        if (decorationId == null) {

            return null;
        }


        if (slotSize <= 0) {

            return null;
        }


        Decoration decoration
                = decorationService.findById(
                        decorationId
                );


        if (decoration == null) {

            return null;
        }


        if (decoration.getRequiredSlotSize() == null) {

            return null;
        }


        if (decoration.getRequiredSlotSize() > slotSize) {

            return null;
        }


        return decoration;
    }


    private TalismanSkill createTalismanSkill(
            Integer skillId,
            Integer level) {

        if (skillId == null) {

            return null;
        }


        Skill skill
                = skillService.findById(
                        skillId
                );


        if (skill == null
                || skill.getMaxLevel() == null) {

            return null;
        }


        int normalizedLevel;


        if (level == null
                || level < 1) {

            normalizedLevel = 1;

        } else {

            normalizedLevel
                    = Math.min(
                            level,
                            skill.getMaxLevel()
                    );
        }


        return new TalismanSkill(
                skill.getSkillId(),
                skill.getSkillName(),
                normalizedLevel,
                skill.getMaxLevel()
        );
    }


    private int getArmorSlotSize(
            Armor armor,
            int slotNumber) {

        if (armor == null) {

            return 0;
        }


        return switch (slotNumber) {

        case 1 ->

            safeSlotSize(
                    armor.getSlot1Size()
            );


        case 2 ->

            safeSlotSize(
                    armor.getSlot2Size()
            );


        case 3 ->

            safeSlotSize(
                    armor.getSlot3Size()
            );


        default ->

            0;
        };
    }


    private int safeSlotSize(
            Integer slotSize) {

        if (slotSize == null) {

            return 0;
        }


        return slotSize;
    }


    private int normalizeSlotSize(
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


    private String buildTalismanUrl(
            SimulatorForm form) {

        UriComponentsBuilder builder
                = UriComponentsBuilder.fromPath(
                        "/talisman"
                );


        addQueryParam(
                builder,
                "weaponId",
                form.getWeaponId()
        );


        addQueryParam(
                builder,
                "monsterId",
                form.getMonsterId()
        );


        addQueryParam(
                builder,
                "hitzoneId",
                form.getHitzoneId()
        );


        addQueryParam(
                builder,
                "rampageDecorationId",
                form.getRampageDecorationId()
        );


        addQueryParam(
                builder,
                "bowChargeId",
                form.getBowChargeId()
        );


        addQueryParam(
                builder,
                "bowCoatingId",
                form.getBowCoatingId()
        );


        addQueryParam(
                builder,
                "bowgunAmmoId",
                form.getBowgunAmmoId()
        );


        addQueryParam(
                builder,
                "weaponDecoration1Id",
                form.getWeaponDecoration1Id()
        );


        addQueryParam(
                builder,
                "weaponDecoration2Id",
                form.getWeaponDecoration2Id()
        );


        addQueryParam(
                builder,
                "weaponDecoration3Id",
                form.getWeaponDecoration3Id()
        );


        addQueryParam(
                builder,
                "headArmorId",
                form.getHeadArmorId()
        );


        addQueryParam(
                builder,
                "headDecoration1Id",
                form.getHeadDecoration1Id()
        );


        addQueryParam(
                builder,
                "headDecoration2Id",
                form.getHeadDecoration2Id()
        );


        addQueryParam(
                builder,
                "headDecoration3Id",
                form.getHeadDecoration3Id()
        );


        addQueryParam(
                builder,
                "chestArmorId",
                form.getChestArmorId()
        );


        addQueryParam(
                builder,
                "chestDecoration1Id",
                form.getChestDecoration1Id()
        );


        addQueryParam(
                builder,
                "chestDecoration2Id",
                form.getChestDecoration2Id()
        );


        addQueryParam(
                builder,
                "chestDecoration3Id",
                form.getChestDecoration3Id()
        );


        addQueryParam(
                builder,
                "armArmorId",
                form.getArmArmorId()
        );


        addQueryParam(
                builder,
                "armDecoration1Id",
                form.getArmDecoration1Id()
        );


        addQueryParam(
                builder,
                "armDecoration2Id",
                form.getArmDecoration2Id()
        );


        addQueryParam(
                builder,
                "armDecoration3Id",
                form.getArmDecoration3Id()
        );


        addQueryParam(
                builder,
                "waistArmorId",
                form.getWaistArmorId()
        );


        addQueryParam(
                builder,
                "waistDecoration1Id",
                form.getWaistDecoration1Id()
        );


        addQueryParam(
                builder,
                "waistDecoration2Id",
                form.getWaistDecoration2Id()
        );


        addQueryParam(
                builder,
                "waistDecoration3Id",
                form.getWaistDecoration3Id()
        );


        addQueryParam(
                builder,
                "legArmorId",
                form.getLegArmorId()
        );


        addQueryParam(
                builder,
                "legDecoration1Id",
                form.getLegDecoration1Id()
        );


        addQueryParam(
                builder,
                "legDecoration2Id",
                form.getLegDecoration2Id()
        );


        addQueryParam(
                builder,
                "legDecoration3Id",
                form.getLegDecoration3Id()
        );


        /* 護石スキル */

        addQueryParam(
                builder,
                "talismanSkill1Id",
                form.getTalismanSkill1Id()
        );


        addQueryParam(
                builder,
                "talismanSkill1Level",
                form.getTalismanSkill1Level()
        );


        addQueryParam(
                builder,
                "talismanSkill2Id",
                form.getTalismanSkill2Id()
        );


        addQueryParam(
                builder,
                "talismanSkill2Level",
                form.getTalismanSkill2Level()
        );


        /* 護石スロット */

        addQueryParam(
                builder,
                "talismanSlot1Size",
                form.getTalismanSlot1Size()
        );


        addQueryParam(
                builder,
                "talismanSlot2Size",
                form.getTalismanSlot2Size()
        );


        addQueryParam(
                builder,
                "talismanSlot3Size",
                form.getTalismanSlot3Size()
        );


        /* 護石装飾品 */

        addQueryParam(
                builder,
                "talismanDecoration1Id",
                form.getTalismanDecoration1Id()
        );


        addQueryParam(
                builder,
                "talismanDecoration2Id",
                form.getTalismanDecoration2Id()
        );


        addQueryParam(
                builder,
                "talismanDecoration3Id",
                form.getTalismanDecoration3Id()
        );


        return builder
                .build()
                .toUriString();
    }


    private void addQueryParam(
            UriComponentsBuilder builder,
            String name,
            Object value) {

        if (value != null) {

            builder.queryParam(
                    name,
                    value
            );
        }
    }
}
