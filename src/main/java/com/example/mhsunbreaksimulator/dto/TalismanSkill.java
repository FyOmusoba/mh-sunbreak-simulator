package com.example.mhsunbreaksimulator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TalismanSkill {

    /*
     * スキルID
     */
    private Integer skillId;

    /*
     * スキル名
     */
    private String skillName;

    /*
     * 護石についているLv
     */
    private Integer level;

    /*
     * そのスキル本来の最大Lv
     */
    private Integer maxLevel;
}