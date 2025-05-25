package com.example.tabelog.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.tabelog.entity.Category;
import com.example.tabelog.entity.Restaurant;
import com.example.tabelog.repository.CategoryRepository;
import com.example.tabelog.repository.RestaurantRepository;
import com.example.tabelog.security.UserDetailsImpl;
import com.example.tabelog.service.CategoryService;
import com.example.tabelog.service.RestaurantService;

@Controller
public class HomeController {
	private final RestaurantService restaurantService;
	private final CategoryService categoryService;
	
	public HomeController(RestaurantRepository restaurantRepository, CategoryRepository categoryRepository, RestaurantService restaurantService, CategoryService categoryService) {
		this.restaurantService = restaurantService;
		this.categoryService = categoryService;
	}
	
	@GetMapping("/")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
        if (userDetailsImpl != null && userDetailsImpl.getUser().getRole().getName().equals("ROLE_ADMIN")) {
            return "redirect:/admin";
        }
        
		Page<Restaurant> newRestaurants = restaurantService.findAllRestaurantsByOrderByAverageScoreDesc(PageRequest.of(0, 5));
		Category washoku = categoryService.findFirstCategoryByName("和食");
		Category udon = categoryService.findFirstCategoryByName("うどん");
		Category don = categoryService.findFirstCategoryByName("丼物");
		Category ramen = categoryService.findFirstCategoryByName("ラーメン");
		Category oden = categoryService.findFirstCategoryByName("おでん");
		Category fried = categoryService.findFirstCategoryByName("揚げ物");
		List<Category> categories = categoryService.findAllCategories();
		
		model.addAttribute("newRestaurants", newRestaurants);
		model.addAttribute("washoku", washoku);
		model.addAttribute("udon", udon);
		model.addAttribute("don", don);
		model.addAttribute("ramen", ramen);
		model.addAttribute("oden", oden);
		model.addAttribute("fried", fried);
		model.addAttribute("categories", categories);
		
		return "index";
	}
}
