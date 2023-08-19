package com.littlepay.dto;

import com.littlepay.util.Constants;
import com.littlepay.util.DateTimeConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.time.DateTime;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "pan")
public class Trip {

    @CsvBindByName(column = "Started")
    @CsvCustomBindByPosition(position = 0, converter = DateTimeConverter.class)
    private DateTime started;


    @CsvBindByName(column = "Finished")
    @CsvCustomBindByPosition(position = 1, converter = DateTimeConverter.class)
    private DateTime finished;


    @CsvBindByName(column = "DurationSecs")
    @CsvBindByPosition(position = 2)
    private Integer duration;

    @CsvBindByName(column = "FromStopId")
    @CsvBindByPosition(position = 3)
    private String fromStopId;

    @CsvBindByName(column = "ToStopId")
    @CsvBindByPosition(position = 4)
    private String toStopId;


    @CsvBindByName(column = "ChargeAmount")
    @CsvBindByPosition(position = 5, format = "$%s")
    private BigDecimal changeAmount;


    @CsvBindByName(column = "CompanyId")
    @CsvBindByPosition(position = 6)
    private String companyId;

    @CsvBindByName(column = "BusID")
    @CsvBindByPosition(position = 7)
    private String busId;

    @CsvBindByName(column = "PAN")
    @CsvBindByPosition(position = 8)
    private String pan;

    @CsvBindByName(column = "Status")
    @CsvBindByPosition(position = 9)
    private Constants.TRIP_STATUS status;

}