package com.example.mhsunbreaksimulator.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.BowgunAmmo;

@Mapper
public interface BowgunAmmoMapper {

    /*
     * 指定した武器で使用できる弾一覧を取得
     */
    List<BowgunAmmo> findByWeaponId(
            @Param("weaponId") Integer weaponId
    );


    /*
     * IDから1件取得
     */
    BowgunAmmo findById(
            @Param("bowgunAmmoId") Integer bowgunAmmoId
    );
}