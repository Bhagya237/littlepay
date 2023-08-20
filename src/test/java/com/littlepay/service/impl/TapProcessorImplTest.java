package com.littlepay.service.impl;

import com.littlepay.dto.Tap;
import com.littlepay.dto.Trip;
import com.littlepay.exceptions.TapSequenceException;
import com.littlepay.service.FareCalculator;
import com.littlepay.util.Constants;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class TapProcessorImplTest {

    @Mock
    private FareCalculator fareCalculator;

    @InjectMocks
    private TapProcessorImpl tapProcessor;

    @Test
    public void trip_bind_values() {

        Mockito.when(fareCalculator.calculateFare("Stop1", "Stop2")).thenReturn(new BigDecimal("20.00"));

        String panCustomerOne = UUID.randomUUID().toString();
        DateTime now = new DateTime();
        Tap tap1 = Tap.builder().pan(panCustomerOne).stopId("Stop1").tapType(Constants.TAP_TYPE.ON).dateTime(now).companyId("Com1").busId("Bus2").build();
        Tap tap2 = Tap.builder().pan(panCustomerOne).stopId("Stop2").tapType(Constants.TAP_TYPE.OFF).dateTime(now.plusSeconds(20)).companyId("Com1").busId("Bus2").build();

        List<Trip> trips = tapProcessor.processTaps(List.of(tap2, tap1));

        assertEquals(1, trips.size());

        Trip trip = trips.get(0);

        assertEquals(panCustomerOne, trip.getPan());
        assertEquals(now, trip.getStarted());
        assertEquals(now.plusSeconds(20), trip.getFinished());
        assertEquals("Stop1", trip.getFromStopId());
        assertEquals("Stop2", trip.getToStopId());
        assertEquals("Com1", trip.getCompanyId());
        assertEquals("Bus2", trip.getBusId());
        assertEquals(Constants.TRIP_STATUS.COMPLETED, trip.getStatus());
        assertEquals(new BigDecimal("20.00"), trip.getChangeAmount());

        Mockito.verify(fareCalculator, times(1)).calculateFare("Stop1", "Stop2");

    }

    @Test()
    public void correct_tap_sequence_in_input() {

        String panCustomerOne = UUID.randomUUID().toString();

        DateTime now = new DateTime();
        Tap tap1 = Tap.builder().pan(panCustomerOne).stopId("Stop1").tapType(Constants.TAP_TYPE.ON).dateTime(now).companyId("Com1").busId("Bus2").build();

        // tap off happening before tap on
        Tap tap2 = Tap.builder().pan(panCustomerOne).stopId("Stop2").tapType(Constants.TAP_TYPE.OFF).dateTime(now.minusSeconds(20)).companyId("Com1").busId("Bus2").build();


        Exception exception = assertThrows(TapSequenceException.class, () -> {
            tapProcessor.processTaps(List.of(tap1, tap2));
        });
        assertTrue(exception.getMessage().contains("Invalid tap sequence"));
    }

    @Test
    public void calculate_trip_duration() {

        Mockito.when(fareCalculator.calculateFare("Stop1", "Stop2")).thenReturn(new BigDecimal("22.00"));
        String panCustomerOne = UUID.randomUUID().toString();
        DateTime now = new DateTime();
        Tap tap1 = Tap.builder().pan(panCustomerOne).stopId("Stop1").tapType(Constants.TAP_TYPE.ON).dateTime(now).companyId("Com1").busId("Bus2").build();
        Tap tap2 = Tap.builder().pan(panCustomerOne).stopId("Stop2").tapType(Constants.TAP_TYPE.OFF).dateTime(now.plusSeconds(20)).companyId("Com1").busId("Bus2").build();

        List<Trip> trips = tapProcessor.processTaps(List.of(tap1, tap2));

        assertEquals(1, trips.size());
        assertEquals((Integer) 20, trips.get(0).getDuration());
        Mockito.verify(fareCalculator, times(1)).calculateFare("Stop1", "Stop2");
    }

    @Test
    public void calculate_incomplete_trip_duration() {

        Mockito.when(fareCalculator.calculateFare("Stop1")).thenReturn(new BigDecimal("22.00"));
        String panCustomerOne = UUID.randomUUID().toString();
        DateTime now = new DateTime();
        Tap tap1 = Tap.builder().pan(panCustomerOne).stopId("Stop1").tapType(Constants.TAP_TYPE.ON).dateTime(now).companyId("Com1").busId("Bus2").build();

        List<Trip> trips = tapProcessor.processTaps(List.of(tap1));

        assertEquals(1, trips.size());
        assertEquals((Integer) 0, trips.get(0).getDuration());
        assertNull(trips.get(0).getFinished());
        Mockito.verify(fareCalculator, times(1)).calculateFare("Stop1");
    }

    @Test
    public void calculate_CANCELLED_trip_type() {

        Mockito.when(fareCalculator.calculateFare("Stop1", "Stop1")).thenReturn(new BigDecimal("20.00"));
        String panCustomerOne = UUID.randomUUID().toString();
        DateTime now = new DateTime();
        Tap tap1 = Tap.builder().pan(panCustomerOne).stopId("Stop1").tapType(Constants.TAP_TYPE.ON).dateTime(now).companyId("Com1").busId("Bus2").build();
        Tap tap2 = Tap.builder().pan(panCustomerOne).stopId("Stop1").tapType(Constants.TAP_TYPE.OFF).dateTime(now.plusSeconds(20)).companyId("Com1").busId("Bus2").build();


        List<Trip> trips = tapProcessor.processTaps(List.of(tap1, tap2));

        assertEquals(1, trips.size());
        assertEquals(Constants.TRIP_STATUS.CANCELLED, trips.get(0).getStatus());
        Mockito.verify(fareCalculator, times(1)).calculateFare("Stop1", "Stop1");


    }

    @Test
    public void calculate_COMPLETED_trip_type() {

        Mockito.when(fareCalculator.calculateFare("Stop1", "Stop2")).thenReturn(new BigDecimal("20.00"));

        String panCustomerOne = UUID.randomUUID().toString();
        DateTime now = new DateTime();
        Tap tap1 = Tap.builder().pan(panCustomerOne).stopId("Stop1").tapType(Constants.TAP_TYPE.ON).dateTime(now).companyId("Com1").busId("Bus2").build();
        Tap tap2 = Tap.builder().pan(panCustomerOne).stopId("Stop2").tapType(Constants.TAP_TYPE.OFF).dateTime(now.plusSeconds(20)).companyId("Com1").busId("Bus2").build();

        List<Trip> trips = tapProcessor.processTaps(List.of(tap1, tap2));

        assertEquals(1, trips.size());
        assertEquals(Constants.TRIP_STATUS.COMPLETED, trips.get(0).getStatus());
        Mockito.verify(fareCalculator, times(1)).calculateFare("Stop1", "Stop2");
    }


    @Test
    public void calculate_INCOMPLETE_trip_type() {

        Mockito.when(fareCalculator.calculateFare("Stop1")).thenReturn(new BigDecimal("20.00"));
        String panCustomerOne = UUID.randomUUID().toString();
        DateTime now = new DateTime();
        Tap tap1 = Tap.builder().pan(panCustomerOne).stopId("Stop1").tapType(Constants.TAP_TYPE.ON).dateTime(now).companyId("Com1").busId("Bus2").build();

        List<Trip> trips = tapProcessor.processTaps(List.of(tap1));

        assertEquals(1, trips.size());
        assertEquals(Constants.TRIP_STATUS.INCOMPLETE, trips.get(0).getStatus());
        Mockito.verify(fareCalculator, times(1)).calculateFare("Stop1");
    }


    @Test
    public void single_customer_multiple_bus_trips_types() {


        Mockito.when(fareCalculator.calculateFare("Stop1", "Stop2")).thenReturn(new BigDecimal("20.00"));
        Mockito.when(fareCalculator.calculateFare("Stop1")).thenReturn(new BigDecimal("20.00"));

        String panCustomerOne = UUID.randomUUID().toString();

        DateTime now = new DateTime();
        Tap tap1 = Tap.builder().pan(panCustomerOne).stopId("Stop1").tapType(Constants.TAP_TYPE.ON).dateTime(now).companyId("Com1").busId("Bus2").build();
        Tap tap2 = Tap.builder().pan(panCustomerOne).stopId("Stop2").tapType(Constants.TAP_TYPE.OFF).dateTime(now.plusSeconds(20)).companyId("Com1").busId("Bus2").build();


        DateTime now2 = new DateTime().plusSeconds(5);
        Tap tap3 = Tap.builder().pan(panCustomerOne).stopId("Stop1").tapType(Constants.TAP_TYPE.ON).dateTime(now2).companyId("Com1").busId("Bus1").build();
        Tap tap4 = Tap.builder().pan(panCustomerOne).stopId("Stop1").tapType(Constants.TAP_TYPE.OFF).dateTime(now2.plusSeconds(0)).companyId("Com1").busId("Bus1").build();


        DateTime now3 = new DateTime().plusSeconds(10);
        Tap tap5 = Tap.builder().pan(panCustomerOne).stopId("Stop1").tapType(Constants.TAP_TYPE.ON).dateTime(now3).companyId("Com1").busId("Bus3").build();


        List<Trip> trips = tapProcessor.processTaps(List.of(tap1, tap2, tap5, tap3, tap4));

        assertEquals(3, trips.size());
        assertEquals(Constants.TRIP_STATUS.COMPLETED, trips.get(0).getStatus());
        assertEquals(Constants.TRIP_STATUS.CANCELLED, trips.get(1).getStatus());
        assertEquals(Constants.TRIP_STATUS.INCOMPLETE, trips.get(2).getStatus());
        Mockito.verify(fareCalculator, times(1)).calculateFare("Stop1", "Stop2");
        Mockito.verify(fareCalculator, times(1)).calculateFare("Stop1", "Stop1");
        Mockito.verify(fareCalculator, times(1)).calculateFare("Stop1");

    }


    @Test
    public void multiple_customer_multiple_bus_trips_types() {


        Mockito.when(fareCalculator.calculateFare("Stop1", "Stop2")).thenReturn(new BigDecimal("20.00"));
        Mockito.when(fareCalculator.calculateFare("Stop1", "Stop3")).thenReturn(new BigDecimal("21.00"));
        Mockito.when(fareCalculator.calculateFare("Stop1")).thenReturn(new BigDecimal("20.00"));

        String panCustomerOne = UUID.randomUUID().toString();
        String panCustomerTwo = UUID.randomUUID().toString();

        DateTime now = new DateTime();
        Tap tap1 = Tap.builder().pan(panCustomerOne).stopId("Stop1").tapType(Constants.TAP_TYPE.ON).dateTime(now).companyId("Com1").busId("Bus2").build();
        Tap tap2 = Tap.builder().pan(panCustomerOne).stopId("Stop2").tapType(Constants.TAP_TYPE.OFF).dateTime(now.plusSeconds(20)).companyId("Com1").busId("Bus2").build();


        DateTime now2 = new DateTime().plusSeconds(5);
        Tap tap3 = Tap.builder().pan(panCustomerOne).stopId("Stop1").tapType(Constants.TAP_TYPE.ON).dateTime(now2).companyId("Com1").busId("Bus1").build();
        Tap tap4 = Tap.builder().pan(panCustomerOne).stopId("Stop1").tapType(Constants.TAP_TYPE.OFF).dateTime(now2.plusSeconds(0)).companyId("Com1").busId("Bus1").build();


        DateTime now3 = new DateTime().plusSeconds(10);
        Tap tap5 = Tap.builder().pan(panCustomerOne).stopId("Stop1").tapType(Constants.TAP_TYPE.ON).dateTime(now3).companyId("Com1").busId("Bus3").build();


        DateTime now4 = new DateTime().plusSeconds(15);
        Tap tap6 = Tap.builder().pan(panCustomerTwo).stopId("Stop1").tapType(Constants.TAP_TYPE.ON).dateTime(now4).companyId("Com1").busId("Bus3").build();
        Tap tap7 = Tap.builder().pan(panCustomerTwo).stopId("Stop3").tapType(Constants.TAP_TYPE.OFF).dateTime(now4.plusSeconds(20)).companyId("Com1").busId("Bus3").build();


        List<Trip> trips = tapProcessor.processTaps(List.of(tap1, tap2, tap5, tap6, tap3, tap4, tap7));

        assertEquals(4, trips.size());
        assertEquals(Constants.TRIP_STATUS.COMPLETED, trips.get(0).getStatus());
        assertEquals(Constants.TRIP_STATUS.CANCELLED, trips.get(1).getStatus());
        assertEquals(Constants.TRIP_STATUS.INCOMPLETE, trips.get(2).getStatus());
        assertEquals(Constants.TRIP_STATUS.COMPLETED, trips.get(3).getStatus());
        Mockito.verify(fareCalculator, times(1)).calculateFare("Stop1", "Stop2");
        Mockito.verify(fareCalculator, times(1)).calculateFare("Stop1", "Stop1");
        Mockito.verify(fareCalculator, times(1)).calculateFare("Stop1", "Stop3");
        Mockito.verify(fareCalculator, times(1)).calculateFare("Stop1");

    }


}