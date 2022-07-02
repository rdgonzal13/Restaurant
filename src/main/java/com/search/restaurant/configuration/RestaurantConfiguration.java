package com.search.restaurant.configuration;


import com.search.restaurant.service.RestaurantLoader;
import com.search.restaurant.service.RestaurantSearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantConfiguration {


    @Bean
    public RestaurantLoader restaurantLoader(){
        return new RestaurantLoader(RestaurantLoader.loadRestaurants() , RestaurantLoader.loadCuisines());
    }

    @Bean
    public RestaurantSearchService searchService(){
        return new RestaurantSearchService(restaurantLoader());
    }

}
