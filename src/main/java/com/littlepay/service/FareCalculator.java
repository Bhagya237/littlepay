package com.littlepay.service;

import com.littlepay.exceptions.FareConfigException;

import java.math.BigDecimal;

public interface FareCalculator {

    void addFare(String tapOn, String tapOff, BigDecimal price);
    BigDecimal calculateFare(String tapOn, String tapOff) throws FareConfigException;

    BigDecimal calculateFare(String tapOn) throws FareConfigException;
}
