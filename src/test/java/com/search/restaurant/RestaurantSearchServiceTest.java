package com.search.restaurant;

import com.search.restaurant.model.Cuisine;
import com.search.restaurant.model.Restaurant;
import com.search.restaurant.service.RestaurantLoader;
import com.search.restaurant.service.RestaurantSearchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

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
        this.loader = new RestaurantLoader(RestaurantLoader.loadRestaurants(cuisines, "price_restaurants.csv"), cuisines);
        this.searchService = new RestaurantSearchService(loader);

        //Test return all 5 best matches
        List<Restaurant> test = searchService.findRestaurants(null, null, null, 50, null);
        Assertions.assertEquals(5, test.size());
        Assertions.assertTrue(test.get(0).getPrice() <= test.get(1).getPrice());
        Assertions.assertTrue(test.get(1).getPrice() <= test.get(2).getPrice());
        Assertions.assertTrue(test.get(2).getPrice() <= test.get(3).getPrice());
        Assertions.assertTrue(test.get(3).getPrice() <= test.get(4).getPrice());

        //Test return only 3 best matches
        test = searchService.findRestaurants(null, null, null, 20, null);
        Assertions.assertEquals(2, test.size());
        Assertions.assertTrue(test.get(0).getPrice() <= test.get(1).getPrice());

    }

     @Test
    public void testSearchWithNameOnly() {
         this.loader = new RestaurantLoader(RestaurantLoader.loadRestaurants(cuisines, "distance_restaurants.csv"), cuisines);
         this.searchService = new RestaurantSearchService(loader);

         //Test no matches found
         List<Restaurant> test = searchService.findRestaurants("Not found", null, null, null, null);
         Assertions.assertTrue(test.isEmpty());

         test = searchService.findRestaurants("Deliciousgenix", null, null, null, null);
         Assertions.assertEquals(1, test.size());

         //Test that if all match , sorted by distance first
         test = searchService.findRestaurants("Del", null, null, null, null);
         Assertions.assertEquals(5, test.size());
         Assertions.assertTrue(test.get(0).getDistance() <= test.get(1).getDistance());
         Assertions.assertTrue(test.get(1).getDistance() <= test.get(2).getDistance());
         Assertions.assertTrue(test.get(2).getDistance() <= test.get(3).getDistance());
         Assertions.assertTrue(test.get(3).getDistance() <= test.get(4).getDistance());
    }


    @Test
    public void testSearchCuisineOnly() {
        this.loader = new RestaurantLoader(RestaurantLoader.loadRestaurants(cuisines, "distance_restaurants.csv"), cuisines);
        this.searchService = new RestaurantSearchService(loader);

        //Test no matches found
        List<Restaurant> test = searchService.findRestaurants(null, null, null, null, "no match");
        Assertions.assertTrue(test.isEmpty());

        test = searchService.findRestaurants(null, null, null, null, "Kor");
        Assertions.assertEquals(1, test.size());

        //Test that if all match , sorted by distance first
        test = searchService.findRestaurants(null, null, null, null, "Spa");
        Assertions.assertEquals(3, test.size());
        Assertions.assertTrue(test.get(0).getDistance() <= test.get(1).getDistance());
        Assertions.assertTrue(test.get(1).getDistance() <= test.get(2).getDistance());

    }




}
