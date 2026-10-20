package com.example.mhsunbreaksimulator.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.mhsunbreaksimulator.form.SimulatorForm;
import com.example.mhsunbreaksimulator.service.PresetSaveService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PresetController {

    private final PresetSaveService presetSaveService;


    /*
     * =====================================================
     * 現在のシミュレーター構成をプリセット保存
     * =====================================================
     */
    @PostMapping("/preset/save")
    public String savePreset(
            Principal principal,
            @RequestParam String presetName,
            @ModelAttribute SimulatorForm simulatorForm,
            @RequestParam Map<String, String> requestParams) {


        /*
         * =====================================================
         * ログインユーザー確認
         * =====================================================
         */

        if (principal == null
                || principal.getName() == null
                || principal.getName().isBlank()) {

            return "redirect:/login";
        }


        /*
         * =====================================================
         * プリセット保存
         * =====================================================
         */

        try {

            presetSaveService.save(
                    principal.getName(),
                    presetName,
                    simulatorForm,
                    requestParams
            );


            return buildSimulatorRedirect(
                    requestParams,
                    "presetSaved",
                    "true"
            );


        } catch (IllegalArgumentException e) {

            return buildSimulatorRedirect(
                    requestParams,
                    "presetError",
                    e.getMessage()
            );


        } catch (DataIntegrityViolationException e) {

            /*
             * preset には
             * UNIQUE (user_id, preset_name)
             * があるため、同一ユーザーが同じ名前を
             * 保存した場合などにここへ来る。
             */

            return buildSimulatorRedirect(
                    requestParams,
                    "presetError",
                    "同じ名前のプリセットが既に存在します。"
            );
        }
    }


    /*
     * =====================================================
     * シミュレーターへ戻るURL作成
     * =====================================================
     *
     * 保存前の武器・防具・装飾品・護石・
     * 条件付きスキルなどのURLパラメータを維持する。
     *
     * presetName と _csrf は
     * シミュレーター状態ではないため除外する。
     */
    private String buildSimulatorRedirect(
            Map<String, String> requestParams,
            String messageParameterName,
            String message) {

        UriComponentsBuilder builder
                = UriComponentsBuilder.fromPath(
                        "/simulator"
                );


        if (requestParams != null) {

            for (Map.Entry<String, String> entry
                    : requestParams.entrySet()) {

                String name
                        = entry.getKey();

                String value
                        = entry.getValue();


                if (name == null
                        || value == null
                        || "presetName".equals(name)
                        || "_csrf".equals(name)
                        || "presetSaved".equals(name)
                        || "presetError".equals(name)) {

                    continue;
                }


                builder.queryParam(
                        name,
                        value
                );
            }
        }


        if (messageParameterName != null
                && message != null
                && !message.isBlank()) {

            builder.queryParam(
                    messageParameterName,
                    message
            );
        }


        String url
                = builder
                        .build()
                        .encode()
                        .toUriString();


        return "redirect:" + url;
    }
}