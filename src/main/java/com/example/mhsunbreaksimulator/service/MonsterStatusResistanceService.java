package com.example.mhsunbreaksimulator.service;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.entity.MonsterStatusResistance;
import com.example.mhsunbreaksimulator.mapper.MonsterStatusResistanceMapper;

@Service
public class MonsterStatusResistanceService {

    private final MonsterStatusResistanceMapper
            monsterStatusResistanceMapper;


    public MonsterStatusResistanceService(
            MonsterStatusResistanceMapper monsterStatusResistanceMapper) {

        this.monsterStatusResistanceMapper
                = monsterStatusResistanceMapper;
    }


    /*
     * Java内部で使用している状態異常名から
     * モンスターの状態異常耐性を取得する。
     *
     * Java内部：
     *
     * poison
     * paralysis
     * sleep
     * blast
     *
     * ↓
     *
     * DB：
     *
     * 毒
     * 麻痺
     * 睡眠
     * 爆破
     */
    public MonsterStatusResistance findByMonsterIdAndStatusType(
            Integer monsterId,
            String statusType) {

        if (monsterId == null
                || statusType == null) {

            return null;
        }


        String databaseStatusType
                = convertToDatabaseStatusType(
                        statusType
                );


        /*
         * 未対応の状態異常の場合。
         *
         * 例えば
         *
         * exhaust
         *
         * はmonster_status_resistanceには
         * 存在しないため取得しない。
         */
        if (databaseStatusType == null) {

            return null;
        }


        return monsterStatusResistanceMapper
                .findByMonsterIdAndStatusType(
                        monsterId,
                        databaseStatusType
                );
    }


    /*
     * Java内部名
     * ↓
     * DB日本語名
     */
    private String convertToDatabaseStatusType(
            String statusType) {

        switch (statusType) {

        case "poison":

            return "毒";


        case "paralysis":

            return "麻痺";


        case "sleep":

            return "睡眠";


        case "blast":

            return "爆破";


        default:

            return null;
        }
    }
}