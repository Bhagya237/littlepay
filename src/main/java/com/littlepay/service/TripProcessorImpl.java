package com.littlepay.service;

import com.littlepay.dto.Tap;
import com.littlepay.dto.Trip;
import com.littlepay.exceptions.FareConfigException;
import com.littlepay.exceptions.TapSequenceException;
import com.littlepay.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripProcessorImpl implements TripProcessor {

    private final FareCalculator fareCalculator;
    private final Map<String, Tap> tapHistory = new HashMap<>();

    @Override
    public List<Trip> processTrips(List<Tap> taps) {
        log.info("Processing trips");

        // Calculate complete and cancelled trips
        List<Trip> completeAndCancelledTrips = calculateCompleteAndCancelledTrips(taps);

        // Calculate incomplete trips
        List<Trip> incompleteTrips = calculateIncompleteTrips();


        // Concatenate and sort complete, cancelled, and incomplete trips
        // sort by tap start time to bring taps in order
        return Stream.concat(completeAndCancelledTrips.stream(), incompleteTrips.stream())
                .sorted(Comparator.comparing(Trip::getStarted))
                .collect(Collectors.toList());
    }

    private List<Trip> calculateCompleteAndCancelledTrips(List<Tap> taps) {
        log.info("Calculating complete and cancelled trips");
        return taps.stream().sorted(Comparator.comparing(Tap::getDateTime)) // sort by tap start time to bring taps in order
                .map(this::processTrip)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<Trip> calculateIncompleteTrips() {
        log.info("Calculating incomplete trips");
        return tapHistory.values().stream()
                .map(tap -> buildTrip(tap, null))
                .collect(Collectors.toList());
    }

    private Trip processTrip(Tap tap) throws FareConfigException {
        log.info("Processing trip for tap: {}", tap);
        String key = generateKey(tap.getPan(), tap.getBusId(), tap.getCompanyId());
        if (tapHistory.containsKey(key)) {
            Tap firstTap = tapHistory.remove(key);

            if(firstTap.getTapType().equals(Constants.TAP_TYPE.OFF)) {
                // invalid data coming from the file
                throw new TapSequenceException("Invalid tap sequence");
            }

            return buildTrip(firstTap, tap);
        } else {
            tapHistory.put(key, tap);
        }
        return null;
    }

    private Trip buildTrip(Tap start, Tap end) throws FareConfigException {
        Optional<Tap> tapEnd = Optional.ofNullable(end);
        int duration = 0;
        Constants.TRIP_STATUS tripStatus;
        BigDecimal fare;

        if (tapEnd.isPresent()) {
            duration = calculateDuration(start.getDateTime(), end.getDateTime());
            fare = fareCalculator.calculateFare(start.getStopId(), end.getStopId());
            tripStatus = calculateTripType(start, end);
        } else {
            // no duration in seconds for incomplete trips
            fare = fareCalculator.calculateFare(start.getStopId());
            tripStatus = calculateTripType(start);
        }
        Trip trip = Trip.builder()
                .started(start.getDateTime())
                .finished(tapEnd.isPresent() ? end.getDateTime() : null)
                .duration(duration)
                .fromStopId(start.getStopId())
                .toStopId(tapEnd.isPresent() ? end.getStopId() : null)
                .changeAmount(fare)
                .companyId(start.getCompanyId())
                .busId(start.getBusId())
                .pan(start.getPan())
                .status(tripStatus)
                .build();
        log.info("Building on start {} end {} to {}", start, end, trip);
        return trip;
    }

    private Constants.TRIP_STATUS calculateTripType(Tap one, Tap two) {
        if (two == null) {
            return Constants.TRIP_STATUS.INCOMPLETE;
        } else if (one.getStopId().equals(two.getStopId())) {
            return Constants.TRIP_STATUS.CANCELLED;
        } else {
            return Constants.TRIP_STATUS.COMPLETED;
        }
    }

    private Constants.TRIP_STATUS calculateTripType(Tap one) {
        return calculateTripType(one, null);
    }

    private int calculateDuration(DateTime start, DateTime end) {
        Seconds seconds = Seconds.secondsBetween(start, end);
        return seconds.getSeconds();
    }

    private String generateKey(String pan, String busId, String companyId) {
        return (pan + busId + companyId).toUpperCase();
    }
}
