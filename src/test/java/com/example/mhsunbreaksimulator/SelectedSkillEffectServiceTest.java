	package com.example.mhsunbreaksimulator;
	
	import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.SkillStateSelection;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;
import com.example.mhsunbreaksimulator.service.SelectedSkillEffectService;
	
	public class SelectedSkillEffectServiceTest {
	
	    private final SelectedSkillEffectService selectedSkillEffectService
	            = new SelectedSkillEffectService();
	
	    @Test
	    void testSelectedStateIsResolved() {
	
	        SkillEffectState hit1To4
	                = createState(
	                        "HIT_1_4",
	                        "1～4ヒット",
	                        "5.000",
	                        "5.000"
	                );
	
	        SkillEffectState hit5Plus
	                = createState(
	                        "HIT_5_PLUS",
	                        "5ヒット以上",
	                        "15.000",
	                        "15.000"
	                );
	
	        SkillEffectState inactive
	                = createState(
	                        "INACTIVE",
	                        "0ヒット",
	                        "0.000",
	                        "0.000"
	                );
	
	        SkillStateSelection selection
	                = new SkillStateSelection(
	                        35,
	                        "連撃",
	                        3,
	                        "連続攻撃命中時",
	                        false,
	                        true,
	                        List.of(
	                                hit1To4,
	                                hit5Plus,
	                                inactive
	                        )
	                );
	
	        Map<Integer, SkillEffectState> result
	                = selectedSkillEffectService.resolveSelectedEffects(
	                        List.of(selection),
	                        Map.of(
	                                "skillState_35",
	                                "HIT_5_PLUS"
	                        )
	                );
	
	        SkillEffectState selected
	                = result.get(35);
	
	        assertSame(
	                hit5Plus,
	                selected
	        );
	
	        assertEquals(
	                "HIT_5_PLUS",
	                selected.getStateCode()
	        );
	
	        assertEquals(
	                new BigDecimal("15.000"),
	                selected.getAttackAdd()
	        );
	
	        assertEquals(
	                new BigDecimal("15.000"),
	                selected.getElementAdd()
	        );
	
	        System.out.println(
	                "選択状態："
	                + selected.getStateCode()
	                + " / "
	                + selected.getStateName()
	                + " / attackAdd="
	                + selected.getAttackAdd()
	                + " / elementAdd="
	                + selected.getElementAdd()
	        );
	    }
	
	    @Test
	    void testInvalidStateFallsBackToInactive() {
	
	        SkillEffectState active
	                = createState(
	                        "ACTIVE",
	                        "発動",
	                        "10.000",
	                        "10.000"
	                );
	
	        SkillEffectState inactive
	                = createState(
	                        "INACTIVE",
	                        "未発動",
	                        "0.000",
	                        "0.000"
	                );
	
	        SkillStateSelection selection
	                = new SkillStateSelection(
	                        999,
	                        "テストスキル",
	                        1,
	                        "テスト条件",
	                        true,
	                        false,
	                        List.of(
	                                active,
	                                inactive
	                        )
	                );
	
	        Map<Integer, SkillEffectState> result
	                = selectedSkillEffectService.resolveSelectedEffects(
	                        List.of(selection),
	                        Map.of(
	                                "skillState_999",
	                                "HOGE"
	                        )
	                );
	
	        SkillEffectState selected
	                = result.get(999);
	
	        assertSame(
	                inactive,
	                selected
	        );
	
	        assertEquals(
	                "INACTIVE",
	                selected.getStateCode()
	        );
	
	        System.out.println(
	                "不正値フォールバック："
	                + selected.getStateCode()
	                + " / "
	                + selected.getStateName()
	        );
	    }
	
	    @Test
	    void testMissingParameterFallsBackToInactive() {
	
	        SkillEffectState active
	                = createState(
	                        "ACTIVE",
	                        "発動",
	                        "10.000",
	                        "10.000"
	                );
	
	        SkillEffectState inactive
	                = createState(
	                        "INACTIVE",
	                        "未発動",
	                        "0.000",
	                        "0.000"
	                );
	
	        SkillStateSelection selection
	                = new SkillStateSelection(
	                        999,
	                        "テストスキル",
	                        1,
	                        "テスト条件",
	                        true,
	                        false,
	                        List.of(
	                                active,
	                                inactive
	                        )
	                );
	
	        Map<Integer, SkillEffectState> result
	                = selectedSkillEffectService.resolveSelectedEffects(
	                        List.of(selection),
	                        Map.of()
	                );
	
	        SkillEffectState selected
	                = result.get(999);
	
	        assertSame(
	                inactive,
	                selected
	        );
	
	        assertEquals(
	                "INACTIVE",
	                selected.getStateCode()
	        );
	
	        System.out.println(
	                "未指定フォールバック："
	                + selected.getStateCode()
	                + " / "
	                + selected.getStateName()
	        );
	    }
	
	    private SkillEffectState createState(
	            String stateCode,
	            String stateName,
	            String attackAdd,
	            String elementAdd) {
	
	        SkillEffectState state
	                = new SkillEffectState();
	
	        state.setStateCode(
	                stateCode
	        );
	
	        state.setStateName(
	                stateName
	        );
	
	        state.setAttackAdd(
	                new BigDecimal(
	                        attackAdd
	                )
	        );
	
	        state.setElementAdd(
	                new BigDecimal(
	                        elementAdd
	                )
	        );
	
	        return state;
	    }
	}