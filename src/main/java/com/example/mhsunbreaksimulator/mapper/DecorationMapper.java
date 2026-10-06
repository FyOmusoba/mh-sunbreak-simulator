package com.example.mhsunbreaksimulator.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.Decoration;

@Mapper
public interface DecorationMapper {

    List<Decoration> findAll();

    // マスター確認用
    List<Decoration> findByRequiredSlotSize(
            @Param("requiredSlotSize") Integer requiredSlotSize
    );

    // 実際の装備スロットに装着可能な装飾品を取得
    List<Decoration> findUsableBySlotSize(
            @Param("slotSize") Integer slotSize
    );

    Decoration findById(
            @Param("decorationId") Integer decorationId
    );
}