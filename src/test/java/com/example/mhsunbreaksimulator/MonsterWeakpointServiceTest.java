package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.MonsterHitzoneValueResult;
import com.example.mhsunbreaksimulator.dto.MonsterWeakpointResult;
import com.example.mhsunbreaksimulator.service.MonsterWeakpointService;

public class MonsterWeakpointServiceTest {

    private final MonsterWeakpointService
            monsterWeakpointService
            = new MonsterWeakpointService();


    /*
     * =====================================================
     * 物理肉質44
     * =====================================================
     *
     * 弱点特効の基準値45未満。
     */
    @Test
    void testPhysicalHitzone44() {

        MonsterHitzoneValueResult hitzone
                = createHitzoneValueResult(
                        "首",
                        "SHOT",
                        44,
                        false,
                        0,
                        false,
                        0
                );


        MonsterWeakpointResult result
                = monsterWeakpointService.calculate(
                        hitzone
                );


        assertEquals(
                "首",
                result.getPartName()
        );

        assertEquals(
                "SHOT",
                result.getPhysicalHitzoneType()
        );

        assertEquals(
                44,
                result.getPhysicalHitzone()
        );

        assertEquals(
                45,
                result.getPhysicalWeakpointThreshold()
        );

        assertFalse(
                result.isPhysicalWeakpoint()
        );


        System.out.println(
                "物理肉質44：weakpoint="
                + result.isPhysicalWeakpoint()
        );
    }


    /*
     * =====================================================
     * 物理肉質45
     * =====================================================
     *
     * ちょうど基準値。
     */
    @Test
    void testPhysicalHitzone45() {

        MonsterHitzoneValueResult hitzone
                = createHitzoneValueResult(
                        "頭部",
                        "SHOT",
                        45,
                        true,
                        20,
                        false,
                        0
                );


        MonsterWeakpointResult result
                = monsterWeakpointService.calculate(
                        hitzone
                );


        assertEquals(
                45,
                result.getPhysicalHitzone()
        );

        assertTrue(
                result.isPhysicalWeakpoint()
        );


        System.out.println(
                "物理肉質45：weakpoint="
                + result.isPhysicalWeakpoint()
        );
    }


    /*
     * =====================================================
     * 物理肉質70
     * =====================================================
     */
    @Test
    void testPhysicalHitzone70() {

        MonsterHitzoneValueResult hitzone
                = createHitzoneValueResult(
                        "頭部",
                        "SEVER",
                        70,
                        true,
                        30,
                        false,
                        0
                );


        MonsterWeakpointResult result
                = monsterWeakpointService.calculate(
                        hitzone
                );


        assertEquals(
                "SEVER",
                result.getPhysicalHitzoneType()
        );

        assertEquals(
                70,
                result.getPhysicalHitzone()
        );

        assertTrue(
                result.isPhysicalWeakpoint()
        );


        System.out.println(
                "物理肉質70：weakpoint="
                + result.isPhysicalWeakpoint()
        );
    }


    /*
     * =====================================================
     * 物理肉質0
     * =====================================================
     */
    @Test
    void testPhysicalHitzone0() {

        MonsterHitzoneValueResult hitzone
                = createHitzoneValueResult(
                        "背中",
                        "SHOT",
                        0,
                        false,
                        0,
                        false,
                        0
                );


        MonsterWeakpointResult result
                = monsterWeakpointService.calculate(
                        hitzone
                );


        assertEquals(
                0,
                result.getPhysicalHitzone()
        );

        assertFalse(
                result.isPhysicalWeakpoint()
        );


        System.out.println(
                "物理肉質0：weakpoint="
                + result.isPhysicalWeakpoint()
        );
    }


    /*
     * =====================================================
     * 主属性肉質19
     * =====================================================
     *
     * 弱点特効【属性】の基準値20未満。
     */
    @Test
    void testElementHitzone19() {

        MonsterHitzoneValueResult hitzone
                = createHitzoneValueResult(
                        "頭部",
                        "SHOT",
                        45,
                        true,
                        19,
                        false,
                        0
                );


        MonsterWeakpointResult result
                = monsterWeakpointService.calculate(
                        hitzone
                );


        assertTrue(
                result.isElemental()
        );

        assertEquals(
                19,
                result.getElementHitzone()
        );

        assertEquals(
                20,
                result.getElementWeakpointThreshold()
        );

        assertFalse(
                result.isElementWeakpoint()
        );


        System.out.println(
                "属性肉質19：weakpoint="
                + result.isElementWeakpoint()
        );
    }


    /*
     * =====================================================
     * 主属性肉質20
     * =====================================================
     *
     * ちょうど弱点特効【属性】の基準値。
     */
    @Test
    void testElementHitzone20() {

        MonsterHitzoneValueResult hitzone
                = createHitzoneValueResult(
                        "頭部",
                        "SHOT",
                        45,
                        true,
                        20,
                        false,
                        0
                );


        MonsterWeakpointResult result
                = monsterWeakpointService.calculate(
                        hitzone
                );


        assertTrue(
                result.isElemental()
        );

        assertEquals(
                20,
                result.getElementHitzone()
        );

        assertTrue(
                result.isElementWeakpoint()
        );


        System.out.println(
                "属性肉質20：weakpoint="
                + result.isElementWeakpoint()
        );
    }


    /*
     * =====================================================
     * 非五属性
     * =====================================================
     *
     * elemental=falseなら、
     * 肉質値が20以上でも属性弱点にはしない。
     *
     * 毒・麻痺・睡眠・爆破・無などを
     * 誤判定しないためのテスト。
     */
    @Test
    void testNonElementalDoesNotBecomeWeakpoint() {

        MonsterHitzoneValueResult hitzone
                = createHitzoneValueResult(
                        "頭部",
                        "SEVER",
                        70,
                        false,
                        20,
                        false,
                        0
                );


        MonsterWeakpointResult result
                = monsterWeakpointService.calculate(
                        hitzone
                );


        assertFalse(
                result.isElemental()
        );

        assertEquals(
                20,
                result.getElementHitzone()
        );

        assertFalse(
                result.isElementWeakpoint()
        );


        System.out.println(
                "非五属性・肉質値20：weakpoint="
                + result.isElementWeakpoint()
        );
    }


    /*
     * =====================================================
     * 副属性肉質19
     * =====================================================
     */
    @Test
    void testSubElementHitzone19() {

        MonsterHitzoneValueResult hitzone
                = createHitzoneValueResult(
                        "頭部",
                        "SEVER",
                        70,
                        true,
                        10,
                        true,
                        19
                );


        MonsterWeakpointResult result
                = monsterWeakpointService.calculate(
                        hitzone
                );


        assertTrue(
                result.isSubElemental()
        );

        assertEquals(
                19,
                result.getSubElementHitzone()
        );

        assertFalse(
                result.isSubElementWeakpoint()
        );


        System.out.println(
                "副属性肉質19：weakpoint="
                + result.isSubElementWeakpoint()
        );
    }


    /*
     * =====================================================
     * 副属性肉質20
     * =====================================================
     */
    @Test
    void testSubElementHitzone20() {

        MonsterHitzoneValueResult hitzone
                = createHitzoneValueResult(
                        "頭部",
                        "SEVER",
                        70,
                        true,
                        10,
                        true,
                        20
                );


        MonsterWeakpointResult result
                = monsterWeakpointService.calculate(
                        hitzone
                );


        assertTrue(
                result.isSubElemental()
        );

        assertEquals(
                20,
                result.getSubElementHitzone()
        );

        assertTrue(
                result.isSubElementWeakpoint()
        );


        System.out.println(
                "副属性肉質20：weakpoint="
                + result.isSubElementWeakpoint()
        );
    }


    /*
     * =====================================================
     * 属性情報の引き継ぎ
     * =====================================================
     */
    @Test
    void testElementValuesAreCopied() {

        MonsterHitzoneValueResult hitzone
                = createHitzoneValueResult(
                        "頭部",
                        "SHOT",
                        45,
                        true,
                        20,
                        true,
                        30
                );


        MonsterWeakpointResult result
                = monsterWeakpointService.calculate(
                        hitzone
                );


        assertTrue(
                result.isElemental()
        );

        assertEquals(
                20,
                result.getElementHitzone()
        );

        assertTrue(
                result.isElementWeakpoint()
        );


        assertTrue(
                result.isSubElemental()
        );

        assertEquals(
                30,
                result.getSubElementHitzone()
        );

        assertTrue(
                result.isSubElementWeakpoint()
        );


        System.out.println(
                "属性情報：main="
                + result.getElementHitzone()
                + " / mainWeakpoint="
                + result.isElementWeakpoint()
                + " / sub="
                + result.getSubElementHitzone()
                + " / subWeakpoint="
                + result.isSubElementWeakpoint()
        );
    }


    /*
     * =====================================================
     * null
     * =====================================================
     */
    @Test
    void testNullHitzoneValueResult() {

        MonsterWeakpointResult result
                = monsterWeakpointService.calculate(
                        null
                );


        assertNull(
                result.getPartName()
        );

        assertNull(
                result.getPhysicalHitzoneType()
        );

        assertEquals(
                0,
                result.getPhysicalHitzone()
        );

        assertFalse(
                result.isPhysicalWeakpoint()
        );

        assertEquals(
                45,
                result.getPhysicalWeakpointThreshold()
        );


        assertFalse(
                result.isElementWeakpoint()
        );

        assertEquals(
                20,
                result.getElementWeakpointThreshold()
        );


        assertFalse(
                result.isSubElementWeakpoint()
        );


        System.out.println(
                "hitzoneValueResult=null：正常"
        );
    }


    /*
     * =====================================================
     * テスト用
     * =====================================================
     */
    private MonsterHitzoneValueResult createHitzoneValueResult(
            String partName,
            String physicalHitzoneType,
            Integer physicalHitzone,
            boolean elemental,
            Integer elementHitzone,
            boolean subElemental,
            Integer subElementHitzone) {

        MonsterHitzoneValueResult result
                = new MonsterHitzoneValueResult();


        result.setPartName(
                partName
        );

        result.setPhysicalHitzoneType(
                physicalHitzoneType
        );

        result.setPhysicalHitzone(
                physicalHitzone
        );


        result.setElemental(
                elemental
        );

        result.setElementHitzone(
                elementHitzone
        );


        result.setSubElemental(
                subElemental
        );

        result.setSubElementHitzone(
                subElementHitzone
        );


        return result;
    }
}