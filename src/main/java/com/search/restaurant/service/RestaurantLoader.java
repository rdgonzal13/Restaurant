package com.search.restaurant.service;

import com.opencsv.CSVReader;
import com.search.restaurant.model.Cuisine;
import com.search.restaurant.model.Restaurant;
import lombok.Data;

import java.io.File;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@Data
public class RestaurantLoader {

    private final List<Restaurant> restaurants;

    private final List<Cuisine> cuisines;

    public RestaurantLoader(List<Restaurant> restaurants, List<Cuisine> cuisines) {
        this.restaurants = restaurants;
        this.cuisines = cuisines;
    }

    public static List<Restaurant> loadRestaurants(List<Cuisine> cuisines) {
        List<Restaurant> records = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(new File(getResourceURL("restaurants.csv"))))) {
            List<String[]> allValues = csvReader.readAll();
            allValues.remove(0);
            for (String[] values : allValues) {
                int cuisineId = Integer.parseInt(values[4]);
                records.add(Restaurant.builder()
                        .name(values[0])
                        .rating(Integer.parseInt(values[1]))
                        .distance(Integer.parseInt(values[2]))
                        .price(Integer.parseInt(values[3]))
                        .cuisine(cuisines.stream()
                                .filter(x -> x.getId() == cuisineId)
                                .map(Cuisine::getName).findFirst()
                                .orElse("other")) //only 19 so no need to map;
                        .cuisineId(cuisineId)
                        .build());
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return records;
    }

    public static List<Cuisine> loadCuisines() {
        List<Cuisine> cuisines = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(new File(getResourceURL("cuisines.csv"))))) {
            List<String[]> allValues = csvReader.readAll();
            allValues.remove(0);
            for (String[] values : allValues) {
                cuisines.add(Cuisine.builder()
                        .id(Integer.parseInt(values[0]))
                        .name(values[1]).build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cuisines;
    }

    private static java.net.URI getResourceURL(String fileName) {
        try {
            URL resource = RestaurantLoader.class.getClassLoader().getResource(fileName);
            assert resource != null;
            return resource.toURI();
        } catch (URISyntaxException e) {
            System.out.println(String.format("Could not fetch %s information", fileName));
        }
        return null;
    }
}
