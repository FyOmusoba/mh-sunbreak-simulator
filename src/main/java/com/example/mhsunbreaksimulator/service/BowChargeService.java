package com.example.mhsunbreaksimulator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.entity.BowCharge;
import com.example.mhsunbreaksimulator.mapper.BowChargeMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BowChargeService {

    private final BowChargeMapper bowChargeMapper;

    public List<BowCharge> findByWeaponId(
            Integer weaponId) {

        return bowChargeMapper.findByWeaponId(
                weaponId
        );
    }

    public BowCharge findById(
            Integer bowChargeId) {

        return bowChargeMapper.findById(
                bowChargeId
        );
    }
}