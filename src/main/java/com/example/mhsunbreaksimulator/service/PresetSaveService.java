package com.example.mhsunbreaksimulator.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mhsunbreaksimulator.entity.Armor;
import com.example.mhsunbreaksimulator.entity.BowCharge;
import com.example.mhsunbreaksimulator.entity.BowCoating;
import com.example.mhsunbreaksimulator.entity.BowgunAmmo;
import com.example.mhsunbreaksimulator.entity.Decoration;
import com.example.mhsunbreaksimulator.entity.Preset;
import com.example.mhsunbreaksimulator.entity.RampageDecoration;
import com.example.mhsunbreaksimulator.entity.Skill;
import com.example.mhsunbreaksimulator.entity.User;
import com.example.mhsunbreaksimulator.entity.Weapon;
import com.example.mhsunbreaksimulator.form.SimulatorForm;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresetSaveService {

    private final UserService userService;

    private final WeaponService weaponService;

    private final ArmorService armorService;

    private final DecorationService decorationService;

    private final RampageDecorationService
            rampageDecorationService;

    private final BowChargeService
            bowChargeService;

    private final BowCoatingService
            bowCoatingService;

    private final BowgunAmmoService
            bowgunAmmoService;

    private final SkillService
            skillService;


    private final PresetService
            presetService;

    private final PresetArmorService
            presetArmorService;

    private final PresetCharmService
            presetCharmService;

    private final PresetDecorationService
            presetDecorationService;

    private final PresetSkillStateService
            presetSkillStateService;

    private final PresetBowSettingService
            presetBowSettingService;

    private final PresetBowgunSettingService
            presetBowgunSettingService;

    private final PresetWeaponAugmentService
            presetWeaponAugmentService;


    /*
     * =====================================================
     * 現在のシミュレーター構成をプリセット保存
     * =====================================================
     */
    @Transactional
    public Preset save(
            String username,
            String presetName,
            SimulatorForm simulatorForm,
            Map<String, String> requestParams) {


        /*
         * =====================================================
         * 基本入力確認
         * =====================================================
         */

        if (username == null
                || username.isBlank()) {

            throw new IllegalArgumentException(
                    "ログインユーザーが取得できません。"
            );
        }


        if (presetName == null
                || presetName.isBlank()) {

            throw new IllegalArgumentException(
                    "プリセット名を入力してください。"
            );
        }


        if (simulatorForm == null) {

            throw new IllegalArgumentException(
                    "シミュレーター情報が取得できません。"
            );
        }


        /*
         * =====================================================
         * ログインユーザー
         * =====================================================
         */

        User user
                = userService.findByUsername(
                        username
                );


        if (user == null
                || user.getUserId() == null) {

            throw new IllegalArgumentException(
                    "ユーザーが見つかりません。"
            );
        }


        /*
         * =====================================================
         * 武器
         * =====================================================
         */

        Weapon weapon
                = weaponService.findById(
                        simulatorForm.getWeaponId()
                );


        /*
         * preset.weapon_id は NOT NULL なので、
         * 武器未選択では保存させない。
         */
        if (weapon == null
                || weapon.getWeaponId() == null) {

            throw new IllegalArgumentException(
                    "武器を選択してください。"
            );
        }


        /*
         * =====================================================
         * 百竜装飾品
         * =====================================================
         */

        Integer rampageDecorationId
                = findValidRampageDecorationId(
                        simulatorForm.getRampageDecorationId(),
                        weapon
                );


        /*
         * =====================================================
         * preset 本体
         * =====================================================
         */

        Preset preset
                = presetService.create(
                        user.getUserId(),
                        presetName.trim(),
                        weapon.getWeaponId(),
                        rampageDecorationId
                );


        Integer presetId
                = preset.getPresetId();


        if (presetId == null) {

            throw new IllegalStateException(
                    "プリセットIDの採番に失敗しました。"
            );
        }


        /*
         * =====================================================
         * 武器傀異錬成
         * =====================================================
         */

        Integer weaponAugmentAttackLevel
                = normalizeAugmentLevel(
                        simulatorForm.getWeaponAugmentAttackLevel(),
                        4
                );

        Integer weaponAugmentElementLevel
                = normalizeAugmentLevel(
                        simulatorForm.getWeaponAugmentElementLevel(),
                        8
                );

        Integer weaponAugmentAffinityLevel
                = normalizeAugmentLevel(
                        simulatorForm.getWeaponAugmentAffinityLevel(),
                        3
                );

        Integer weaponAugmentSharpnessLevel
                = normalizeAugmentLevel(
                        simulatorForm.getWeaponAugmentSharpnessLevel(),
                        4
                );


        if (weaponAugmentAttackLevel != null
                || weaponAugmentElementLevel != null
                || weaponAugmentAffinityLevel != null
                || weaponAugmentSharpnessLevel != null) {

            presetWeaponAugmentService.create(
                    presetId,
                    weaponAugmentAttackLevel == null
                            ? 0
                            : weaponAugmentAttackLevel,
                    weaponAugmentElementLevel == null
                            ? 0
                            : weaponAugmentElementLevel,
                    weaponAugmentAffinityLevel == null
                            ? 0
                            : weaponAugmentAffinityLevel,
                    weaponAugmentSharpnessLevel == null
                            ? 0
                            : weaponAugmentSharpnessLevel
            );
        }


        /*
         * =====================================================
         * 防具
         * =====================================================
         */

        Armor headArmor
                = saveArmor(
                        presetId,
                        "頭",
                        simulatorForm.getHeadArmorId()
                );


        Armor chestArmor
                = saveArmor(
                        presetId,
                        "胴",
                        simulatorForm.getChestArmorId()
                );


        Armor armArmor
                = saveArmor(
                        presetId,
                        "腕",
                        simulatorForm.getArmArmorId()
                );


        Armor waistArmor
                = saveArmor(
                        presetId,
                        "腰",
                        simulatorForm.getWaistArmorId()
                );


        Armor legArmor
                = saveArmor(
                        presetId,
                        "脚",
                        simulatorForm.getLegArmorId()
                );


        /*
         * =====================================================
         * 武器装飾品
         * =====================================================
         */

        saveDecoration(
                presetId,
                "WEAPON",
                null,
                1,
                simulatorForm.getWeaponDecoration1Id(),
                safeSlotSize(
                        weapon.getSlot1Size()
                )
        );


        saveDecoration(
                presetId,
                "WEAPON",
                null,
                2,
                simulatorForm.getWeaponDecoration2Id(),
                safeSlotSize(
                        weapon.getSlot2Size()
                )
        );


        saveDecoration(
                presetId,
                "WEAPON",
                null,
                3,
                simulatorForm.getWeaponDecoration3Id(),
                safeSlotSize(
                        weapon.getSlot3Size()
                )
        );


        /*
         * =====================================================
         * 防具装飾品
         * =====================================================
         */

        saveArmorDecorations(
                presetId,
                "頭",
                headArmor,
                simulatorForm.getHeadDecoration1Id(),
                simulatorForm.getHeadDecoration2Id(),
                simulatorForm.getHeadDecoration3Id()
        );


        saveArmorDecorations(
                presetId,
                "胴",
                chestArmor,
                simulatorForm.getChestDecoration1Id(),
                simulatorForm.getChestDecoration2Id(),
                simulatorForm.getChestDecoration3Id()
        );


        saveArmorDecorations(
                presetId,
                "腕",
                armArmor,
                simulatorForm.getArmDecoration1Id(),
                simulatorForm.getArmDecoration2Id(),
                simulatorForm.getArmDecoration3Id()
        );


        saveArmorDecorations(
                presetId,
                "腰",
                waistArmor,
                simulatorForm.getWaistDecoration1Id(),
                simulatorForm.getWaistDecoration2Id(),
                simulatorForm.getWaistDecoration3Id()
        );


        saveArmorDecorations(
                presetId,
                "脚",
                legArmor,
                simulatorForm.getLegDecoration1Id(),
                simulatorForm.getLegDecoration2Id(),
                simulatorForm.getLegDecoration3Id()
        );


        /*
         * =====================================================
         * 護石
         * =====================================================
         */

        Skill talismanSkill1
                = skillService.findById(
                        simulatorForm.getTalismanSkill1Id()
                );


        Skill talismanSkill2
                = skillService.findById(
                        simulatorForm.getTalismanSkill2Id()
                );


        Integer talismanSkill1Id
                = talismanSkill1 == null
                        ? null
                        : talismanSkill1.getSkillId();


        Integer talismanSkill2Id
                = talismanSkill2 == null
                        ? null
                        : talismanSkill2.getSkillId();


        Integer talismanSkill1Level
                = normalizeSkillLevel(
                        talismanSkill1,
                        simulatorForm.getTalismanSkill1Level()
                );


        Integer talismanSkill2Level
                = normalizeSkillLevel(
                        talismanSkill2,
                        simulatorForm.getTalismanSkill2Level()
                );


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


        presetCharmService.create(
                presetId,
                talismanSkill1Id,
                talismanSkill1Level,
                talismanSkill2Id,
                talismanSkill2Level,
                talismanSlot1Size,
                talismanSlot2Size,
                talismanSlot3Size
        );


        /*
         * =====================================================
         * 護石装飾品
         * =====================================================
         */

        saveDecoration(
                presetId,
                "CHARM",
                null,
                1,
                simulatorForm.getTalismanDecoration1Id(),
                talismanSlot1Size
        );


        saveDecoration(
                presetId,
                "CHARM",
                null,
                2,
                simulatorForm.getTalismanDecoration2Id(),
                talismanSlot2Size
        );


        saveDecoration(
                presetId,
                "CHARM",
                null,
                3,
                simulatorForm.getTalismanDecoration3Id(),
                talismanSlot3Size
        );


        /*
         * =====================================================
         * 弓専用設定
         * =====================================================
         */

        if ("弓".equals(
                weapon.getWeaponType()
        )) {

            BowCoating bowCoating
                    = findValidBowCoating(
                            simulatorForm.getBowCoatingId(),
                            weapon.getWeaponId()
                    );


            BowCharge bowCharge
                    = findValidBowCharge(
                            simulatorForm.getBowChargeId(),
                            weapon.getWeaponId()
                    );


            presetBowSettingService.create(
                    presetId,
                    bowCoating == null
                            ? null
                            : bowCoating.getCoatingType(),
                    bowCharge == null
                            ? null
                            : bowCharge.getBowChargeId(),
                    simulatorForm.getBowAttackType()
            );
        }


        /*
         * =====================================================
         * ボウガン専用設定
         * =====================================================
         */

        if ("ライトボウガン".equals(
                weapon.getWeaponType()
        )
                ||
                "ヘビィボウガン".equals(
                        weapon.getWeaponType()
                )) {

            /*
             * 現時点で特殊攻撃を持つのは
             * ライトボウガンの起爆竜弾のみ。
             *
             * ヘビィボウガンは将来
             * 機関竜弾 / 狙撃竜弾へ対応するまでは
             * NORMAL_AMMOとして扱う。
             */
            boolean wyvernblastSelected
                    = "ライトボウガン".equals(
                            weapon.getWeaponType()
                    )
                    && "WYVERNBLAST".equals(
                            simulatorForm.getBowgunAttackType()
                    );


            if (wyvernblastSelected) {

                presetBowgunSettingService.create(
                        presetId,
                        "WYVERNBLAST",
                        normalizeWyvernblastExplosionSize(
                                simulatorForm.getWyvernblastExplosionSize()
                        ),
                        null,
                        null,
                        null
                );

            } else {

                BowgunAmmo bowgunAmmo
                        = findValidBowgunAmmo(
                                simulatorForm.getBowgunAmmoId(),
                                weapon.getWeaponId()
                        );


                /*
                 * 通常弾は従来どおり、
                 * 弾未選択ならボウガン設定行を作成しない。
                 *
                 * 既存の4引数create(...)を使用することで、
                 * 既存テストとの互換性も維持する。
                 */
                if (bowgunAmmo != null) {

                    presetBowgunSettingService.create(
                            presetId,
                            bowgunAmmo.getAmmoType(),
                            bowgunAmmo.getAmmoLevel(),
                            null
                    );
                }
            }
        }


        /*
         * =====================================================
         * 条件付きスキル状態
         * =====================================================
         *
         * 現在の画面では
         *
         * skillState_123=ACTIVE
         *
         * の形式でURLパラメータとして保持しているため、
         * その値を保存する。
         */
        saveSkillStates(
                presetId,
                requestParams
        );


        return preset;
    }


    /*
     * =====================================================
     * 武器傀異錬成Lv正規化
     * =====================================================
     */
    private Integer normalizeAugmentLevel(
            Integer level,
            int maxLevel) {

        if (level == null
                || level <= 0) {

            return null;
        }


        return Math.min(
                level,
                maxLevel
        );
    }


    /*
     * =====================================================
     * 防具保存
     * =====================================================
     */
    private Armor saveArmor(
            Integer presetId,
            String expectedPart,
            Integer armorId) {

        if (armorId == null) {

            return null;
        }


        Armor armor
                = armorService.findById(
                        armorId
                );


        if (armor == null
                || !expectedPart.equals(
                        armor.getPart()
                )) {

            return null;
        }


        presetArmorService.create(
                presetId,
                expectedPart,
                armor.getArmorId(),
                normalizeSlotSize(
                        armor.getSlot1Size()
                ),
                normalizeSlotSize(
                        armor.getSlot2Size()
                ),
                normalizeSlotSize(
                        armor.getSlot3Size()
                ),

                /*
                 * 防具傀異錬成UIはまだ未実装なので
                 * 現時点では差分0として保存する。
                 */
                0,
                0,
                0,
                0,
                0,
                0
        );


        return armor;
    }


    /*
     * =====================================================
     * 防具装飾品保存
     * =====================================================
     */
    private void saveArmorDecorations(
            Integer presetId,
            String part,
            Armor armor,
            Integer decoration1Id,
            Integer decoration2Id,
            Integer decoration3Id) {

        if (armor == null) {

            return;
        }


        saveDecoration(
                presetId,
                "ARMOR",
                part,
                1,
                decoration1Id,
                safeSlotSize(
                        armor.getSlot1Size()
                )
        );


        saveDecoration(
                presetId,
                "ARMOR",
                part,
                2,
                decoration2Id,
                safeSlotSize(
                        armor.getSlot2Size()
                )
        );


        saveDecoration(
                presetId,
                "ARMOR",
                part,
                3,
                decoration3Id,
                safeSlotSize(
                        armor.getSlot3Size()
                )
        );
    }


    /*
     * =====================================================
     * 通常装飾品保存
     * =====================================================
     */
    private void saveDecoration(
            Integer presetId,
            String equipmentKind,
            String equipmentPart,
            int slotNo,
            Integer decorationId,
            int slotSize) {

        if (decorationId == null
                || slotSize <= 0) {

            return;
        }


        Decoration decoration
                = decorationService.findById(
                        decorationId
                );


        if (decoration == null
                || decoration.getRequiredSlotSize() == null) {

            return;
        }


        if (decoration.getRequiredSlotSize()
                > slotSize) {

            return;
        }


        presetDecorationService.create(
                presetId,
                equipmentKind,
                equipmentPart,
                slotNo,
                decoration.getDecorationId()
        );
    }


    /*
     * =====================================================
     * 百竜装飾品確認
     * =====================================================
     */
    private Integer findValidRampageDecorationId(
            Integer rampageDecorationId,
            Weapon weapon) {

        if (rampageDecorationId == null
                || weapon == null) {

            return null;
        }


        int slotSize
                = safeSlotSize(
                        weapon.getRampageSlotSize()
                );


        if (slotSize <= 0) {

            return null;
        }


        RampageDecoration rampageDecoration
                = rampageDecorationService.findById(
                        rampageDecorationId
                );


        if (rampageDecoration == null
                || rampageDecoration.getRequiredSlotSize() == null) {

            return null;
        }


        if (rampageDecoration.getRequiredSlotSize()
                > slotSize) {

            return null;
        }


        return rampageDecoration.getRampageDecorationId();
    }


    /*
     * =====================================================
     * 弓ビン確認
     * =====================================================
     */
    private BowCoating findValidBowCoating(
            Integer bowCoatingId,
            Integer weaponId) {

        if (bowCoatingId == null
                || weaponId == null) {

            return null;
        }


        BowCoating bowCoating
                = bowCoatingService.findById(
                        bowCoatingId
                );


        if (bowCoating == null
                || !weaponId.equals(
                        bowCoating.getWeaponId()
                )) {

            return null;
        }


        return bowCoating;
    }


    /*
     * =====================================================
     * 弓溜め段階確認
     * =====================================================
     */
    private BowCharge findValidBowCharge(
            Integer bowChargeId,
            Integer weaponId) {

        if (bowChargeId == null
                || weaponId == null) {

            return null;
        }


        BowCharge bowCharge
                = bowChargeService.findById(
                        bowChargeId
                );


        if (bowCharge == null
                || !weaponId.equals(
                        bowCharge.getWeaponId()
                )) {

            return null;
        }


        return bowCharge;
    }


    /*
     * =====================================================
     * ボウガン弾確認
     * =====================================================
     */
    private BowgunAmmo findValidBowgunAmmo(
            Integer bowgunAmmoId,
            Integer weaponId) {

        if (bowgunAmmoId == null
                || weaponId == null) {

            return null;
        }


        BowgunAmmo bowgunAmmo
                = bowgunAmmoService.findById(
                        bowgunAmmoId
                );


        if (bowgunAmmo == null
                || !weaponId.equals(
                        bowgunAmmo.getWeaponId()
                )) {

            return null;
        }


        return bowgunAmmo;
    }


    /*
     * =====================================================
     * 起爆竜弾・爆発段階正規化
     * =====================================================
     */
    private String normalizeWyvernblastExplosionSize(
            String explosionSize) {

        if ("SMALL".equals(
                explosionSize
        )) {

            return "SMALL";
        }


        if ("MEDIUM".equals(
                explosionSize
        )) {

            return "MEDIUM";
        }


        /*
         * null / 空文字 / 不正値も含め、
         * デフォルトはLARGE。
         */
        return "LARGE";
    }


    /*
     * =====================================================
     * 条件付きスキル状態保存
     * =====================================================
     */
    private void saveSkillStates(
            Integer presetId,
            Map<String, String> requestParams) {

        if (requestParams == null
                || requestParams.isEmpty()) {

            return;
        }


        String prefix
                = "skillState_";


        for (Map.Entry<String, String> entry
                : requestParams.entrySet()) {

            String parameterName
                    = entry.getKey();


            if (parameterName == null
                    || !parameterName.startsWith(
                            prefix
                    )) {

                continue;
            }


            String skillIdText
                    = parameterName.substring(
                            prefix.length()
                    );


            Integer skillId;


            try {

                skillId
                        = Integer.valueOf(
                                skillIdText
                        );

            } catch (NumberFormatException e) {

                continue;
            }


            String stateCode
                    = entry.getValue();


            if (stateCode == null
                    || stateCode.isBlank()) {

                continue;
            }


            /*
             * 存在しない skill_id は保存しない。
             */
            if (skillService.findById(
                    skillId
            ) == null) {

                continue;
            }


            presetSkillStateService.create(
                    presetId,
                    skillId,
                    stateCode
            );
        }
    }


    /*
     * =====================================================
     * 護石スキルLv補正
     * =====================================================
     */
    private Integer normalizeSkillLevel(
            Skill skill,
            Integer level) {

        if (skill == null) {

            return null;
        }


        int normalizedLevel;


        if (level == null
                || level < 1) {

            normalizedLevel = 1;

        } else {

            normalizedLevel = level;
        }


        if (skill.getMaxLevel() != null
                && skill.getMaxLevel() > 0) {

            normalizedLevel
                    = Math.min(
                            normalizedLevel,
                            skill.getMaxLevel()
                    );
        }


        return normalizedLevel;
    }


    /*
     * =====================================================
     * スロットサイズ補正
     * =====================================================
     */
    private int normalizeSlotSize(
            Integer slotSize) {

        if (slotSize == null
                || slotSize < 0) {

            return 0;
        }


        if (slotSize > 4) {

            return 4;
        }


        return slotSize;
    }


    private int safeSlotSize(
            Integer slotSize) {

        if (slotSize == null) {

            return 0;
        }


        return slotSize;
    }
}