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
    private String _id;
    private String countryCode;
    private String country;
    private String countryName;
    // private Integer totalConfirmed;
    private Integer totalMiss;
    // private Integer totalDeaths;
    private Integer totalFound;
    // private Integer totalRecovered;
    private Integer totalConfirmed;
    // private Integer dailyConfirmed;
    // private Integer dailyDeaths;
    // private Integer activeCases;
    // private Integer totalCritical;
    // private Integer totalConfirmedPerMillionPopulation;
    private Integer totalFoundPerMillionPopulation;
    // private Integer totalDeathsPerMillionPopulation;
    private Integer totalMissPerMillionPopulation;
    private String lastUpdated;

    private String flagURL;
}
