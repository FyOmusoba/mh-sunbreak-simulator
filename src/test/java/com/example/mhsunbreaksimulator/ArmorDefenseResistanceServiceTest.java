package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.dto.DefenseResistanceResult;
import com.example.mhsunbreaksimulator.entity.Armor;
import com.example.mhsunbreaksimulator.service.ArmorDefenseResistanceService;
import com.example.mhsunbreaksimulator.service.DefenseResistanceService;

public class ArmorDefenseResistanceServiceTest {

    private final DefenseResistanceService
            defenseResistanceService
            = new DefenseResistanceService();


    private final ArmorDefenseResistanceService
            armorDefenseResistanceService
            = new ArmorDefenseResistanceService(
                    defenseResistanceService
            );


    /*
     * =====================================================
     * 防具5部位の基礎値集計
     * =====================================================
     */
    @Test
    void testFiveArmorAggregation() {

        Armor head
                = createArmor(
                        100,
                        1,
                        2,
                        3,
                        4,
                        5
                );

        Armor chest
                = createArmor(
                        110,
                        -1,
                        0,
                        1,
                        2,
                        3
                );

        Armor arms
                = createArmor(
                        120,
                        2,
                        3,
                        4,
                        5,
                        6
                );

        Armor waist
                = createArmor(
                        130,
                        0,
                        -2,
                        -3,
                        -4,
                        -5
                );

        Armor legs
                = createArmor(
                        140,
                        5,
                        5,
                        5,
                        5,
                        5
                );


        List<Armor> armors
                = List.of(
                        head,
                        chest,
                        arms,
                        waist,
                        legs
                );


        DefenseResistanceResult result
                = armorDefenseResistanceService.calculate(
                        armors,
                        "大剣",
                        null
                );


        /*
         * 防御
         *
         * 100 + 110 + 120 + 130 + 140
         * = 600
         */
        assertEquals(
                600,
                result.getBaseDefense()
        );

        assertEquals(
                600,
                result.getFinalDefense()
        );


        /*
         * 火
         *
         * 1 - 1 + 2 + 0 + 5
         * = 7
         */
        assertEquals(
                7,
                result.getBaseFireRes()
        );

        assertEquals(
                7,
                result.getFinalFireRes()
        );


        /*
         * 水
         *
         * 2 + 0 + 3 - 2 + 5
         * = 8
         */
        assertEquals(
                8,
                result.getBaseWaterRes()
        );

        assertEquals(
                8,
                result.getFinalWaterRes()
        );


        /*
         * 雷
         *
         * 3 + 1 + 4 - 3 + 5
         * = 10
         */
        assertEquals(
                10,
                result.getBaseThunderRes()
        );

        assertEquals(
                10,
                result.getFinalThunderRes()
        );


        /*
         * 氷
         *
         * 4 + 2 + 5 - 4 + 5
         * = 12
         */
        assertEquals(
                12,
                result.getBaseIceRes()
        );

        assertEquals(
                12,
                result.getFinalIceRes()
        );


        /*
         * 龍
         *
         * 5 + 3 + 6 - 5 + 5
         * = 14
         */
        assertEquals(
                14,
                result.getBaseDragonRes()
        );

        assertEquals(
                14,
                result.getFinalDragonRes()
        );


        System.out.println(
                "防具5部位集計："
                + "defense="
                + result.getBaseDefense()
                + " / 火="
                + result.getBaseFireRes()
                + " / 水="
                + result.getBaseWaterRes()
                + " / 雷="
                + result.getBaseThunderRes()
                + " / 氷="
                + result.getBaseIceRes()
                + " / 龍="
                + result.getBaseDragonRes()
        );
    }


    /*
     * =====================================================
     * 防具5部位
     * ＋
     * スキル補正
     * =====================================================
     */
    @Test
    void testArmorAggregationWithSkillModifier() {

        Armor head
                = createArmor(
                        100,
                        1,
                        2,
                        3,
                        4,
                        5
                );

        Armor chest
                = createArmor(
                        100,
                        1,
                        2,
                        3,
                        4,
                        5
                );

        Armor arms
                = createArmor(
                        100,
                        1,
                        2,
                        3,
                        4,
                        5
                );

        Armor waist
                = createArmor(
                        100,
                        1,
                        2,
                        3,
                        4,
                        5
                );

        Armor legs
                = createArmor(
                        100,
                        1,
                        2,
                        3,
                        4,
                        5
                );


        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();


        /*
         * 防御：
         *
         * 基礎500
         * ×1.10
         * +35
         *
         * =585
         */
        modifier.setDefenseMul(
                new BigDecimal("1.10")
        );

        modifier.setDefenseAdd(
                new BigDecimal("35")
        );


        modifier.setFireResAdd(
                new BigDecimal("3")
        );

        modifier.setWaterResAdd(
                new BigDecimal("5")
        );

        modifier.setThunderResAdd(
                new BigDecimal("-2")
        );

        modifier.setIceResAdd(
                new BigDecimal("10")
        );

        modifier.setDragonResAdd(
                new BigDecimal("-5")
        );


        DefenseResistanceResult result
                = armorDefenseResistanceService.calculate(
                        List.of(
                                head,
                                chest,
                                arms,
                                waist,
                                legs
                        ),
                        "太刀",
                        modifier
                );


        assertEquals(
                500,
                result.getBaseDefense()
        );

        assertEquals(
                585,
                result.getFinalDefense()
        );


        /*
         * 基礎耐性
         *
         * 火 = 5
         * 水 = 10
         * 雷 = 15
         * 氷 = 20
         * 龍 = 25
         *
         * ↓
         *
         * スキル加算後
         *
         * 火 = 8
         * 水 = 15
         * 雷 = 13
         * 氷 = 30
         * 龍 = 20
         */

        assertEquals(
                5,
                result.getBaseFireRes()
        );

        assertEquals(
                8,
                result.getFinalFireRes()
        );


        assertEquals(
                10,
                result.getBaseWaterRes()
        );

        assertEquals(
                15,
                result.getFinalWaterRes()
        );


        assertEquals(
                15,
                result.getBaseThunderRes()
        );

        assertEquals(
                13,
                result.getFinalThunderRes()
        );


        assertEquals(
                20,
                result.getBaseIceRes()
        );

        assertEquals(
                30,
                result.getFinalIceRes()
        );


        assertEquals(
                25,
                result.getBaseDragonRes()
        );

        assertEquals(
                20,
                result.getFinalDragonRes()
        );


        System.out.println(
                "防具＋スキル補正："
                + "baseDefense="
                + result.getBaseDefense()
                + " / finalDefense="
                + result.getFinalDefense()
                + " / 火="
                + result.getFinalFireRes()
                + " / 水="
                + result.getFinalWaterRes()
                + " / 雷="
                + result.getFinalThunderRes()
                + " / 氷="
                + result.getFinalIceRes()
                + " / 龍="
                + result.getFinalDragonRes()
        );
    }


    /*
     * =====================================================
     * null防具を含む
     * =====================================================
     *
     * 未装備部位などがnullでも
     * 他部位だけ正常に集計する。
     */
    @Test
    void testNullArmorIsIgnored() {

        Armor head
                = createArmor(
                        150,
                        2,
                        3,
                        4,
                        5,
                        6
                );


        Armor legs
                = createArmor(
                        200,
                        1,
                        1,
                        1,
                        1,
                        1
                );


        List<Armor> armors
                = new ArrayList<>();

        armors.add(
                head
        );

        armors.add(
                null
        );

        armors.add(
                null
        );

        armors.add(
                null
        );

        armors.add(
                legs
        );


        DefenseResistanceResult result
                = armorDefenseResistanceService.calculate(
                        armors,
                        "大剣",
                        null
                );


        assertEquals(
                350,
                result.getBaseDefense()
        );

        assertEquals(
                3,
                result.getBaseFireRes()
        );

        assertEquals(
                4,
                result.getBaseWaterRes()
        );

        assertEquals(
                5,
                result.getBaseThunderRes()
        );

        assertEquals(
                6,
                result.getBaseIceRes()
        );

        assertEquals(
                7,
                result.getBaseDragonRes()
        );


        System.out.println(
                "null防具無視："
                + "defense="
                + result.getBaseDefense()
                + " / 火="
                + result.getBaseFireRes()
                + " / 龍="
                + result.getBaseDragonRes()
        );
    }


    /*
     * =====================================================
     * 防具リストnull
     * =====================================================
     */
    @Test
    void testNullArmorList() {

        DefenseResistanceResult result
                = armorDefenseResistanceService.calculate(
                        null,
                        "大剣",
                        null
                );


        assertEquals(
                0,
                result.getBaseDefense()
        );

        assertEquals(
                0,
                result.getFinalDefense()
        );

        assertEquals(
                0,
                result.getFinalFireRes()
        );

        assertEquals(
                0,
                result.getFinalWaterRes()
        );

        assertEquals(
                0,
                result.getFinalThunderRes()
        );

        assertEquals(
                0,
                result.getFinalIceRes()
        );

        assertEquals(
                0,
                result.getFinalDragonRes()
        );

        assertFalse(
                result.getNullifyNonDragonElement()
        );


        System.out.println(
                "防具リストnull：正常"
        );
    }


    /*
     * =====================================================
     * 防具フィールドnull
     * =====================================================
     */
    @Test
    void testNullArmorValues() {

        Armor armor
                = new Armor();

        armor.setDefense(
                null
        );

        armor.setFireRes(
                null
        );

        armor.setWaterRes(
                null
        );

        armor.setThunderRes(
                null
        );

        armor.setIceRes(
                null
        );

        armor.setDragonRes(
                null
        );


        DefenseResistanceResult result
                = armorDefenseResistanceService.calculate(
                        List.of(
                                armor
                        ),
                        null,
                        null
                );


        assertEquals(
                0,
                result.getBaseDefense()
        );

        assertEquals(
                0,
                result.getBaseFireRes()
        );

        assertEquals(
                0,
                result.getBaseWaterRes()
        );

        assertEquals(
                0,
                result.getBaseThunderRes()
        );

        assertEquals(
                0,
                result.getBaseIceRes()
        );

        assertEquals(
                0,
                result.getBaseDragonRes()
        );


        System.out.println(
                "防具値null：正常"
        );
    }


    /*
     * =====================================================
     * 龍気活性
     * ＋
     * 龍気変換
     * =====================================================
     *
     * Armor集計Serviceを経由しても、
     * 特殊耐性処理がそのまま機能することを確認。
     */
    @Test
    void testDragonheartAndDragonConversionThroughArmorService() {

        Armor head
                = createArmor(
                        100,
                        -10,
                        5,
                        10,
                        15,
                        20
                );


        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setAllElementResSet(
                50
        );

        modifier.setPostConversionAllElementResSet(
                0
        );

        modifier.setResistanceToElementRate(
                new BigDecimal("0.25")
        );

        modifier.setNullifyNonDragonElement(
                true
        );


        DefenseResistanceResult result
                = armorDefenseResistanceService.calculate(
                        List.of(
                                head
                        ),
                        "大剣",
                        modifier
                );


        assertEquals(
                250,
                result.getConversionResistanceTotal()
        );

        assertDecimalEquals(
                "62.50",
                result.getConvertedElementAddBeforeRound()
        );

        assertDecimalEquals(
                "62",
                result.getConvertedElementAdd()
        );


        assertEquals(
                0,
                result.getFinalFireRes()
        );

        assertEquals(
                0,
                result.getFinalWaterRes()
        );

        assertEquals(
                0,
                result.getFinalThunderRes()
        );

        assertEquals(
                0,
                result.getFinalIceRes()
        );

        assertEquals(
                0,
                result.getFinalDragonRes()
        );


        System.out.println(
                "Armor経由・龍気活性＋龍気変換："
                + "conversionTotal="
                + result.getConversionResistanceTotal()
                + " / elementAdd="
                + result.getConvertedElementAdd()
                + " / finalRes="
                + result.getFinalFireRes()
        );
    }


    /*
     * =====================================================
     * テスト用Armor生成
     * =====================================================
     */
    private Armor createArmor(
            Integer defense,
            Integer fireRes,
            Integer waterRes,
            Integer thunderRes,
            Integer iceRes,
            Integer dragonRes) {

        Armor armor
                = new Armor();


        armor.setDefense(
                defense
        );

        armor.setFireRes(
                fireRes
        );

        armor.setWaterRes(
                waterRes
        );

        armor.setThunderRes(
                thunderRes
        );

        armor.setIceRes(
                iceRes
        );

        armor.setDragonRes(
                dragonRes
        );


        return armor;
    }


    /*
     * =====================================================
     * BigDecimal比較
     * =====================================================
     */
    private void assertDecimalEquals(
            String expected,
            BigDecimal actual) {

        assertEquals(
                0,
                new BigDecimal(
                        expected
                ).compareTo(
                        actual
                )
        );
    }
}