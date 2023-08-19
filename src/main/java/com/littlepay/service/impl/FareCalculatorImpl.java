package com.littlepay.service.impl;

import com.littlepay.exceptions.FareConfigException;
import com.littlepay.service.FareCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * Implementation for calculating fares based on tap on and tap off points.
 */
@Service
@Slf4j
public class FareCalculatorImpl implements FareCalculator {

    // Store fare calculation rules for tap on and tap off points
    private final Map<String, Map<String, BigDecimal>> tripPrices = new HashMap<>();

    /**
     * Constructor that initializes fare calculation rules.
     */
    public FareCalculatorImpl() {
        log.info("Initializing fare calculation values.");
        // Define fare rules for various tap on and tap off points
        addFare("Stop1", "Stop2", new BigDecimal("3.25"));
        addFare("Stop2", "Stop3", new BigDecimal("5.50"));
        addFare("Stop1", "Stop3", new BigDecimal("3.70"));
        log.debug("Fare calculation values initialized. {}", tripPrices);
    }

    /**
     * {@inheritDoc}
     */
    public void addFare(String tapOn, String tapOff, BigDecimal price) {
        // Add fare rules for both directions of travel
        tripPrices.computeIfAbsent(tapOn.toUpperCase(), k -> new HashMap<>()).put(tapOff.toUpperCase(), price);
        tripPrices.computeIfAbsent(tapOff.toUpperCase(), k -> new HashMap<>()).put(tapOn.toUpperCase(), price);
    }

    /**
     * {@inheritDoc}
     * @throws FareConfigException If fare rules are not defined for the given tap points.
     */
    @Override
    public BigDecimal calculateFare(String tapOn, String tapOff) {

        log.info("Calculating fare for tap on {} and tap off {}", tapOn, tapOff);
        // Check if tap on and tap off points are the same
        if (tapOn.equalsIgnoreCase(tapOff)) {
            return BigDecimal.ZERO;
        }

        // Check if fare rules exist for the given points
        if (!tripPrices.containsKey(tapOn.toUpperCase()) || !tripPrices.get(tapOn.toUpperCase()).containsKey(tapOff.toUpperCase())) {
            throw new FareConfigException("Fare rules not defined for the given tap points." + tapOn + " " + tapOff);
        }
        return tripPrices.get(tapOn.toUpperCase()).get(tapOff.toUpperCase());
    }

    /**
     * {@inheritDoc}
     * @throws FareConfigException If fare rules are not defined for the given tap on point.
     */
    @Override
    public BigDecimal calculateFare(String tapOn) {
        log.info("Calculating fare for tap on {}", tapOn);
        // Check if fare rules exist for the given tap on point
        if (!tripPrices.containsKey(tapOn.toUpperCase())) {
            throw new FareConfigException("Fare rules not defined for the given tap on point " + tapOn);
        }
        // return max fare for the given tap on point
        Optional<BigDecimal> maxValue = tripPrices.get(tapOn.toUpperCase()).values().stream().max(Comparator.naturalOrder());
        return maxValue.orElse(BigDecimal.ZERO);
    }

}
