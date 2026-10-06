package com.example.mhsunbreaksimulator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.entity.Skill;
import com.example.mhsunbreaksimulator.mapper.SkillMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillMapper skillMapper;


    /*
     * 全スキル取得
     */
    public List<Skill> findAll() {

        return skillMapper.findAll();
    }


    /*
     * スキルIDから1件取得
     */
    public Skill findById(Integer skillId) {

        if (skillId == null) {
            return null;
        }

        return skillMapper.findById(skillId);
    }
}