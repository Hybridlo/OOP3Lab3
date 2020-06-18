package com.lab3.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientData {
    private Long id;
    private String name;
    private Boolean isSick;
    private String sickness;
    private String treatment;
    private String sicknessHistory;
}
