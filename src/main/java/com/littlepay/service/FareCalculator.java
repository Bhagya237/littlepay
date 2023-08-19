package com.littlepay.service;

import java.math.BigDecimal;

/**
 *  Calculating fares based on tap on and tap off points.
 */
public interface FareCalculator {

    /**
     * Adds a fare calculation rule for a given tap on and tap off points.
     *
     * @param tapOn  The tap on point.
     * @param tapOff The tap off point.
     * @param price  The fare price for the trip.
     */
    void addFare(String tapOn, String tapOff, BigDecimal price);

    /**
     * Calculates the fare based on tap on and tap off points.
     *
     * @param tapOn  The tap on point.
     * @param tapOff The tap off point.
     * @return The calculated fare.
     */
    BigDecimal calculateFare(String tapOn, String tapOff);

    /**
     * Calculates the maximum fare based on the tap on point.
     *
     * @param tapOn The tap on point.
     * @return The maximum calculated fare.
     */
    BigDecimal calculateFare(String tapOn);
}
