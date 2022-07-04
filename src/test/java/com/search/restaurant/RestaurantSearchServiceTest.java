package com.search.restaurant;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
    public void setup() {
        this.cuisines = createTestCuisineData();
        this.restaurants = createTestRestaurantData(this.cuisines);

        this.loader = new RestaurantLoader(restaurants, cuisines);
        this.searchService = new RestaurantSearchService(loader);

    }

    @Test
    public void testSearchWithAllNull() {
        List<Restaurant> test = searchService.findRestaurants(null, null, null, null, null);
        Assertions.assertTrue(test.isEmpty());
    }

    @Test
    public void testSearchWithNameOnly() {

        //Name not found
        List<Restaurant> test = searchService.findRestaurants("Not found", null, null, null, null);
        Assertions.assertTrue(test.isEmpty());

        //Test with 2 matching
        test = searchService.findRestaurants("Taste", null, null, null, null);
        Assertions.assertEquals(2, test.size());
        Assertions.assertTrue(test.get(0).getDistance() <= test.get(1).getDistance());



        //Test with 1 matching
        test = searchService.findRestaurants(restaurants.get(2).getName(), null, null, null, null);
        Assertions.assertEquals(1, test.size());

    }

    @Test
    public void testSearchWithDistanceOnly() {

        //Test no matches found
        List<Restaurant> test = searchService.findRestaurants(null, 1, null, null, null);
        Assertions.assertTrue(test.isEmpty());

        //Test one matches found (only one distance less than value)
        test = searchService.findRestaurants(null, 5, null, null, null);
        Assertions.assertEquals(1, test.size());
        ;

        //Test two matches found (two distances less than value)
        test = searchService.findRestaurants(null, 8, null, null, null);
        Assertions.assertEquals(2, test.size());

        //Test three matches found (3 distances less than value)
        test = searchService.findRestaurants(null, 10, null, null, null);
        Assertions.assertEquals(3, test.size());



    }

    @Test
    public void testSearchCuisineOnly() {
        List<Restaurant> test = searchService.findRestaurants(null, null, null, null, cuisines.get(1).getName() );
        Assertions.assertEquals(1, test.size());

        //Test two matches found (two prices less than value)
        test = searchService.findRestaurants(null, null, null, null, cuisines.get(0).getName());
        Assertions.assertEquals(2, test.size());

    }

    @Test
    public void testSearchRatingOnly() {

        //Test one matches found (only one price less than value)
        List<Restaurant> test = searchService.findRestaurants(null, null, 5, null, null);
        Assertions.assertEquals(1, test.size());

        //Test two matches found (two prices less than value)
        test = searchService.findRestaurants(null, null, 4, null, null);
        Assertions.assertEquals(2, test.size());

        //Test three matches found (3 prices less than value)
        test = searchService.findRestaurants(null, null, 3, null, null);
        Assertions.assertEquals(3, test.size());

    }

    @Test
    public void testSearchPriceOnly() {

        //Test no matches found
        List<Restaurant> test = searchService.findRestaurants(null, null, null, 1, null);
        Assertions.assertTrue(test.isEmpty());

        //Test one matches found (only one price less than value)
        test = searchService.findRestaurants(null, null, null, 3, null);
        Assertions.assertEquals(1, test.size());


        //Test two matches found (two prices less than value)
        test = searchService.findRestaurants(null, null, null, 5, null);
        Assertions.assertEquals(2, test.size());

        //Test three matches found (3 prices less than value)
        test = searchService.findRestaurants(null, null, null, 8, null);
        Assertions.assertEquals(3, test.size());


    }

    //@Test
    public void testSearchWithNameAndDistance() {
        //TODO: implement
    }


    //@Test
    public void testSearchWithNameAndCuisineAndDistance() {
        //TODO: implement
    }

    //@Test
    public void testSearchWithNameAndCuisineAndDistanceAndPrice() {
        //TODO: implement
    }

    //@Test
    public void testSearchWithNameAndCuisineAndDistanceAndPriceAndRating() {
        //TODO: implement
    }

    //@Test
    public void testOnlyReturns5best(){
        //TODO: implemenent
    }

    private List<Cuisine> createTestCuisineData() {
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

    private List<Restaurant> createTestRestaurantData(List<Cuisine> cuisines) {
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
