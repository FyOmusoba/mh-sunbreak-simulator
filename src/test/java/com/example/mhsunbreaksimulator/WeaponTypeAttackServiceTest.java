package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.mhsunbreaksimulator.entity.WeaponTypeAttack;
import com.example.mhsunbreaksimulator.service.WeaponTypeAttackService;

@SpringBootTest
public class WeaponTypeAttackServiceTest {

    @Autowired
    private WeaponTypeAttackService weaponTypeAttackService;


    @Test
    void testFindByWeaponType() {

        String weaponType
                = "大剣";


        WeaponTypeAttack attack
                = weaponTypeAttackService.findByWeaponType(
                        weaponType
                );


        assertNotNull(
                attack
        );


        assertEquals(
                "大剣",
                attack.getWeaponType()
        );


        assertEquals(
                "通常溜め斬り（溜めなし）",
                attack.getBaseAttackName()
        );


        assertEquals(
                0,
                attack.getMotionValue().compareTo(
                        new BigDecimal("48")
                )
        );


        assertEquals(
                0,
                attack.getElementModifier().compareTo(
                        BigDecimal.ONE
                )
        );


        assertEquals(
                1,
                attack.getHitCount()
        );


        assertEquals(
                "斬",
                attack.getAttackProperty()
        );


        assertNotNull(
                attack.getNote()
        );


        System.out.println(
                "武器種="
                + attack.getWeaponType()
                + " / 基準攻撃="
                + attack.getBaseAttackName()
                + " / MV="
                + attack.getMotionValue()
                + " / 属性補正="
                + attack.getElementModifier()
                + " / hit数="
                + attack.getHitCount()
                + " / 攻撃属性="
                + attack.getAttackProperty()
                + " / 備考="
                + attack.getNote()
        );
    }


    @Test
    void testNullWeaponType() {

        WeaponTypeAttack attack
                = weaponTypeAttackService.findByWeaponType(
                        null
                );


        assertNull(
                attack
        );
    }


    @Test
    void testBlankWeaponType() {

        WeaponTypeAttack attack
                = weaponTypeAttackService.findByWeaponType(
                        "   "
                );


        assertNull(
                attack
        );
    }
}