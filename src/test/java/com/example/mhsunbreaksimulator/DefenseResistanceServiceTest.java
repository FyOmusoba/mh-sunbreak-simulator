package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.ConditionalSkillModifierTotal;
import com.example.mhsunbreaksimulator.dto.DefenseResistanceResult;
import com.example.mhsunbreaksimulator.service.DefenseResistanceService;

public class DefenseResistanceServiceTest {

    private final DefenseResistanceService
            defenseResistanceService
            = new DefenseResistanceService();


    /*
     * =====================================================
     * 通常防御力
     * =====================================================
     *
     * 500 × 1.10 + 35
     * = 585
     */
    @Test
    void testNormalDefense() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setDefenseMul(
                new BigDecimal("1.10")
        );

        modifier.setDefenseAdd(
                new BigDecimal("35")
        );


        DefenseResistanceResult result
                = defenseResistanceService.calculate(
                        500,
                        0,
                        0,
                        0,
                        0,
                        0,
                        "大剣",
                        modifier
                );


        assertDecimalEquals(
                "585",
                result.getDefenseBeforeRound()
        );

        assertEquals(
                585,
                result.getFinalDefense()
        );


        System.out.println(
                "通常防御："
                + "beforeRound="
                + result.getDefenseBeforeRound()
                + " / final="
                + result.getFinalDefense()
        );
    }


    /*
     * =====================================================
     * 防御力小数
     * =====================================================
     *
     * 現在のServiceでは
     * 最終防御力を切り捨てる。
     *
     * 501 × 1.05 + 10
     * = 536.05
     * → 536
     */
    @Test
    void testDefenseDecimalFloor() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setDefenseMul(
                new BigDecimal("1.05")
        );

        modifier.setDefenseAdd(
                new BigDecimal("10")
        );


        DefenseResistanceResult result
                = defenseResistanceService.calculate(
                        501,
                        0,
                        0,
                        0,
                        0,
                        0,
                        "太刀",
                        modifier
                );


        assertDecimalEquals(
                "536.05",
                result.getDefenseBeforeRound()
        );

        assertEquals(
                536,
                result.getFinalDefense()
        );


        System.out.println(
                "防御小数："
                + result.getDefenseBeforeRound()
                + " → "
                + result.getFinalDefense()
        );
    }


    /*
     * =====================================================
     * 防御力0未満防止
     * =====================================================
     */
    @Test
    void testDefenseDoesNotBecomeNegative() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setDefenseAdd(
                new BigDecimal("-200")
        );


        DefenseResistanceResult result
                = defenseResistanceService.calculate(
                        100,
                        0,
                        0,
                        0,
                        0,
                        0,
                        "大剣",
                        modifier
                );


        assertDecimalEquals(
                "0",
                result.getDefenseBeforeRound()
        );

        assertEquals(
                0,
                result.getFinalDefense()
        );


        System.out.println(
                "防御力マイナス防止：final=0"
        );
    }


    /*
     * =====================================================
     * 通常属性耐性加算
     * =====================================================
     */
    @Test
    void testNormalResistanceAdd() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setFireResAdd(
                new BigDecimal("5")
        );

        modifier.setWaterResAdd(
                new BigDecimal("-3")
        );

        modifier.setThunderResAdd(
                new BigDecimal("10")
        );

        modifier.setIceResAdd(
                new BigDecimal("2")
        );

        modifier.setDragonResAdd(
                new BigDecimal("7")
        );


        DefenseResistanceResult result
                = defenseResistanceService.calculate(
                        0,
                        10,
                        5,
                        -5,
                        20,
                        0,
                        "大剣",
                        modifier
                );


        assertEquals(
                15,
                result.getFireResBeforeSet()
        );

        assertEquals(
                2,
                result.getWaterResBeforeSet()
        );

        assertEquals(
                5,
                result.getThunderResBeforeSet()
        );

        assertEquals(
                22,
                result.getIceResBeforeSet()
        );

        assertEquals(
                7,
                result.getDragonResBeforeSet()
        );


        assertEquals(
                15,
                result.getFinalFireRes()
        );

        assertEquals(
                2,
                result.getFinalWaterRes()
        );

        assertEquals(
                5,
                result.getFinalThunderRes()
        );

        assertEquals(
                22,
                result.getFinalIceRes()
        );

        assertEquals(
                7,
                result.getFinalDragonRes()
        );


        System.out.println(
                "通常耐性："
                + "火="
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
     * 龍気変換用耐性
     * マイナス値は0
     * =====================================================
     */
    @Test
    void testNegativeResistanceBecomesZeroForConversion() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setResistanceToElementRate(
                new BigDecimal("0.25")
        );


        DefenseResistanceResult result
                = defenseResistanceService.calculate(
                        0,
                        -20,
                        10,
                        -5,
                        15,
                        20,
                        "大剣",
                        modifier
                );


        assertEquals(
                0,
                result.getConversionFireRes()
        );

        assertEquals(
                10,
                result.getConversionWaterRes()
        );

        assertEquals(
                0,
                result.getConversionThunderRes()
        );

        assertEquals(
                15,
                result.getConversionIceRes()
        );

        assertEquals(
                20,
                result.getConversionDragonRes()
        );


        assertEquals(
                45,
                result.getConversionResistanceTotal()
        );


        assertDecimalEquals(
                "11.25",
                result.getConvertedElementAddBeforeRound()
        );


        /*
         * 大剣なので八捨九入。
         *
         * 11.25
         * → 11
         */
        assertDecimalEquals(
                "11",
                result.getConvertedElementAdd()
        );


        System.out.println(
                "龍気変換・負耐性除外："
                + "conversionTotal="
                + result.getConversionResistanceTotal()
                + " / elementAdd="
                + result.getConvertedElementAdd()
        );
    }


    /*
     * =====================================================
     * 龍気変換用耐性
     * 1属性最大50
     * =====================================================
     */
    @Test
    void testConversionResistanceUpperLimit50() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setResistanceToElementRate(
                new BigDecimal("0.25")
        );


        DefenseResistanceResult result
                = defenseResistanceService.calculate(
                        0,
                        80,
                        70,
                        60,
                        55,
                        100,
                        "大剣",
                        modifier
                );


        assertEquals(
                50,
                result.getConversionFireRes()
        );

        assertEquals(
                50,
                result.getConversionWaterRes()
        );

        assertEquals(
                50,
                result.getConversionThunderRes()
        );

        assertEquals(
                50,
                result.getConversionIceRes()
        );

        assertEquals(
                50,
                result.getConversionDragonRes()
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


        System.out.println(
                "龍気変換・耐性上限："
                + "total="
                + result.getConversionResistanceTotal()
                + " / beforeRound="
                + result.getConvertedElementAddBeforeRound()
                + " / add="
                + result.getConvertedElementAdd()
        );
    }


    /*
     * =====================================================
     * 龍気活性
     * =====================================================
     *
     * 元耐性が何であっても
     * allElementResSet = 50
     *
     * なら変換前有効耐性50。
     */
    @Test
    void testDragonheartResistanceSet50() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setAllElementResSet(
                50
        );

        modifier.setNullifyNonDragonElement(
                true
        );


        DefenseResistanceResult result
                = defenseResistanceService.calculate(
                        0,
                        -20,
                        0,
                        10,
                        30,
                        100,
                        "大剣",
                        modifier
                );


        assertEquals(
                50,
                result.getAllElementResSet()
        );


        assertEquals(
                50,
                result.getConversionFireRes()
        );

        assertEquals(
                50,
                result.getConversionWaterRes()
        );

        assertEquals(
                50,
                result.getConversionThunderRes()
        );

        assertEquals(
                50,
                result.getConversionIceRes()
        );

        assertEquals(
                50,
                result.getConversionDragonRes()
        );


        assertEquals(
                250,
                result.getConversionResistanceTotal()
        );


        /*
         * 龍気変換そのものはないので
         * rate=0。
         */
        assertDecimalEquals(
                "0",
                result.getConvertedElementAdd()
        );


        assertEquals(
                50,
                result.getFinalFireRes()
        );

        assertEquals(
                50,
                result.getFinalWaterRes()
        );

        assertEquals(
                50,
                result.getFinalThunderRes()
        );

        assertEquals(
                50,
                result.getFinalIceRes()
        );

        assertEquals(
                50,
                result.getFinalDragonRes()
        );


        assertTrue(
                result.getNullifyNonDragonElement()
        );


        System.out.println(
                "龍気活性："
                + "conversionTotal="
                + result.getConversionResistanceTotal()
                + " / finalRes="
                + result.getFinalFireRes()
                + " / nullifyNonDragon="
                + result.getNullifyNonDragonElement()
        );
    }


    /*
     * =====================================================
     * 龍気活性
     * ＋
     * 龍気変換Lv3【朱・近接】
     * =====================================================
     *
     * 50 × 5
     * = 250
     *
     * 250 × 0.25
     * = 62.50
     *
     * 八捨九入
     * → 62
     *
     * 最終耐性
     * → 全て0
     */
    @Test
    void testDragonheartAndDragonConversionMelee() {

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
                = defenseResistanceService.calculate(
                        700,
                        0,
                        0,
                        0,
                        0,
                        0,
                        "太刀",
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


        assertTrue(
                result.getNullifyNonDragonElement()
        );


        System.out.println(
                "龍気活性＋龍気変換【朱】近接："
                + "totalRes="
                + result.getConversionResistanceTotal()
                + " / beforeRound="
                + result.getConvertedElementAddBeforeRound()
                + " / elementAdd="
                + result.getConvertedElementAdd()
                + " / finalRes="
                + result.getFinalFireRes()
        );
    }


    /*
     * =====================================================
     * 弓
     * 八捨九入
     * =====================================================
     *
     * 25 + 25 + 25 + 24 + 24
     * = 123
     *
     * 123 × 0.08
     * = 9.84
     *
     * 弓
     * → 八捨九入
     * → 9
     */
    @Test
    void testBowDragonConversionRounding() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setResistanceToElementRate(
                new BigDecimal("0.08")
        );


        DefenseResistanceResult result
                = defenseResistanceService.calculate(
                        0,
                        25,
                        25,
                        25,
                        24,
                        24,
                        "弓",
                        modifier
                );


        assertEquals(
                123,
                result.getConversionResistanceTotal()
        );

        assertDecimalEquals(
                "9.84",
                result.getConvertedElementAddBeforeRound()
        );

        assertDecimalEquals(
                "9",
                result.getConvertedElementAdd()
        );


        System.out.println(
                "弓・八捨九入："
                + result.getConvertedElementAddBeforeRound()
                + " → "
                + result.getConvertedElementAdd()
        );
    }


    /*
     * =====================================================
     * ボウガン
     * 小数保持
     * =====================================================
     *
     * 同じ
     *
     * 123 × 0.08
     * = 9.84
     *
     * ライトボウガンでは
     * Service上、小数を保持する。
     */
    @Test
    void testLightBowgunKeepsDecimal() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setResistanceToElementRate(
                new BigDecimal("0.08")
        );


        DefenseResistanceResult result
                = defenseResistanceService.calculate(
                        0,
                        25,
                        25,
                        25,
                        24,
                        24,
                        "ライトボウガン",
                        modifier
                );


        assertEquals(
                123,
                result.getConversionResistanceTotal()
        );

        assertDecimalEquals(
                "9.84",
                result.getConvertedElementAddBeforeRound()
        );

        assertDecimalEquals(
                "9.84",
                result.getConvertedElementAdd()
        );


        System.out.println(
                "ライト・小数保持："
                + "beforeRound="
                + result.getConvertedElementAddBeforeRound()
                + " / add="
                + result.getConvertedElementAdd()
        );
    }


    /*
     * =====================================================
     * ヘビィボウガン
     * 小数保持
     * =====================================================
     */
    @Test
    void testHeavyBowgunKeepsDecimal() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setResistanceToElementRate(
                new BigDecimal("0.08")
        );


        DefenseResistanceResult result
                = defenseResistanceService.calculate(
                        0,
                        25,
                        25,
                        25,
                        24,
                        24,
                        "ヘビィボウガン",
                        modifier
                );


        assertDecimalEquals(
                "9.84",
                result.getConvertedElementAdd()
        );


        System.out.println(
                "ヘビィ・小数保持：add="
                + result.getConvertedElementAdd()
        );
    }


    /*
     * =====================================================
     * 八捨九入
     * 0.9以上は繰り上げ
     * =====================================================
     *
     * 10 + 10 + 10 + 10 + 10
     * = 50
     *
     * 50 × 0.218
     * = 10.900
     *
     * → 11
     *
     * ※ここは丸め関数そのものの境界テスト。
     */
    @Test
    void testEightDownNineUpBoundary() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setResistanceToElementRate(
                new BigDecimal("0.218")
        );


        DefenseResistanceResult result
                = defenseResistanceService.calculate(
                        0,
                        10,
                        10,
                        10,
                        10,
                        10,
                        "大剣",
                        modifier
                );


        assertDecimalEquals(
                "10.900",
                result.getConvertedElementAddBeforeRound()
        );

        assertDecimalEquals(
                "11",
                result.getConvertedElementAdd()
        );


        System.out.println(
                "八捨九入境界："
                + result.getConvertedElementAddBeforeRound()
                + " → "
                + result.getConvertedElementAdd()
        );
    }


    /*
     * =====================================================
     * 龍気変換後0固定
     * =====================================================
     */
    @Test
    void testPostConversionResistanceSetZero() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setPostConversionAllElementResSet(
                0
        );

        modifier.setResistanceToElementRate(
                new BigDecimal("0.15")
        );


        DefenseResistanceResult result
                = defenseResistanceService.calculate(
                        0,
                        10,
                        20,
                        30,
                        40,
                        50,
                        "大剣",
                        modifier
                );


        /*
         * 変換元は0になる前の値。
         */
        assertEquals(
                150,
                result.getConversionResistanceTotal()
        );


        assertDecimalEquals(
                "22.50",
                result.getConvertedElementAddBeforeRound()
        );

        assertDecimalEquals(
                "22",
                result.getConvertedElementAdd()
        );


        /*
         * 最終耐性だけ0。
         */
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
                "龍気変換後0固定："
                + "conversionTotal="
                + result.getConversionResistanceTotal()
                + " / elementAdd="
                + result.getConvertedElementAdd()
                + " / finalRes=0"
        );
    }


    /*
     * =====================================================
     * modifier = null
     * =====================================================
     */
    @Test
    void testNullModifier() {

        DefenseResistanceResult result
                = defenseResistanceService.calculate(
                        500,
                        10,
                        5,
                        0,
                        -5,
                        15,
                        "大剣",
                        null
                );


        assertEquals(
                500,
                result.getFinalDefense()
        );

        assertEquals(
                10,
                result.getFinalFireRes()
        );

        assertEquals(
                5,
                result.getFinalWaterRes()
        );

        assertEquals(
                0,
                result.getFinalThunderRes()
        );

        assertEquals(
                -5,
                result.getFinalIceRes()
        );

        assertEquals(
                15,
                result.getFinalDragonRes()
        );


        assertNull(
                result.getAllElementResSet()
        );

        assertNull(
                result.getPostConversionAllElementResSet()
        );

        assertDecimalEquals(
                "0",
                result.getResistanceToElementRate()
        );

        assertDecimalEquals(
                "0",
                result.getConvertedElementAdd()
        );

        assertFalse(
                result.getNullifyNonDragonElement()
        );


        System.out.println(
                "modifier=null：正常"
        );
    }


    /*
     * =====================================================
     * 全基礎値null
     * =====================================================
     */
    @Test
    void testNullBaseValues() {

        DefenseResistanceResult result
                = defenseResistanceService.calculate(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
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

        assertDecimalEquals(
                "0",
                result.getConvertedElementAdd()
        );


        System.out.println(
                "全基礎値null：正常"
        );
    }


    /*
     * =====================================================
     * weaponType = null
     * =====================================================
     *
     * 武器種不明なら、
     * 勝手に丸めず生値を保持する。
     */
    @Test
    void testNullWeaponTypeKeepsRawValue() {

        ConditionalSkillModifierTotal modifier
                = new ConditionalSkillModifierTotal();

        modifier.setResistanceToElementRate(
                new BigDecimal("0.08")
        );


        DefenseResistanceResult result
                = defenseResistanceService.calculate(
                        0,
                        25,
                        25,
                        25,
                        24,
                        24,
                        null,
                        modifier
                );


        assertDecimalEquals(
                "9.84",
                result.getConvertedElementAddBeforeRound()
        );

        assertDecimalEquals(
                "9.84",
                result.getConvertedElementAdd()
        );


        System.out.println(
                "weaponType=null："
                + "raw="
                + result.getConvertedElementAdd()
        );
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