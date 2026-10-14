package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowShotModifier;

@Service
public class BowShotModifierService {

    public BowShotModifier calculate(
            String shotType,
            Integer shotLevel) {

        BowShotModifier modifier
                = new BowShotModifier();

        if (shotType == null
                || shotType.isBlank()
                || shotLevel == null) {

            return modifier;
        }

        switch (shotType) {

        case "連射":
            applyRapid(
                    modifier,
                    shotLevel
            );
            break;

        case "拡散":
            applySpread(
                    modifier,
                    shotLevel
            );
            break;

        case "貫通":
            applyPierce(
                    modifier,
                    shotLevel
            );
            break;

        default:
            break;
        }

        return modifier;
    }


    /* ========================================== */
    /* 連射 */
    /* ========================================== */

    private void applyRapid(
            BowShotModifier modifier,
            Integer shotLevel) {

        switch (shotLevel) {

        case 1:
            set(
                    modifier,
                    "14",
                    1
            );
            break;

        case 2:
            set(
                    modifier,
                    "9",
                    2
            );
            break;

        case 3:
            set(
                    modifier,
                    "10",
                    3
            );
            break;

        case 4:
            set(
                    modifier,
                    "10",
                    4
            );
            break;

        case 5:
            set(
                    modifier,
                    "11",
                    4
            );
            break;

        default:
            break;
        }
    }


    /* ========================================== */
    /* 拡散 */
    /* ========================================== */

    private void applySpread(
            BowShotModifier modifier,
            Integer shotLevel) {

        switch (shotLevel) {

        case 1:
            set(
                    modifier,
                    "5",
                    3
            );
            break;

        case 2:
            set(
                    modifier,
                    "8",
                    3
            );
            break;

        case 3:
            set(
                    modifier,
                    "7",
                    5
            );
            break;

        case 4:
            set(
                    modifier,
                    "9",
                    5
            );
            break;

        case 5:
            set(
                    modifier,
                    "10",
                    5
            );
            break;

        default:
            break;
        }
    }


    /* ========================================== */
    /* 貫通 */
    /* ========================================== */

    private void applyPierce(
            BowShotModifier modifier,
            Integer shotLevel) {

        switch (shotLevel) {

        case 1:
            set(
                    modifier,
                    "4",
                    7
            );
            break;

        case 2:
            set(
                    modifier,
                    "6",
                    7
            );
            break;

        case 3:
            set(
                    modifier,
                    "8",
                    7
            );
            break;

        case 4:
            set(
                    modifier,
                    "10",
                    7
            );
            break;

        case 5:
            set(
                    modifier,
                    "12",
                    7
            );
            break;

        default:
            break;
        }
    }


    /* ========================================== */
    /* 共通設定 */
    /* ========================================== */

    private void set(
            BowShotModifier modifier,
            String motionValue,
            Integer maxHitCount) {

        modifier.setMotionValuePerHit(
                new BigDecimal(
                        motionValue
                )
        );

        modifier.setMaxHitCount(
                maxHitCount
        );
    }
}