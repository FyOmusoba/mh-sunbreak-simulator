package com.example.mhsunbreaksimulator.controller;

import java.security.Principal;
import java.sql.SQLException;
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
import com.example.mhsunbreaksimulator.service.PresetDeleteService;
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

    private final PresetDeleteService presetDeleteService;

    private final PresetService presetService;

    private final UserService userService;


    /*
     * =====================================================
     * プリセット一覧
     * =====================================================
     */
    @GetMapping("/preset/list")
    public String showPresetList(
            Principal principal,
            Model model) {

        if (principal == null
                || principal.getName() == null
                || principal.getName().isBlank()) {

            return "redirect:/login";
        }


        User user
                = userService.findByUsername(
                        principal.getName()
                );


        if (user == null
                || user.getUserId() == null) {

            return "redirect:/login";
        }


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
     */
    @GetMapping("/preset/load")
    public String loadPreset(
            Principal principal,
            @RequestParam Integer presetId) {

        if (principal == null
                || principal.getName() == null
                || principal.getName().isBlank()) {

            return "redirect:/login";
        }


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
     * プリセット削除
     * =====================================================
     *
     * POSTにすることで、
     * URLを開いただけで削除されないようにする。
     */
    @PostMapping("/preset/delete")
    public String deletePreset(
            Principal principal,
            @RequestParam Integer presetId) {

        if (principal == null
                || principal.getName() == null
                || principal.getName().isBlank()) {

            return "redirect:/login";
        }


        boolean deleted
                = presetDeleteService.delete(
                        presetId,
                        principal.getName()
                );


        if (!deleted) {

            return buildPresetListRedirect(
                    "presetError",
                    "プリセットを削除できませんでした。"
            );
        }


        return buildPresetListRedirect(
                "presetDeleted",
                "true"
        );
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

        if (principal == null
                || principal.getName() == null
                || principal.getName().isBlank()) {

            return "redirect:/login";
        }


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

            String errorMessage
                    = isDuplicatePresetNameViolation(e)
                            ? "同じ名前のプリセットが既に存在します。"
                            : "プリセットを保存できませんでした。入力内容またはデータベース制約を確認してください。";


            return buildSimulatorRedirect(
                    requestParams,
                    "presetError",
                    errorMessage
            );
        }
    }


    /*
     * =====================================================
     * プリセット名重複エラー判定
     * =====================================================
     *
     * PostgreSQL の UNIQUE違反 SQLSTATE 23505 のうち、
     * preset のプリセット名制約 uq_preset_name に
     * 該当する場合だけ「同じ名前」と判定する。
     *
     * それ以外のDB制約違反を、
     * 誤ってプリセット名重複として表示しないための処理。
     */
    private boolean isDuplicatePresetNameViolation(
            DataIntegrityViolationException exception) {

        Throwable cause
                = exception;


        while (cause != null) {

            if (cause instanceof SQLException sqlException) {

                String message
                        = sqlException.getMessage();


                if ("23505".equals(
                        sqlException.getSQLState()
                )
                        && message != null
                        && message.contains(
                                "uq_preset_name"
                        )) {

                    return true;
                }
            }


            cause = cause.getCause();
        }


        return false;
    }


    /*
     * =====================================================
     * 読み込み後のシミュレーターURL作成
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
     * プリセット一覧URL作成
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