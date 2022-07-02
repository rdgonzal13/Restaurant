package com.search.restaurant.service;

import com.search.restaurant.model.Cuisine;
import com.search.restaurant.model.Restaurant;
import org.apache.commons.lang3.StringUtils;

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

    private final List<Restaurant> sortByDistance;

    private final List<Restaurant> sortByPrice;

    private final Map<String, Set<Restaurant>> cuisineMap;

    public RestaurantSearchService(RestaurantLoader restaurantLoader) {
        this.restaurantLoader = restaurantLoader;
        this.ratingMap = indexByRating();
        this.sortByDistance = sortedByDistance();
        this.sortByPrice = sortedByPrice();
        this.cuisineMap = indexByCuisine();

    }


    public Set<Restaurant> findRestaurants(String name,Integer distance, Integer rating, Integer price, String cuisine) throws IllegalArgumentException{
            validateInput(rating);

            var outputs = new ArrayList<Set<Restaurant>>();

            if(distance != null){
                outputs.add(findByDistance(distance));
            }
            if (rating != null){
                outputs.add(ratingMap.get(rating));
            }

            if(price != null){
                outputs.add(findByPrice(price));
            }

            if(StringUtils.isNotEmpty(cuisine)){
                outputs.add(findByCuisine(cuisine));
            }

            if(StringUtils.isNotEmpty(name)){
                outputs.add(findByName(name));
            }

            //return nothing if no match.
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

    private Set<Restaurant> findByDistance(Integer distance){
        return sortByDistance
                .stream()
                .filter(restaurant -> restaurant.getDistance() <= distance)
                .collect(Collectors.toSet());
    }

    private Set<Restaurant> findByPrice(Integer price){
        return sortByPrice
                .stream()
                .filter(restaurant -> restaurant.getPrice() <= price)
                .collect(Collectors.toSet());

    }

    private Set<Restaurant> findByCuisine(String cuisineName){
        Set<Restaurant> matching = new HashSet<>();
        restaurantLoader.getCuisines().stream()
                .filter(cuisine -> cuisine.getName().contains(cuisineName)).
                collect(Collectors.toList())
                .forEach(cuisine -> {
                    matching.addAll(cuisineMap.get(cuisine.getName()));
                });
        return matching;

    }


    private void validateInput(Integer rating){
        if(rating != null && (rating <= 0 || rating > 5)){
            throw new IllegalArgumentException("Rating is outside of range");
        }
    }


    private List<Restaurant> sortedByDistance(){
        List<Restaurant> sorted = restaurantLoader.getRestaurants();
        Collections.sort(sorted,Comparator.comparingInt(Restaurant::getDistance));
        return sorted;
    }

    private List<Restaurant> sortedByPrice(){
        List<Restaurant> sorted = restaurantLoader.getRestaurants();
        Collections.sort(sorted,Comparator.comparingInt(Restaurant::getPrice));
        return sorted;
    }

    //Index restaurants by Rating for faster lookup Do when bean is constructed not during search
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

    private Map<String, Set<Restaurant>> indexByCuisine(){
        Map<String, Set<Restaurant>> cuisineSetMap = new HashMap<>();
        restaurantLoader.getRestaurants().forEach(restaurant -> {
            if(cuisineSetMap.containsKey(restaurant.getCuisine())){
                cuisineSetMap.get(restaurant.getCuisine()).add(restaurant);
            }else {
                Set<Restaurant> restaurants = new HashSet<>();
                restaurants.add(restaurant);
                cuisineSetMap.put(restaurant.getCuisine(), restaurants);
            }
        });
        return cuisineSetMap;
    }







}
