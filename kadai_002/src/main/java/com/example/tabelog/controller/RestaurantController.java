package com.example.tabelog.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.tabelog.entity.Favorite;
import com.example.tabelog.entity.Restaurant;
import com.example.tabelog.entity.Review;
import com.example.tabelog.entity.User;
import com.example.tabelog.form.ReservationInputForm;
import com.example.tabelog.repository.CategoryRepository;
import com.example.tabelog.repository.CategoryRestaurantRepository;
import com.example.tabelog.repository.FavoriteRepository;
import com.example.tabelog.repository.RestaurantRepository;
import com.example.tabelog.repository.ReviewRepository;
import com.example.tabelog.security.UserDetailsImpl;
import com.example.tabelog.service.FavoriteService;
import com.example.tabelog.service.ReviewService;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {
	private final RestaurantRepository restaurantRepository;
	private final CategoryRestaurantRepository categoryRestaurantRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewService reviewService;
	private final FavoriteRepository favoriteRepository;
    private final FavoriteService favoriteService;
	
	public RestaurantController(RestaurantRepository restaurantRepository, CategoryRepository categoryRepository, ReviewRepository reviewRepository, ReviewService reviewService, FavoriteRepository favoriteRepository, FavoriteService favoriteService, CategoryRestaurantRepository categoryRestaurantRepository) {
		this.restaurantRepository = restaurantRepository;
		//this.restaurantService = restaurantService;
		this.categoryRestaurantRepository = categoryRestaurantRepository;
		this.reviewRepository = reviewRepository;
		this.reviewService = reviewService;
		this.favoriteRepository = favoriteRepository;
        this.favoriteService = favoriteService;
	}
	
	@GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword,
    		            @RequestParam(name = "area", required = false) String area,
                        @RequestParam(name = "category", required = false) Integer category,
                        @RequestParam(name = "highestPrice", required = false) Integer highestPrice,
                        @RequestParam(name = "order", required = false) String order,
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        Model model) {
		
		Page<Restaurant> restaurantPage = null;
		
		// キーワードで検索
		if (keyword != null && !keyword.isEmpty()) {
			// 価格の昇順
			if (order != null && order.equals("highestPriceDesc")) {
				restaurantPage = restaurantRepository.findByNameLikeOrAddressLikeOrderByHighestPriceDesc("%" + keyword + "%", "%" + keyword + "%", pageable);
			// 作成日時の降順でソート（デフォルト）
			} else  {
				restaurantPage = restaurantRepository.findByNameLikeOrAddressLikeOrderByCreatedAtDesc("%" + keyword + "%", "%" + keyword + "%", pageable);
			}
		// エリアで検索
		} else if (area != null && !area.isEmpty()) {
			if (order != null && order.equals("highestPriceDesc")) {
				restaurantPage = restaurantRepository.findByAddressLikeOrderByHighestPriceDesc("%" + keyword + "%", "%" + keyword + "%", pageable);
			} else  {
				restaurantPage = restaurantRepository.findByAddressLikeOrderByCreatedAtDesc("%" + keyword + "%", "%" + keyword + "%", pageable);
			}
		// カテゴリIDで検索
		} else if (category != null) {
			// カテゴリIDが指定されている場合の処理
			if (order != null && order.equals("highestPriceDesc")) {
				categoryRestaurantRepository.findByCategoryAndRestaurantOrderByHighestPriceDesc(category, pageable);
		    } else {
		    	categoryRestaurantRepository.findByCategoryAndRestaurantOrderByCreatedAtDesc(category, pageable);
			    }
		// 上限価格で検索
		} else if (highestPrice != null) {
			if (order != null && order.equals("highestPriceDesc")) {
				restaurantPage = restaurantRepository.findByHighestPriceLessThanEqualOrderByCreatedAtDesc(highestPrice, pageable);
			} else {
				restaurantPage = restaurantRepository.findByHighestPriceLessThanEqualOrderByHighestPriceDesc(highestPrice, pageable);
				}
		// 全店舗検索
        } else {
        	if (order != null && order.equals("highestPriceDesc")) {
        		restaurantPage = restaurantRepository.findAllByOrderByHighestPriceDesc(pageable);
        	} else {
        		restaurantPage = restaurantRepository.findAllByOrderByCreatedAtDesc(pageable);
        	}
        }                
        
        model.addAttribute("restaurantPage", restaurantPage);
        model.addAttribute("category", category);
        model.addAttribute("keyword", keyword);
        model.addAttribute("order", order);                              
        
        return "restaurants/index";
    }
	
	@GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id,
    		           @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    		           Model model) {
        Restaurant restaurant = restaurantRepository.getReferenceById(id);
        Favorite favorite = null;
        boolean hasUserAlreadyReviewed = false;
        boolean isFavorite = false;
        
        if (userDetailsImpl != null) {
            User user = userDetailsImpl.getUser();
            hasUserAlreadyReviewed = reviewService.hasUserAlreadyReviewed(restaurant, user);
            isFavorite = favoriteService.isFavorite(restaurant, user);
            if (isFavorite) {
                favorite = favoriteRepository.findByRestaurantAndUser(restaurant, user);
            }
        }
        
        List<Review> newReviews = reviewRepository.findTop2ByRestaurantOrderByCreatedAtDesc(restaurant);        
        long totalReviewCount = reviewRepository.countByRestaurant(restaurant);
        
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("reservationInputForm", new ReservationInputForm());
        model.addAttribute("favorite", favorite);
        model.addAttribute("hasUserAlreadyReviewed", hasUserAlreadyReviewed);
        model.addAttribute("newReviews", newReviews);
        model.addAttribute("totalReviewCount", totalReviewCount);
        model.addAttribute("isFavorite", isFavorite);
        
        return "restaurants/show";
	}
}
