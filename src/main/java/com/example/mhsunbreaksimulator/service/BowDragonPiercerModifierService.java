package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowDragonPiercerModifier;

@Service
public class BowDragonPiercerModifierService {

    /*
     * =====================================================
     * 竜の一矢
     * =====================================================
     *
     * 溜め段階ごとの1ヒットあたりモーション値
     *
     * Lv1 = 28
     * Lv2 = 30
     * Lv3 = 32
     * Lv4 = 34
     *
     * 最大ヒット数 = 7
     */
    public BowDragonPiercerModifier calculate(
            Integer chargeLevel) {

        BowDragonPiercerModifier modifier
                = new BowDragonPiercerModifier();

        if (chargeLevel == null) {

            return modifier;
        }


        switch (chargeLevel) {

        case 1:

            set(
                    modifier,
                    "28"
            );

            break;


        case 2:

            set(
                    modifier,
                    "30"
            );

            break;


        case 3:

            set(
                    modifier,
                    "32"
            );

            break;


        case 4:

            set(
                    modifier,
                    "34"
            );

            break;


        default:

            /*
             * 不正な溜め段階の場合は
             * 初期値
             *
             * motionValuePerHit = 0
             * maxHitCount = 0
             *
             * のまま返す。
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
            BowDragonPiercerModifier modifier,
            String motionValue) {

        modifier.setMotionValuePerHit(
                new BigDecimal(
                        motionValue
                )
        );

        modifier.setMaxHitCount(
                7
        );
    }
}