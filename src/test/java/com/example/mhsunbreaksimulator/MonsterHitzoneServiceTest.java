package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.mhsunbreaksimulator.entity.Monster;
import com.example.mhsunbreaksimulator.entity.MonsterHitzone;
import com.example.mhsunbreaksimulator.service.MonsterHitzoneService;
import com.example.mhsunbreaksimulator.service.MonsterService;

@SpringBootTest
public class MonsterHitzoneServiceTest {

    /*
     * ガイアデルム
     */
    private static final Integer GAISMAGORM_ID
            = 16;


    /*
     * ガイアデルム頭部
     */
    private static final Integer GAISMAGORM_HEAD_HITZONE_ID
            = 501;


    @Autowired
    private MonsterService monsterService;


    @Autowired
    private MonsterHitzoneService monsterHitzoneService;


    /*
     * =====================================================
     * モンスターID指定取得
     * =====================================================
     */
    @Test
    void testFindMonsterById() {

        Monster monster
                = monsterService.findById(
                        GAISMAGORM_ID
                );


        assertNotNull(
                monster
        );

        assertEquals(
                GAISMAGORM_ID,
                monster.getMonsterId()
        );

        assertEquals(
                "ガイアデルム",
                monster.getMonsterName()
        );


        System.out.println(
                "モンスター取得："
                + monster.getMonsterId()
                + " / "
                + monster.getMonsterName()
        );
    }


    /*
     * =====================================================
     * 全モンスター一覧
     * =====================================================
     */
    @Test
    void testFindAllMonsters() {

        List<Monster> monsters
                = monsterService.findAll();


        assertNotNull(
                monsters
        );

        assertFalse(
                monsters.isEmpty()
        );


        boolean containsGaismagorm
                = monsters.stream()
                        .anyMatch(
                                monster ->
                                        GAISMAGORM_ID.equals(
                                                monster.getMonsterId()
                                        )
                                        && "ガイアデルム".equals(
                                                monster.getMonsterName()
                                        )
                        );


        assertTrue(
                containsGaismagorm
        );


        System.out.println(
                "モンスター一覧："
                + monsters.size()
                + "体 / ガイアデルム存在確認OK"
        );
    }


    /*
     * =====================================================
     * ガイアデルム全部位取得
     * =====================================================
     *
     * 現在DBには9部位。
     */
    @Test
    void testFindHitzonesByMonsterId() {

        List<MonsterHitzone> hitzones
                = monsterHitzoneService.findByMonsterId(
                        GAISMAGORM_ID
                );


        assertNotNull(
                hitzones
        );

        assertEquals(
                9,
                hitzones.size()
        );


        System.out.println(
                "ガイアデルム肉質："
                + hitzones.size()
                + "部位"
        );


        for (MonsterHitzone hitzone : hitzones) {

            System.out.println(
                    hitzone.getPartName()
                    + "：斬="
                    + hitzone.getSever()
                    + " / 打="
                    + hitzone.getBlunt()
                    + " / 弾="
                    + hitzone.getShot()
                    + " / 火="
                    + hitzone.getFire()
                    + " / 水="
                    + hitzone.getWater()
                    + " / 雷="
                    + hitzone.getThunder()
                    + " / 氷="
                    + hitzone.getIce()
                    + " / 龍="
                    + hitzone.getDragon()
            );
        }
    }


    /*
     * =====================================================
     * 頭部肉質取得
     * =====================================================
     *
     * DB：
     *
     * 頭部
     * 斬70
     * 打72
     * 弾45
     * 火10
     * 水0
     * 雷20
     * 氷0
     * 龍30
     */
    @Test
    void testFindHeadHitzoneById() {

        MonsterHitzone hitzone
                = monsterHitzoneService.findById(
                        GAISMAGORM_HEAD_HITZONE_ID
                );


        assertNotNull(
                hitzone
        );

        assertEquals(
                GAISMAGORM_HEAD_HITZONE_ID,
                hitzone.getHitzoneId()
        );

        assertEquals(
                GAISMAGORM_ID,
                hitzone.getMonsterId()
        );

        assertEquals(
                "頭部",
                hitzone.getPartName()
        );


        assertEquals(
                70,
                hitzone.getSever()
        );

        assertEquals(
                72,
                hitzone.getBlunt()
        );

        assertEquals(
                45,
                hitzone.getShot()
        );


        assertEquals(
                10,
                hitzone.getFire()
        );

        assertEquals(
                0,
                hitzone.getWater()
        );

        assertEquals(
                20,
                hitzone.getThunder()
        );

        assertEquals(
                0,
                hitzone.getIce()
        );

        assertEquals(
                30,
                hitzone.getDragon()
        );


        System.out.println(
                "ガイアデルム頭部："
                + "斬="
                + hitzone.getSever()
                + " / 打="
                + hitzone.getBlunt()
                + " / 弾="
                + hitzone.getShot()
                + " / 火="
                + hitzone.getFire()
                + " / 水="
                + hitzone.getWater()
                + " / 雷="
                + hitzone.getThunder()
                + " / 氷="
                + hitzone.getIce()
                + " / 龍="
                + hitzone.getDragon()
        );
    }


    /*
     * =====================================================
     * 正しい
     *
     * monsterId
     * +
     * hitzoneId
     *
     * の組み合わせ。
     * =====================================================
     */
    @Test
    void testFindByIdAndMonsterIdValid() {

        MonsterHitzone hitzone
                = monsterHitzoneService
                        .findByIdAndMonsterId(
                                GAISMAGORM_HEAD_HITZONE_ID,
                                GAISMAGORM_ID
                        );


        assertNotNull(
                hitzone
        );

        assertEquals(
                "頭部",
                hitzone.getPartName()
        );


        System.out.println(
                "正しいモンスター＋部位：取得成功"
        );
    }


    /*
     * =====================================================
     * 不正な組み合わせ
     * =====================================================
     *
     * hitzoneId=501は
     * monsterId=16のガイアデルム頭部。
     *
     * monsterIdを999999として渡した場合は
     * nullになる。
     */
    @Test
    void testFindByIdAndMonsterIdInvalid() {

        MonsterHitzone hitzone
                = monsterHitzoneService
                        .findByIdAndMonsterId(
                                GAISMAGORM_HEAD_HITZONE_ID,
                                999999
                        );


        assertNull(
                hitzone
        );


        System.out.println(
                "別モンスター＋部位：null 正常"
        );
    }


    /*
     * =====================================================
     * monsterId=null
     * =====================================================
     */
    @Test
    void testNullMonsterId() {

        Monster monster
                = monsterService.findById(
                        null
                );


        List<MonsterHitzone> hitzones
                = monsterHitzoneService
                        .findByMonsterId(
                                null
                        );


        assertNull(
                monster
        );

        assertNotNull(
                hitzones
        );

        assertTrue(
                hitzones.isEmpty()
        );


        System.out.println(
                "monsterId=null：正常"
        );
    }


    /*
     * =====================================================
     * hitzoneId=null
     * =====================================================
     */
    @Test
    void testNullHitzoneId() {

        MonsterHitzone hitzone
                = monsterHitzoneService.findById(
                        null
                );


        MonsterHitzone checkedHitzone
                = monsterHitzoneService
                        .findByIdAndMonsterId(
                                null,
                                GAISMAGORM_ID
                        );


        assertNull(
                hitzone
        );

        assertNull(
                checkedHitzone
        );


        System.out.println(
                "hitzoneId=null：正常"
        );
    }
}