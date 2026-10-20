package com.example.mhsunbreaksimulator.entity;

import lombok.Data;

@Data
public class PresetDecoration {

    private Integer presetDecorationId;

    private Integer presetId;

    private String equipmentKind;

    private String equipmentPart;

    private Integer slotNo;

    private Integer decorationId;
}