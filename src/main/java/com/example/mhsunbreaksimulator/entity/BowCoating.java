package com.example.mhsunbreaksimulator.entity;

import lombok.Data;

@Data
public class BowCoating {

    private Integer bowCoatingId;

    private Integer weaponId;

    private String coatingType;

    private Boolean isEnhanced;
}