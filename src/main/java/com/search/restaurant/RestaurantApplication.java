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


	public static void main(String[] args) {
		SpringApplication.run(RestaurantApplication.class, args);
	}


}
