package com.biblioteca.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "biblioteca.multa")
public class MultaProperties {

    private BigDecimal valorPorDia = new BigDecimal("2.00");

    public BigDecimal getValorPorDia() {
        return valorPorDia;
    }

    public void setValorPorDia(BigDecimal valorPorDia) {
        this.valorPorDia = valorPorDia;
    }
}
