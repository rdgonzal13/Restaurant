package com.search.restaurant.service;

import com.search.restaurant.model.Restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RestaurantSearchService {


    private final RestaurantLoader restaurantLoader;

    private final Map<Integer , Set<Restaurant>> ratingMap;

    public RestaurantSearchService(RestaurantLoader restaurantLoader) {
        this.restaurantLoader = restaurantLoader;
        this.ratingMap = indexByRating();

    }


    public Set<Restaurant> findRestaurants(String name, Integer rating) throws IllegalArgumentException{
            validateInput(rating);

            var outputs = new ArrayList<Set<Restaurant>>();

            if(!name.isEmpty()){
                outputs.add(findByName(name));
            }

            if (rating != null){
                outputs.add(ratingMap.get(rating));
            }


        return outputs.stream().reduce(new HashSet<Restaurant>(), (x , y) -> {
                if(x.isEmpty()){
                    return y;
                }else{
                    x.retainAll(y);
                    return x;
                }
            });
    }

    private Set<Restaurant> findByName(String name){
       return this.restaurantLoader.getRestaurants().stream()
                .filter(restaurant -> restaurant.getName()
                        .contains(name))
                .collect(Collectors.toSet());
    }

    private void validateInput(Integer rating){
        if(rating != null && (rating <= 0 || rating > 5)){
            throw new IllegalArgumentException("Rating is outside of range");
        }
    }









    //Index restaurants by Rating for faster lookup - Do when bean is constructed/ not during search
    private Map<Integer, Set<Restaurant>> indexByRating (){
        Map<Integer, Set<Restaurant>> nRatingMap = new HashMap<>();
        restaurantLoader.getRestaurants().forEach(restaurant -> {
            int rating = restaurant.getRating();
            while(rating > 0){
                if(nRatingMap.containsKey(rating)){
                    nRatingMap.get(rating).add(restaurant);
                }else{
                    Set<Restaurant> byRating = new HashSet<>();
                    byRating.add(restaurant);
                    nRatingMap.put(rating, byRating);
                }
                rating --;
            }
        });
        return nRatingMap;
    }






}
