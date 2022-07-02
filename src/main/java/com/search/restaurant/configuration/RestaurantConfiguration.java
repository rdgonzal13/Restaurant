package com.search.restaurant.configuration;


import com.search.restaurant.model.Cuisine;
import com.search.restaurant.model.Restaurant;
import com.search.restaurant.service.RestaurantLoader;
import com.search.restaurant.service.RestaurantSearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RestaurantConfiguration {


    @Bean
    public RestaurantLoader restaurantLoader(){
        List<Cuisine> cuisineList = RestaurantLoader.loadCuisines();
        List<Restaurant> restaurants = RestaurantLoader.loadRestaurants(cuisineList);
        return new RestaurantLoader(restaurants, cuisineList);
    }

    @Bean
    public RestaurantSearchService searchService(){
        return new RestaurantSearchService(restaurantLoader());
    }

}
