package com.littlepay;

import com.littlepay.service.FileProcessor;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * Hello littlePay!
 */
@SpringBootApplication
@RequiredArgsConstructor
public class LittlePayApplication implements CommandLineRunner {

    private final FileProcessor fileProcessor;

    public static void main(String[] args) {
        SpringApplication.run(LittlePayApplication.class, args);
    }

    @Override
    public void run(String... args) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        fileProcessor.generateTripFile();
    }
}
