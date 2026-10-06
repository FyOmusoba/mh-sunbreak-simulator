package com.example.mhsunbreaksimulator.entity;

import lombok.Data;

@Data
public class Armor {

    private Integer armorId;

    private String armorName;

    private String part;

    private Integer skill1Id;
    private String skill1Name;
    private Integer skill1Level;
    private Integer skill1MaxLevel;

    private Integer skill2Id;
    private String skill2Name;
    private Integer skill2Level;
    private Integer skill2MaxLevel;

    private Integer skill3Id;
    private String skill3Name;
    private Integer skill3Level;
    private Integer skill3MaxLevel;

    private Integer skill4Id;
    private String skill4Name;
    private Integer skill4Level;
    private Integer skill4MaxLevel;

    private Integer slot1Size;
    private Integer slot2Size;
    private Integer slot3Size;

    private Integer defense;

    private Integer fireRes;
    private Integer waterRes;
    private Integer thunderRes;
    private Integer iceRes;
    private Integer dragonRes;
}