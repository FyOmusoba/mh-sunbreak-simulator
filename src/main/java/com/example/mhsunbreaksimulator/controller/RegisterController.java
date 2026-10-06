package com.example.mhsunbreaksimulator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.mhsunbreaksimulator.form.RegisterForm;
import com.example.mhsunbreaksimulator.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegister(Model model) {

        model.addAttribute(
                "registerForm",
                new RegisterForm()
        );

        return "register";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute RegisterForm registerForm,
            Model model) {

        // ユーザー名の重複チェック
        if (userService.existsByUsername(
                registerForm.getUsername())) {

            model.addAttribute(
                    "error",
                    "このユーザー名は既に使用されています。"
            );

            return "register";
        }

        // パスワード確認
        if (!registerForm.getPassword()
                .equals(registerForm.getConfirmPassword())) {

            model.addAttribute(
                    "error",
                    "パスワードが一致していません。"
            );

            return "register";
        }

        // 登録
        userService.register(
                registerForm.getUsername(),
                registerForm.getPassword()
        );

        return "redirect:/login";
    }
}