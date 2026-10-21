package com.example.mhsunbreaksimulator.service;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.WeaponAugmentModifier;
import com.example.mhsunbreaksimulator.entity.Weapon;

@Service
public class WeaponAugmentModifierService {

    /*
     * =====================================================
     * 武器傀異錬成補正
     * =====================================================
     */
    public WeaponAugmentModifier calculate(
            Weapon weapon,
            Integer attackLevel,
            Integer elementLevel,
            Integer affinityLevel,
            Integer sharpnessLevel) {

        WeaponAugmentModifier result
                = new WeaponAugmentModifier();


        if (weapon == null) {

            return result;
        }


        /*
         * =================================================
         * 攻撃力強化
         * =================================================
         */
        result.setAttackAdd(
                calculateAttackAdd(
                        attackLevel
                )
        );


        /*
         * =================================================
         * 会心率強化
         * =================================================
         */
        result.setAffinityAdd(
                calculateAffinityAdd(
                        affinityLevel
                )
        );


        /*
         * =================================================
         * 斬れ味強化
         * =================================================
         *
         * 弓・ライト・ヘビィには
         * 斬れ味が存在しないため0。
         */
        result.setSharpnessAdd(
                calculateSharpnessAdd(
                        weapon.getWeaponType(),
                        sharpnessLevel
                )
        );


        /*
         * =================================================
         * 属性・状態異常強化
         * =================================================
         */

        int normalizedElementLevel
                = normalizeLevel(
                        elementLevel,
                        8
                );


        /*
         * 双剣で
         *
         * 通常属性 + 状態異常
         *
         * の組み合わせの場合は、
         * 専用の「属性・状態異常強化」となり、
         * 最大Lv4。
         */
        if ("双剣".equals(
                weapon.getWeaponType()
        )
                && isMixedElementAndStatus(
                        weapon.getElementType(),
                        weapon.getSubElementType()
                )) {

            normalizedElementLevel
                    = Math.min(
                            normalizedElementLevel,
                            4
                    );
        }


        result.setElementAdd(
                calculateElementOrStatusAdd(
                        weapon.getWeaponType(),
                        weapon.getElementType(),
                        normalizedElementLevel
                )
        );


        result.setSubElementAdd(
                calculateElementOrStatusAdd(
                        weapon.getWeaponType(),
                        weapon.getSubElementType(),
                        normalizedElementLevel
                )
        );


        return result;
    }


    /*
     * =====================================================
     * 攻撃力強化
     * =====================================================
     *
     * Lv1 +5
     * Lv2 +10
     * Lv3 +15
     * Lv4 +20
     */
    private int calculateAttackAdd(
            Integer level) {

        int normalizedLevel
                = normalizeLevel(
                        level,
                        4
                );


        return switch (normalizedLevel) {

        case 1 -> 5;
        case 2 -> 10;
        case 3 -> 15;
        case 4 -> 20;

        default -> 0;
        };
    }


    /*
     * =====================================================
     * 会心率強化
     * =====================================================
     *
     * Lv1 +5%
     * Lv2 +10%
     * Lv3 +15%
     */
    private int calculateAffinityAdd(
            Integer level) {

        int normalizedLevel
                = normalizeLevel(
                        level,
                        3
                );


        return switch (normalizedLevel) {

        case 1 -> 5;
        case 2 -> 10;
        case 3 -> 15;

        default -> 0;
        };
    }


    /*
     * =====================================================
     * 斬れ味強化
     * =====================================================
     *
     * Lv1 +10
     * Lv2 +20
     * Lv3 +30
     * Lv4 +50
     */
    private int calculateSharpnessAdd(
            String weaponType,
            Integer level) {

        if (isGunnerWeapon(
                weaponType
        )) {

            return 0;
        }


        int normalizedLevel
                = normalizeLevel(
                        level,
                        4
                );


        return switch (normalizedLevel) {

        case 1 -> 10;
        case 2 -> 20;
        case 3 -> 30;
        case 4 -> 50;

        default -> 0;
        };
    }


    /*
     * =====================================================
     * 属性 / 状態異常強化
     * =====================================================
     */
    private int calculateElementOrStatusAdd(
            String weaponType,
            String elementType,
            int level) {

        if (elementType == null
                || elementType.isBlank()
                || "無".equals(
                        elementType
                )) {

            return 0;
        }


        /*
         * ボウガンには属性強化・状態異常強化の
         * 武器傀異錬成項目が存在しない。
         */
        if ("ライトボウガン".equals(
                weaponType
        )
                ||
                "ヘビィボウガン".equals(
                        weaponType
                )) {

            return 0;
        }


        if (isNormalElement(
                elementType
        )) {

            return calculateNormalElementAdd(
                    weaponType,
                    level
            );
        }


        if (isStatusElement(
                elementType
        )) {

            return calculateStatusAdd(
                    weaponType,
                    level
            );
        }


        return 0;
    }


    /*
     * =====================================================
     * 通常属性強化
     * =====================================================
     *
     * 火 / 水 / 雷 / 氷 / 龍
     */
    private int calculateNormalElementAdd(
            String weaponType,
            int level) {

        int normalizedLevel
                = normalizeLevel(
                        level,
                        8
                );


        /*
         * 大剣・ハンマー
         */
        if ("大剣".equals(
                weaponType
        )
                ||
                "ハンマー".equals(
                        weaponType
                )) {

            return switch (normalizedLevel) {

            case 1 -> 5;
            case 2 -> 10;
            case 3 -> 15;
            case 4 -> 20;
            case 5 -> 25;
            case 6 -> 33;
            case 7 -> 43;
            case 8 -> 55;

            default -> 0;
            };
        }


        /*
         * ランス・ガンランス
         */
        if ("ランス".equals(
                weaponType
        )
                ||
                "ガンランス".equals(
                        weaponType
                )) {

            return switch (normalizedLevel) {

            case 1 -> 3;
            case 2 -> 6;
            case 3 -> 10;
            case 4 -> 14;
            case 5 -> 18;
            case 6 -> 24;
            case 7 -> 32;
            case 8 -> 42;

            default -> 0;
            };
        }


        /*
         * その他
         *
         * 太刀
         * 片手剣
         * 双剣
         * 狩猟笛
         * スラッシュアックス
         * チャージアックス
         * 操虫棍
         * 弓
         */
        return switch (normalizedLevel) {

        case 1 -> 3;
        case 2 -> 6;
        case 3 -> 9;
        case 4 -> 12;
        case 5 -> 15;
        case 6 -> 20;
        case 7 -> 27;
        case 8 -> 35;

        default -> 0;
        };
    }


    /*
     * =====================================================
     * 状態異常強化
     * =====================================================
     *
     * 毒 / 麻痺 / 睡眠 / 爆破
     */
    private int calculateStatusAdd(
            String weaponType,
            int level) {

        int normalizedLevel
                = normalizeLevel(
                        level,
                        4
                );


        /*
         * 大剣
         */
        if ("大剣".equals(
                weaponType
        )) {

            return switch (normalizedLevel) {

            case 1 -> 6;
            case 2 -> 12;
            case 3 -> 18;
            case 4 -> 24;

            default -> 0;
            };
        }


        /*
         * 双剣・弓
         */
        if ("双剣".equals(
                weaponType
        )
                ||
                "弓".equals(
                        weaponType
                )) {

            return switch (normalizedLevel) {

            case 1 -> 3;
            case 2 -> 6;
            case 3 -> 9;
            case 4 -> 12;

            default -> 0;
            };
        }


        /*
         * その他
         */
        return switch (normalizedLevel) {

        case 1 -> 4;
        case 2 -> 8;
        case 3 -> 12;
        case 4 -> 16;

        default -> 0;
        };
    }


    /*
     * =====================================================
     * 通常属性判定
     * =====================================================
     */
    private boolean isNormalElement(
            String elementType) {

        return "火".equals(
                elementType
        )
                ||
                "水".equals(
                        elementType
                )
                ||
                "雷".equals(
                        elementType
                )
                ||
                "氷".equals(
                        elementType
                )
                ||
                "龍".equals(
                        elementType
                );
    }


    /*
     * =====================================================
     * 状態異常判定
     * =====================================================
     */
    private boolean isStatusElement(
            String elementType) {

        return "毒".equals(
                elementType
        )
                ||
                "麻痺".equals(
                        elementType
                )
                ||
                "睡眠".equals(
                        elementType
                )
                ||
                "爆破".equals(
                        elementType
                );
    }


    /*
     * =====================================================
     * 双剣
     * 通常属性 + 状態異常判定
     * =====================================================
     */
    private boolean isMixedElementAndStatus(
            String mainElementType,
            String subElementType) {

        if (mainElementType == null
                || subElementType == null) {

            return false;
        }


        return (
                isNormalElement(
                        mainElementType
                )
                &&
                isStatusElement(
                        subElementType
                )
        )
                ||
                (
                        isStatusElement(
                                mainElementType
                        )
                        &&
                        isNormalElement(
                                subElementType
                        )
                );
    }


    /*
     * =====================================================
     * ガンナー武器判定
     * =====================================================
     */
    private boolean isGunnerWeapon(
            String weaponType) {

        return "弓".equals(
                weaponType
        )
                ||
                "ライトボウガン".equals(
                        weaponType
                )
                ||
                "ヘビィボウガン".equals(
                        weaponType
                );
    }


    /*
     * =====================================================
     * Lv補正
     * =====================================================
     */
    private int normalizeLevel(
            Integer level,
            int maxLevel) {

        if (level == null
                || level <= 0) {

            return 0;
        }


        return Math.min(
                level,
                maxLevel
        );
    }
}