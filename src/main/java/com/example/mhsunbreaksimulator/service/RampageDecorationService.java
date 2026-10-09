package com.example.mhsunbreaksimulator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.entity.RampageDecoration;
import com.example.mhsunbreaksimulator.mapper.RampageDecorationMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RampageDecorationService {

    private final RampageDecorationMapper rampageDecorationMapper;

    public List<RampageDecoration> findAll() {
        return rampageDecorationMapper.findAll();
    }

    public List<RampageDecoration> findByRequiredSlotSize(
            Integer requiredSlotSize) {

        return rampageDecorationMapper
                .findByRequiredSlotSize(requiredSlotSize);
    }

    public List<RampageDecoration> findUsableBySlotSize(
            Integer slotSize) {

        return rampageDecorationMapper
                .findUsableBySlotSize(slotSize);
    }

    public RampageDecoration findById(
            Integer rampageDecorationId) {

        return rampageDecorationMapper
                .findById(rampageDecorationId);
    }
}