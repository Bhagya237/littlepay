package com.littlepay.service.impl;

import com.littlepay.dto.Tap;
import com.littlepay.dto.Trip;
import com.littlepay.exceptions.FareConfigException;
import com.littlepay.exceptions.TapSequenceException;
import com.littlepay.service.FareCalculator;
import com.littlepay.service.TapProcessor;
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
public class TripProcessorImpl implements TapProcessor {

    private final FareCalculator fareCalculator;
    private final Map<String, Tap> tapHistory = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Trip> processTaps(List<Tap> taps) {
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

    /**
     * Calculates complete and cancelled trips from a list of taps.
     *
     * @param taps The list of taps to process.
     * @return A list of complete and cancelled trips.
     */
    private List<Trip> calculateCompleteAndCancelledTrips(List<Tap> taps) {
        log.info("Calculating complete and cancelled trips");
        return taps.stream().sorted(Comparator.comparing(Tap::getDateTime)) // sort by tap start time to bring taps in order
                .map(this::processTrip)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Calculates incomplete trips based on tap history.
     *
     * @return A list of incomplete trips.
     */
    private List<Trip> calculateIncompleteTrips() {
        log.info("Calculating incomplete trips");
        List<Trip> trips = tapHistory.values().stream()
                .map(tap -> buildTrip(tap, null))
                .collect(Collectors.toList());

        // clear the tap history
        tapHistory.clear();
        return trips;
    }

    /**
     * Processes a single tap and builds a corresponding Trip.
     *
     * @param tap The tap to process.
     * @return The constructed Trip object, or null if tap history is not available.
     * @throws FareConfigException If the fare configuration is invalid.
     */
    private Trip processTrip(Tap tap) throws FareConfigException {
        log.info("Processing trip for tap: {}", tap);
        String key = generateKey(tap.getPan(), tap.getBusId(), tap.getCompanyId());
        if (tapHistory.containsKey(key)) {
            Tap firstTap = tapHistory.remove(key);

            if (firstTap.getTapType().equals(Constants.TAP_TYPE.OFF)) {
                // invalid data coming from the file
                throw new TapSequenceException("Invalid tap sequence");
            }

            return buildTrip(firstTap, tap);
        } else {
            tapHistory.put(key, tap);
        }
        return null;
    }

    /**
     * Builds a Trip object based on tap data.
     *
     * @param start The starting tap.
     * @param end   The ending tap (can be null for incomplete trips).
     * @return A constructed Trip object.
     */
    private Trip buildTrip(Tap start, Tap end)  {
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
        log.debug("Building on start {} end {} to {}", start, end, trip);
        return trip;
    }

    /**
     * Calculates the trip type based on two taps.
     *
     * @param one The first tap.
     * @param two The second tap.
     * @return The trip status.
     */
    private Constants.TRIP_STATUS calculateTripType(Tap one, Tap two) {
        if (Optional.ofNullable(two).isEmpty()) {
            return Constants.TRIP_STATUS.INCOMPLETE;
        } else if (one.getStopId().equals(two.getStopId())) {
            return Constants.TRIP_STATUS.CANCELLED;
        } else {
            return Constants.TRIP_STATUS.COMPLETED;
        }
    }

    /**
     * Calculates the trip type for a single tap.
     *
     * @param one The tap to calculate for.
     * @return The trip status.
     */
    private Constants.TRIP_STATUS calculateTripType(Tap one) {
        return calculateTripType(one, null);
    }

    /**
     * Calculates the duration between two DateTime instances.
     *
     * @param start The starting DateTime.
     * @param end   The ending DateTime.
     * @return The calculated duration in seconds.
     */
    private int calculateDuration(DateTime start, DateTime end) {
        Seconds seconds = Seconds.secondsBetween(start, end);
        return seconds.getSeconds();
    }

    /**
     * Generates a key for tap history.
     * The key is a concatenation of PAN, bus ID, and company ID.
     * to uniquely identify a tap.
     *
     * @param pan       The PAN number.
     * @param busId     The bus ID.
     * @param companyId The company ID.
     * @return The generated key.
     */
    private String generateKey(String pan, String busId, String companyId) {
        return (pan + busId + companyId).toUpperCase();
    }
}
