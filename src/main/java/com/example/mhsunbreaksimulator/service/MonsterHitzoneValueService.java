package com.example.mhsunbreaksimulator.service;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.MonsterHitzoneValueResult;
import com.example.mhsunbreaksimulator.entity.MonsterHitzone;
import com.example.mhsunbreaksimulator.entity.Weapon;

@Service
public class MonsterHitzoneValueService {

    /*
     * =====================================================
     * 武器情報＋選択部位から
     * 使用する肉質を決定する
     * =====================================================
     */
    public MonsterHitzoneValueResult calculate(
            Weapon weapon,
            MonsterHitzone hitzone) {

        MonsterHitzoneValueResult result
                = new MonsterHitzoneValueResult();


        /*
         * 部位未選択なら
         * 初期値のまま返す。
         */
        if (hitzone == null) {

            return result;
        }


        result.setPartName(
                hitzone.getPartName()
        );


        /*
         * 武器未選択なら
         * 部位名だけ保持して終了。
         */
        if (weapon == null) {

            return result;
        }


        /*
         * =====================================================
         * 物理肉質
         * =====================================================
         *
         * weapon.attackProperty
         *
         * 斬
         * 打
         * 弾
         *
         * に応じて使用肉質を決定する。
         */
        applyPhysicalHitzone(
                result,
                weapon.getAttackProperty(),
                hitzone
        );


        /*
         * =====================================================
         * 主属性
         * =====================================================
         */
        result.setElementType(
                weapon.getElementType()
        );


        applyMainElementHitzone(
                result,
                weapon.getElementType(),
                hitzone
        );


        /*
         * =====================================================
         * 副属性
         * =====================================================
         */
        result.setSubElementType(
                weapon.getSubElementType()
        );


        applySubElementHitzone(
                result,
                weapon.getSubElementType(),
                hitzone
        );


        return result;
    }


    /*
     * =====================================================
     * 物理肉質
     * =====================================================
     */
    private void applyPhysicalHitzone(
            MonsterHitzoneValueResult result,
            String attackProperty,
            MonsterHitzone hitzone) {

        if (attackProperty == null) {

            return;
        }


        switch (attackProperty) {

        case "斬":

            result.setPhysicalHitzoneType(
                    "SEVER"
            );

            result.setPhysicalHitzone(
                    valueOrZero(
                            hitzone.getSever()
                    )
            );

            break;


        case "打":

            result.setPhysicalHitzoneType(
                    "BLUNT"
            );

            result.setPhysicalHitzone(
                    valueOrZero(
                            hitzone.getBlunt()
                    )
            );

            break;


        case "弾":

            result.setPhysicalHitzoneType(
                    "SHOT"
            );

            result.setPhysicalHitzone(
                    valueOrZero(
                            hitzone.getShot()
                    )
            );

            break;


        default:

            /*
             * 未知の攻撃属性は
             * 初期値のまま。
             */
            break;
        }
    }


    /*
     * =====================================================
     * 主属性肉質
     * =====================================================
     */
    private void applyMainElementHitzone(
            MonsterHitzoneValueResult result,
            String elementType,
            MonsterHitzone hitzone) {

        if (elementType == null) {

            return;
        }


        switch (elementType) {

        case "火":

            result.setElemental(
                    true
            );

            result.setElementHitzone(
                    valueOrZero(
                            hitzone.getFire()
                    )
            );

            break;


        case "水":

            result.setElemental(
                    true
            );

            result.setElementHitzone(
                    valueOrZero(
                            hitzone.getWater()
                    )
            );

            break;


        case "雷":

            result.setElemental(
                    true
            );

            result.setElementHitzone(
                    valueOrZero(
                            hitzone.getThunder()
                    )
            );

            break;


        case "氷":

            result.setElemental(
                    true
            );

            result.setElementHitzone(
                    valueOrZero(
                            hitzone.getIce()
                    )
            );

            break;


        case "龍":

            result.setElemental(
                    true
            );

            result.setElementHitzone(
                    valueOrZero(
                            hitzone.getDragon()
                    )
            );

            break;


        /*
         * 毒
         * 麻痺
         * 睡眠
         * 爆破
         * 無
         *
         * は属性肉質を使用しない。
         */
        default:

            result.setElemental(
                    false
            );

            result.setElementHitzone(
                    0
            );

            break;
        }
    }


    /*
     * =====================================================
     * 副属性肉質
     * =====================================================
     */
    private void applySubElementHitzone(
            MonsterHitzoneValueResult result,
            String subElementType,
            MonsterHitzone hitzone) {

        if (subElementType == null) {

            return;
        }


        switch (subElementType) {

        case "火":

            result.setSubElemental(
                    true
            );

            result.setSubElementHitzone(
                    valueOrZero(
                            hitzone.getFire()
                    )
            );

            break;


        case "水":

            result.setSubElemental(
                    true
            );

            result.setSubElementHitzone(
                    valueOrZero(
                            hitzone.getWater()
                    )
            );

            break;


        case "雷":

            result.setSubElemental(
                    true
            );

            result.setSubElementHitzone(
                    valueOrZero(
                            hitzone.getThunder()
                    )
            );

            break;


        case "氷":

            result.setSubElemental(
                    true
            );

            result.setSubElementHitzone(
                    valueOrZero(
                            hitzone.getIce()
                    )
            );

            break;


        case "龍":

            result.setSubElemental(
                    true
            );

            result.setSubElementHitzone(
                    valueOrZero(
                            hitzone.getDragon()
                    )
            );

            break;


        /*
         * 毒
         * 麻痺
         * 睡眠
         * 爆破
         *
         * などは属性肉質を使用しない。
         */
        default:

            result.setSubElemental(
                    false
            );

            result.setSubElementHitzone(
                    0
            );

            break;
        }
    }


    /*
     * Integerがnullなら0
     */
    private int valueOrZero(
            Integer value) {

        return value == null
                ? 0
                : value;
    }
}