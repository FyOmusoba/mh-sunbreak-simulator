package com.example.mhsunbreaksimulator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.entity.Weapon;
import com.example.mhsunbreaksimulator.mapper.WeaponMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeaponService {

    private final WeaponMapper weaponMapper;

    public List<Weapon> findAll() {
        return weaponMapper.findAll();
    }

    public List<Weapon> findByWeaponType(String weaponType) {
        return weaponMapper.findByWeaponType(weaponType);
    }

    public Weapon findById(Integer weaponId) {
        return weaponMapper.findById(weaponId);
    }
}