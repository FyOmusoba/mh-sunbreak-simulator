package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.SharpnessModifier;

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
}