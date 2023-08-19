package com.littlepay.service;

import com.littlepay.dto.Tap;
import com.littlepay.dto.Trip;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.IOException;
import java.util.List;

/**
 * Interface for reading and writing CSV files.
 */
public interface CsvHelper {

    /**
     * Reads data from the CSV file and converts it to a list of Tap objects.
     *
     * @return A list of Tap objects containing data read from the CSV.
     * @throws IOException If an I/O error occurs while reading the CSV file.
     */
    List<Tap> readData() throws IOException;


    /**
     * Writes Trip data to the CSV file
     *
     * @param trips The list of Trip objects to be written to the CSV.
     * @throws IOException              If an I/O error occurs while writing the CSV file.
     * @throws CsvRequiredFieldEmptyException If a required field is found to be empty.
     * @throws CsvDataTypeMismatchException  If a data type mismatch occurs during CSV writing.
     */
    void writeData(List<Trip> trips) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException;
}
