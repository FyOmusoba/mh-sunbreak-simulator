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


        case "貫通滅龍弾":

            applyPiercingDragonAmmo(
                    result
            );

            break;


        case "斬裂弾":

            applySlicingAmmo(
                    result
            );

            break;


        case "徹甲榴弾":

            applyStickyAmmo(
                    result,
                    ammoLevel
            );

            break;


        case "拡散弾":

            applyClusterAmmo(
                    result,
                    ammoLevel
            );

            break;


        case "竜撃弾":

            applyWyvernAmmo(
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
     * 物理MV：10
     * 属性MV：22
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


    /*
     * 貫通滅龍弾
     *
     * 1hitあたり
     *
     * 物理MV：20
     * 龍属性MV：44
     *
     * 最大7hit
     */
    private void applyPiercingDragonAmmo(
            BowgunAmmoModifier result) {

        result.setPhysicalMotionValuePerHit(
                new BigDecimal("20")
        );

        result.setElementMotionValuePerHit(
                new BigDecimal("44")
        );

        result.setMaxHitCount(
                7
        );
    }


    /*
     * 斬裂弾
     *
     * 着弾：
     * 物理MV 1
     *
     * 炸裂：
     * 切断属性
     * 物理MV 14 × 3hit
     */
    private void applySlicingAmmo(
            BowgunAmmoModifier result) {

        result.setImpactPhysicalMotionValue(
                new BigDecimal("1")
        );

        result.setSecondaryPhysicalMotionValuePerHit(
                new BigDecimal("14")
        );

        result.setSecondaryHitCount(
                3
        );

        result.setSecondaryUsesSeveringHitzone(
                true
        );

        result.setSecondaryIgnoresPhysicalHitzone(
                false
        );
    }


    /*
     * 徹甲榴弾
     */
    private void applyStickyAmmo(
            BowgunAmmoModifier result,
            Integer ammoLevel) {

        result.setImpactPhysicalMotionValue(
                new BigDecimal("1")
        );

        result.setSecondaryHitCount(
                1
        );

        result.setSecondaryIgnoresPhysicalHitzone(
                true
        );

        result.setSecondaryUsesSeveringHitzone(
                false
        );


        switch (ammoLevel) {

        case 1:

            result.setSecondaryFixedDamageMotionValuePerHit(
                    new BigDecimal("12")
            );

            result.setSecondaryFixedElementValuePerHit(
                    new BigDecimal("3")
            );

            break;


        case 2:

            result.setSecondaryFixedDamageMotionValuePerHit(
                    new BigDecimal("15")
            );

            result.setSecondaryFixedElementValuePerHit(
                    new BigDecimal("5")
            );

            break;


        case 3:

            result.setSecondaryFixedDamageMotionValuePerHit(
                    new BigDecimal("19")
            );

            result.setSecondaryFixedElementValuePerHit(
                    new BigDecimal("5")
            );

            break;


        default:

            result.setImpactPhysicalMotionValue(
                    BigDecimal.ZERO
            );

            result.setSecondaryHitCount(
                    0
            );

            result.setSecondaryIgnoresPhysicalHitzone(
                    false
            );

            break;
        }
    }


    /*
     * 拡散弾
     */
    private void applyClusterAmmo(
            BowgunAmmoModifier result,
            Integer ammoLevel) {

        result.setImpactPhysicalMotionValue(
                new BigDecimal("1")
        );

        result.setSecondaryIgnoresPhysicalHitzone(
                true
        );

        result.setSecondaryUsesSeveringHitzone(
                false
        );


        switch (ammoLevel) {

        case 1:

            result.setSecondaryFixedDamageMotionValuePerHit(
                    new BigDecimal("10")
            );

            result.setSecondaryFixedElementValuePerHit(
                    new BigDecimal("5")
            );

            result.setSecondaryHitCount(
                    3
            );

            break;


        case 2:

            result.setSecondaryFixedDamageMotionValuePerHit(
                    new BigDecimal("15")
            );

            result.setSecondaryFixedElementValuePerHit(
                    new BigDecimal("8")
            );

            result.setSecondaryHitCount(
                    4
            );

            break;


        case 3:

            result.setSecondaryFixedDamageMotionValuePerHit(
                    new BigDecimal("20")
            );

            result.setSecondaryFixedElementValuePerHit(
                    new BigDecimal("10")
            );

            result.setSecondaryHitCount(
                    5
            );

            break;


        default:

            result.setImpactPhysicalMotionValue(
                    BigDecimal.ZERO
            );

            result.setSecondaryFixedDamageMotionValuePerHit(
                    BigDecimal.ZERO
            );

            result.setSecondaryFixedElementValuePerHit(
                    BigDecimal.ZERO
            );

            result.setSecondaryHitCount(
                    0
            );

            result.setSecondaryIgnoresPhysicalHitzone(
                    false
            );

            break;
        }
    }


    /*
     * 竜撃弾
     *
     * 1段目：
     * 固定0.80 × 攻撃力
     * ＋ 火40
     *
     * 2段目：
     * 固定0.19 × 攻撃力
     * ＋ 火20
     */
    private void applyWyvernAmmo(
            BowgunAmmoModifier result) {

        /*
         * 1段目
         */
        result.setSecondaryFixedDamageMotionValuePerHit(
                new BigDecimal("80")
        );

        result.setSecondaryFixedElementValuePerHit(
                new BigDecimal("40")
        );

        result.setSecondaryHitCount(
                1
        );

        result.setSecondaryIgnoresPhysicalHitzone(
                true
        );

        result.setSecondaryUsesSeveringHitzone(
                false
        );


        /*
         * 2段目
         */
        result.setAdditionalFixedDamageMotionValuePerHit(
                new BigDecimal("19")
        );

        result.setAdditionalFixedElementValuePerHit(
                new BigDecimal("20")
        );

        result.setAdditionalFixedHitCount(
                1
        );

        result.setAdditionalIgnoresPhysicalHitzone(
                true
        );
    }
}