package com.example.mhsunbreaksimulator.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.mhsunbreaksimulator.entity.Preset;
import com.example.mhsunbreaksimulator.entity.User;
import com.example.mhsunbreaksimulator.form.SimulatorForm;
import com.example.mhsunbreaksimulator.service.PresetLoadService;
import com.example.mhsunbreaksimulator.service.PresetSaveService;
import com.example.mhsunbreaksimulator.service.PresetService;
import com.example.mhsunbreaksimulator.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PresetController {

    private final PresetSaveService presetSaveService;

    private final PresetLoadService presetLoadService;

    private final PresetService presetService;

    private final UserService userService;


    /*
     * =====================================================
     * プリセット一覧
     * =====================================================
     *
     * ログイン中ユーザーが所有している
     * プリセットだけを取得して表示する。
     */
    @GetMapping("/preset/list")
    public String showPresetList(
            Principal principal,
            Model model) {


        /*
         * =====================================================
         * ログイン確認
         * =====================================================
         */

        if (principal == null
                || principal.getName() == null
                || principal.getName().isBlank()) {

            return "redirect:/login";
        }


        /*
         * =====================================================
         * ログインユーザー取得
         * =====================================================
         */

        User user
                = userService.findByUsername(
                        principal.getName()
                );


        if (user == null
                || user.getUserId() == null) {

            return "redirect:/login";
        }


        /*
         * =====================================================
         * ユーザーのプリセット一覧取得
         * =====================================================
         */

        List<Preset> presets
                = presetService.findByUserId(
                        user.getUserId()
                );


        model.addAttribute(
                "presets",
                presets
        );


        return "preset-list";
    }


    /*
     * =====================================================
     * プリセット読み込み
     * =====================================================
     *
     * 保存済みプリセットを
     * シミュレーター用URLパラメータへ復元し、
     * /simulator へ移動する。
     */
    @GetMapping("/preset/load")
    public String loadPreset(
            Principal principal,
            @RequestParam Integer presetId) {


        /*
         * =====================================================
         * ログイン確認
         * =====================================================
         */

        if (principal == null
                || principal.getName() == null
                || principal.getName().isBlank()) {

            return "redirect:/login";
        }


        /*
         * =====================================================
         * プリセット復元
         * =====================================================
         */

        try {

            Map<String, String> parameters
                    = presetLoadService.load(
                            principal.getName(),
                            presetId
                    );


            return buildSimulatorRedirect(
                    parameters
            );


        } catch (IllegalArgumentException e) {

            return buildPresetListRedirect(
                    "presetError",
                    e.getMessage()
            );
        }
    }


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
     * プリセット読み込み後の
     * シミュレーターURL作成
     * =====================================================
     */
    private String buildSimulatorRedirect(
            Map<String, String> parameters) {

        UriComponentsBuilder builder
                = UriComponentsBuilder.fromPath(
                        "/simulator"
                );


        if (parameters != null) {

            for (Map.Entry<String, String> entry
                    : parameters.entrySet()) {

                String name
                        = entry.getKey();

                String value
                        = entry.getValue();


                if (name == null
                        || name.isBlank()
                        || value == null
                        || value.isBlank()) {

                    continue;
                }


                builder.queryParam(
                        name,
                        value
                );
            }
        }


        String url
                = builder
                        .build()
                        .encode()
                        .toUriString();


        return "redirect:" + url;
    }


    /*
     * =====================================================
     * 保存後のシミュレーターURL作成
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


    /*
     * =====================================================
     * プリセット一覧へ戻るURL作成
     * =====================================================
     */
    private String buildPresetListRedirect(
            String messageParameterName,
            String message) {

        UriComponentsBuilder builder
                = UriComponentsBuilder.fromPath(
                        "/preset/list"
                );


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