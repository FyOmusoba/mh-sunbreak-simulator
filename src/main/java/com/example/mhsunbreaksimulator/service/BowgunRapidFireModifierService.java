package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowgunRapidFireModifier;
import com.example.mhsunbreaksimulator.entity.BowgunAmmo;

@Service
public class BowgunRapidFireModifierService {

    public BowgunRapidFireModifier calculate(
            BowgunAmmo bowgunAmmo) {

        BowgunRapidFireModifier result
                = new BowgunRapidFireModifier();


        if (bowgunAmmo == null
                || !Boolean.TRUE.equals(
                        bowgunAmmo.getRapidFire()
                )) {

            return result;
        }


        result.setRapidFire(
                true
        );


        /*
         * 速射発射数
         *
         * 基本：3発
         *
         * 一部の弾：
         * 2発
         */
        result.setShotCount(
                isTwoShotRapidFireAmmo(
                        bowgunAmmo.getAmmoType()
                )
                        ? 2
                        : 3
        );


        /*
         * 速射1発ごとの物理補正
         */
        result.setPhysicalMulPerShot(
                new BigDecimal("0.70")
        );


        /*
         * 速射1発ごとの属性補正
         */
        result.setElementMulPerShot(
                new BigDecimal("0.50")
        );


        return result;
    }


    /*
     * 2発速射になる弾
     */
    private boolean isTwoShotRapidFireAmmo(
            String ammoType) {

        if (ammoType == null) {

            return false;
        }


        return switch (ammoType) {

        /*
         * 貫通属性弾
         */
        case "貫通火炎弾",
             "貫通水冷弾",
             "貫通電撃弾",
             "貫通氷結弾",

             /*
              * 龍属性弾
              */
             "滅龍弾",
             "貫通滅龍弾",

             /*
              * 特殊物理弾
              */
             "斬裂弾",
             "徹甲榴弾",

             /*
              * 状態異常弾
              */
             "毒弾",
             "麻痺弾",
             "睡眠弾",
             "減気弾"

                -> true;


        default -> false;
        };
    }
}