package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowgunAmmoModifier;
import com.example.mhsunbreaksimulator.entity.BowgunAmmo;

@Service
public class BowgunAmmoModifierService {

    public BowgunAmmoModifier calculate(
            BowgunAmmo bowgunAmmo) {

        BowgunAmmoModifier result
                = new BowgunAmmoModifier();


        if (bowgunAmmo == null
                || bowgunAmmo.getAmmoType() == null
                || bowgunAmmo.getAmmoLevel() == null) {

            return result;
        }


        String ammoType
                = bowgunAmmo.getAmmoType();

        Integer ammoLevel
                = bowgunAmmo.getAmmoLevel();


        switch (ammoType) {

        case "通常弾":

            applyNormalAmmo(
                    result,
                    ammoLevel
            );

            break;


        case "貫通弾":

            applyPierceAmmo(
                    result,
                    ammoLevel
            );

            break;


        case "散弾":

            applySpreadAmmo(
                    result,
                    ammoLevel
            );

            break;


        case "放散弾":

            applyShrapnelAmmo(
                    result,
                    ammoLevel
            );

            break;


        case "火炎弾":

        case "水冷弾":

        case "電撃弾":

        case "氷結弾":

            applyElementAmmo(
                    result
            );

            break;


        case "貫通火炎弾":

        case "貫通水冷弾":

        case "貫通電撃弾":

        case "貫通氷結弾":

            applyPiercingElementAmmo(
                    result
            );

            break;


        case "滅龍弾":

            applyDragonAmmo(
                    result
            );

            break;


        default:

            break;
        }


        return result;
    }


    /*
     * 通常弾
     */
    private void applyNormalAmmo(
            BowgunAmmoModifier result,
            Integer ammoLevel) {

        switch (ammoLevel) {

        case 1:

            result.setPhysicalMotionValuePerHit(
                    new BigDecimal("16")
            );

            result.setMaxHitCount(
                    1
            );

            break;


        case 2:

            result.setPhysicalMotionValuePerHit(
                    new BigDecimal("22")
            );

            result.setMaxHitCount(
                    1
            );

            break;


        case 3:

            result.setPhysicalMotionValuePerHit(
                    new BigDecimal("34")
            );

            result.setMaxHitCount(
                    1
            );

            break;


        default:

            break;
        }
    }


    /*
     * 貫通弾
     */
    private void applyPierceAmmo(
            BowgunAmmoModifier result,
            Integer ammoLevel) {

        switch (ammoLevel) {

        case 1:

            result.setPhysicalMotionValuePerHit(
                    new BigDecimal("7")
            );

            result.setMaxHitCount(
                    7
            );

            break;


        case 2:

            result.setPhysicalMotionValuePerHit(
                    new BigDecimal("7")
            );

            result.setMaxHitCount(
                    7
            );

            break;


        case 3:

            result.setPhysicalMotionValuePerHit(
                    new BigDecimal("9")
            );

            result.setMaxHitCount(
                    7
            );

            break;


        default:

            break;
        }
    }


    /*
     * 散弾
     */
    private void applySpreadAmmo(
            BowgunAmmoModifier result,
            Integer ammoLevel) {

        switch (ammoLevel) {

        case 1:

            result.setPhysicalMotionValuePerHit(
                    new BigDecimal("7")
            );

            result.setMaxHitCount(
                    3
            );

            break;


        case 2:

            result.setPhysicalMotionValuePerHit(
                    new BigDecimal("7")
            );

            result.setMaxHitCount(
                    4
            );

            break;


        case 3:

            result.setPhysicalMotionValuePerHit(
                    new BigDecimal("10")
            );

            result.setMaxHitCount(
                    5
            );

            break;


        default:

            break;
        }
    }


    /*
     * 放散弾
     */
    private void applyShrapnelAmmo(
            BowgunAmmoModifier result,
            Integer ammoLevel) {

        switch (ammoLevel) {

        case 1:

            result.setPhysicalMotionValuePerHit(
                    new BigDecimal("6")
            );

            result.setMaxHitCount(
                    3
            );

            break;


        case 2:

            result.setPhysicalMotionValuePerHit(
                    new BigDecimal("5")
            );

            result.setMaxHitCount(
                    4
            );

            break;


        case 3:

            result.setPhysicalMotionValuePerHit(
                    new BigDecimal("5")
            );

            result.setMaxHitCount(
                    5
            );

            break;


        default:

            break;
        }
    }


    /*
     * 通常属性弾
     *
     * 火炎弾
     * 水冷弾
     * 電撃弾
     * 氷結弾
     *
     * 物理MV：16
     * 属性MV：40
     * 最大1hit
     */
    private void applyElementAmmo(
            BowgunAmmoModifier result) {

        result.setPhysicalMotionValuePerHit(
                new BigDecimal("16")
        );

        result.setElementMotionValuePerHit(
                new BigDecimal("40")
        );

        result.setMaxHitCount(
                1
        );
    }


    /*
     * 貫通属性弾
     *
     * 貫通火炎弾
     * 貫通水冷弾
     * 貫通電撃弾
     * 貫通氷結弾
     *
     * 1hitあたり
     * 物理MV：10
     * 属性MV：22
     *
     * 最大7hit
     */
    private void applyPiercingElementAmmo(
            BowgunAmmoModifier result) {

        result.setPhysicalMotionValuePerHit(
                new BigDecimal("10")
        );

        result.setElementMotionValuePerHit(
                new BigDecimal("22")
        );

        result.setMaxHitCount(
                7
        );
    }


    /*
     * 滅龍弾
     *
     * 物理MV：48
     * 龍属性MV：80
     * 最大1hit
     */
    private void applyDragonAmmo(
            BowgunAmmoModifier result) {

        result.setPhysicalMotionValuePerHit(
                new BigDecimal("48")
        );

        result.setElementMotionValuePerHit(
                new BigDecimal("80")
        );

        result.setMaxHitCount(
                1
        );
    }
}