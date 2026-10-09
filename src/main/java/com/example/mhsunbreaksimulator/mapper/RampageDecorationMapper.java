package com.example.mhsunbreaksimulator.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mhsunbreaksimulator.entity.RampageDecoration;

@Mapper
public interface RampageDecorationMapper {

    List<RampageDecoration> findAll();

    // 指定した百竜スロットサイズと同じ装飾品を取得
    List<RampageDecoration> findByRequiredSlotSize(
            @Param("requiredSlotSize") Integer requiredSlotSize
    );

    // 実際の武器の百竜スロットに装着可能な装飾品を取得
    List<RampageDecoration> findUsableBySlotSize(
            @Param("slotSize") Integer slotSize
    );

    // IDから1件取得
    RampageDecoration findById(
            @Param("rampageDecorationId") Integer rampageDecorationId
    );
}