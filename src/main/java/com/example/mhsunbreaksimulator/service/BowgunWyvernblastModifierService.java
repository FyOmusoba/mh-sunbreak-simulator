package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowgunWyvernblastModifier;

@Service
public class BowgunWyvernblastModifierService {

    /*
     * =====================================================
     * ライトボウガン
     * 起爆竜弾
     * =====================================================
     *
     * 爆発段階：
     *
     * SMALL
     * → MV26 / 火3 / 耐久消費1
     *
     * MEDIUM
     * → MV32 / 火5 / 耐久消費2
     *
     * LARGE
     * → MV49 / 火7 / 耐久消費4
     */
    public BowgunWyvernblastModifier calculate(
            String explosionSize) {

        BowgunWyvernblastModifier modifier
                = new BowgunWyvernblastModifier();


        if (explosionSize == null) {

            return modifier;
        }


        switch (explosionSize) {

        case "SMALL":

            set(
                    modifier,
                    "26",
                    "3",
                    1
            );

            break;


        case "MEDIUM":

            set(
                    modifier,
                    "32",
                    "5",
                    2
            );

            break;


        case "LARGE":

            set(
                    modifier,
                    "49",
                    "7",
                    4
            );

            break;


        default:

            /*
             * 不正値の場合は
             *
             * fixedDamageMotionValue = 0
             * fixedFireValue = 0
             * durabilityCost = 0
             *
             * の初期値のまま返す。
             */
            break;
        }


        return modifier;
    }


    /*
     * =====================================================
     * 共通設定
     * =====================================================
     */
    private void set(
            BowgunWyvernblastModifier modifier,
            String fixedDamageMotionValue,
            String fixedFireValue,
            Integer durabilityCost) {

        modifier.setFixedDamageMotionValue(
                new BigDecimal(
                        fixedDamageMotionValue
                )
        );


        modifier.setFixedFireValue(
                new BigDecimal(
                        fixedFireValue
                )
        );


        modifier.setDurabilityCost(
                durabilityCost
        );
    }
}