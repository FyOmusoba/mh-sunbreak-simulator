package com.example.mhsunbreaksimulator.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.MonsterHitzone;

@Mapper
public interface MonsterHitzoneMapper {

    /*
     * モンスターIDから
     * 全部位の肉質データを取得する。
     *
     * 例：
     *
     * 頭部
     * 首
     * 胴
     * 翼
     * 尻尾
     *
     * など。
     */
    List<MonsterHitzone> findByMonsterId(
            @Param("monsterId") Integer monsterId);


    /*
     * 肉質IDから
     * 1部位を取得する。
     *
     * 最終的に画面上で
     * 選択された部位を取得するときに使用する。
     */
    MonsterHitzone findById(
            @Param("hitzoneId") Integer hitzoneId);
}