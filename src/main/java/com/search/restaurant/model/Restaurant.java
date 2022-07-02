package com.search.restaurant.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Restaurant {

    private String name;
    private int rating;
    private double string;
    private String cuisine;
    private int cuisineId;
    private int distance;
    private int price;





}
