package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.mhsunbreaksimulator.entity.BowgunAmmo;
import com.example.mhsunbreaksimulator.service.BowgunAmmoService;

@SpringBootTest
public class BowgunAmmoServiceTest {

    @Autowired
    private BowgunAmmoService bowgunAmmoService;


    @Test
    void testFindByWeaponId() {

        Integer weaponId = 851;

        List<BowgunAmmo> ammoList
                = bowgunAmmoService.findByWeaponId(
                        weaponId
                );


        assertNotNull(
                ammoList
        );

        assertFalse(
                ammoList.isEmpty()
        );


        System.out.println(
                "取得件数="
                + ammoList.size()
        );


        for (BowgunAmmo ammo : ammoList) {

            assertEquals(
                    weaponId,
                    ammo.getWeaponId()
            );

            System.out.println(
                    "ID="
                    + ammo.getBowgunAmmoId()
                    + " / 弾="
                    + ammo.getAmmoType()
                    + " / Lv="
                    + ammo.getAmmoLevel()
                    + " / 装填数="
                    + ammo.getCapacity()
                    + " / 速射="
                    + ammo.getRapidFire()
                    + " / 反動="
                    + ammo.getRecoil()
                    + " / リロード="
                    + ammo.getReload()
                    + " / 移動射撃="
                    + ammo.getMovingShot()
                    + " / 移動リロード="
                    + ammo.getMovingReload()
                    + " / 単発自動装填="
                    + ammo.getSingleAuto()
            );
        }
    }
}