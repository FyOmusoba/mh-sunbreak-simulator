package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.dto.DefenseResistanceResult;

@Service
public class DefenseResistanceService {

    /*
     * =====================================================
     * 防御力・属性耐性計算
     * =====================================================
     *
     * baseDefense
     *     スキル補正前の防御力
     *
     * baseFireRes ～ baseDragonRes
     *     スキル補正前の5属性耐性
     *
     * weaponType
     *     龍気変換の小数処理判定に使用
     *
     * modifier
     *     スキル補正合計
     */
    public DefenseResistanceResult calculate(
            Integer baseDefense,
            Integer baseFireRes,
            Integer baseWaterRes,
            Integer baseThunderRes,
            Integer baseIceRes,
            Integer baseDragonRes,
            String weaponType,
            ConditionalSkillModifierTotal modifier) {

        DefenseResistanceResult result
                = new DefenseResistanceResult();


        /*
         * =================================================
         * 基礎値
         * =================================================
         */

        int safeBaseDefense
                = safeInteger(
                        baseDefense
                );

        int safeBaseFireRes
                = safeInteger(
                        baseFireRes
                );

        int safeBaseWaterRes
                = safeInteger(
                        baseWaterRes
                );

        int safeBaseThunderRes
                = safeInteger(
                        baseThunderRes
                );

        int safeBaseIceRes
                = safeInteger(
                        baseIceRes
                );

        int safeBaseDragonRes
                = safeInteger(
                        baseDragonRes
                );


        result.setBaseDefense(
                safeBaseDefense
        );

        result.setBaseFireRes(
                safeBaseFireRes
        );

        result.setBaseWaterRes(
                safeBaseWaterRes
        );

        result.setBaseThunderRes(
                safeBaseThunderRes
        );

        result.setBaseIceRes(
                safeBaseIceRes
        );

        result.setBaseDragonRes(
                safeBaseDragonRes
        );


        /*
         * =================================================
         * modifier
         * =================================================
         */

        BigDecimal defenseAdd
                = modifier == null
                        ? BigDecimal.ZERO
                        : safeAdd(
                                modifier.getDefenseAdd()
                        );

        BigDecimal defenseMul
                = modifier == null
                        ? BigDecimal.ONE
                        : safeMultiplier(
                                modifier.getDefenseMul()
                        );


        BigDecimal fireResAdd
                = modifier == null
                        ? BigDecimal.ZERO
                        : safeAdd(
                                modifier.getFireResAdd()
                        );

        BigDecimal waterResAdd
                = modifier == null
                        ? BigDecimal.ZERO
                        : safeAdd(
                                modifier.getWaterResAdd()
                        );

        BigDecimal thunderResAdd
                = modifier == null
                        ? BigDecimal.ZERO
                        : safeAdd(
                                modifier.getThunderResAdd()
                        );

        BigDecimal iceResAdd
                = modifier == null
                        ? BigDecimal.ZERO
                        : safeAdd(
                                modifier.getIceResAdd()
                        );

        BigDecimal dragonResAdd
                = modifier == null
                        ? BigDecimal.ZERO
                        : safeAdd(
                                modifier.getDragonResAdd()
                        );


        result.setDefenseAdd(
                defenseAdd
        );

        result.setDefenseMul(
                defenseMul
        );

        result.setFireResAdd(
                fireResAdd
        );

        result.setWaterResAdd(
                waterResAdd
        );

        result.setThunderResAdd(
                thunderResAdd
        );

        result.setIceResAdd(
                iceResAdd
        );

        result.setDragonResAdd(
                dragonResAdd
        );


        /*
         * =================================================
         * 防御力
         * =================================================
         *
         * 基礎防御力
         * ×
         * 防御倍率
         * ＋
         * 防御加算
         *
         *
         * 例：
         *
         * 防御力500
         * 防御Lv7
         *
         * 500 × 1.10 + 35
         * = 585
         */
        BigDecimal defenseBeforeRound
                = BigDecimal.valueOf(
                        safeBaseDefense
                )
                .multiply(
                        defenseMul
                )
                .add(
                        defenseAdd
                );


        /*
         * 防御力が万が一マイナスになった場合は
         * 0未満にしない。
         */
        if (defenseBeforeRound.compareTo(
                BigDecimal.ZERO
        ) < 0) {

            defenseBeforeRound
                    = BigDecimal.ZERO;
        }


        result.setDefenseBeforeRound(
                defenseBeforeRound
        );


        /*
         * 表示用防御力は整数化。
         *
         * 小数部分は切り捨てる。
         */
        int finalDefense
                = defenseBeforeRound
                        .setScale(
                                0,
                                RoundingMode.FLOOR
                        )
                        .intValue();


        result.setFinalDefense(
                finalDefense
        );


        /*
         * =================================================
         * 通常の属性耐性加算
         * =================================================
         *
         * 防具耐性
         * ＋
         * スキル耐性
         *
         * の段階。
         *
         * まだ龍気活性などの
         * 固定処理は行わない。
         */

        int fireResBeforeSet
                = calculateResistanceBeforeSet(
                        safeBaseFireRes,
                        fireResAdd
                );

        int waterResBeforeSet
                = calculateResistanceBeforeSet(
                        safeBaseWaterRes,
                        waterResAdd
                );

        int thunderResBeforeSet
                = calculateResistanceBeforeSet(
                        safeBaseThunderRes,
                        thunderResAdd
                );

        int iceResBeforeSet
                = calculateResistanceBeforeSet(
                        safeBaseIceRes,
                        iceResAdd
                );

        int dragonResBeforeSet
                = calculateResistanceBeforeSet(
                        safeBaseDragonRes,
                        dragonResAdd
                );


        result.setFireResBeforeSet(
                fireResBeforeSet
        );

        result.setWaterResBeforeSet(
                waterResBeforeSet
        );

        result.setThunderResBeforeSet(
                thunderResBeforeSet
        );

        result.setIceResBeforeSet(
                iceResBeforeSet
        );

        result.setDragonResBeforeSet(
                dragonResBeforeSet
        );


        /*
         * =================================================
         * 変換前の全耐性固定
         * =================================================
         *
         * 主に龍気活性。
         *
         * null
         * → 通常耐性をそのまま使用
         *
         * 30
         * → 全耐性30
         *
         * 50
         * → 全耐性50
         */

        Integer allElementResSet
                = modifier == null
                        ? null
                        : modifier.getAllElementResSet();


        result.setAllElementResSet(
                allElementResSet
        );


        int effectiveFireRes
                = applyResistanceSet(
                        fireResBeforeSet,
                        allElementResSet
                );

        int effectiveWaterRes
                = applyResistanceSet(
                        waterResBeforeSet,
                        allElementResSet
                );

        int effectiveThunderRes
                = applyResistanceSet(
                        thunderResBeforeSet,
                        allElementResSet
                );

        int effectiveIceRes
                = applyResistanceSet(
                        iceResBeforeSet,
                        allElementResSet
                );

        int effectiveDragonRes
                = applyResistanceSet(
                        dragonResBeforeSet,
                        allElementResSet
                );


        /*
         * =================================================
         * 龍気変換用耐性
         * =================================================
         *
         * マイナス値
         * → 0
         *
         * 50超過
         * → 50
         *
         * として計算する。
         */

        int conversionFireRes
                = clampConversionResistance(
                        effectiveFireRes
                );

        int conversionWaterRes
                = clampConversionResistance(
                        effectiveWaterRes
                );

        int conversionThunderRes
                = clampConversionResistance(
                        effectiveThunderRes
                );

        int conversionIceRes
                = clampConversionResistance(
                        effectiveIceRes
                );

        int conversionDragonRes
                = clampConversionResistance(
                        effectiveDragonRes
                );


        result.setConversionFireRes(
                conversionFireRes
        );

        result.setConversionWaterRes(
                conversionWaterRes
        );

        result.setConversionThunderRes(
                conversionThunderRes
        );

        result.setConversionIceRes(
                conversionIceRes
        );

        result.setConversionDragonRes(
                conversionDragonRes
        );


        int conversionResistanceTotal
                = conversionFireRes
                + conversionWaterRes
                + conversionThunderRes
                + conversionIceRes
                + conversionDragonRes;


        result.setConversionResistanceTotal(
                conversionResistanceTotal
        );


        /*
         * =================================================
         * 龍気変換レート
         * =================================================
         */

        BigDecimal resistanceToElementRate
                = modifier == null
                        ? BigDecimal.ZERO
                        : safeAdd(
                                modifier
                                        .getResistanceToElementRate()
                        );


        result.setResistanceToElementRate(
                resistanceToElementRate
        );


        /*
         * =================================================
         * 龍気変換による属性加算
         * 計算前の生値
         * =================================================
         *
         * 変換元耐性合計
         * ×
         * 変換率
         */

        BigDecimal convertedElementAddBeforeRound
                = BigDecimal.valueOf(
                        conversionResistanceTotal
                )
                .multiply(
                        resistanceToElementRate
                );


        result.setConvertedElementAddBeforeRound(
                convertedElementAddBeforeRound
        );


        /*
         * =================================================
         * 武器種別の小数処理
         * =================================================
         *
         * 剣士・弓
         * → 八捨九入
         *
         * ライト・ヘビィ
         * → 小数を保持
         *
         *
         * weaponTypeがnullの場合も
         * 情報を失わないよう、
         * 生値をそのまま保持する。
         */

        BigDecimal convertedElementAdd
                = applyDragonConversionRounding(
                        convertedElementAddBeforeRound,
                        weaponType
                );


        result.setConvertedElementAdd(
                convertedElementAdd
        );


        /*
         * =================================================
         * 龍気変換後の耐性固定
         * =================================================
         *
         * 龍気変換【朱】
         * → 0
         */

        Integer postConversionAllElementResSet
                = modifier == null
                        ? null
                        : modifier
                                .getPostConversionAllElementResSet();


        result.setPostConversionAllElementResSet(
                postConversionAllElementResSet
        );


        /*
         * =================================================
         * 最終耐性
         * =================================================
         *
         * 龍気変換【朱】なら
         * postConversionAllElementResSet = 0
         *
         * なので、
         *
         * 最終耐性
         * 0 / 0 / 0 / 0 / 0
         *
         * となる。
         */

        result.setFinalFireRes(
                applyResistanceSet(
                        effectiveFireRes,
                        postConversionAllElementResSet
                )
        );

        result.setFinalWaterRes(
                applyResistanceSet(
                        effectiveWaterRes,
                        postConversionAllElementResSet
                )
        );

        result.setFinalThunderRes(
                applyResistanceSet(
                        effectiveThunderRes,
                        postConversionAllElementResSet
                )
        );

        result.setFinalIceRes(
                applyResistanceSet(
                        effectiveIceRes,
                        postConversionAllElementResSet
                )
        );

        result.setFinalDragonRes(
                applyResistanceSet(
                        effectiveDragonRes,
                        postConversionAllElementResSet
                )
        );


        /*
         * =================================================
         * 非龍属性無効化
         * =================================================
         */

        boolean nullifyNonDragonElement
                = modifier != null
                && Boolean.TRUE.equals(
                        modifier.getNullifyNonDragonElement()
                );


        result.setNullifyNonDragonElement(
                nullifyNonDragonElement
        );


        return result;
    }


    /*
     * =====================================================
     * 通常耐性加算
     * =====================================================
     */
    private int calculateResistanceBeforeSet(
            int baseResistance,
            BigDecimal resistanceAdd) {

        BigDecimal value
                = BigDecimal.valueOf(
                        baseResistance
                )
                .add(
                        safeAdd(
                                resistanceAdd
                        )
                );


        /*
         * DB上の耐性補正は整数値だが、
         * BigDecimalとして保持しているため
         * ここで整数へ変換する。
         */
        return value.intValue();
    }


    /*
     * =====================================================
     * 耐性固定値適用
     * =====================================================
     */
    private int applyResistanceSet(
            int currentResistance,
            Integer resistanceSet) {

        if (resistanceSet == null) {

            return currentResistance;
        }


        return resistanceSet;
    }


    /*
     * =====================================================
     * 龍気変換用耐性
     * =====================================================
     *
     * マイナス
     * → 0
     *
     * 0～50
     * → そのまま
     *
     * 51以上
     * → 50
     */
    private int clampConversionResistance(
            int resistance) {

        if (resistance < 0) {

            return 0;
        }


        if (resistance > 50) {

            return 50;
        }


        return resistance;
    }


    /*
     * =====================================================
     * 龍気変換の小数処理
     * =====================================================
     */
    private BigDecimal applyDragonConversionRounding(
            BigDecimal value,
            String weaponType) {

        if (value == null) {

            return BigDecimal.ZERO;
        }


        /*
         * 武器種不明なら、
         * 勝手に丸めず生値を保持する。
         */
        if (weaponType == null
                || weaponType.isBlank()) {

            return value;
        }


        /*
         * ライト・ヘビィの属性弾は
         * 八捨九入をせず、
         * 小数を保持したまま
         * 後続の属性弾計算へ渡す。
         */
        if (isBowgun(
                weaponType
        )) {

            return value;
        }


        /*
         * 剣士武器＋弓
         * → 八捨九入
         */
        return roundEightDownNineUp(
                value
        );
    }


    /*
     * =====================================================
     * ボウガン判定
     * =====================================================
     */
    private boolean isBowgun(
            String weaponType) {

        return "ライトボウガン".equals(
                weaponType
        )
                || "ヘビィボウガン".equals(
                        weaponType
                );
    }


    /*
     * =====================================================
     * 八捨九入
     * =====================================================
     *
     * 小数部分が
     *
     * 0.00 ～ 0.89
     * → 切り捨て
     *
     * 0.90 ～ 0.99...
     * → 繰り上げ
     *
     *
     * 例：
     *
     * 37.50
     * → 37
     *
     * 62.50
     * → 62
     *
     * 10.90
     * → 11
     */
    private BigDecimal roundEightDownNineUp(
            BigDecimal value) {

        if (value == null
                || value.compareTo(
                        BigDecimal.ZERO
                ) <= 0) {

            return BigDecimal.ZERO;
        }


        BigDecimal integerPart
                = value.setScale(
                        0,
                        RoundingMode.FLOOR
                );


        BigDecimal decimalPart
                = value.subtract(
                        integerPart
                );


        if (decimalPart.compareTo(
                new BigDecimal("0.9")
        ) >= 0) {

            return integerPart.add(
                    BigDecimal.ONE
            );
        }


        return integerPart;
    }


    /*
     * =====================================================
     * Integer null対策
     * =====================================================
     */
    private int safeInteger(
            Integer value) {

        return value == null
                ? 0
                : value;
    }


    /*
     * =====================================================
     * 加算値null対策
     * =====================================================
     */
    private BigDecimal safeAdd(
            BigDecimal value) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }


    /*
     * =====================================================
     * 倍率null対策
     * =====================================================
     */
    private BigDecimal safeMultiplier(
            BigDecimal value) {

        return value == null
                ? BigDecimal.ONE
                : value;
    }
}