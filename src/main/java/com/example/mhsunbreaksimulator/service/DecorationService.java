package com.example.mhsunbreaksimulator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.entity.Decoration;
import com.example.mhsunbreaksimulator.mapper.DecorationMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DecorationService {

    private final DecorationMapper decorationMapper;

    public List<Decoration> findAll() {
        return decorationMapper.findAll();
    }

    public List<Decoration> findByRequiredSlotSize(
            Integer requiredSlotSize) {

        return decorationMapper
                .findByRequiredSlotSize(requiredSlotSize);
    }

    public List<Decoration> findUsableBySlotSize(
            Integer slotSize) {

        return decorationMapper
                .findUsableBySlotSize(slotSize);
    }

    public Decoration findById(
            Integer decorationId) {

        return decorationMapper.findById(decorationId);
    }
}