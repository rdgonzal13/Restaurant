package com.search.restaurant.controller;

import com.search.restaurant.model.Restaurant;
import com.search.restaurant.model.Restaurants;
import com.search.restaurant.service.RestaurantSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class RestaurantSearchController {

    private final RestaurantSearchService searchService;

    public RestaurantSearchController(RestaurantSearchService searchService) {
        this.searchService = searchService;
    }


    @GetMapping("/restaurants")
    public ResponseEntity<Set<Restaurant>> searchLocalRestaurants(@RequestParam(value = "name", required = false) String name,
                                                                  @RequestParam(value = "distance" , required = false) Integer distance,
                                                                  @RequestParam(value = "customer_rating" , required = false) Integer rating,
                                                                  @RequestParam(value = "cuisine" , required = false) String cuisine,
                                                                  @RequestParam(value = "price", required = false) Integer price){

        try {
            Set<Restaurant> restaurants = searchService.findRestaurants(name, distance, rating, price, cuisine);
            return ResponseEntity.of(Optional.of(restaurants));
        }catch (IllegalArgumentException e){
            //TODO: Add message here;
            return ResponseEntity.badRequest().build();
        }

    }





}
