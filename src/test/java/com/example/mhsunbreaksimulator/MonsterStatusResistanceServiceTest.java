package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.mhsunbreaksimulator.entity.MonsterStatusResistance;
import com.example.mhsunbreaksimulator.service.MonsterStatusResistanceService;

@SpringBootTest
public class MonsterStatusResistanceServiceTest {

    /*
     * ガイアデルム
     *
     * monster_id = 16
     */
    private static final Integer GAISMAGORM_ID
            = 16;


    @Autowired
    private MonsterStatusResistanceService
            monsterStatusResistanceService;


    /*
     * =====================================================
     * 毒
     * =====================================================
     *
     * Java内部：
     * poison
     *
     * DB：
     * 毒
     */
    @Test
    void testPoison() {

        MonsterStatusResistance result
                = monsterStatusResistanceService
                        .findByMonsterIdAndStatusType(
                                GAISMAGORM_ID,
                                "poison"
                        );


        assertNotNull(
                result
        );

        assertEquals(
                GAISMAGORM_ID,
                result.getMonsterId()
        );

        assertEquals(
                "毒",
                result.getStatusType()
        );

        assertEquals(
                250,
                result.getInitialThreshold()
        );

        assertEquals(
                150,
                result.getIncreasePerProc()
        );

        assertEquals(
                700,
                result.getMaxThreshold()
        );

        assertBigDecimalEquals(
                "5.00",
                result.getDecayValue()
        );

        assertBigDecimalEquals(
                "15.00",
                result.getEffectDurationSec()
        );

        assertEquals(
                150,
                result.getProcDamage()
        );

        assertFalse(
                result.getIsImmune()
        );


        System.out.println(
                "poison → 毒："
                + "initial="
                + result.getInitialThreshold()
                + " / increase="
                + result.getIncreasePerProc()
                + " / max="
                + result.getMaxThreshold()
                + " / damage="
                + result.getProcDamage()
        );
    }


    /*
     * =====================================================
     * 麻痺
     * =====================================================
     */
    @Test
    void testParalysis() {

        MonsterStatusResistance result
                = monsterStatusResistanceService
                        .findByMonsterIdAndStatusType(
                                GAISMAGORM_ID,
                                "paralysis"
                        );


        assertNotNull(
                result
        );

        assertEquals(
                "麻痺",
                result.getStatusType()
        );

        assertEquals(
                300,
                result.getInitialThreshold()
        );

        assertEquals(
                100,
                result.getIncreasePerProc()
        );

        assertEquals(
                700,
                result.getMaxThreshold()
        );

        assertBigDecimalEquals(
                "5.00",
                result.getDecayValue()
        );

        assertBigDecimalEquals(
                "7.00",
                result.getEffectDurationSec()
        );

        assertNull(
                result.getProcDamage()
        );

        assertFalse(
                result.getIsImmune()
        );


        System.out.println(
                "paralysis → 麻痺："
                + "initial="
                + result.getInitialThreshold()
                + " / increase="
                + result.getIncreasePerProc()
                + " / max="
                + result.getMaxThreshold()
                + " / duration="
                + result.getEffectDurationSec()
        );
    }


    /*
     * =====================================================
     * 睡眠
     * =====================================================
     */
    @Test
    void testSleep() {

        MonsterStatusResistance result
                = monsterStatusResistanceService
                        .findByMonsterIdAndStatusType(
                                GAISMAGORM_ID,
                                "sleep"
                        );


        assertNotNull(
                result
        );

        assertEquals(
                "睡眠",
                result.getStatusType()
        );

        assertEquals(
                250,
                result.getInitialThreshold()
        );

        assertEquals(
                150,
                result.getIncreasePerProc()
        );

        assertEquals(
                700,
                result.getMaxThreshold()
        );

        assertBigDecimalEquals(
                "5.00",
                result.getDecayValue()
        );

        assertBigDecimalEquals(
                "40.00",
                result.getEffectDurationSec()
        );

        assertNull(
                result.getProcDamage()
        );

        assertFalse(
                result.getIsImmune()
        );


        System.out.println(
                "sleep → 睡眠："
                + "initial="
                + result.getInitialThreshold()
                + " / increase="
                + result.getIncreasePerProc()
                + " / max="
                + result.getMaxThreshold()
                + " / duration="
                + result.getEffectDurationSec()
        );
    }


    /*
     * =====================================================
     * 爆破
     * =====================================================
     */
    @Test
    void testBlast() {

        MonsterStatusResistance result
                = monsterStatusResistanceService
                        .findByMonsterIdAndStatusType(
                                GAISMAGORM_ID,
                                "blast"
                        );


        assertNotNull(
                result
        );

        assertEquals(
                "爆破",
                result.getStatusType()
        );

        assertEquals(
                70,
                result.getInitialThreshold()
        );

        assertEquals(
                30,
                result.getIncreasePerProc()
        );

        assertEquals(
                670,
                result.getMaxThreshold()
        );

        assertBigDecimalEquals(
                "5.00",
                result.getDecayValue()
        );

        assertBigDecimalEquals(
                "0.00",
                result.getEffectDurationSec()
        );

        assertEquals(
                100,
                result.getProcDamage()
        );

        assertFalse(
                result.getIsImmune()
        );


        System.out.println(
                "blast → 爆破："
                + "initial="
                + result.getInitialThreshold()
                + " / increase="
                + result.getIncreasePerProc()
                + " / max="
                + result.getMaxThreshold()
                + " / damage="
                + result.getProcDamage()
        );
    }


    /*
     * 減気は
     * monster_status_resistance
     * の対象外。
     */
    @Test
    void testExhaustUnsupported() {

        MonsterStatusResistance result
                = monsterStatusResistanceService
                        .findByMonsterIdAndStatusType(
                                GAISMAGORM_ID,
                                "exhaust"
                        );


        assertNull(
                result
        );


        System.out.println(
                "exhaust：対象外なのでnull 正常"
        );
    }


    /*
     * monsterId=null
     */
    @Test
    void testNullMonsterId() {

        MonsterStatusResistance result
                = monsterStatusResistanceService
                        .findByMonsterIdAndStatusType(
                                null,
                                "poison"
                        );


        assertNull(
                result
        );


        System.out.println(
                "monsterId=null：正常"
        );
    }


    /*
     * statusType=null
     */
    @Test
    void testNullStatusType() {

        MonsterStatusResistance result
                = monsterStatusResistanceService
                        .findByMonsterIdAndStatusType(
                                GAISMAGORM_ID,
                                null
                        );


        assertNull(
                result
        );


        System.out.println(
                "statusType=null：正常"
        );
    }


    private void assertBigDecimalEquals(
            String expected,
            BigDecimal actual) {

        assertNotNull(
                actual
        );

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