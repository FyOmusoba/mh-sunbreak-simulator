package com.example.mhsunbreaksimulator.entity;

import lombok.Data;

@Data
public class BowgunAmmo {

    private Integer bowgunAmmoId;

    private Integer weaponId;

    private String ammoType;

    private Integer ammoLevel;

    private Integer capacity;

    private Boolean rapidFire;

    private String recoil;

    private String reload;

    private Boolean movingShot;

    private Boolean movingReload;

    private Boolean singleAuto;
}