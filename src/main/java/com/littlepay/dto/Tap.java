package com.littlepay.dto;

import com.littlepay.util.Constants;
import com.littlepay.util.DateTimeConverter;
import com.littlepay.util.StringConverter;
import com.littlepay.util.TapTypeConverter;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.time.DateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "pan")
public class Tap {

    @CsvBindByPosition(position = 0)
    private Integer id;

    @CsvCustomBindByPosition(position = 1, converter = DateTimeConverter.class)
    private DateTime dateTime;

    @CsvCustomBindByPosition(position = 2, converter = TapTypeConverter.class)
    private Constants.TAP_TYPE tapType;

    @CsvCustomBindByPosition(position = 3, converter = StringConverter.class)
    private String stopId;

    @CsvCustomBindByPosition(position = 4, converter = StringConverter.class)
    private String companyId;

    @CsvCustomBindByPosition(position = 5, converter = StringConverter.class)
    private String busId;

    @CsvCustomBindByPosition(position = 6, converter = StringConverter.class)
    private String pan;
}

