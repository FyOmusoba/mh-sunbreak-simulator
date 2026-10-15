package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.ElementHitzoneDamageResult;
import com.example.mhsunbreaksimulator.dto.FinalDamageResult;
import com.example.mhsunbreaksimulator.dto.PhysicalHitzoneDamageResult;
import com.example.mhsunbreaksimulator.service.FinalDamageService;

public class FinalDamageServiceTest {

    private final FinalDamageService
            finalDamageService
            = new FinalDamageService();


    /*
     * =====================================================
     * 四捨五入
     * 55.49 → 55
     * =====================================================
     */
    @Test
    void testRoundDownBelowHalf() {

        PhysicalHitzoneDamageResult physicalResult
                = new PhysicalHitzoneDamageResult();

        physicalResult.setNormalPhysicalDamagePerHit(
                new BigDecimal("55.49")
        );


        FinalDamageResult result
                = finalDamageService.calculate(
                        physicalResult,
                        null
                );


        assertEquals(
                55,
                result.getNormalPhysicalDamage()
        );

        assertEquals(
                55,
                result.getNormalTotalDamage()
        );


        System.out.println(
                "55.49 → "
                + result.getNormalPhysicalDamage()
        );
    }


    /*
     * =====================================================
     * 四捨五入
     * 55.50 → 56
     * =====================================================
     */
    @Test
    void testRoundUpAtHalf() {

        PhysicalHitzoneDamageResult physicalResult
                = new PhysicalHitzoneDamageResult();

        physicalResult.setNormalPhysicalDamagePerHit(
                new BigDecimal("55.50")
        );


        FinalDamageResult result
                = finalDamageService.calculate(
                        physicalResult,
                        null
                );


        assertEquals(
                56,
                result.getNormalPhysicalDamage()
        );

        assertEquals(
                56,
                result.getNormalTotalDamage()
        );


        System.out.println(
                "55.50 → "
                + result.getNormalPhysicalDamage()
        );
    }


    /*
     * =====================================================
     * 非会心
     * =====================================================
     *
     * 物理
     * 55.5525
     * → 56
     *
     * 属性
     * 27.77625
     * → 28
     *
     * 合計
     * 84
     */
    @Test
    void testNormalTotalDamage() {

        PhysicalHitzoneDamageResult physicalResult
                = new PhysicalHitzoneDamageResult();

        physicalResult.setNormalPhysicalDamagePerHit(
                new BigDecimal("55.5525")
        );


        ElementHitzoneDamageResult elementResult
                = new ElementHitzoneDamageResult();

        elementResult.setNormalElementDamagePerHit(
                new BigDecimal("27.7762500")
        );


        FinalDamageResult result
                = finalDamageService.calculate(
                        physicalResult,
                        elementResult
                );


        assertDecimalEquals(
                "55.5525",
                result.getNormalPhysicalDamageBeforeRound()
        );

        assertDecimalEquals(
                "27.7762500",
                result.getNormalElementDamageBeforeRound()
        );


        assertEquals(
                56,
                result.getNormalPhysicalDamage()
        );

        assertEquals(
                28,
                result.getNormalElementDamage()
        );

        assertEquals(
                84,
                result.getNormalTotalDamage()
        );


        System.out.println(
                "非会心："
                + "physical="
                + result.getNormalPhysicalDamage()
                + " / element="
                + result.getNormalElementDamage()
                + " / total="
                + result.getNormalTotalDamage()
        );
    }


    /*
     * =====================================================
     * プラス会心
     * =====================================================
     *
     * 物理
     * 77.7735
     * → 78
     *
     * 属性
     * 30.553875
     * → 31
     *
     * 合計
     * 109
     */
    @Test
    void testPositiveCriticalTotalDamage() {

        PhysicalHitzoneDamageResult physicalResult
                = new PhysicalHitzoneDamageResult();

        physicalResult.setPositiveCriticalPhysicalDamagePerHit(
                new BigDecimal("77.773500")
        );


        ElementHitzoneDamageResult elementResult
                = new ElementHitzoneDamageResult();

        elementResult.setPositiveCriticalElementDamagePerHit(
                new BigDecimal("30.553875000")
        );


        FinalDamageResult result
                = finalDamageService.calculate(
                        physicalResult,
                        elementResult
                );


        assertDecimalEquals(
                "77.773500",
                result.getPositiveCriticalPhysicalDamageBeforeRound()
        );

        assertDecimalEquals(
                "30.553875000",
                result.getPositiveCriticalElementDamageBeforeRound()
        );


        assertEquals(
                78,
                result.getPositiveCriticalPhysicalDamage()
        );

        assertEquals(
                31,
                result.getPositiveCriticalElementDamage()
        );

        assertEquals(
                109,
                result.getPositiveCriticalTotalDamage()
        );


        System.out.println(
                "プラス会心："
                + "physical="
                + result.getPositiveCriticalPhysicalDamage()
                + " / element="
                + result.getPositiveCriticalElementDamage()
                + " / total="
                + result.getPositiveCriticalTotalDamage()
        );
    }


    /*
     * =====================================================
     * マイナス会心
     * =====================================================
     *
     * 物理
     * 41.664375
     * → 42
     *
     * 属性は通常属性を使用
     *
     * 27.77625
     * → 28
     *
     * 合計
     * 70
     */
    @Test
    void testNegativeCriticalTotalDamage() {

        PhysicalHitzoneDamageResult physicalResult
                = new PhysicalHitzoneDamageResult();

        physicalResult.setNegativeCriticalPhysicalDamagePerHit(
                new BigDecimal("41.664375")
        );


        ElementHitzoneDamageResult elementResult
                = new ElementHitzoneDamageResult();

        elementResult.setNormalElementDamagePerHit(
                new BigDecimal("27.7762500")
        );

        /*
         * ここに別の属性会心値を入れても、
         * マイナス会心では使用しない。
         */
        elementResult.setPositiveCriticalElementDamagePerHit(
                new BigDecimal("99.999")
        );


        FinalDamageResult result
                = finalDamageService.calculate(
                        physicalResult,
                        elementResult
                );


        assertDecimalEquals(
                "41.664375",
                result.getNegativeCriticalPhysicalDamageBeforeRound()
        );


        assertEquals(
                42,
                result.getNegativeCriticalPhysicalDamage()
        );

        assertEquals(
                28,
                result.getNegativeCriticalElementDamage()
        );

        assertEquals(
                70,
                result.getNegativeCriticalTotalDamage()
        );


        System.out.println(
                "マイナス会心："
                + "physical="
                + result.getNegativeCriticalPhysicalDamage()
                + " / element="
                + result.getNegativeCriticalElementDamage()
                + " / total="
                + result.getNegativeCriticalTotalDamage()
        );
    }


    /*
     * =====================================================
     * 物理のみ
     * =====================================================
     */
    @Test
    void testPhysicalOnly() {

        PhysicalHitzoneDamageResult physicalResult
                = new PhysicalHitzoneDamageResult();

        physicalResult.setNormalPhysicalDamagePerHit(
                new BigDecimal("50.6")
        );

        physicalResult.setPositiveCriticalPhysicalDamagePerHit(
                new BigDecimal("63.2")
        );

        physicalResult.setNegativeCriticalPhysicalDamagePerHit(
                new BigDecimal("37.8")
        );


        FinalDamageResult result
                = finalDamageService.calculate(
                        physicalResult,
                        null
                );


        assertEquals(
                51,
                result.getNormalTotalDamage()
        );

        assertEquals(
                63,
                result.getPositiveCriticalTotalDamage()
        );

        assertEquals(
                38,
                result.getNegativeCriticalTotalDamage()
        );


        System.out.println(
                "物理のみ："
                + "normal="
                + result.getNormalTotalDamage()
                + " / positive="
                + result.getPositiveCriticalTotalDamage()
                + " / negative="
                + result.getNegativeCriticalTotalDamage()
        );
    }


    /*
     * =====================================================
     * 属性のみ
     * =====================================================
     */
    @Test
    void testElementOnly() {

        ElementHitzoneDamageResult elementResult
                = new ElementHitzoneDamageResult();

        elementResult.setNormalElementDamagePerHit(
                new BigDecimal("20.6")
        );

        elementResult.setPositiveCriticalElementDamagePerHit(
                new BigDecimal("23.4")
        );


        FinalDamageResult result
                = finalDamageService.calculate(
                        null,
                        elementResult
                );


        assertEquals(
                21,
                result.getNormalTotalDamage()
        );

        assertEquals(
                23,
                result.getPositiveCriticalTotalDamage()
        );

        /*
         * マイナス会心では
         * 属性は通常値を使うので21。
         */
        assertEquals(
                21,
                result.getNegativeCriticalTotalDamage()
        );


        System.out.println(
                "属性のみ："
                + "normal="
                + result.getNormalTotalDamage()
                + " / positive="
                + result.getPositiveCriticalTotalDamage()
                + " / negative="
                + result.getNegativeCriticalTotalDamage()
        );
    }


    /*
     * =====================================================
     * physicalResult = null
     * elementResult = null
     * =====================================================
     */
    @Test
    void testBothNull() {

        FinalDamageResult result
                = finalDamageService.calculate(
                        null,
                        null
                );


        assertEquals(
                0,
                result.getNormalTotalDamage()
        );

        assertEquals(
                0,
                result.getPositiveCriticalTotalDamage()
        );

        assertEquals(
                0,
                result.getNegativeCriticalTotalDamage()
        );


        System.out.println(
                "physicalResult=null"
                + " / elementResult=null：正常"
        );
    }


    /*
     * =====================================================
     * DTO内部の値 = null
     * =====================================================
     */
    @Test
    void testNullDamageValues() {

        PhysicalHitzoneDamageResult physicalResult
                = new PhysicalHitzoneDamageResult();

        physicalResult.setNormalPhysicalDamagePerHit(
                null
        );

        physicalResult.setPositiveCriticalPhysicalDamagePerHit(
                null
        );

        physicalResult.setNegativeCriticalPhysicalDamagePerHit(
                null
        );


        ElementHitzoneDamageResult elementResult
                = new ElementHitzoneDamageResult();

        elementResult.setNormalElementDamagePerHit(
                null
        );

        elementResult.setPositiveCriticalElementDamagePerHit(
                null
        );


        FinalDamageResult result
                = finalDamageService.calculate(
                        physicalResult,
                        elementResult
                );


        assertEquals(
                0,
                result.getNormalTotalDamage()
        );

        assertEquals(
                0,
                result.getPositiveCriticalTotalDamage()
        );

        assertEquals(
                0,
                result.getNegativeCriticalTotalDamage()
        );


        System.out.println(
                "各ダメージ値null：正常"
        );
    }


    /*
     * =====================================================
     * 物理と属性を
     * 合算してから丸めていないことを確認
     * =====================================================
     *
     * 物理
     * 10.49
     * → 10
     *
     * 属性
     * 10.49
     * → 10
     *
     * 正しい結果
     * → 20
     *
     *
     * もし先に合算すると
     *
     * 10.49 + 10.49
     * = 20.98
     * → 21
     *
     * となってしまう。
     */
    @Test
    void testRoundSeparatelyBeforeAdding() {

        PhysicalHitzoneDamageResult physicalResult
                = new PhysicalHitzoneDamageResult();

        physicalResult.setNormalPhysicalDamagePerHit(
                new BigDecimal("10.49")
        );


        ElementHitzoneDamageResult elementResult
                = new ElementHitzoneDamageResult();

        elementResult.setNormalElementDamagePerHit(
                new BigDecimal("10.49")
        );


        FinalDamageResult result
                = finalDamageService.calculate(
                        physicalResult,
                        elementResult
                );


        assertEquals(
                10,
                result.getNormalPhysicalDamage()
        );

        assertEquals(
                10,
                result.getNormalElementDamage()
        );

        assertEquals(
                20,
                result.getNormalTotalDamage()
        );


        System.out.println(
                "個別四捨五入確認："
                + "10.49→"
                + result.getNormalPhysicalDamage()
                + " + 10.49→"
                + result.getNormalElementDamage()
                + " = "
                + result.getNormalTotalDamage()
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