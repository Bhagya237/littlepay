package com.littlepay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Trip {

    private Date started;

    private Date finished;

    private Integer duration;

    private String fromStopId;

    private String toStopId;

    private BigDecimal changeAmount;

    private String companyId;

    private String busId;

    private String pan;

    private String status;
}
