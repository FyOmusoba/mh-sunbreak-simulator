package com.example.mhsunbreaksimulator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.entity.Armor;
import com.example.mhsunbreaksimulator.mapper.ArmorMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArmorService {

    private final ArmorMapper armorMapper;

    public List<Armor> findAll() {
        return armorMapper.findAll();
    }

    public List<Armor> findByPart(String part) {
        return armorMapper.findByPart(part);
    }

    public Armor findById(Integer armorId) {
        return armorMapper.findById(armorId);
    }
}