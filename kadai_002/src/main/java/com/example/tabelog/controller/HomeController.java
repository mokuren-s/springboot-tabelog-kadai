package com.example.tabelog.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.tabelog.entity.CategoryRestaurant;
import com.example.tabelog.entity.Restaurant;
import com.example.tabelog.repository.CategoryRepository;
import com.example.tabelog.repository.CategoryRestaurantRepository;
import com.example.tabelog.repository.RestaurantRepository;

@Controller
public class HomeController {
	private final RestaurantRepository restaurantRepository;
	private CategoryRestaurantRepository categoryRestaurantRepository = null;
	
	public HomeController(RestaurantRepository restaurantRepository, CategoryRepository categoryRepository) {
		this.restaurantRepository = restaurantRepository;
		this.categoryRestaurantRepository = categoryRestaurantRepository;
	}
	
	@GetMapping("/")
	public String index(@RequestParam(name = "name", required = false) String name, 
			            Model model) {
		List<Restaurant> newRestaurant = restaurantRepository.findTop10ByOrderByCreatedAtDesc();
		List<CategoryRestaurant> categoriesRestaurants = categoryRestaurantRepository.findCategoryIdsByRestaurantOrderByIdAsc(null);
		
		model.addAttribute("newRestaurant", newRestaurant);
        model.addAttribute("categoriesRestaurants", categoriesRestaurants);
		
		return "index";
	}
}
