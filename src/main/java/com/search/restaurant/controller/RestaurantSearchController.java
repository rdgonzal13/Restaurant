package com.search.restaurant.controller;

import com.search.restaurant.model.Restaurants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestaurantSearchController {



    @GetMapping("/restaurants")
    public ResponseEntity<Restaurants> searchLocalRestaurants(@RequestParam(value = "name", required = false) String name,
                                                              @RequestParam(value = "distance" , required = false) Integer distance,
                                                              @RequestParam(value = "customer_rating" , required = false) Integer rating,
                                                              @RequestParam(value = "cuisine" , required = false) String cuisine,
                                                              @RequestParam(value = "price", required = false) Integer price){
        return new ResponseEntity<>(HttpStatus.OK);
    }





}
