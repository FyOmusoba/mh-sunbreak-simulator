package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowCoatingModifier;

@Service
public class BowCoatingModifierService {

    public BowCoatingModifier calculate(
            String coatingType) {

        BowCoatingModifier modifier
                = new BowCoatingModifier();

        if (coatingType == null
                || coatingType.isBlank()) {

            return modifier;
        }

        String normalizedType
                = coatingType.replace(
                        "ビン",
                        ""
                );


        switch (normalizedType) {

        case "強撃":

            modifier.setPhysicalMul(
                    new BigDecimal("1.35")
            );

            break;


        case "接撃":

            modifier.setPhysicalMul(
                    new BigDecimal("1.20")
            );

            break;


        case "毒":
        case "麻痺":
        case "睡眠":
        case "爆破":
        case "減気":

            /*
             * 状態異常系ビンは
             * ここでは物理倍率を変更しない。
             *
             * 状態異常蓄積値は、
             * 後の状態異常計算で別途処理する。
             */
            break;


        default:
            break;
        }

        return modifier;
    }
}