package com.search.restaurant.controller;

import com.search.restaurant.model.Restaurant;
import com.search.restaurant.service.RestaurantSearchService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class RestaurantSearchController {

    private final RestaurantSearchService searchService;

    public RestaurantSearchController(RestaurantSearchService searchService) {
        this.searchService = searchService;
    }


    @Operation(summary = "Find top 5 local restaurants based on name, price , distance, rating, price and cuisine")
    @GetMapping("/restaurants")
    public ResponseEntity<List<Restaurant>> searchLocalRestaurants(@RequestParam(value = "name", required = false) String name,
                                                                   @RequestParam(value = "distance", required = false) Integer distance,
                                                                   @RequestParam(value = "customer_rating", required = false) Integer rating,
                                                                   @RequestParam(value = "cuisine", required = false) String cuisine,
                                                                   @RequestParam(value = "price", required = false) Integer price) {

        try {
            List<Restaurant> restaurants = searchService.findRestaurants(name, distance, rating, price, cuisine);
            return ResponseEntity.of(Optional.of(restaurants));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

    }


}
