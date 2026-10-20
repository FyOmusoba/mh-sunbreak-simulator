package com.example.mhsunbreaksimulator.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.entity.BowCoating;
import com.example.mhsunbreaksimulator.entity.BowgunAmmo;
import com.example.mhsunbreaksimulator.entity.Preset;
import com.example.mhsunbreaksimulator.entity.PresetArmor;
import com.example.mhsunbreaksimulator.entity.PresetBowSetting;
import com.example.mhsunbreaksimulator.entity.PresetBowgunSetting;
import com.example.mhsunbreaksimulator.entity.PresetCharm;
import com.example.mhsunbreaksimulator.entity.PresetDecoration;
import com.example.mhsunbreaksimulator.entity.PresetSkillState;
import com.example.mhsunbreaksimulator.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresetLoadService {

    private final UserService userService;

    private final PresetService presetService;

    private final PresetArmorService presetArmorService;

    private final PresetCharmService presetCharmService;

    private final PresetDecorationService presetDecorationService;

    private final PresetSkillStateService presetSkillStateService;

    private final PresetBowSettingService presetBowSettingService;

    private final PresetBowgunSettingService presetBowgunSettingService;

    private final BowCoatingService bowCoatingService;

    private final BowgunAmmoService bowgunAmmoService;


    /*
     * =====================================================
     * プリセットをシミュレーター用URLパラメータへ復元
     * =====================================================
     *
     * 保存されているプリセット情報から、
     *
     * weaponId
     * rampageDecorationId
     * 防具
     * 装飾品
     * 護石
     * 弓設定
     * ボウガン設定
     * 条件付きスキル状態
     *
     * を復元する。
     *
     * モンスター・肉質はプリセット保存対象ではないため、
     * ここでは復元しない。
     */
    public Map<String, String> load(
            String username,
            Integer presetId) {


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


        if (presetId == null) {

            throw new IllegalArgumentException(
                    "プリセットIDが指定されていません。"
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
         * プリセット本体
         * =====================================================
         *
         * presetId + userId で検索することで、
         * 他ユーザーのプリセットを読み込めないようにする。
         */

        Preset preset
                = presetService.findByIdAndUserId(
                        presetId,
                        user.getUserId()
                );


        if (preset == null) {

            throw new IllegalArgumentException(
                    "プリセットが見つかりません。"
            );
        }


        Map<String, String> parameters
                = new LinkedHashMap<>();


        /*
         * =====================================================
         * 武器
         * =====================================================
         */

        putInteger(
                parameters,
                "weaponId",
                preset.getWeaponId()
        );


        /*
         * =====================================================
         * 百竜装飾品
         * =====================================================
         */

        putInteger(
                parameters,
                "rampageDecorationId",
                preset.getRampageDecorationId()
        );


        /*
         * =====================================================
         * 防具
         * =====================================================
         */

        List<PresetArmor> presetArmors
                = presetArmorService.findByPresetId(
                        presetId
                );


        if (presetArmors != null) {

            for (PresetArmor presetArmor
                    : presetArmors) {

                if (presetArmor == null
                        || presetArmor.getPart() == null
                        || presetArmor.getArmorId() == null) {

                    continue;
                }


                switch (presetArmor.getPart()) {

                    case "頭" -> putInteger(
                            parameters,
                            "headArmorId",
                            presetArmor.getArmorId()
                    );

                    case "胴" -> putInteger(
                            parameters,
                            "chestArmorId",
                            presetArmor.getArmorId()
                    );

                    case "腕" -> putInteger(
                            parameters,
                            "armArmorId",
                            presetArmor.getArmorId()
                    );

                    case "腰" -> putInteger(
                            parameters,
                            "waistArmorId",
                            presetArmor.getArmorId()
                    );

                    case "脚" -> putInteger(
                            parameters,
                            "legArmorId",
                            presetArmor.getArmorId()
                    );

                    default -> {
                    }
                }
            }
        }


        /*
         * =====================================================
         * 護石
         * =====================================================
         */

        PresetCharm presetCharm
                = presetCharmService.findByPresetId(
                        presetId
                );


        if (presetCharm != null) {

            putInteger(
                    parameters,
                    "talismanSkill1Id",
                    presetCharm.getSkill1Id()
            );

            putInteger(
                    parameters,
                    "talismanSkill1Level",
                    presetCharm.getSkill1Level()
            );

            putInteger(
                    parameters,
                    "talismanSkill2Id",
                    presetCharm.getSkill2Id()
            );

            putInteger(
                    parameters,
                    "talismanSkill2Level",
                    presetCharm.getSkill2Level()
            );

            putInteger(
                    parameters,
                    "talismanSlot1Size",
                    presetCharm.getSlot1Size()
            );

            putInteger(
                    parameters,
                    "talismanSlot2Size",
                    presetCharm.getSlot2Size()
            );

            putInteger(
                    parameters,
                    "talismanSlot3Size",
                    presetCharm.getSlot3Size()
            );
        }


        /*
         * =====================================================
         * 通常装飾品
         * =====================================================
         */

        List<PresetDecoration> presetDecorations
                = presetDecorationService.findByPresetId(
                        presetId
                );


        if (presetDecorations != null) {

            for (PresetDecoration presetDecoration
                    : presetDecorations) {

                restoreDecoration(
                        parameters,
                        presetDecoration
                );
            }
        }


        /*
         * =====================================================
         * 弓設定
         * =====================================================
         */

        PresetBowSetting presetBowSetting
                = presetBowSettingService.findByPresetId(
                        presetId
                );


        if (presetBowSetting != null) {

            /*
             * 溜め段階は bow_charge_id を
             * そのまま保存しているため直接復元できる。
             */
            putInteger(
                    parameters,
                    "bowChargeId",
                    presetBowSetting.getBowChargeId()
            );


            /*
             * ビンは coating_type で保存しているため、
             * 現在の武器に対応する bow_coating_id を探す。
             */
            Integer bowCoatingId
                    = findBowCoatingId(
                            preset.getWeaponId(),
                            presetBowSetting.getCoatingType()
                    );


            putInteger(
                    parameters,
                    "bowCoatingId",
                    bowCoatingId
            );
        }


        /*
         * =====================================================
         * ボウガン設定
         * =====================================================
         */

        PresetBowgunSetting presetBowgunSetting
                = presetBowgunSettingService.findByPresetId(
                        presetId
                );


        if (presetBowgunSetting != null) {

            /*
             * ボウガン弾は
             *
             * ammo_type
             * ammo_level
             *
             * で保存しているため、
             * 現在の武器に対応する bowgun_ammo_id を探す。
             */
            Integer bowgunAmmoId
                    = findBowgunAmmoId(
                            preset.getWeaponId(),
                            presetBowgunSetting.getAmmoType(),
                            presetBowgunSetting.getAmmoLevel()
                    );


            putInteger(
                    parameters,
                    "bowgunAmmoId",
                    bowgunAmmoId
            );
        }


        /*
         * =====================================================
         * 条件付きスキル状態
         * =====================================================
         */

        List<PresetSkillState> presetSkillStates
                = presetSkillStateService.findByPresetId(
                        presetId
                );


        if (presetSkillStates != null) {

            for (PresetSkillState presetSkillState
                    : presetSkillStates) {

                if (presetSkillState == null
                        || presetSkillState.getSkillId() == null
                        || presetSkillState.getStateCode() == null
                        || presetSkillState.getStateCode().isBlank()) {

                    continue;
                }


                parameters.put(
                        "skillState_"
                                + presetSkillState.getSkillId(),
                        presetSkillState.getStateCode()
                );
            }
        }


        return parameters;
    }


    /*
     * =====================================================
     * 装飾品復元
     * =====================================================
     */
    private void restoreDecoration(
            Map<String, String> parameters,
            PresetDecoration presetDecoration) {

        if (presetDecoration == null
                || presetDecoration.getEquipmentKind() == null
                || presetDecoration.getSlotNo() == null
                || presetDecoration.getDecorationId() == null) {

            return;
        }


        String parameterName
                = findDecorationParameterName(
                        presetDecoration
                );


        if (parameterName == null) {

            return;
        }


        putInteger(
                parameters,
                parameterName,
                presetDecoration.getDecorationId()
        );
    }


    /*
     * =====================================================
     * 装飾品のURLパラメータ名判定
     * =====================================================
     */
    private String findDecorationParameterName(
            PresetDecoration presetDecoration) {

        String equipmentKind
                = presetDecoration.getEquipmentKind();

        String equipmentPart
                = presetDecoration.getEquipmentPart();

        Integer slotNo
                = presetDecoration.getSlotNo();


        if (slotNo == null
                || slotNo < 1
                || slotNo > 3) {

            return null;
        }


        if ("WEAPON".equals(
                equipmentKind
        )) {

            return "weaponDecoration"
                    + slotNo
                    + "Id";
        }


        if ("CHARM".equals(
                equipmentKind
        )) {

            return "talismanDecoration"
                    + slotNo
                    + "Id";
        }


        if (!"ARMOR".equals(
                equipmentKind
        )
                || equipmentPart == null) {

            return null;
        }


        return switch (equipmentPart) {

            case "頭" ->
                    "headDecoration"
                            + slotNo
                            + "Id";

            case "胴" ->
                    "chestDecoration"
                            + slotNo
                            + "Id";

            case "腕" ->
                    "armDecoration"
                            + slotNo
                            + "Id";

            case "腰" ->
                    "waistDecoration"
                            + slotNo
                            + "Id";

            case "脚" ->
                    "legDecoration"
                            + slotNo
                            + "Id";

            default ->
                    null;
        };
    }


    /*
     * =====================================================
     * 弓ビンID復元
     * =====================================================
     */
    private Integer findBowCoatingId(
            Integer weaponId,
            String coatingType) {

        if (weaponId == null
                || coatingType == null
                || coatingType.isBlank()) {

            return null;
        }


        List<BowCoating> bowCoatings
                = bowCoatingService.findByWeaponId(
                        weaponId
                );


        if (bowCoatings == null) {

            return null;
        }


        for (BowCoating bowCoating
                : bowCoatings) {

            if (bowCoating == null) {

                continue;
            }


            if (Objects.equals(
                    coatingType,
                    bowCoating.getCoatingType()
            )) {

                return bowCoating.getBowCoatingId();
            }
        }


        return null;
    }


    /*
     * =====================================================
     * ボウガン弾ID復元
     * =====================================================
     */
    private Integer findBowgunAmmoId(
            Integer weaponId,
            String ammoType,
            Integer ammoLevel) {

        if (weaponId == null
                || ammoType == null
                || ammoType.isBlank()
                || ammoLevel == null) {

            return null;
        }


        List<BowgunAmmo> bowgunAmmos
                = bowgunAmmoService.findByWeaponId(
                        weaponId
                );


        if (bowgunAmmos == null) {

            return null;
        }


        for (BowgunAmmo bowgunAmmo
                : bowgunAmmos) {

            if (bowgunAmmo == null) {

                continue;
            }


            if (Objects.equals(
                    ammoType,
                    bowgunAmmo.getAmmoType()
            )
                    && Objects.equals(
                            ammoLevel,
                            bowgunAmmo.getAmmoLevel()
                    )) {

                return bowgunAmmo.getBowgunAmmoId();
            }
        }


        return null;
    }


    /*
     * =====================================================
     * Integer → URLパラメータ追加
     * =====================================================
     */
    private void putInteger(
            Map<String, String> parameters,
            String parameterName,
            Integer value) {

        if (parameters == null
                || parameterName == null
                || value == null) {

            return;
        }


        parameters.put(
                parameterName,
                String.valueOf(
                        value
                )
        );
    }
}