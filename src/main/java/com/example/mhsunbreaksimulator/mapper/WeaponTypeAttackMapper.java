package com.example.mhsunbreaksimulator.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.WeaponTypeAttack;

@Mapper
public interface WeaponTypeAttackMapper {

    /*
     * 武器種から基準攻撃を1件取得
     *
     * weapon_type_attack は
     * weapon_type が主キーなので、
     * 1武器種につき必ず最大1件。
     *
     * 例：
     * 大剣
     * 太刀
     * 片手剣
     * ハンマー
     */
    WeaponTypeAttack findByWeaponType(
            @Param("weaponType") String weaponType);
}