package com.example.mhsunbreaksimulator.service;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.entity.WeaponTypeAttack;
import com.example.mhsunbreaksimulator.mapper.WeaponTypeAttackMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeaponTypeAttackService {

    private final WeaponTypeAttackMapper weaponTypeAttackMapper;


    /*
     * 武器種から基準攻撃データを取得
     */
    public WeaponTypeAttack findByWeaponType(
            String weaponType) {

        if (weaponType == null
                || weaponType.isBlank()) {

            return null;
        }


        return weaponTypeAttackMapper.findByWeaponType(
                weaponType
        );
    }
}