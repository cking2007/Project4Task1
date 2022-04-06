package com.example.project4task1;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author
 * @version V1.0
 * @description
 * @date 2022/4/6 10:01 AM
 **/
@NoArgsConstructor
@Data
public class ResultEntity {
    private String countryCode;
    private String country;
    private String countryName;
    private Double lat;
    private Double lng;
    private Integer totalConfirmed;
    private Integer totalDeaths;
    private Integer totalRecovered;
    private Integer dailyConfirmed;
    private Integer dailyDeaths;
    private Integer activeCases;
    private Integer totalCritical;
    private Integer totalConfirmedPerMillionPopulation;
    private Integer totalDeathsPerMillionPopulation;
    private String fR;
    private String pR;
    private String lastUpdated;
}
