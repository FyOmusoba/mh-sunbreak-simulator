package com.example.mhsunbreaksimulator.entity;
import lombok.Data;

@Data
public class Weapon {

    private Integer weaponId;

    private String weaponName;

    private String weaponType;

    private String elementType;

    private Integer attack;

    private Integer elementValue;

    private Integer affinity;

    private Integer rampageSlotSize;

    private Integer slot1Size;

    private Integer slot2Size;

    private Integer slot3Size;

    private String attackProperty;

    private String maxSharpnessColor;

    private Integer sharpRed;

    private Integer sharpOrange;

    private Integer sharpYellow;

    private Integer sharpGreen;

    private Integer sharpBlue;

    private Integer sharpWhite;

    private Integer sharpPurple;

    private String phialType;

    private Integer phialValue;

    private Integer defenseBonus;

    private Integer takumiVal1;

    private Integer takumiVal2;

    private Integer takumiVal3;

    private Integer takumiVal4;

    private String subElementType;

    private Integer subElementValue;

    private Integer bowgunRecoilStage;

    private Integer bowgunReloadStage;
}