package com.search.restaurant.service;

import com.search.restaurant.model.Restaurant;

import java.util.List;

public class RestaurantSearchService {


    private final RestaurantLoader restaurantLoader;


    public RestaurantSearchService(RestaurantLoader restaurantLoader) {
        this.restaurantLoader = restaurantLoader;
    }


    public List<Restaurant> findRestaurants(String name){
        return restaurantLoader.getRestaurants();
    }
}
