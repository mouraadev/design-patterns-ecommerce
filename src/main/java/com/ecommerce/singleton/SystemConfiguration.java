package com.ecommerce.singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SystemConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemConfiguration.class);
    private static final SystemConfiguration INSTANCE = new SystemConfiguration();

    private final String storeName;
    private final double defaultTaxRate;

    private SystemConfiguration() {
        this.storeName = "TechShop Brazil";
        this.defaultTaxRate = 0.08;
    }

    public static SystemConfiguration getInstance() {
        return INSTANCE;
    }

    public String getStoreName() {
        return storeName;
    }

    public double getDefaultTaxRate() {
        return defaultTaxRate;
    }

    public void log(String message) {
        LOGGER.info("[{}] {}", storeName, message);
    }
}
