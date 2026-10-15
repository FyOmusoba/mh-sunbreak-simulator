package com.example.mhsunbreaksimulator.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.entity.Monster;
import com.example.mhsunbreaksimulator.mapper.MonsterMapper;

@Service
public class MonsterService {

    private final MonsterMapper monsterMapper;


    public MonsterService(
            MonsterMapper monsterMapper) {

        this.monsterMapper
                = monsterMapper;
    }


    /*
     * =====================================================
     * 全モンスター取得
     * =====================================================
     *
     * シミュレーター画面の
     * モンスター選択欄などで使用する。
     */
    public List<Monster> findAll() {

        List<Monster> monsters
                = monsterMapper.findAll();


        /*
         * Mapperからnullが返ってきた場合でも
         * 呼び出し側でnullチェックを大量に
         * 書かなくて済むように空Listを返す。
         */
        if (monsters == null) {

            return Collections.emptyList();
        }


        return monsters;
    }


    /*
     * =====================================================
     * モンスターIDから1件取得
     * =====================================================
     */
    public Monster findById(
            Integer monsterId) {

        /*
         * ID未選択ならDB検索しない。
         */
        if (monsterId == null) {

            return null;
        }


        return monsterMapper.findById(
                monsterId
        );
    }
}