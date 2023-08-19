package com.littlepay.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FareCalculatorImpl implements FareCalculator{

    private final Map<String, Map<String, BigDecimal>> tripPrices = new HashMap<>();

    public FareCalculatorImpl() {
        addFare("Stop1", "Stop2", new BigDecimal("3.25"));
        addFare("Stop2", "Stop3", new BigDecimal("5.50"));
        addFare("Stop1", "Stop3", new BigDecimal("3.70"));
    }

    public void addFare(String tapOn, String tapOff, BigDecimal price) {
        tripPrices.computeIfAbsent(tapOn.toUpperCase(), k -> new HashMap<>()).put(tapOff.toUpperCase(), price);
        tripPrices.computeIfAbsent(tapOff.toUpperCase(), k -> new HashMap<>()).put(tapOn.toUpperCase(), price);
    }

    @Override
    public BigDecimal calculateFare(String tapOn, String tapOff) {

        if (tapOn.equalsIgnoreCase(tapOff)) {
            return BigDecimal.ZERO;
        }

        if (!tripPrices.containsKey(tapOn.toUpperCase()) || !tripPrices.get(tapOn.toUpperCase()).containsKey(tapOff.toUpperCase())) {
            throw new RuntimeException();
        }
        return tripPrices.get(tapOn.toUpperCase()).get(tapOff.toUpperCase());
    }

    @Override
    public BigDecimal calculateFare(String tapOn) {

        if (!tripPrices.containsKey(tapOn.toUpperCase())) {
            throw new RuntimeException();
        }
        Optional<BigDecimal> maxValue = tripPrices.get(tapOn.toUpperCase()).values().stream().max(Comparator.naturalOrder());
        return maxValue.orElse(BigDecimal.ZERO);
    }

}
