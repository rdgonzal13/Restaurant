package com.search.restaurant.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cuisine {

    private int id;
    private String name;
}
