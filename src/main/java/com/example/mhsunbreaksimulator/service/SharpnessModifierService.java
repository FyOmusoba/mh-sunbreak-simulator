package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.SharpnessModifier;
import com.example.mhsunbreaksimulator.entity.Weapon;

@Service
public class SharpnessModifierService {

    /*
     * =====================================================
     * 斬れ味補正
     * =====================================================
     *
     * 現在使用する斬れ味色から、
     *
     * ・物理補正
     * ・属性補正
     *
     * を取得する。
     */
    public SharpnessModifier calculate(
            String sharpnessColor) {

        SharpnessModifier result
                = new SharpnessModifier();


        if (sharpnessColor == null
                || sharpnessColor.isBlank()) {

            return result;
        }


        result.setSharpnessColor(
                sharpnessColor
        );


        switch (sharpnessColor) {

        case "赤":

            result.setPhysicalMultiplier(
                    new BigDecimal("0.50")
            );

            result.setElementMultiplier(
                    new BigDecimal("0.25")
            );

            break;


        case "橙":

            result.setPhysicalMultiplier(
                    new BigDecimal("0.75")
            );

            result.setElementMultiplier(
                    new BigDecimal("0.50")
            );

            break;


        case "黄":

            result.setPhysicalMultiplier(
                    new BigDecimal("1.00")
            );

            result.setElementMultiplier(
                    new BigDecimal("0.75")
            );

            break;


        case "緑":

            result.setPhysicalMultiplier(
                    new BigDecimal("1.05")
            );

            result.setElementMultiplier(
                    new BigDecimal("1.00")
            );

            break;


        case "青":

            result.setPhysicalMultiplier(
                    new BigDecimal("1.20")
            );

            result.setElementMultiplier(
                    new BigDecimal("1.0625")
            );

            break;


        case "白":

            result.setPhysicalMultiplier(
                    new BigDecimal("1.32")
            );

            result.setElementMultiplier(
                    new BigDecimal("1.15")
            );

            break;


        case "紫":

            result.setPhysicalMultiplier(
                    new BigDecimal("1.39")
            );

            result.setElementMultiplier(
                    new BigDecimal("1.25")
            );

            break;


        default:

            /*
             * 不明な色の場合は
             * 安全のため倍率1.00。
             */
            result.setPhysicalMultiplier(
                    BigDecimal.ONE
            );

            result.setElementMultiplier(
                    BigDecimal.ONE
            );

            break;
        }


        return result;
    }


    /*
     * =====================================================
     * 武器傀異錬成反映後の最大斬れ味色
     * =====================================================
     *
     * weapon.maxSharpnessColor
     * と
     *
     * takumiVal1～4
     *
     * を利用して、
     *
     * 斬れ味強化によって
     * 上位の斬れ味色まで到達するかを判定する。
     *
     *
     * 例：
     *
     * 基礎最大色：白
     *
     * takumiVal1 = 10
     * takumiVal2 = 40
     *
     * +10 → 白
     * +20 → 紫
     * +50 → 紫
     */
    public String calculateAugmentedSharpnessColor(
            Weapon weapon,
            Integer sharpnessAdd) {

        if (weapon == null) {

            return null;
        }


        String currentColor
                = weapon.getMaxSharpnessColor();


        if (currentColor == null
                || currentColor.isBlank()) {

            return currentColor;
        }


        if (sharpnessAdd == null
                || sharpnessAdd <= 0) {

            return currentColor;
        }


        /*
         * 紫より上の斬れ味色はない。
         */
        if ("紫".equals(
                currentColor
        )) {

            return currentColor;
        }


        int[] takumiValues = {

                safeValue(
                        weapon.getTakumiVal1()
                ),

                safeValue(
                        weapon.getTakumiVal2()
                ),

                safeValue(
                        weapon.getTakumiVal3()
                ),

                safeValue(
                        weapon.getTakumiVal4()
                )
        };


        /*
         * 匠拡張データが全くない武器。
         *
         * 傀異錬成でゲージ量は増えても、
         * 最大色そのものは変化しない。
         */
        int totalTakumiValue
                = 0;


        for (int value : takumiValues) {

            totalTakumiValue
                    += value;
        }


        if (totalTakumiValue <= 0) {

            return currentColor;
        }


        /*
         * 現在の傀異錬成では最大+50。
         */
        int remaining
                = Math.min(
                        sharpnessAdd,
                        50
                );


        /*
         * takumiVal1
         * ↓
         * takumiVal2
         * ↓
         * takumiVal3
         * ↓
         * takumiVal4
         *
         * の順に消費していき、
         * 必要量を超えた時点で
         * 次の斬れ味色へ進む。
         */
        for (int takumiValue : takumiValues) {

            if (remaining <= takumiValue) {

                return currentColor;
            }


            remaining
                    -= takumiValue;


            String nextColor
                    = nextSharpnessColor(
                            currentColor
                    );


            if (nextColor == null) {

                return currentColor;
            }


            currentColor
                    = nextColor;
        }


        return currentColor;
    }


    /*
     * =====================================================
     * 次の斬れ味色
     * =====================================================
     */
    private String nextSharpnessColor(
            String currentColor) {

        if (currentColor == null) {

            return null;
        }


        return switch (currentColor) {

        case "赤" -> "橙";

        case "橙" -> "黄";

        case "黄" -> "緑";

        case "緑" -> "青";

        case "青" -> "白";

        case "白" -> "紫";

        default -> null;
        };
    }


    /*
     * =====================================================
     * null対策
     * =====================================================
     */
    private int safeValue(
            Integer value) {

        return value == null
                || value < 0
                        ? 0
                        : value;
    }
}