package com.littlepay.dto;

import com.littlepay.util.Constants;
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
public class Tap {

    private Integer id;

    private DateTime dateTime;

    private Constants.TAP_TYPE tapType;

    private String stopId;

    private String companyId;

    private String busId;

    private String pan;
}
