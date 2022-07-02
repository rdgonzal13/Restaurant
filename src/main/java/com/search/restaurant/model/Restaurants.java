package com.search.restaurant.model;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Restaurants {

    public List<Restaurant> restaurants;
}
