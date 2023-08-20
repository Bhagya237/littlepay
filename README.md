# LittlePay Trip Processing Application

The LittlePay Trip Processing Application is designed to process tap data and generate trip files. It reads tap data from an input CSV file, processes the trips, and writes the resulting trip data to an output CSV file.

## Features

- Reads tap data from an input CSV file.
- Processes trips using custom logic to determine trip status and fare calculations.
- Writes the processed trip data to an output CSV file.

## Getting Started

### Prerequisites

- Java 17
- Maven

### Running the application

1. create taps.csv file in the root directory of the project
2. Run the following command to build the application with unit tests
   ```sh
   mvn clean compile install
   ```
3. Run the following command to run the application
   ```sh
    mvn spring-boot:run
    ```
4. The application will generate trips.csv in the root directory

## Notes

1. The output given in the example(Tap IDs 1 &2) the duration value should be 300 seconds instead of 900 seconds

## Assumptions

1. The input file is in the root directory of the project with well-formed data and without missing values
2. The second tap in a trip is always marked OFF by the device itself
3. The bus device clock is not changing during a trip (i.e. the clock will not reset)
4. There is least one second gap between tap ON and Tap OFF
5. There are multiple bus companies and same bus number can be in multiple companies 


## Improvements

1. Removed PAN from toString values (To avoid PAN in logs), Better to introduce a masker to mask the PAN for debug logs
2. Add more tests to cover CSV write function
3. Add integration tests to cover the whole application