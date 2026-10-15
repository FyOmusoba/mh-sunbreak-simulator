package com.example.mhsunbreaksimulator.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.entity.MonsterHitzone;
import com.example.mhsunbreaksimulator.mapper.MonsterHitzoneMapper;

@Service
public class MonsterHitzoneService {

    private final MonsterHitzoneMapper monsterHitzoneMapper;


    public MonsterHitzoneService(
            MonsterHitzoneMapper monsterHitzoneMapper) {

        this.monsterHitzoneMapper
                = monsterHitzoneMapper;
    }


    /*
     * =====================================================
     * モンスターIDから全部位取得
     * =====================================================
     *
     * 選択したモンスターの
     *
     * 頭
     * 首
     * 胴
     * 翼
     * 尻尾
     *
     * などの肉質一覧を取得する。
     */
    public List<MonsterHitzone> findByMonsterId(
            Integer monsterId) {

        /*
         * モンスター未選択なら
         * DB検索しない。
         */
        if (monsterId == null) {

            return Collections.emptyList();
        }


        List<MonsterHitzone> hitzones
                = monsterHitzoneMapper.findByMonsterId(
                        monsterId
                );


        /*
         * Mapperからnullが返っても
         * 呼び出し側では安全にfor文を回せる。
         */
        if (hitzones == null) {

            return Collections.emptyList();
        }


        return hitzones;
    }


    /*
     * =====================================================
     * 肉質IDから1部位取得
     * =====================================================
     *
     * シミュレーター画面で選択された
     * 1部位の肉質を取得する。
     */
    public MonsterHitzone findById(
            Integer hitzoneId) {

        if (hitzoneId == null) {

            return null;
        }


        return monsterHitzoneMapper.findById(
                hitzoneId
        );
    }


    /*
     * =====================================================
     * 選択部位が対象モンスターの部位か確認
     * =====================================================
     *
     * URLパラメータなどで
     *
     * monsterId = モンスターA
     * hitzoneId = モンスターBの部位
     *
     * という不正な組み合わせが来た場合に
     * 弾けるようにする。
     */
    public MonsterHitzone findByIdAndMonsterId(
            Integer hitzoneId,
            Integer monsterId) {

        if (hitzoneId == null
                || monsterId == null) {

            return null;
        }


        MonsterHitzone hitzone
                = monsterHitzoneMapper.findById(
                        hitzoneId
                );


        if (hitzone == null) {

            return null;
        }


        if (!monsterId.equals(
                hitzone.getMonsterId()
        )) {

            return null;
        }


        return hitzone;
    }
}