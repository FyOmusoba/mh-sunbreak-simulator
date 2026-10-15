package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.SkillTotal;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;
import com.example.mhsunbreaksimulator.service.ResolvedSkillEffectService;
import com.example.mhsunbreaksimulator.service.SkillEffectStateService;

public class ResolvedSkillEffectServiceTest {

    /*
     * =====================================================
     * 見切りDEFAULT
     * =====================================================
     *
     * 見切りのような
     * 条件選択を必要としない常時スキルでも、
     *
     * DEFAULT効果が最終Mapへ入ることを確認。
     */
    @Test
    void testCriticalEyeDefaultEffectIsResolved() {

        SkillEffectStateService
                skillEffectStateService
                = mock(
                        SkillEffectStateService.class
                );


        ResolvedSkillEffectService service
                = new ResolvedSkillEffectService(
                        skillEffectStateService
                );


        SkillTotal criticalEye
                = new SkillTotal(
                        6,
                        "見切り",
                        7,
                        7
                );


        SkillEffectState defaultEffect
                = new SkillEffectState();

        defaultEffect.setStateCode(
                "DEFAULT"
        );

        defaultEffect.setAffinityAdd(
                new BigDecimal("40")
        );


        when(
                skillEffectStateService
                        .findBySkillIdAndLevelAndStateCode(
                                6,
                                7,
                                "DEFAULT",
                                "大剣"
                        )
        ).thenReturn(
                defaultEffect
        );


        Map<Integer, SkillEffectState> result
                = service.resolveAllEffects(
                        List.of(
                                criticalEye
                        ),
                        Map.of(),
                        "大剣"
                );


        assertEquals(
                1,
                result.size()
        );

        assertTrue(
                result.containsKey(
                        6
                )
        );

        assertSame(
                defaultEffect,
                result.get(
                        6
                )
        );

        assertDecimalEquals(
                "40",
                result.get(
                        6
                ).getAffinityAdd()
        );


        verify(
                skillEffectStateService
        ).findBySkillIdAndLevelAndStateCode(
                6,
                7,
                "DEFAULT",
                "大剣"
        );


        System.out.println(
                "見切りLv7 DEFAULT："
                + "affinity="
                + result.get(
                        6
                ).getAffinityAdd()
        );
    }


    /*
     * =====================================================
     * 超会心DEFAULT
     * =====================================================
     *
     * 超会心Lv3
     *
     * critDamageMul = 1.40
     *
     * が最終Mapへ残ることを確認。
     */
    @Test
    void testCriticalBoostDefaultEffectIsResolved() {

        SkillEffectStateService
                skillEffectStateService
                = mock(
                        SkillEffectStateService.class
                );


        ResolvedSkillEffectService service
                = new ResolvedSkillEffectService(
                        skillEffectStateService
                );


        SkillTotal criticalBoost
                = new SkillTotal(
                        7,
                        "超会心",
                        3,
                        3
                );


        SkillEffectState defaultEffect
                = new SkillEffectState();

        defaultEffect.setStateCode(
                "DEFAULT"
        );

        defaultEffect.setCritDamageMul(
                new BigDecimal("1.40")
        );


        when(
                skillEffectStateService
                        .findBySkillIdAndLevelAndStateCode(
                                7,
                                3,
                                "DEFAULT",
                                "太刀"
                        )
        ).thenReturn(
                defaultEffect
        );


        Map<Integer, SkillEffectState> result
                = service.resolveAllEffects(
                        List.of(
                                criticalBoost
                        ),
                        Map.of(),
                        "太刀"
                );


        assertEquals(
                1,
                result.size()
        );

        assertSame(
                defaultEffect,
                result.get(
                        7
                )
        );

        assertDecimalEquals(
                "1.40",
                result.get(
                        7
                ).getCritDamageMul()
        );


        System.out.println(
                "超会心Lv3 DEFAULT："
                + "critDamageMul="
                + result.get(
                        7
                ).getCritDamageMul()
        );
    }


    /*
     * =====================================================
     * 会心撃【属性】DEFAULT
     * =====================================================
     */
    @Test
    void testCriticalElementDefaultEffectIsResolved() {

        SkillEffectStateService
                skillEffectStateService
                = mock(
                        SkillEffectStateService.class
                );


        ResolvedSkillEffectService service
                = new ResolvedSkillEffectService(
                        skillEffectStateService
                );


        SkillTotal criticalElement
                = new SkillTotal(
                        11,
                        "会心撃【属性】",
                        3,
                        3
                );


        SkillEffectState defaultEffect
                = new SkillEffectState();

        defaultEffect.setStateCode(
                "DEFAULT"
        );

        defaultEffect.setElementCritDamageMul(
                new BigDecimal("1.15")
        );


        when(
                skillEffectStateService
                        .findBySkillIdAndLevelAndStateCode(
                                11,
                                3,
                                "DEFAULT",
                                "双剣"
                        )
        ).thenReturn(
                defaultEffect
        );


        Map<Integer, SkillEffectState> result
                = service.resolveAllEffects(
                        List.of(
                                criticalElement
                        ),
                        Map.of(),
                        "双剣"
                );


        assertSame(
                defaultEffect,
                result.get(
                        11
                )
        );

        assertDecimalEquals(
                "1.15",
                result.get(
                        11
                ).getElementCritDamageMul()
        );


        System.out.println(
                "会心撃【属性】Lv3 DEFAULT："
                + "elementCritDamageMul="
                + result.get(
                        11
                ).getElementCritDamageMul()
        );
    }


    /*
     * =====================================================
     * 複数DEFAULT
     * =====================================================
     *
     * 見切り
     * ＋
     * 超会心
     *
     * を同時に保持できることを確認。
     */
    @Test
    void testMultipleDefaultEffects() {

        SkillEffectStateService
                skillEffectStateService
                = mock(
                        SkillEffectStateService.class
                );


        ResolvedSkillEffectService service
                = new ResolvedSkillEffectService(
                        skillEffectStateService
                );


        SkillTotal criticalEye
                = new SkillTotal(
                        6,
                        "見切り",
                        7,
                        7
                );

        SkillTotal criticalBoost
                = new SkillTotal(
                        7,
                        "超会心",
                        3,
                        3
                );


        SkillEffectState criticalEyeEffect
                = new SkillEffectState();

        criticalEyeEffect.setStateCode(
                "DEFAULT"
        );

        criticalEyeEffect.setAffinityAdd(
                new BigDecimal("40")
        );


        SkillEffectState criticalBoostEffect
                = new SkillEffectState();

        criticalBoostEffect.setStateCode(
                "DEFAULT"
        );

        criticalBoostEffect.setCritDamageMul(
                new BigDecimal("1.40")
        );


        when(
                skillEffectStateService
                        .findBySkillIdAndLevelAndStateCode(
                                6,
                                7,
                                "DEFAULT",
                                "大剣"
                        )
        ).thenReturn(
                criticalEyeEffect
        );


        when(
                skillEffectStateService
                        .findBySkillIdAndLevelAndStateCode(
                                7,
                                3,
                                "DEFAULT",
                                "大剣"
                        )
        ).thenReturn(
                criticalBoostEffect
        );


        Map<Integer, SkillEffectState> result
                = service.resolveAllEffects(
                        List.of(
                                criticalEye,
                                criticalBoost
                        ),
                        Map.of(),
                        "大剣"
                );


        assertEquals(
                2,
                result.size()
        );

        assertDecimalEquals(
                "40",
                result.get(
                        6
                ).getAffinityAdd()
        );

        assertDecimalEquals(
                "1.40",
                result.get(
                        7
                ).getCritDamageMul()
        );


        System.out.println(
                "複数DEFAULT："
                + "見切り="
                + result.get(
                        6
                ).getAffinityAdd()
                + " / 超会心="
                + result.get(
                        7
                ).getCritDamageMul()
        );
    }


    /*
     * =====================================================
     * 条件付き効果で上書き
     * =====================================================
     *
     * 仮に同じskillIdのDEFAULTが存在しても、
     *
     * 現在選択中のACTIVE効果を
     * 最終的に優先する。
     */
    @Test
    void testConditionalActiveOverridesDefault() {

        SkillEffectStateService
                skillEffectStateService
                = mock(
                        SkillEffectStateService.class
                );


        ResolvedSkillEffectService service
                = new ResolvedSkillEffectService(
                        skillEffectStateService
                );


        SkillTotal challenger
                = new SkillTotal(
                        2,
                        "挑戦者",
                        5,
                        5
                );


        SkillEffectState defaultEffect
                = new SkillEffectState();

        defaultEffect.setStateCode(
                "DEFAULT"
        );

        defaultEffect.setAttackAdd(
                BigDecimal.ZERO
        );


        SkillEffectState activeEffect
                = new SkillEffectState();

        activeEffect.setStateCode(
                "ACTIVE"
        );

        activeEffect.setAttackAdd(
                new BigDecimal("20")
        );

        activeEffect.setAffinityAdd(
                new BigDecimal("15")
        );


        when(
                skillEffectStateService
                        .findBySkillIdAndLevelAndStateCode(
                                2,
                                5,
                                "DEFAULT",
                                "大剣"
                        )
        ).thenReturn(
                defaultEffect
        );


        Map<Integer, SkillEffectState>
                selectedConditionalEffects
                = new LinkedHashMap<>();

        selectedConditionalEffects.put(
                2,
                activeEffect
        );


        Map<Integer, SkillEffectState> result
                = service.resolveAllEffects(
                        List.of(
                                challenger
                        ),
                        selectedConditionalEffects,
                        "大剣"
                );


        assertEquals(
                1,
                result.size()
        );

        assertSame(
                activeEffect,
                result.get(
                        2
                )
        );

        assertEquals(
                "ACTIVE",
                result.get(
                        2
                ).getStateCode()
        );

        assertDecimalEquals(
                "20",
                result.get(
                        2
                ).getAttackAdd()
        );

        assertDecimalEquals(
                "15",
                result.get(
                        2
                ).getAffinityAdd()
        );


        System.out.println(
                "条件付きACTIVE上書き："
                + "state="
                + result.get(
                        2
                ).getStateCode()
                + " / attack="
                + result.get(
                        2
                ).getAttackAdd()
                + " / affinity="
                + result.get(
                        2
                ).getAffinityAdd()
        );
    }


    /*
     * =====================================================
     * DEFAULTなし
     * =====================================================
     *
     * DEFAULT行が存在しないスキルで、
     * 条件付き選択もない場合はMapへ入らない。
     */
    @Test
    void testSkillWithoutDefaultIsIgnored() {

        SkillEffectStateService
                skillEffectStateService
                = mock(
                        SkillEffectStateService.class
                );


        ResolvedSkillEffectService service
                = new ResolvedSkillEffectService(
                        skillEffectStateService
                );


        SkillTotal challenger
                = new SkillTotal(
                        2,
                        "挑戦者",
                        5,
                        5
                );


        when(
                skillEffectStateService
                        .findBySkillIdAndLevelAndStateCode(
                                2,
                                5,
                                "DEFAULT",
                                "大剣"
                        )
        ).thenReturn(
                null
        );


        Map<Integer, SkillEffectState> result
                = service.resolveAllEffects(
                        List.of(
                                challenger
                        ),
                        Map.of(),
                        "大剣"
                );


        assertTrue(
                result.isEmpty()
        );

        assertFalse(
                result.containsKey(
                        2
                )
        );


        System.out.println(
                "DEFAULTなし＋条件選択なし：無視 正常"
        );
    }


    /*
     * =====================================================
     * DEFAULTなし
     * ＋
     * 条件付きACTIVEあり
     * =====================================================
     *
     * DEFAULTがなくてもACTIVEが選択済みなら
     * その効果はMapへ入る。
     */
    @Test
    void testConditionalEffectWithoutDefaultIsResolved() {

        SkillEffectStateService
                skillEffectStateService
                = mock(
                        SkillEffectStateService.class
                );


        ResolvedSkillEffectService service
                = new ResolvedSkillEffectService(
                        skillEffectStateService
                );


        SkillTotal challenger
                = new SkillTotal(
                        2,
                        "挑戦者",
                        5,
                        5
                );


        when(
                skillEffectStateService
                        .findBySkillIdAndLevelAndStateCode(
                                2,
                                5,
                                "DEFAULT",
                                "大剣"
                        )
        ).thenReturn(
                null
        );


        SkillEffectState activeEffect
                = new SkillEffectState();

        activeEffect.setStateCode(
                "ACTIVE"
        );

        activeEffect.setAttackAdd(
                new BigDecimal("20")
        );


        Map<Integer, SkillEffectState>
                selectedConditionalEffects
                = new LinkedHashMap<>();

        selectedConditionalEffects.put(
                2,
                activeEffect
        );


        Map<Integer, SkillEffectState> result
                = service.resolveAllEffects(
                        List.of(
                                challenger
                        ),
                        selectedConditionalEffects,
                        "大剣"
                );


        assertEquals(
                1,
                result.size()
        );

        assertSame(
                activeEffect,
                result.get(
                        2
                )
        );


        System.out.println(
                "DEFAULTなし＋ACTIVEあり："
                + "state="
                + result.get(
                        2
                ).getStateCode()
        );
    }


    /*
     * =====================================================
     * skillTotals = null
     * =====================================================
     *
     * 条件付き効果だけでも処理可能。
     */
    @Test
    void testNullSkillTotals() {

        SkillEffectStateService
                skillEffectStateService
                = mock(
                        SkillEffectStateService.class
                );


        ResolvedSkillEffectService service
                = new ResolvedSkillEffectService(
                        skillEffectStateService
                );


        SkillEffectState activeEffect
                = new SkillEffectState();

        activeEffect.setStateCode(
                "ACTIVE"
        );


        Map<Integer, SkillEffectState>
                selectedConditionalEffects
                = new LinkedHashMap<>();

        selectedConditionalEffects.put(
                2,
                activeEffect
        );


        Map<Integer, SkillEffectState> result
                = service.resolveAllEffects(
                        null,
                        selectedConditionalEffects,
                        "大剣"
                );


        assertEquals(
                1,
                result.size()
        );

        assertSame(
                activeEffect,
                result.get(
                        2
                )
        );


        System.out.println(
                "skillTotals=null：条件付き効果のみ保持 正常"
        );
    }


    /*
     * =====================================================
     * selectedConditionalEffects = null
     * =====================================================
     *
     * DEFAULT効果だけでも処理可能。
     */
    @Test
    void testNullConditionalEffects() {

        SkillEffectStateService
                skillEffectStateService
                = mock(
                        SkillEffectStateService.class
                );


        ResolvedSkillEffectService service
                = new ResolvedSkillEffectService(
                        skillEffectStateService
                );


        SkillTotal criticalEye
                = new SkillTotal(
                        6,
                        "見切り",
                        7,
                        7
                );


        SkillEffectState defaultEffect
                = new SkillEffectState();

        defaultEffect.setStateCode(
                "DEFAULT"
        );

        defaultEffect.setAffinityAdd(
                new BigDecimal("40")
        );


        when(
                skillEffectStateService
                        .findBySkillIdAndLevelAndStateCode(
                                6,
                                7,
                                "DEFAULT",
                                "大剣"
                        )
        ).thenReturn(
                defaultEffect
        );


        Map<Integer, SkillEffectState> result
                = service.resolveAllEffects(
                        List.of(
                                criticalEye
                        ),
                        null,
                        "大剣"
                );


        assertEquals(
                1,
                result.size()
        );

        assertSame(
                defaultEffect,
                result.get(
                        6
                )
        );


        System.out.println(
                "selectedConditionalEffects=null："
                + "DEFAULT保持 正常"
        );
    }


    /*
     * =====================================================
     * 両方null
     * =====================================================
     */
    @Test
    void testBothNull() {

        SkillEffectStateService
                skillEffectStateService
                = mock(
                        SkillEffectStateService.class
                );


        ResolvedSkillEffectService service
                = new ResolvedSkillEffectService(
                        skillEffectStateService
                );


        Map<Integer, SkillEffectState> result
                = service.resolveAllEffects(
                        null,
                        null,
                        null
                );


        assertTrue(
                result.isEmpty()
        );


        System.out.println(
                "skillTotals=null / selectedEffects=null：正常"
        );
    }


    /*
     * =====================================================
     * 不正なSkillTotal
     * =====================================================
     */
    @Test
    void testInvalidSkillTotalsAreIgnored() {

        SkillEffectStateService
                skillEffectStateService
                = mock(
                        SkillEffectStateService.class
                );


        ResolvedSkillEffectService service
                = new ResolvedSkillEffectService(
                        skillEffectStateService
                );


        SkillTotal nullId
                = new SkillTotal(
                        null,
                        "テスト",
                        1,
                        1
                );


        SkillTotal nullLevel
                = new SkillTotal(
                        999,
                        "テスト",
                        null,
                        1
                );


        SkillTotal zeroLevel
                = new SkillTotal(
                        998,
                        "テスト",
                        0,
                        1
                );


        Map<Integer, SkillEffectState> result
                = service.resolveAllEffects(
                        List.of(
                                nullId,
                                nullLevel,
                                zeroLevel
                        ),
                        null,
                        "大剣"
                );


        assertTrue(
                result.isEmpty()
        );


        System.out.println(
                "不正SkillTotal：無視 正常"
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