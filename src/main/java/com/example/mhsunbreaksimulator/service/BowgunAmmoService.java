package com.example.mhsunbreaksimulator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.entity.BowgunAmmo;
import com.example.mhsunbreaksimulator.mapper.BowgunAmmoMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BowgunAmmoService {

    private final BowgunAmmoMapper bowgunAmmoMapper;


    /*
     * 指定した武器で使用できる弾一覧を取得
     */
    public List<BowgunAmmo> findByWeaponId(
            Integer weaponId) {

        return bowgunAmmoMapper.findByWeaponId(
                weaponId
        );
    }


    /*
     * IDから1件取得
     */
    public BowgunAmmo findById(
            Integer bowgunAmmoId) {

        return bowgunAmmoMapper.findById(
                bowgunAmmoId
        );
    }
}