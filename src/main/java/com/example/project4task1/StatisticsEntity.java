package com.example.project4task1;

import lombok.Data;

/**
 * @author
 * @version V1.0
 * @description
 * @date 2022/4/6 5:52 PM
 **/
@Data
public class StatisticsEntity {

    private String _id;
    private String countryCode;
    private int searchCount;
    private String lastSearchTime;
}
