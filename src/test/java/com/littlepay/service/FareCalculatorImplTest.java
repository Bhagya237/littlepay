package com.littlepay.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FareCalculatorImplTest {

    @InjectMocks
    private FareCalculatorImpl calculator;

    @Before
    public void setupFareValues() {
        calculator.addFare("ST1", "ST2", new BigDecimal("10.0909"));
        calculator.addFare("ST1", "ST3", new BigDecimal("12.0909"));
        calculator.addFare("ST2", "ST3", new BigDecimal("11.0909"));
        calculator.addFare("ST1", "ST5", new BigDecimal("17.0909"));
    }

    @Test
    public void calculate_fare_on_cancel_trip() {
        BigDecimal fare = calculator.calculateFare("ST1", "ST1");
        assertEquals(BigDecimal.ZERO, fare);
    }


    @Test
    public void calculate_fare_between_tow_stops() {
        BigDecimal fare = calculator.calculateFare("ST1", "ST2");
        assertEquals(new BigDecimal("10.0909"), fare);
    }

    @Test
    public void calculate_fare_to_return_max_amount_on_tap_on_only() {
        BigDecimal fare = calculator.calculateFare("ST1");
        assertEquals(new BigDecimal("17.0909"), fare);
    }
}