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

    private String searchTime;
    private String countryCode;
    private String devices;
    private Integer apiTime;
    private String country;
    private Integer totalMiss;
    private Integer totalFound;
    private Integer totalConfirmed;;
    private String lastUpdated;

    private String androidVersion;
}
