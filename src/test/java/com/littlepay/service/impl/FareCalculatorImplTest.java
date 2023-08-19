package com.littlepay.service.impl;

import com.littlepay.exceptions.FareConfigException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

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
    public void calculate_fare_on_cancel_trip() throws FareConfigException {
        BigDecimal fare = calculator.calculateFare("ST1", "ST1");
        assertEquals(BigDecimal.ZERO, fare);
    }


    @Test
    public void calculate_fare_between_tow_stops() throws FareConfigException {
        BigDecimal fare = calculator.calculateFare("ST1", "ST2");
        assertEquals(new BigDecimal("10.0909"), fare);
    }

    @Test
    public void calculate_fare_to_return_max_amount_on_tap_on_only() throws FareConfigException {
        BigDecimal fare = calculator.calculateFare("ST1");
        assertEquals(new BigDecimal("17.0909"), fare);
    }

    @Test()
    public void calculate_fare_if_tap_on_and_tap_off_fare_are_not_defined() {

        Exception exception = assertThrows(FareConfigException.class, () -> {
            calculator.calculateFare("ST5", "ST3");
        });
        assertTrue(exception.getMessage().contains("Fare rules not defined for the given tap points."));
    }

    @Test()
    public void calculate_fare_if_tap_on_fare_is_not_defined() {

        Exception exception = assertThrows(FareConfigException.class, () -> {
            calculator.calculateFare("ST55");
        });
        assertTrue(exception.getMessage().contains("Fare rules not defined for the given tap on point."));
    }
}