package com.example.mhsunbreaksimulator.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.SkillStateSelection;
import com.example.mhsunbreaksimulator.entity.SkillEffectState;

@Service
public class SelectedSkillEffectService {

    public Map<Integer, SkillEffectState> resolveSelectedEffects(
            List<SkillStateSelection> skillStateSelections,
            Map<String, String> requestParams) {

        Map<Integer, SkillEffectState> selectedEffects
                = new LinkedHashMap<>();

        if (skillStateSelections == null
                || skillStateSelections.isEmpty()) {

            return selectedEffects;
        }

        Map<String, String> safeRequestParams
                = requestParams == null
                        ? Map.of()
                        : requestParams;

        for (SkillStateSelection selection
                : skillStateSelections) {

            if (selection == null
                    || selection.getSkillId() == null
                    || selection.getStates() == null
                    || selection.getStates().isEmpty()) {

                continue;
            }

            String parameterName
                    = "skillState_"
                    + selection.getSkillId();

            String selectedStateCode
                    = safeRequestParams.get(
                            parameterName
                    );

            SkillEffectState selectedState
                    = findState(
                            selection.getStates(),
                            selectedStateCode
                    );

            /*
             * URLに状態指定がない場合、
             * または不正な状態コードだった場合は
             * INACTIVE を優先する。
             */
            if (selectedState == null) {

                selectedState = findState(
                        selection.getStates(),
                        "INACTIVE"
                );
            }

            /*
             * INACTIVE を持たない多状態スキルの場合は
             * 最初の状態を初期値として使用する。
             */
            if (selectedState == null) {

                selectedState
                        = selection.getStates().get(0);
            }

            selectedEffects.put(
                    selection.getSkillId(),
                    selectedState
            );
        }

        return selectedEffects;
    }

    private SkillEffectState findState(
            List<SkillEffectState> states,
            String stateCode) {

        if (states == null
                || states.isEmpty()
                || stateCode == null
                || stateCode.isBlank()) {

            return null;
        }

        for (SkillEffectState state : states) {

            if (state != null
                    && stateCode.equals(
                            state.getStateCode()
                    )) {

                return state;
            }
        }

        return null;
    }
}