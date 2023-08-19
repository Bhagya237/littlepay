package com.littlepay.service;

import java.math.BigDecimal;

public interface FareCalculator {

    void addFare(String tapOn, String tapOff, BigDecimal price);

    BigDecimal calculateFare(String tapOn, String tapOff);

    BigDecimal calculateFare(String tapOn);
}
