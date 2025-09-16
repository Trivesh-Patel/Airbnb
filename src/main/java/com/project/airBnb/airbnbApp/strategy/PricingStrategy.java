package com.project.airBnb.airbnbApp.strategy;

import com.project.airBnb.airbnbApp.entity.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculatePrice(Inventory inventory);
}
