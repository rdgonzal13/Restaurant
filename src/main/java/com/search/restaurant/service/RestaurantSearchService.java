package com.search.restaurant.service;

import com.search.restaurant.model.Restaurant;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class RestaurantSearchService {


    private final RestaurantLoader restaurantLoader;

    private final Map<Integer , Set<Restaurant>> ratingMap;

    private final List<Restaurant> sortByDistance;

    private final List<Integer> sortByDistanceValues;

    private final List<Integer> sortByPriceValues;

    private final List<Restaurant> sortByPrice;

    private final Map<String, Set<Restaurant>> cuisineMap;

    public RestaurantSearchService(RestaurantLoader restaurantLoader) {
        this.restaurantLoader = restaurantLoader;
        this.ratingMap = indexByRating();
        this.sortByDistance = sortedByDistance();
        this.sortByDistanceValues = this.sortByDistance.stream().map(Restaurant::getDistance).collect(Collectors.toList());
        this.sortByPrice = sortedByPrice();
        this.sortByPriceValues = this.sortByPrice.stream().map(Restaurant::getPrice).collect(Collectors.toList());
        this.cuisineMap = indexByCuisine();

    }

    public List<Restaurant> findRestaurants(String name,Integer distance, Integer rating, Integer price, String cuisine) throws IllegalArgumentException{
            validateInput(rating , price, distance);
            var searchResults = new ArrayList<Set<Restaurant>>();

            if(distance != null){
                searchResults.add(findByDistance(distance));
            }
            if (rating != null){
                searchResults.add(findByRating(rating));
            }

            if(price != null){
                searchResults.add(findByPrice(price));
            }

            if(StringUtils.isNotEmpty(cuisine)){
                searchResults.add(findByCuisine(cuisine));
            }

            if(StringUtils.isNotEmpty(name)){
                searchResults.add(findByName(name));
            }

        if (searchResults.isEmpty()) {
            return Collections.emptyList();
        }

        Iterator<Set<Restaurant>> it = searchResults.iterator();
        Set<Restaurant> response = it.next();
        while (it.hasNext()) {
            response.retainAll(it.next());
        }

        return getBestResults(new ArrayList<>(response));
    }

    //Using priority queue to create min heap of size 6 --> O(n) to find top five
    private List<Restaurant> getBestResults(List<Restaurant> restaurants){
        //Sorting By worst result at the top of the priority queue
        PriorityQueue<Restaurant> results = new PriorityQueue<>(Comparator
                .comparing(Restaurant::getDistance, Collections.reverseOrder())
                .thenComparing(Restaurant::getRating)
                .thenComparing(Restaurant::getPrice, Collections.reverseOrder()));
        //Add the first six to the queue,
        for(int n = 0; n < 6 && n < restaurants.size(); n++){
            results.add(restaurants.get(n));
        }

        int n = 6;
        while(n < restaurants.size()){
            results.poll();
            results.add(restaurants.get(n));
            n++;
        }

        if(results.size() == 6){
            results.poll();
        }

        var bestMatches = new ArrayList<>(results);
        bestMatches.sort(Comparator
                .comparing(Restaurant::getDistance)
                .thenComparing(Restaurant::getRating, Collections.reverseOrder())
                .thenComparing(Restaurant::getPrice));
       return bestMatches;
    }



    private Set<Restaurant> findByName(String name){
       return this.restaurantLoader.getRestaurants().stream()
                .filter(restaurant -> restaurant.getName()
                        .contains(name))
                .collect(Collectors.toSet());
    }


    private Set<Restaurant> findByDistance(Integer distance) {
        return new HashSet<>(this.sortByDistance.subList(0, binarySearch(this.sortByDistanceValues , distance)));
    }

    private Set<Restaurant> findByRating(Integer rating){
        return new HashSet<>(ratingMap.get(rating));
    }

    private Set<Restaurant> findByPrice(Integer price){
        return new HashSet<>(this.sortByPrice.subList(0, binarySearch(this.sortByPriceValues , price)));

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

    private int binarySearch(List<Integer> searchValues, int value){
        var index = Collections.binarySearch(searchValues, value);

        if (index >= 0) {
            index++;
            while (index < searchValues.size() && searchValues.get(index).equals(value)) {
                index++;
            }
        } else {
            //Binary search returns -(insertion point) - 1 if the element is not in the array
            index = Math.abs(index) - 1;
        }
        return index;
    }

    private List<Restaurant> sortedByDistance(){
        List<Restaurant> sorted = new ArrayList<>(restaurantLoader.getRestaurants());
        sorted.sort(Comparator.comparingInt(Restaurant::getDistance));
        return sorted;
    }

    private List<Restaurant> sortedByPrice(){
        List<Restaurant> sorted = new ArrayList<>(restaurantLoader.getRestaurants());
        sorted.sort(Comparator.comparingInt(Restaurant::getPrice));
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

    //Index By cuisine when bean is created for faster startup.
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


    private void validateInput(Integer rating, Integer price , Integer distance){
        if(rating != null && (rating <= 0 || rating > 5)){
            throw new IllegalArgumentException("Rating is outside of range");
        }
        if(distance != null && distance < 0){
            throw new IllegalArgumentException("Distance can't be negative");
        }
        if(price != null && price < 0){
            throw new IllegalArgumentException("price can't be negative");
        }
    }


}
