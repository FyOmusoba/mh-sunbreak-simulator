package com.example.mhsunbreaksimulator.form;

import lombok.Data;

@Data
public class SimulatorForm {

    // =========================
    // 武器
    // =========================
    private Integer weaponId;

    // 武器の装飾品
    private Integer weaponDecoration1Id;
    private Integer weaponDecoration2Id;
    private Integer weaponDecoration3Id;


    // =========================
    // 頭
    // =========================
    private Integer headArmorId;

    private Integer headDecoration1Id;
    private Integer headDecoration2Id;
    private Integer headDecoration3Id;


    // =========================
    // 胴
    // =========================
    private Integer chestArmorId;

    private Integer chestDecoration1Id;
    private Integer chestDecoration2Id;
    private Integer chestDecoration3Id;


    // =========================
    // 腕
    // =========================
    private Integer armArmorId;

    private Integer armDecoration1Id;
    private Integer armDecoration2Id;
    private Integer armDecoration3Id;


    // =========================
    // 腰
    // =========================
    private Integer waistArmorId;

    private Integer waistDecoration1Id;
    private Integer waistDecoration2Id;
    private Integer waistDecoration3Id;


    // =========================
    // 脚
    // =========================
    private Integer legArmorId;

    private Integer legDecoration1Id;
    private Integer legDecoration2Id;
    private Integer legDecoration3Id;
}