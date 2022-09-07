package com.proj3ct.IanAutomatonBot.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherModel {
    private String name;
    private Double temp;
    private String description;
    private Double speed;
    private Double humidity;

}
