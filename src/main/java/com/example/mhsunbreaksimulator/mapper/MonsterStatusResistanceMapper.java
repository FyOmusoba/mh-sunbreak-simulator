package com.example.mhsunbreaksimulator.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.MonsterStatusResistance;

@Mapper
public interface MonsterStatusResistanceMapper {

    /*
     * モンスターIDと状態異常種別から
     * 状態異常耐性を1件取得する。
     *
     * statusTypeはDB側の日本語表記を使用する。
     *
     * 例：
     *
     * 毒
     * 麻痺
     * 睡眠
     * 爆破
     */
    MonsterStatusResistance findByMonsterIdAndStatusType(
            @Param("monsterId") Integer monsterId,
            @Param("statusType") String statusType);
}