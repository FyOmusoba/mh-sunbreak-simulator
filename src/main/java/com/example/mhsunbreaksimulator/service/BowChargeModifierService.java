package com.example.mhsunbreaksimulator.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.mhsunbreaksimulator.dto.BowChargeModifier;

@Service
public class BowChargeModifierService {

    public BowChargeModifier calculate(
            Integer chargeLevel) {

        BowChargeModifier modifier
                = new BowChargeModifier();

        if (chargeLevel == null) {
            return modifier;
        }

        switch (chargeLevel) {

        case 1:

            modifier.setPhysicalMul(
                    new BigDecimal("0.65")
            );

            modifier.setElementMul(
                    new BigDecimal("0.80")
            );

            break;


        case 2:

            modifier.setPhysicalMul(
                    new BigDecimal("1.00")
            );

            modifier.setElementMul(
                    new BigDecimal("1.00")
            );

            break;


        case 3:

            modifier.setPhysicalMul(
                    new BigDecimal("1.25")
            );

            modifier.setElementMul(
                    new BigDecimal("1.10")
            );

            break;


        case 4:

            modifier.setPhysicalMul(
                    new BigDecimal("1.35")
            );

            modifier.setElementMul(
                    new BigDecimal("1.20")
            );

            break;


        default:

            /*
             * 不正な溜め段階の場合は
             * 補正なし（1.0倍）のまま返す
             */
            break;
        }

        return modifier;
    }
}