package com.example.mhsunbreaksimulator.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.SkillTotal;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResolvedSkillEffectService {

    private final SkillEffectStateService
            skillEffectStateService;


    /*
     * =====================================================
     * 実際に計算へ使用するスキル効果を確定
     * =====================================================
     *
     * skillTotals
     *     現在装備している全スキルとLv
     *
     * selectedConditionalEffects
     *     条件付きスキルで現在選択されている状態
     *
     * weaponType
     *     武器種別効果の検索に使用
     *
     *
     * 処理：
     *
     * 1.
     * 全スキルからDEFAULT効果を取得
     *
     * 2.
     * 条件付きスキルについては
     * selectedConditionalEffectsで上書き
     *
     *
     * これによって、
     *
     * 攻撃
     * 見切り
     * 超会心
     * 会心撃【属性】
     * 防御
     *
     * などの常時スキルと、
     *
     * 挑戦者
     * 力の解放
     * 龍気活性
     * 龍気変換
     *
     * などの条件付きスキルを
     * 1つのMapへ統合する。
     */
    public Map<Integer, SkillEffectState> resolveAllEffects(
            List<SkillTotal> skillTotals,
            Map<Integer, SkillEffectState> selectedConditionalEffects,
            String weaponType) {

        Map<Integer, SkillEffectState> resolvedEffects
                = new LinkedHashMap<>();


        /*
         * =================================================
         * DEFAULT効果
         * =================================================
         */
        if (skillTotals != null) {

            for (SkillTotal skillTotal
                    : skillTotals) {

                if (skillTotal == null
                        || skillTotal.getSkillId() == null
                        || skillTotal.getLevel() == null
                        || skillTotal.getLevel() < 1) {

                    continue;
                }


                SkillEffectState defaultEffect
                        = skillEffectStateService
                                .findBySkillIdAndLevelAndStateCode(
                                        skillTotal.getSkillId(),
                                        skillTotal.getLevel(),
                                        "DEFAULT",
                                        weaponType
                                );


                /*
                 * DEFAULT行が存在するスキルだけ追加。
                 *
                 * 条件付きスキルは
                 * DEFAULTがないものも多いため、
                 * nullなら単純に無視する。
                 */
                if (defaultEffect != null) {

                    resolvedEffects.put(
                            skillTotal.getSkillId(),
                            defaultEffect
                    );
                }
            }
        }


        /*
         * =================================================
         * 条件付き・状態付き効果
         * =================================================
         *
         * 同じskillIdがすでにDEFAULTとして
         * 入っている場合でも、
         *
         * 現在選択されている状態を優先して
         * 上書きする。
         */
        if (selectedConditionalEffects != null) {

            for (Map.Entry<Integer, SkillEffectState> entry
                    : selectedConditionalEffects.entrySet()) {

                Integer skillId
                        = entry.getKey();

                SkillEffectState selectedEffect
                        = entry.getValue();


                if (skillId == null
                        || selectedEffect == null) {

                    continue;
                }


                resolvedEffects.put(
                        skillId,
                        selectedEffect
                );
            }
        }


        return resolvedEffects;
    }
}