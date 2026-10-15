package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.MonsterHitzoneValueResult;
import com.example.mhsunbreaksimulator.entity.MonsterHitzone;
import com.example.mhsunbreaksimulator.entity.Weapon;
import com.example.mhsunbreaksimulator.service.MonsterHitzoneValueService;

public class MonsterHitzoneValueServiceTest {

    private final MonsterHitzoneValueService
            monsterHitzoneValueService
            = new MonsterHitzoneValueService();


    /*
     * =====================================================
     * 斬肉質
     * =====================================================
     *
     * ガイアデルム頭部
     *
     * sever = 70
     */
    @Test
    void testSeverHitzone() {

        Weapon weapon
                = createWeapon(
                        "斬",
                        "無",
                        null
                );


        MonsterHitzoneValueResult result
                = monsterHitzoneValueService.calculate(
                        weapon,
                        createGaismagormHead()
                );


        assertEquals(
                "頭部",
                result.getPartName()
        );

        assertEquals(
                "SEVER",
                result.getPhysicalHitzoneType()
        );

        assertEquals(
                70,
                result.getPhysicalHitzone()
        );


        System.out.println(
                "斬肉質："
                + result.getPhysicalHitzone()
        );
    }


    /*
     * =====================================================
     * 打肉質
     * =====================================================
     *
     * blunt = 72
     */
    @Test
    void testBluntHitzone() {

        Weapon weapon
                = createWeapon(
                        "打",
                        "無",
                        null
                );


        MonsterHitzoneValueResult result
                = monsterHitzoneValueService.calculate(
                        weapon,
                        createGaismagormHead()
                );


        assertEquals(
                "BLUNT",
                result.getPhysicalHitzoneType()
        );

        assertEquals(
                72,
                result.getPhysicalHitzone()
        );


        System.out.println(
                "打肉質："
                + result.getPhysicalHitzone()
        );
    }


    /*
     * =====================================================
     * 弾肉質
     * =====================================================
     *
     * shot = 45
     */
    @Test
    void testShotHitzone() {

        Weapon weapon
                = createWeapon(
                        "弾",
                        "無",
                        null
                );


        MonsterHitzoneValueResult result
                = monsterHitzoneValueService.calculate(
                        weapon,
                        createGaismagormHead()
                );


        assertEquals(
                "SHOT",
                result.getPhysicalHitzoneType()
        );

        assertEquals(
                45,
                result.getPhysicalHitzone()
        );


        System.out.println(
                "弾肉質："
                + result.getPhysicalHitzone()
        );
    }


    /*
     * =====================================================
     * 雷属性肉質
     * =====================================================
     *
     * thunder = 20
     */
    @Test
    void testThunderElementHitzone() {

        Weapon weapon
                = createWeapon(
                        "弾",
                        "雷",
                        null
                );


        MonsterHitzoneValueResult result
                = monsterHitzoneValueService.calculate(
                        weapon,
                        createGaismagormHead()
                );


        assertTrue(
                result.isElemental()
        );

        assertEquals(
                "雷",
                result.getElementType()
        );

        assertEquals(
                20,
                result.getElementHitzone()
        );


        System.out.println(
                "雷属性肉質："
                + result.getElementHitzone()
        );
    }


    /*
     * =====================================================
     * 龍属性肉質
     * =====================================================
     *
     * dragon = 30
     */
    @Test
    void testDragonElementHitzone() {

        Weapon weapon
                = createWeapon(
                        "斬",
                        "龍",
                        null
                );


        MonsterHitzoneValueResult result
                = monsterHitzoneValueService.calculate(
                        weapon,
                        createGaismagormHead()
                );


        assertTrue(
                result.isElemental()
        );

        assertEquals(
                30,
                result.getElementHitzone()
        );


        System.out.println(
                "龍属性肉質："
                + result.getElementHitzone()
        );
    }


    /*
     * =====================================================
     * 毒は属性肉質を使用しない
     * =====================================================
     */
    @Test
    void testPoisonDoesNotUseElementHitzone() {

        Weapon weapon
                = createWeapon(
                        "斬",
                        "毒",
                        null
                );


        MonsterHitzoneValueResult result
                = monsterHitzoneValueService.calculate(
                        weapon,
                        createGaismagormHead()
                );


        assertEquals(
                "毒",
                result.getElementType()
        );

        assertFalse(
                result.isElemental()
        );

        assertEquals(
                0,
                result.getElementHitzone()
        );


        System.out.println(
                "毒：属性肉質を使用しない 正常"
        );
    }


    /*
     * =====================================================
     * 無属性
     * =====================================================
     */
    @Test
    void testNoElement() {

        Weapon weapon
                = createWeapon(
                        "斬",
                        "無",
                        null
                );


        MonsterHitzoneValueResult result
                = monsterHitzoneValueService.calculate(
                        weapon,
                        createGaismagormHead()
                );


        assertFalse(
                result.isElemental()
        );

        assertEquals(
                0,
                result.getElementHitzone()
        );


        System.out.println(
                "無属性：属性肉質0 正常"
        );
    }


    /*
     * =====================================================
     * 副属性：氷
     * =====================================================
     *
     * ガイアデルム頭部
     *
     * ice = 0
     *
     * 属性としては有効だが、
     * この部位の氷肉質自体が0。
     */
    @Test
    void testSubIceElement() {

        Weapon weapon
                = createWeapon(
                        "斬",
                        "火",
                        "氷"
                );


        MonsterHitzoneValueResult result
                = monsterHitzoneValueService.calculate(
                        weapon,
                        createGaismagormHead()
                );


        assertEquals(
                "氷",
                result.getSubElementType()
        );

        assertTrue(
                result.isSubElemental()
        );

        assertEquals(
                0,
                result.getSubElementHitzone()
        );


        System.out.println(
                "副属性氷："
                + "elemental="
                + result.isSubElemental()
                + " / hitzone="
                + result.getSubElementHitzone()
        );
    }


    /*
     * =====================================================
     * 副属性：毒
     * =====================================================
     *
     * 毒は属性肉質を使用しない。
     */
    @Test
    void testSubPoisonDoesNotUseElementHitzone() {

        Weapon weapon
                = createWeapon(
                        "斬",
                        "火",
                        "毒"
                );


        MonsterHitzoneValueResult result
                = monsterHitzoneValueService.calculate(
                        weapon,
                        createGaismagormHead()
                );


        assertEquals(
                "毒",
                result.getSubElementType()
        );

        assertFalse(
                result.isSubElemental()
        );

        assertEquals(
                0,
                result.getSubElementHitzone()
        );


        System.out.println(
                "副属性毒：属性肉質を使用しない 正常"
        );
    }


    /*
     * =====================================================
     * 武器=null
     * =====================================================
     *
     * 部位名だけ保持。
     */
    @Test
    void testNullWeapon() {

        MonsterHitzoneValueResult result
                = monsterHitzoneValueService.calculate(
                        null,
                        createGaismagormHead()
                );


        assertEquals(
                "頭部",
                result.getPartName()
        );

        assertNull(
                result.getPhysicalHitzoneType()
        );

        assertEquals(
                0,
                result.getPhysicalHitzone()
        );


        System.out.println(
                "weapon=null：正常"
        );
    }


    /*
     * =====================================================
     * 部位=null
     * =====================================================
     */
    @Test
    void testNullHitzone() {

        Weapon weapon
                = createWeapon(
                        "弾",
                        "雷",
                        null
                );


        MonsterHitzoneValueResult result
                = monsterHitzoneValueService.calculate(
                        weapon,
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

        assertEquals(
                0,
                result.getElementHitzone()
        );


        System.out.println(
                "hitzone=null：正常"
        );
    }


    /*
     * =====================================================
     * テスト用武器
     * =====================================================
     */
    private Weapon createWeapon(
            String attackProperty,
            String elementType,
            String subElementType) {

        Weapon weapon
                = new Weapon();

        weapon.setAttackProperty(
                attackProperty
        );

        weapon.setElementType(
                elementType
        );

        weapon.setSubElementType(
                subElementType
        );

        return weapon;
    }


    /*
     * =====================================================
     * ガイアデルム頭部
     * =====================================================
     *
     * 斬70
     * 打72
     * 弾45
     *
     * 火10
     * 水0
     * 雷20
     * 氷0
     * 龍30
     */
    private MonsterHitzone createGaismagormHead() {

        MonsterHitzone hitzone
                = new MonsterHitzone();

        hitzone.setHitzoneId(
                501
        );

        hitzone.setMonsterId(
                16
        );

        hitzone.setPartName(
                "頭部"
        );


        hitzone.setSever(
                70
        );

        hitzone.setBlunt(
                72
        );

        hitzone.setShot(
                45
        );


        hitzone.setFire(
                10
        );

        hitzone.setWater(
                0
        );

        hitzone.setThunder(
                20
        );

        hitzone.setIce(
                0
        );

        hitzone.setDragon(
                30
        );


        return hitzone;
    }
}