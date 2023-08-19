package com.littlepay.service.impl;

import com.littlepay.dto.Tap;
import com.littlepay.dto.Trip;
import com.littlepay.service.CsvHelper;
import com.littlepay.util.CustomMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.MappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@Slf4j
public class CsvHelperImpl implements CsvHelper {

    @Value("${littlepay.file.input}")
    private String inputFilePath;

    @Value("${littlepay.file.output}")
    private String outputFilePath;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Tap> readData() throws IOException {
        Path path = Path.of(inputFilePath);
        log.info("Reading data from {}", path.toAbsolutePath());
        try (Reader reader = Files.newBufferedReader(path)) {
            CsvToBean<Tap> cb = new CsvToBeanBuilder<Tap>(reader)
                    .withSkipLines(1) // Skip the header line
                    .withSeparator(',')
                    .withType(Tap.class) // Bind to Tap class
                    .build();
            return cb.parse();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeData(List<Trip> trips) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        // Define the output file path
        Path outputPath = Path.of(outputFilePath);
        log.info("Writing data to {}", outputPath.toAbsolutePath());

        // Define a custom mapping strategy for Trip class
        MappingStrategy<Trip> mappingStrategy = new CustomMappingStrategy<>();
        mappingStrategy.setType(Trip.class);


        try (var writer = Files.newBufferedWriter(outputPath)) {
            StatefulBeanToCsv<Trip> sbc = new StatefulBeanToCsvBuilder<Trip>(writer)
                    .withApplyQuotesToAll(false)
                    .withMappingStrategy(mappingStrategy)
                    .build();
            sbc.write(trips); // Write Trip objects to CSV
        }
    }
}
