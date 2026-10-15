package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowgunStatusModifier;
import com.example.mhsunbreaksimulator.entity.BowgunAmmo;

@Service
public class BowgunStatusModifierService {

    public BowgunStatusModifier calculate(
            BowgunAmmo bowgunAmmo) {

        BowgunStatusModifier result
                = new BowgunStatusModifier();


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

        case "毒弾":

            applyPoisonAmmo(
                    result,
                    ammoLevel
            );

            break;


        case "麻痺弾":

            applyParalysisAmmo(
                    result,
                    ammoLevel
            );

            break;


        case "睡眠弾":

            applySleepAmmo(
                    result,
                    ammoLevel
            );

            break;


        case "減気弾":

            applyExhaustAmmo(
                    result,
                    ammoLevel
            );

            break;


        default:

            break;
        }


        return result;
    }


    /*
     * 毒弾
     *
     * Lv1：毒蓄積40
     * Lv2：毒蓄積80
     */
    private void applyPoisonAmmo(
            BowgunStatusModifier result,
            Integer ammoLevel) {

        result.setStatusType(
                "poison"
        );


        switch (ammoLevel) {

        case 1:

            result.setStatusBuildupPerHit(
                    new BigDecimal("40")
            );

            break;


        case 2:

            result.setStatusBuildupPerHit(
                    new BigDecimal("80")
            );

            break;


        default:

            return;
        }


        result.setMaxHitCount(
                1
        );

        result.setStatusAmmo(
                true
        );
    }


    /*
     * 麻痺弾
     *
     * Lv1：麻痺蓄積40
     * Lv2：麻痺蓄積80
     */
    private void applyParalysisAmmo(
            BowgunStatusModifier result,
            Integer ammoLevel) {

        result.setStatusType(
                "paralysis"
        );


        switch (ammoLevel) {

        case 1:

            result.setStatusBuildupPerHit(
                    new BigDecimal("40")
            );

            break;


        case 2:

            result.setStatusBuildupPerHit(
                    new BigDecimal("80")
            );

            break;


        default:

            return;
        }


        result.setMaxHitCount(
                1
        );

        result.setStatusAmmo(
                true
        );
    }


    /*
     * 睡眠弾
     *
     * Lv1：睡眠蓄積40
     * Lv2：睡眠蓄積80
     */
    private void applySleepAmmo(
            BowgunStatusModifier result,
            Integer ammoLevel) {

        result.setStatusType(
                "sleep"
        );


        switch (ammoLevel) {

        case 1:

            result.setStatusBuildupPerHit(
                    new BigDecimal("40")
            );

            break;


        case 2:

            result.setStatusBuildupPerHit(
                    new BigDecimal("80")
            );

            break;


        default:

            return;
        }


        result.setMaxHitCount(
                1
        );

        result.setStatusAmmo(
                true
        );
    }


    /*
     * 減気弾
     *
     * Lv1：
     * 減気50
     * 気絶20
     *
     * Lv2：
     * 減気100
     * 気絶30
     *
     * 毒・麻痺・睡眠とは違うため
     * statusBuildupPerHitには入れず、
     *
     * exhaustBuildupPerHit
     * stunBuildupPerHit
     *
     * に分けて保持する。
     */
    private void applyExhaustAmmo(
            BowgunStatusModifier result,
            Integer ammoLevel) {

        result.setStatusType(
                "exhaust"
        );


        switch (ammoLevel) {

        case 1:

            result.setExhaustBuildupPerHit(
                    new BigDecimal("50")
            );

            result.setStunBuildupPerHit(
                    new BigDecimal("20")
            );

            break;


        case 2:

            result.setExhaustBuildupPerHit(
                    new BigDecimal("100")
            );

            result.setStunBuildupPerHit(
                    new BigDecimal("30")
            );

            break;


        default:

            return;
        }


        result.setMaxHitCount(
                1
        );

        result.setStatusAmmo(
                true
        );
    }
}