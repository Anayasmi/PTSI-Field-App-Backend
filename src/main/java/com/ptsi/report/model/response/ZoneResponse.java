package com.ptsi.report.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ZoneResponse {

    private Float zoneId;

    private String zoneName;

    private List <StaffValue> projectCoordinators;
}
