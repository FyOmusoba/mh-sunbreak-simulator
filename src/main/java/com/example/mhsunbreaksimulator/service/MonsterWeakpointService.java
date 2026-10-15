package com.example.mhsunbreaksimulator.service;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.MonsterHitzoneValueResult;
import com.example.mhsunbreaksimulator.dto.MonsterWeakpointResult;

@Service
public class MonsterWeakpointService {

    /*
     * 弱点特効の物理肉質基準値。
     *
     * 物理肉質45以上で有効。
     */
    private static final int PHYSICAL_WEAKPOINT_THRESHOLD
            = 45;


    /*
     * 弱点特効【属性】の属性肉質基準値。
     *
     * 属性肉質20以上で有効。
     */
    private static final int ELEMENT_WEAKPOINT_THRESHOLD
            = 20;


    /*
     * =====================================================
     * 肉質判定結果から
     * 物理弱点・属性弱点を判定する
     * =====================================================
     */
    public MonsterWeakpointResult calculate(
            MonsterHitzoneValueResult hitzoneValueResult) {

        MonsterWeakpointResult result
                = new MonsterWeakpointResult();


        /*
         * 判定基準値。
         *
         * 入力がnullの場合でも
         * Result側に基準値を保持する。
         */
        result.setPhysicalWeakpointThreshold(
                PHYSICAL_WEAKPOINT_THRESHOLD
        );

        result.setElementWeakpointThreshold(
                ELEMENT_WEAKPOINT_THRESHOLD
        );


        /*
         * 肉質情報がない場合。
         */
        if (hitzoneValueResult == null) {

            return result;
        }


        /*
         * =====================================================
         * 基本情報
         * =====================================================
         */

        result.setPartName(
                hitzoneValueResult.getPartName()
        );

        result.setPhysicalHitzoneType(
                hitzoneValueResult
                        .getPhysicalHitzoneType()
        );


        /*
         * =====================================================
         * 物理弱点
         * =====================================================
         */

        int physicalHitzone
                = valueOrZero(
                        hitzoneValueResult
                                .getPhysicalHitzone()
                );


        result.setPhysicalHitzone(
                physicalHitzone
        );


        /*
         * 物理肉質45以上
         * ↓
         * 弱点特効の対象。
         */
        result.setPhysicalWeakpoint(
                physicalHitzone
                        >= PHYSICAL_WEAKPOINT_THRESHOLD
        );


        /*
         * =====================================================
         * 主属性弱点
         * =====================================================
         */

        boolean elemental
                = hitzoneValueResult
                        .isElemental();


        int elementHitzone
                = valueOrZero(
                        hitzoneValueResult
                                .getElementHitzone()
                );


        result.setElemental(
                elemental
        );

        result.setElementHitzone(
                elementHitzone
        );


        /*
         * 五属性であり、
         * 属性肉質20以上の場合のみ
         * 弱点特効【属性】の対象。
         *
         * そのため、
         *
         * 毒
         * 麻痺
         * 睡眠
         * 爆破
         * 無
         *
         * は肉質値が仮に存在しても
         * ここではtrueにならない。
         */
        result.setElementWeakpoint(
                elemental
                        && elementHitzone
                        >= ELEMENT_WEAKPOINT_THRESHOLD
        );


        /*
         * =====================================================
         * 副属性弱点
         * =====================================================
         */

        boolean subElemental
                = hitzoneValueResult
                        .isSubElemental();


        int subElementHitzone
                = valueOrZero(
                        hitzoneValueResult
                                .getSubElementHitzone()
                );


        result.setSubElemental(
                subElemental
        );

        result.setSubElementHitzone(
                subElementHitzone
        );


        /*
         * 副属性も同じく、
         *
         * 五属性
         * ＋
         * 属性肉質20以上
         *
         * ならtrue。
         */
        result.setSubElementWeakpoint(
                subElemental
                        && subElementHitzone
                        >= ELEMENT_WEAKPOINT_THRESHOLD
        );


        return result;
    }


    /*
     * =====================================================
     * null安全
     * =====================================================
     */
    private int valueOrZero(
            Integer value) {

        return value == null
                ? 0
                : value;
    }
}