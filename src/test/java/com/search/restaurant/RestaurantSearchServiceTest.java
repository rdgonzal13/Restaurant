package com.search.restaurant;

import com.search.restaurant.model.Cuisine;
import com.search.restaurant.model.Restaurant;
import com.search.restaurant.service.RestaurantLoader;
import com.search.restaurant.service.RestaurantSearchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RestaurantSearchServiceTest {

    private List<Restaurant> restaurants;

    private List<Cuisine> cuisines;

    private RestaurantLoader loader;

    private RestaurantSearchService searchService;


    @BeforeAll
    public void setup(){
        this.cuisines = createTestCuisineData();
        this.restaurants = createTestRestaurantData(this.cuisines);

        this.loader = new RestaurantLoader(restaurants, cuisines);
        this.searchService = new RestaurantSearchService(loader);

    }


    @Test
    public void testSearchWithNameOnly(){

        //Null value
        Set<Restaurant> test = searchService.findRestaurants(null , null , null, null, null);
        Assertions.assertTrue(test.isEmpty());

        //Name not found
        test = searchService.findRestaurants("Not found", null , null, null, null);
        Assertions.assertTrue(test.isEmpty());

        //Test with 2 matching
        test = searchService.findRestaurants("Taste", null , null, null, null);
        Assertions.assertEquals(2, test.size());
        Assertions.assertTrue(test.contains(restaurants.get(0)));
        Assertions.assertTrue(test.contains(restaurants.get(1)));


        //Test with 1 matching
        test = searchService.findRestaurants(restaurants.get(2).getName(), null , null, null, null);
        Assertions.assertEquals(1, test.size());
        Assertions.assertTrue(test.contains(restaurants.get(2)));

    }

    //@Test
    public void testSearchWithDistanceOnly(){

    }

    //@Test
    public void testSearchCuisineOnly(){

    }

    //@Test
    public void testSearchRatingOnly(){

    }

    //@Test
    public void testSearchPriceOnly(){

    }

    //@Test
    public void testSearchWithNameAndDistance(){

    }


    //@Test
    public void testSearchWithNameAndCuisineAndDistance(){

    }

    //@Test
    public void testSearchWithNameAndCuisineAndDistanceAndPrice(){

    }

    //@Test
    public void testSearchWithNameAndCuisineAndDistanceAndPriceAndRating(){

    }

    private List<Cuisine> createTestCuisineData(){
        List<Cuisine> cuisines = new ArrayList<>();
        cuisines.add(Cuisine.builder()
                .name("Spanish")
                .id(1)
                .build());
        cuisines.add(Cuisine.builder()
                .name("Italian")
                .id(2)
                .build());
        return cuisines;
    }

    private List<Restaurant> createTestRestaurantData(List<Cuisine> cuisines){
        List<Restaurant> restaurants = new ArrayList<>();

        restaurants.add(Restaurant.builder()
                .name("Taste test")
                .cuisine(cuisines.get(0).getName())
                .cuisineId(cuisines.get(0).getId())
                .distance(10)
                .price(5)
                .rating(5)
                .build());

        restaurants.add(Restaurant.builder()
                .name("Taste test 2")
                .cuisine(cuisines.get(0).getName())
                .cuisineId(cuisines.get(0).getId())
                .distance(5)
                .price(3).rating(4)
                .build());

        restaurants.add(Restaurant.builder()
                .name("A different name")
                .cuisine(cuisines.get(1).getName())
                .cuisineId(cuisines.get(1).getId())
                .distance(8)
                .price(8).rating(3)
                .build());

        return restaurants;
    }






}
