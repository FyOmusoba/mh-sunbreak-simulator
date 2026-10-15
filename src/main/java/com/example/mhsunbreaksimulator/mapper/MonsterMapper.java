package com.example.mhsunbreaksimulator.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.Monster;

@Mapper
public interface MonsterMapper {

    /*
     * 全モンスター取得
     */
    List<Monster> findAll();


    /*
     * モンスターIDから1件取得
     */
    Monster findById(
            @Param("monsterId") Integer monsterId);
}