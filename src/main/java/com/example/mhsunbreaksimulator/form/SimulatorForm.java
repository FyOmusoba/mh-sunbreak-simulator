package com.example.mhsunbreaksimulator.form;

import lombok.Data;

@Data
public class SimulatorForm {

    private Integer weaponId;

    // モンスター
    private Integer monsterId;

    // モンスター部位肉質
    private Integer hitzoneId;

    // 百竜装飾品
    private Integer rampageDecorationId;

    /*
     * =====================================================
     * 武器傀異錬成
     * =====================================================
     */

    // 攻撃力強化Lv
    private Integer weaponAugmentAttackLevel;

    // 属性強化Lv
    private Integer weaponAugmentElementLevel;

    // 会心率強化Lv
    private Integer weaponAugmentAffinityLevel;

    // 斬れ味強化Lv
    private Integer weaponAugmentSharpnessLevel;

    /*
     * =====================================================
     * 弓
     * =====================================================
     */

    /*
     * 弓の攻撃種別
     *
     * NORMAL
     * → 通常矢
     *
     * DRAGON_PIERCER
     * → 竜の一矢
     *
     * 既存URLとの互換性のため、
     * パラメータ未指定時はNORMAL。
     */
    private String bowAttackType
            = "NORMAL";

    // 弓の選択中溜め段階
    private Integer bowChargeId;

    // 弓の選択中ビン
    private Integer bowCoatingId;

    /*
     * =====================================================
     * ボウガン
     * =====================================================
     */

    /*
     * ボウガンの攻撃種別
     *
     * NORMAL_AMMO
     * → 通常の弾選択
     *
     * WYVERNBLAST
     * → ライトボウガン・起爆竜弾
     *
     * 将来的に
     *
     * WYVERNHEART
     * → ヘビィボウガン・機関竜弾
     *
     * WYVERNSNIPE
     * → ヘビィボウガン・狙撃竜弾
     *
     * を追加する。
     *
     * 既存URLとの互換性のため、
     * パラメータ未指定時はNORMAL_AMMO。
     */
    private String bowgunAttackType
            = "NORMAL_AMMO";

    /*
     * 起爆竜弾の爆発段階
     *
     * SMALL
     * → 小爆発
     *
     * MEDIUM
     * → 中爆発
     *
     * LARGE
     * → 大爆発
     *
     * 起爆竜弾選択時の初期値は
     * LARGEとする。
     */
    private String wyvernblastExplosionSize
            = "LARGE";

    // ボウガンの選択中弾
    private Integer bowgunAmmoId;

    // 武器装飾品
    private Integer weaponDecoration1Id;
    private Integer weaponDecoration2Id;
    private Integer weaponDecoration3Id;

    // 頭
    private Integer headArmorId;
    private Integer headDecoration1Id;
    private Integer headDecoration2Id;
    private Integer headDecoration3Id;

    // 胴
    private Integer chestArmorId;
    private Integer chestDecoration1Id;
    private Integer chestDecoration2Id;
    private Integer chestDecoration3Id;

    // 腕
    private Integer armArmorId;
    private Integer armDecoration1Id;
    private Integer armDecoration2Id;
    private Integer armDecoration3Id;

    // 腰
    private Integer waistArmorId;
    private Integer waistDecoration1Id;
    private Integer waistDecoration2Id;
    private Integer waistDecoration3Id;

    // 脚
    private Integer legArmorId;
    private Integer legDecoration1Id;
    private Integer legDecoration2Id;
    private Integer legDecoration3Id;

    // 護石スキル1
    private Integer talismanSkill1Id;
    private Integer talismanSkill1Level;

    // 護石スキル2
    private Integer talismanSkill2Id;
    private Integer talismanSkill2Level;

    // 護石スロット
    private Integer talismanSlot1Size;
    private Integer talismanSlot2Size;
    private Integer talismanSlot3Size;

    // 護石装飾品
    private Integer talismanDecoration1Id;
    private Integer talismanDecoration2Id;
    private Integer talismanDecoration3Id;
}