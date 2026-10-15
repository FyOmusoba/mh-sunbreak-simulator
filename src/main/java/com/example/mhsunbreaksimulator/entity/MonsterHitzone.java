package com.example.mhsunbreaksimulator.entity;

import lombok.Data;

@Data
public class MonsterHitzone {

    /*
     * monster_hitzoneテーブル主キー
     */
    private Integer hitzoneId;


    /*
     * 対象モンスターID
     */
    private Integer monsterId;


    /*
     * 部位名
     *
     * 例：
     * 頭部
     * 首
     * 胴
     * 翼
     * 尻尾
     */
    private String partName;


    /*
     * -----------------------------
     * 物理肉質
     * -----------------------------
     */


    /*
     * 斬肉質
     */
    private Integer sever;


    /*
     * 打肉質
     */
    private Integer blunt;


    /*
     * 弾肉質
     */
    private Integer shot;


    /*
     * -----------------------------
     * 属性肉質
     * -----------------------------
     */


    /*
     * 火属性肉質
     */
    private Integer fire;


    /*
     * 水属性肉質
     */
    private Integer water;


    /*
     * 雷属性肉質
     */
    private Integer thunder;


    /*
     * 氷属性肉質
     */
    private Integer ice;


    /*
     * 龍属性肉質
     */
    private Integer dragon;
}