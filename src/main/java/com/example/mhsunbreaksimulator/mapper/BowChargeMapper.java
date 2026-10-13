package com.example.mhsunbreaksimulator.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.BowCharge;

@Mapper
public interface BowChargeMapper {

    // 指定した武器の溜め段階を取得
    List<BowCharge> findByWeaponId(
            @Param("weaponId") Integer weaponId
    );

    // IDから1件取得
    BowCharge findById(
            @Param("bowChargeId") Integer bowChargeId
    );
}