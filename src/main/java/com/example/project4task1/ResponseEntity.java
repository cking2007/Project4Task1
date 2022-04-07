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
public class ResponseEntity {
    private String _id;
    private String countryCode;
    private String countryName;
    private Integer totalMiss;
    private Integer totalFound;
    private Integer totalConfirmed;
    private Integer totalFoundPerMillionPopulation;
    private Integer totalMissPerMillionPopulation;
    private String lastUpdated;

    private String flagURL;
}
