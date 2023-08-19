package com.littlepay.service;

import com.littlepay.dto.Tap;
import com.littlepay.dto.Trip;

import java.util.List;

public interface TripProcessor {
    List<Trip> processTrips(List<Tap> taps);
}
