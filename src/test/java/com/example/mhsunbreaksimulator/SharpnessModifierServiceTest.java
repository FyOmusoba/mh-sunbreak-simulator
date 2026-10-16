package com.example.mhsunbreaksimulator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.example.mhsunbreaksimulator.dto.SharpnessModifier;
import com.example.mhsunbreaksimulator.service.SharpnessModifierService;

public class SharpnessModifierServiceTest {

    private final SharpnessModifierService sharpnessModifierService
            = new SharpnessModifierService();


    @Test
    void testPurpleSharpness() {

        SharpnessModifier result
                = sharpnessModifierService.calculate(
                        "紫"
                );


        assertEquals(
                "紫",
                result.getSharpnessColor()
        );


        assertEquals(
                0,
                result.getPhysicalMultiplier()
                        .compareTo(
                                new BigDecimal("1.39")
                        )
        );


        assertEquals(
                0,
                result.getElementMultiplier()
                        .compareTo(
                                new BigDecimal("1.25")
                        )
        );
    }


    @Test
    void testWhiteSharpness() {

        SharpnessModifier result
                = sharpnessModifierService.calculate(
                        "白"
                );


        assertEquals(
                0,
                result.getPhysicalMultiplier()
                        .compareTo(
                                new BigDecimal("1.32")
                        )
        );


        assertEquals(
                0,
                result.getElementMultiplier()
                        .compareTo(
                                new BigDecimal("1.15")
                        )
        );
    }


    @Test
    void testBlueSharpness() {

        SharpnessModifier result
                = sharpnessModifierService.calculate(
                        "青"
                );


        assertEquals(
                0,
                result.getPhysicalMultiplier()
                        .compareTo(
                                new BigDecimal("1.20")
                        )
        );


        assertEquals(
                0,
                result.getElementMultiplier()
                        .compareTo(
                                new BigDecimal("1.0625")
                        )
        );
    }


    @Test
    void testGreenSharpness() {

        SharpnessModifier result
                = sharpnessModifierService.calculate(
                        "緑"
                );


        assertEquals(
                0,
                result.getPhysicalMultiplier()
                        .compareTo(
                                new BigDecimal("1.05")
                        )
        );


        assertEquals(
                0,
                result.getElementMultiplier()
                        .compareTo(
                                BigDecimal.ONE
                        )
        );
    }


    @Test
    void testYellowSharpness() {

        SharpnessModifier result
                = sharpnessModifierService.calculate(
                        "黄"
                );


        assertEquals(
                0,
                result.getPhysicalMultiplier()
                        .compareTo(
                                BigDecimal.ONE
                        )
        );


        assertEquals(
                0,
                result.getElementMultiplier()
                        .compareTo(
                                new BigDecimal("0.75")
                        )
        );
    }


    @Test
    void testOrangeSharpness() {

        SharpnessModifier result
                = sharpnessModifierService.calculate(
                        "橙"
                );


        assertEquals(
                0,
                result.getPhysicalMultiplier()
                        .compareTo(
                                new BigDecimal("0.75")
                        )
        );


        assertEquals(
                0,
                result.getElementMultiplier()
                        .compareTo(
                                new BigDecimal("0.50")
                        )
        );
    }


    @Test
    void testRedSharpness() {

        SharpnessModifier result
                = sharpnessModifierService.calculate(
                        "赤"
                );


        assertEquals(
                0,
                result.getPhysicalMultiplier()
                        .compareTo(
                                new BigDecimal("0.50")
                        )
        );


        assertEquals(
                0,
                result.getElementMultiplier()
                        .compareTo(
                                new BigDecimal("0.25")
                        )
        );
    }


    @Test
    void testNullSharpness() {

        SharpnessModifier result
                = sharpnessModifierService.calculate(
                        null
                );


        assertEquals(
                BigDecimal.ONE,
                result.getPhysicalMultiplier()
        );


        assertEquals(
                BigDecimal.ONE,
                result.getElementMultiplier()
        );
    }
}