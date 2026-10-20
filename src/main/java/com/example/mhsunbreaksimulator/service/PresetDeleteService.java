package com.example.mhsunbreaksimulator.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mhsunbreaksimulator.entity.Preset;
import com.example.mhsunbreaksimulator.entity.PresetArmor;
import com.example.mhsunbreaksimulator.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresetDeleteService {

    private final UserService userService;

    private final PresetService presetService;

    private final PresetArmorService presetArmorService;

    private final PresetArmorAugmentSkillService
            presetArmorAugmentSkillService;

    private final PresetDecorationService
            presetDecorationService;

    private final PresetSkillStateService
            presetSkillStateService;

    private final PresetCharmService
            presetCharmService;

    private final PresetBowSettingService
            presetBowSettingService;

    private final PresetBowgunSettingService
            presetBowgunSettingService;

    private final PresetWeaponAugmentService
            presetWeaponAugmentService;


    /*
     * =====================================================
     * プリセット一括削除
     * =====================================================
     *
     * DBの外部キーは ON DELETE CASCADE ではないため、
     * 子 → 親
     * の順番で手動削除する。
     *
     * username から user_id を取得することで、
     * 他ユーザーのプリセットを削除できないようにする。
     */
    @Transactional
    public boolean delete(
            Integer presetId,
            String username) {

        if (presetId == null
                || username == null
                || username.isBlank()) {

            return false;
        }


        /*
         * =====================================================
         * ログインユーザー取得
         * =====================================================
         */

        User user
                = userService.findByUsername(
                        username
                );

        if (user == null
                || user.getUserId() == null) {

            return false;
        }


        /*
         * =====================================================
         * プリセット所有者確認
         * =====================================================
         */

        Preset preset
                = presetService.findByIdAndUserId(
                        presetId,
                        user.getUserId()
                );

        if (preset == null) {

            return false;
        }


        /*
         * =====================================================
         * 防具錬成スキル削除
         * =====================================================
         *
         * preset_armor_augment_skill
         *      ↓
         * preset_armor
         *
         * の順番で削除する必要がある。
         */

        List<PresetArmor> presetArmors
                = presetArmorService.findByPresetId(
                        presetId
                );

        if (presetArmors != null) {

            for (PresetArmor presetArmor
                    : presetArmors) {

                if (presetArmor == null
                        || presetArmor.getPresetArmorId() == null) {

                    continue;
                }

                presetArmorAugmentSkillService
                        .deleteByPresetArmorId(
                                presetArmor.getPresetArmorId()
                        );
            }
        }


        /*
         * =====================================================
         * preset の子テーブル削除
         * =====================================================
         */

        presetDecorationService.deleteByPresetId(
                presetId
        );

        presetSkillStateService.deleteByPresetId(
                presetId
        );

        presetCharmService.deleteByPresetId(
                presetId
        );

        presetBowSettingService.deleteByPresetId(
                presetId
        );

        presetBowgunSettingService.deleteByPresetId(
                presetId
        );

        presetWeaponAugmentService.deleteByPresetId(
                presetId
        );


        /*
         * 防具は、その子である錬成スキルを
         * 先に削除した後で削除する。
         */
        presetArmorService.deleteByPresetId(
                presetId
        );


        /*
         * =====================================================
         * 最後に親 preset を削除
         * =====================================================
         */

        return presetService.delete(
                presetId,
                user.getUserId()
        );
    }
}