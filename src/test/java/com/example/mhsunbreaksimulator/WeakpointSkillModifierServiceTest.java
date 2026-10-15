package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.MonsterWeakpointResult;
import com.example.mhsunbreaksimulator.dto.WeakpointSkillModifier;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;
import com.example.mhsunbreaksimulator.service.WeakpointSkillModifierService;

public class WeakpointSkillModifierServiceTest {

    private final WeakpointSkillModifierService
            weakpointSkillModifierService
            = new WeakpointSkillModifierService();


    /*
     * =====================================================
     * 弱点特効
     * 物理肉質44
     * =====================================================
     *
     * 弱点ではないので、
     * Lv3を装備していても会心率+50は発動しない。
     */
    @Test
    void testWeaknessExploitPhysical44() {

        MonsterWeakpointResult weakpoint
                = createWeakpointResult(
                        false,
                        false,
                        false
                );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();


        effects.put(
                8,
                createWeaknessExploitEffect(
                        "50"
                )
        );


        WeakpointSkillModifier result
                = weakpointSkillModifierService.calculate(
                        weakpoint,
                        effects
                );


        assertFalse(
                result.isPhysicalWeakpointActive()
        );

        assertEquals(
                BigDecimal.ZERO,
                result.getAffinityAdd()
        );


        System.out.println(
                "弱点特効・物理44："
                + "active="
                + result.isPhysicalWeakpointActive()
                + " / affinity="
                + result.getAffinityAdd()
        );
    }


    /*
     * =====================================================
     * 弱点特効
     * 物理肉質45
     * =====================================================
     *
     * Lv3
     * affinityAdd = 50
     */
    @Test
    void testWeaknessExploitPhysical45() {

        MonsterWeakpointResult weakpoint
                = createWeakpointResult(
                        true,
                        false,
                        false
                );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();


        effects.put(
                8,
                createWeaknessExploitEffect(
                        "50"
                )
        );


        WeakpointSkillModifier result
                = weakpointSkillModifierService.calculate(
                        weakpoint,
                        effects
                );


        assertTrue(
                result.isPhysicalWeakpointActive()
        );

        assertEquals(
                0,
                new BigDecimal("50")
                        .compareTo(
                                result.getAffinityAdd()
                        )
        );


        System.out.println(
                "弱点特効・物理45："
                + "active="
                + result.isPhysicalWeakpointActive()
                + " / affinity="
                + result.getAffinityAdd()
        );
    }


    /*
     * =====================================================
     * 弱点特効【属性】
     * 主属性肉質19
     * =====================================================
     *
     * 弱点ではないので
     * ×1.15は発動しない。
     */
    @Test
    void testElementExploitMain19() {

        MonsterWeakpointResult weakpoint
                = createWeakpointResult(
                        false,
                        false,
                        false
                );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();


        effects.put(
                34,
                createElementExploitEffect(
                        "1.15000"
                )
        );


        WeakpointSkillModifier result
                = weakpointSkillModifierService.calculate(
                        weakpoint,
                        effects
                );


        assertFalse(
                result.isMainElementWeakpointActive()
        );

        assertEquals(
                0,
                BigDecimal.ONE.compareTo(
                        result.getMainElementDamageMul()
                )
        );


        System.out.println(
                "弱点特効【属性】・主属性19："
                + "active="
                + result.isMainElementWeakpointActive()
                + " / mul="
                + result.getMainElementDamageMul()
        );
    }


    /*
     * =====================================================
     * 弱点特効【属性】
     * 主属性肉質20
     * =====================================================
     *
     * Lv3
     * ×1.15
     */
    @Test
    void testElementExploitMain20() {

        MonsterWeakpointResult weakpoint
                = createWeakpointResult(
                        false,
                        true,
                        false
                );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();


        effects.put(
                34,
                createElementExploitEffect(
                        "1.15000"
                )
        );


        WeakpointSkillModifier result
                = weakpointSkillModifierService.calculate(
                        weakpoint,
                        effects
                );


        assertTrue(
                result.isMainElementWeakpointActive()
        );

        assertEquals(
                0,
                new BigDecimal("1.15000")
                        .compareTo(
                                result.getMainElementDamageMul()
                        )
        );


        assertFalse(
                result.isSubElementWeakpointActive()
        );

        assertEquals(
                0,
                BigDecimal.ONE.compareTo(
                        result.getSubElementDamageMul()
                )
        );


        System.out.println(
                "弱点特効【属性】・主属性20："
                + "main="
                + result.getMainElementDamageMul()
                + " / sub="
                + result.getSubElementDamageMul()
        );
    }


    /*
     * =====================================================
     * 主属性だけ発動
     * =====================================================
     */
    @Test
    void testOnlyMainElementWeakpoint() {

        MonsterWeakpointResult weakpoint
                = createWeakpointResult(
                        false,
                        true,
                        false
                );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();


        effects.put(
                34,
                createElementExploitEffect(
                        "1.12500"
                )
        );


        WeakpointSkillModifier result
                = weakpointSkillModifierService.calculate(
                        weakpoint,
                        effects
                );


        assertTrue(
                result.isMainElementWeakpointActive()
        );

        assertFalse(
                result.isSubElementWeakpointActive()
        );


        assertEquals(
                0,
                new BigDecimal("1.12500")
                        .compareTo(
                                result.getMainElementDamageMul()
                        )
        );

        assertEquals(
                0,
                BigDecimal.ONE.compareTo(
                        result.getSubElementDamageMul()
                )
        );


        System.out.println(
                "属性弱点・主属性のみ："
                + "main="
                + result.getMainElementDamageMul()
                + " / sub="
                + result.getSubElementDamageMul()
        );
    }


    /*
     * =====================================================
     * 副属性だけ発動
     * =====================================================
     *
     * 例：
     * 氷炎魔剣ヴィルマクス
     *
     * 主：爆破
     * 副：氷
     *
     * 氷側だけ属性弱点になるケース。
     */
    @Test
    void testOnlySubElementWeakpoint() {

        MonsterWeakpointResult weakpoint
                = createWeakpointResult(
                        false,
                        false,
                        true
                );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();


        effects.put(
                34,
                createElementExploitEffect(
                        "1.10000"
                )
        );


        WeakpointSkillModifier result
                = weakpointSkillModifierService.calculate(
                        weakpoint,
                        effects
                );


        assertFalse(
                result.isMainElementWeakpointActive()
        );

        assertTrue(
                result.isSubElementWeakpointActive()
        );


        assertEquals(
                0,
                BigDecimal.ONE.compareTo(
                        result.getMainElementDamageMul()
                )
        );

        assertEquals(
                0,
                new BigDecimal("1.10000")
                        .compareTo(
                                result.getSubElementDamageMul()
                        )
        );


        System.out.println(
                "属性弱点・副属性のみ："
                + "main="
                + result.getMainElementDamageMul()
                + " / sub="
                + result.getSubElementDamageMul()
        );
    }


    /*
     * =====================================================
     * 主属性＋副属性
     * 両方発動
     * =====================================================
     */
    @Test
    void testBothElementWeakpoints() {

        MonsterWeakpointResult weakpoint
                = createWeakpointResult(
                        false,
                        true,
                        true
                );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();


        effects.put(
                34,
                createElementExploitEffect(
                        "1.15000"
                )
        );


        WeakpointSkillModifier result
                = weakpointSkillModifierService.calculate(
                        weakpoint,
                        effects
                );


        assertTrue(
                result.isMainElementWeakpointActive()
        );

        assertTrue(
                result.isSubElementWeakpointActive()
        );


        assertEquals(
                0,
                new BigDecimal("1.15000")
                        .compareTo(
                                result.getMainElementDamageMul()
                        )
        );

        assertEquals(
                0,
                new BigDecimal("1.15000")
                        .compareTo(
                                result.getSubElementDamageMul()
                        )
        );


        System.out.println(
                "属性弱点・両方："
                + "main="
                + result.getMainElementDamageMul()
                + " / sub="
                + result.getSubElementDamageMul()
        );
    }


    /*
     * =====================================================
     * 弱点特効＋弱点特効【属性】
     * 同時発動
     * =====================================================
     */
    @Test
    void testBothWeakpointSkills() {

        MonsterWeakpointResult weakpoint
                = createWeakpointResult(
                        true,
                        true,
                        false
                );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();


        effects.put(
                8,
                createWeaknessExploitEffect(
                        "30"
                )
        );

        effects.put(
                34,
                createElementExploitEffect(
                        "1.12500"
                )
        );


        WeakpointSkillModifier result
                = weakpointSkillModifierService.calculate(
                        weakpoint,
                        effects
                );


        assertTrue(
                result.isPhysicalWeakpointActive()
        );

        assertTrue(
                result.isMainElementWeakpointActive()
        );


        assertEquals(
                0,
                new BigDecimal("30")
                        .compareTo(
                                result.getAffinityAdd()
                        )
        );

        assertEquals(
                0,
                new BigDecimal("1.12500")
                        .compareTo(
                                result.getMainElementDamageMul()
                        )
        );


        System.out.println(
                "弱点系2種同時："
                + "affinity="
                + result.getAffinityAdd()
                + " / elementMul="
                + result.getMainElementDamageMul()
        );
    }


    /*
     * =====================================================
     * INACTIVE状態
     * =====================================================
     *
     * 肉質条件を満たしていても、
     * stateCodeがACTIVEでなければ適用しない。
     */
    @Test
    void testInactiveEffect() {

        MonsterWeakpointResult weakpoint
                = createWeakpointResult(
                        true,
                        true,
                        true
                );


        SkillEffectState weaknessExploit
                = createWeaknessExploitEffect(
                        "50"
                );

        weaknessExploit.setStateCode(
                "INACTIVE"
        );


        SkillEffectState elementExploit
                = createElementExploitEffect(
                        "1.15000"
                );

        elementExploit.setStateCode(
                "INACTIVE"
        );


        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();

        effects.put(
                8,
                weaknessExploit
        );

        effects.put(
                34,
                elementExploit
        );


        WeakpointSkillModifier result
                = weakpointSkillModifierService.calculate(
                        weakpoint,
                        effects
                );


        assertFalse(
                result.isPhysicalWeakpointActive()
        );

        assertFalse(
                result.isMainElementWeakpointActive()
        );

        assertFalse(
                result.isSubElementWeakpointActive()
        );


        assertEquals(
                BigDecimal.ZERO,
                result.getAffinityAdd()
        );

        assertEquals(
                0,
                BigDecimal.ONE.compareTo(
                        result.getMainElementDamageMul()
                )
        );

        assertEquals(
                0,
                BigDecimal.ONE.compareTo(
                        result.getSubElementDamageMul()
                )
        );


        System.out.println(
                "INACTIVE：弱点補正なし 正常"
        );
    }


    /*
     * =====================================================
     * スキル未装備
     * =====================================================
     */
    @Test
    void testNoWeakpointSkills() {

        MonsterWeakpointResult weakpoint
                = createWeakpointResult(
                        true,
                        true,
                        true
                );


        WeakpointSkillModifier result
                = weakpointSkillModifierService.calculate(
                        weakpoint,
                        Map.of()
                );


        assertFalse(
                result.isPhysicalWeakpointActive()
        );

        assertFalse(
                result.isMainElementWeakpointActive()
        );

        assertFalse(
                result.isSubElementWeakpointActive()
        );


        assertEquals(
                BigDecimal.ZERO,
                result.getAffinityAdd()
        );

        assertEquals(
                0,
                BigDecimal.ONE.compareTo(
                        result.getMainElementDamageMul()
                )
        );

        assertEquals(
                0,
                BigDecimal.ONE.compareTo(
                        result.getSubElementDamageMul()
                )
        );


        System.out.println(
                "弱点系スキル未装備：補正なし 正常"
        );
    }


    /*
     * =====================================================
     * selectedSkillEffects = null
     * =====================================================
     */
    @Test
    void testNullSelectedSkillEffects() {

        MonsterWeakpointResult weakpoint
                = createWeakpointResult(
                        true,
                        true,
                        true
                );


        WeakpointSkillModifier result
                = weakpointSkillModifierService.calculate(
                        weakpoint,
                        null
                );


        assertEquals(
                BigDecimal.ZERO,
                result.getAffinityAdd()
        );

        assertEquals(
                0,
                BigDecimal.ONE.compareTo(
                        result.getMainElementDamageMul()
                )
        );

        assertEquals(
                0,
                BigDecimal.ONE.compareTo(
                        result.getSubElementDamageMul()
                )
        );


        System.out.println(
                "selectedSkillEffects=null：正常"
        );
    }


    /*
     * =====================================================
     * weakpointResult = null
     * =====================================================
     */
    @Test
    void testNullWeakpointResult() {

        Map<Integer, SkillEffectState> effects
                = new LinkedHashMap<>();


        effects.put(
                8,
                createWeaknessExploitEffect(
                        "50"
                )
        );

        effects.put(
                34,
                createElementExploitEffect(
                        "1.15000"
                )
        );


        WeakpointSkillModifier result
                = weakpointSkillModifierService.calculate(
                        null,
                        effects
                );


        assertEquals(
                BigDecimal.ZERO,
                result.getAffinityAdd()
        );

        assertEquals(
                0,
                BigDecimal.ONE.compareTo(
                        result.getMainElementDamageMul()
                )
        );

        assertEquals(
                0,
                BigDecimal.ONE.compareTo(
                        result.getSubElementDamageMul()
                )
        );


        System.out.println(
                "weakpointResult=null：正常"
        );
    }


    /*
     * =====================================================
     * テスト用
     * 弱点判定結果
     * =====================================================
     */
    private MonsterWeakpointResult createWeakpointResult(
            boolean physicalWeakpoint,
            boolean elementWeakpoint,
            boolean subElementWeakpoint) {

        MonsterWeakpointResult result
                = new MonsterWeakpointResult();


        result.setPhysicalWeakpoint(
                physicalWeakpoint
        );

        result.setElementWeakpoint(
                elementWeakpoint
        );

        result.setSubElementWeakpoint(
                subElementWeakpoint
        );


        return result;
    }


    /*
     * =====================================================
     * テスト用
     * 弱点特効
     * =====================================================
     */
    private SkillEffectState createWeaknessExploitEffect(
            String affinityAdd) {

        SkillEffectState effect
                = new SkillEffectState();


        effect.setStateCode(
                "ACTIVE"
        );

        effect.setAffinityAdd(
                new BigDecimal(
                        affinityAdd
                )
        );


        return effect;
    }


    /*
     * =====================================================
     * テスト用
     * 弱点特効【属性】
     * =====================================================
     */
    private SkillEffectState createElementExploitEffect(
            String elementDamageMul) {

        SkillEffectState effect
                = new SkillEffectState();


        effect.setStateCode(
                "ACTIVE"
        );

        effect.setElementDamageMul(
                new BigDecimal(
                        elementDamageMul
                )
        );


        return effect;
    }
}