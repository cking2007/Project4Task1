package com.example.project4task1;

import lombok.Data;

/**
 * @author
 * @version V1.0
 * @description
 * @date 2022/4/6 4:58 PM
 **/

@Data
public class DashEntity {

    private String dateTime;
    private String countryCode;
    private String devices;
    private String apiTime;
    private String country;
    private Integer totalConfirmed;
    private Integer totalDeaths;
    private Integer totalRecovered;
    private Integer dailyConfirmed;
    private Integer dailyDeaths;
    private Integer activeCases;
    private String lastUpdated;
}
