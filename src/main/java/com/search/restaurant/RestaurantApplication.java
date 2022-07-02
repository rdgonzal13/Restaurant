package com.search.restaurant;

import com.opencsv.CSVReader;
import com.search.restaurant.model.Cuisine;
import com.search.restaurant.model.Restaurant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class RestaurantApplication {

	//TODO: handle csv better
	public static final List<Restaurant> RESTAURANTS = new ArrayList<>();

	public static final List<Cuisine> CUISINES = new ArrayList<>();

	public static void main(String[] args) {
		SpringApplication.run(RestaurantApplication.class, args);
	}


	@PostConstruct
	public void init(){
		readAllRestaurants();
		readAllCuisines();

		System.out.println("Successfully read all cuisines and restaurants");
	}

	private void readAllRestaurants(){
		URL resource = getClass().getClassLoader().getResource("restaurants.csv");
		assert resource != null;
		try (CSVReader csvReader = new CSVReader(new FileReader(new File(resource.toURI())))) {
			List<String []> allValues = csvReader.readAll();
			allValues.remove(0);
			for(String [] values : allValues){
				RESTAURANTS.add(Restaurant.builder()
						.name(values[0])
						.rating(Integer.parseInt(values[1]))
						.distance(Integer.parseInt(values[2]))
						.price(Integer.parseInt(values[3]))
						.cuisineId(Integer.parseInt(values[4]))
						.build());
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	private void readAllCuisines(){
		URL resource = getClass().getClassLoader().getResource("cuisines.csv");
		assert resource != null;
		try (CSVReader csvReader = new CSVReader(new FileReader(new File(resource.toURI())))) {
			List<String []> allValues = csvReader.readAll();
			allValues.remove(0);
			for(String [] values : allValues){
				CUISINES.add(Cuisine.builder()
						.id(Integer.parseInt(values[0]))
						.name(values[1]).build());
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}
