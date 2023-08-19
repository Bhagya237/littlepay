package com.littlepay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tap {

    private Integer id;

    private Date dateTime;

    private String tapType;

    private String stopId;

    private String companyId;

    private String busId;

    private String pan;
}
