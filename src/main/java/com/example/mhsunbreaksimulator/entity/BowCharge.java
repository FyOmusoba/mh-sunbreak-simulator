package com.example.mhsunbreaksimulator.entity;

import lombok.Data;

@Data
public class BowCharge {

    private Integer bowChargeId;

    private Integer weaponId;

    private Integer chargeLevel;

    private String shotType;

    private Integer shotLevel;

    private Boolean initiallyAvailable;
}