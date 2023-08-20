package com.littlepay.service.impl;

import com.littlepay.dto.Tap;
import com.littlepay.dto.Trip;
import com.littlepay.service.CsvHelper;
import com.littlepay.service.FileProcessor;
import com.littlepay.service.TapProcessor;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Implementation of the FileProcessor interface responsible for generating trip files.
 */
@Service
@RequiredArgsConstructor
public class FileProcessorImpl implements FileProcessor {

    private final CsvHelper data;
    private final TapProcessor processor;

    /**
     * {@inheritDoc}
     */
    @Override
    public void generateTripFile() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        // Read taps from CSV using the CSVHelper
        List<Tap> taps = data.readData();

        // Process trips using the  TripProcessor
        List<Trip> trips = processor.processTaps(taps);

        // Write trips to CSV using the CSVHelper
        data.writeData(trips);
    }
}
