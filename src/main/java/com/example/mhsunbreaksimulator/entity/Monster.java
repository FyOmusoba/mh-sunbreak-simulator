package com.example.mhsunbreaksimulator.entity;

import lombok.Data;

@Data
public class Monster {

    /*
     * monsterテーブル主キー
     */
    private Integer monsterId;


    /*
     * モンスター名
     */
    private String monsterName;
}