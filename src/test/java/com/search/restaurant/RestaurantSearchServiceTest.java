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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RestaurantSearchServiceTest {

    private List<Cuisine> cuisines;

    private RestaurantLoader loader;

    private RestaurantSearchService searchService;


    @BeforeAll
    public void setup() {
        cuisines = RestaurantLoader.loadCuisines("cuisines.csv");
    }

    @Test
    public void testSearchWithAllNull() {
        List<Restaurant> test = searchService.findRestaurants(null, null, null, null, null);
        Assertions.assertTrue(test.isEmpty());
    }

//    @Test
//    public void testSearchWithNameOnly() {
//        //Name not found
//        List<Restaurant> test = searchService.findRestaurants("Not found", null, null, null, null);
//        Assertions.assertTrue(test.isEmpty());
//
//        //Test with 2 matching
//        test = searchService.findRestaurants("Taste", null, null, null, null);
//        Assertions.assertEquals(2, test.size());
//        Assertions.assertTrue(test.get(0).getDistance() <= test.get(1).getDistance());
//
//        //Test with 1 matching
//        test = searchService.findRestaurants(restaurants.get(2).getName(), null, null, null, null);
//        Assertions.assertEquals(1, test.size());
//    }

    @Test
    public void testSearchWithDistanceOnly() {
        this.loader = new RestaurantLoader(RestaurantLoader.loadRestaurants(cuisines, "distance_restaurants.csv"), cuisines);
        this.searchService = new RestaurantSearchService(loader);

        //Test no matches found
        List<Restaurant> test = searchService.findRestaurants(null, 1, null, null, null);
        Assertions.assertEquals(1, test.size());

        //Test one matches found (only one distance less than value)
        test = searchService.findRestaurants(null, 2, null, null, null);
        Assertions.assertEquals(2, test.size());
        Assertions.assertTrue(test.get(0).getDistance() <= test.get(1).getDistance());

        //Test two matches found (two distances less than value)
        test = searchService.findRestaurants(null, 3, null, null, null);
        Assertions.assertEquals(3, test.size());

        //Best results should have distance first.
        Assertions.assertTrue(test.get(0).getDistance() <= test.get(1).getDistance());
        Assertions.assertTrue(test.get(0).getDistance() <= test.get(1).getDistance());

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
        this.loader = new RestaurantLoader(RestaurantLoader.loadRestaurants(cuisines, "rating_restaurants.csv"), cuisines);
        this.searchService = new RestaurantSearchService(loader);

        //Test return all 5 best matches
        List<Restaurant> test = searchService.findRestaurants(null, null, 1, null, null);
        Assertions.assertEquals(5, test.size());
        Assertions.assertTrue(test.get(0).getRating() >= test.get(1).getRating());
        Assertions.assertTrue(test.get(1).getRating() >= test.get(2).getRating());
        Assertions.assertTrue(test.get(2).getRating() >= test.get(3).getRating());
        Assertions.assertTrue(test.get(3).getRating() >= test.get(4).getRating());

        //Test return only 3 best matches
        test = searchService.findRestaurants(null, null, 4, null, null);
        Assertions.assertEquals(3, test.size());
        Assertions.assertTrue(test.get(0).getRating() >= test.get(1).getRating());
        Assertions.assertTrue(test.get(1).getRating() >= test.get(2).getRating());

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
