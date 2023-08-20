package com.littlepay.service;

import com.littlepay.dto.Tap;
import com.littlepay.dto.Trip;

import java.util.List;

public interface TapProcessor {

    /**
     * Processes a list of taps and returns a list of corresponding trips.
     *
     * @param taps The list of taps to process.
     * @return A list of processed trips.
     */
    List<Trip> processTaps(List<Tap> taps);
}
