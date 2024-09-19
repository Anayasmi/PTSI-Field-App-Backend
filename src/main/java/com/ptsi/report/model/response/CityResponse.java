package com.ptsi.report.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CityResponse {

    private Double cityId;

    private String cityName;

    private Double projectCoordinator;

    private String staffName;
}
