package com.arminzheng.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SoaProperties
 *
 * @author ape
 * @since 2022.01.08
 */
@ConfigurationProperties(prefix = "soa")
public class SoaProperties {

    /** test、dev、prod */
    private String active = "dev";

    private String address;

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
