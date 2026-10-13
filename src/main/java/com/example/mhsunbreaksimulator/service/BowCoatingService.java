package com.example.mhsunbreaksimulator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.entity.BowCoating;
import com.example.mhsunbreaksimulator.mapper.BowCoatingMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BowCoatingService {

    private final BowCoatingMapper bowCoatingMapper;

    public List<BowCoating> findByWeaponId(
            Integer weaponId) {

        return bowCoatingMapper.findByWeaponId(
                weaponId
        );
    }

    public BowCoating findById(
            Integer bowCoatingId) {

        return bowCoatingMapper.findById(
                bowCoatingId
        );
    }
}