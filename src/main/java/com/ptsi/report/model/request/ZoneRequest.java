package com.ptsi.report.model.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ZoneRequest {

    private Float zoneId;
    private String projectCoordinator;
    private Boolean isUpdate;
    private Boolean isDeleted;
}
