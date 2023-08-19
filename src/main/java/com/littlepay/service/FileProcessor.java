package com.littlepay.service;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.IOException;

public interface FileProcessor {

    /**
     * Generates a trip file by reading taps, processing trips, and writing the resulting trips.
     *
     * @throws IOException               If an I/O error occurs during file processing.
     * @throws CsvRequiredFieldEmptyException If a required field is found to be empty.
     * @throws CsvDataTypeMismatchException  If a data type mismatch occurs during CSV writing.
     */
    void generateTripFile() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException;
}
