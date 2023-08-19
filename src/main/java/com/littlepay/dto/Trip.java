package com.littlepay.dto;

import com.littlepay.util.Constants;
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
public class Trip {

    private DateTime started;

    private DateTime finished;

    private Integer duration;

    private String fromStopId;

    private String toStopId;

    private BigDecimal changeAmount;

    private String companyId;

    private String busId;

    private String pan;

    private Constants.TRIP_STATUS status;
}
