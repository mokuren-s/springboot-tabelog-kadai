package com.example.tabelog.controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.tabelog.entity.Category;
import com.example.tabelog.entity.Favorite;
import com.example.tabelog.entity.Restaurant;
import com.example.tabelog.entity.Review;
import com.example.tabelog.entity.User;
import com.example.tabelog.form.ReservationInputForm;
import com.example.tabelog.repository.CategoryRestaurantRepository;
import com.example.tabelog.repository.FavoriteRepository;
import com.example.tabelog.repository.ReviewRepository;
import com.example.tabelog.security.UserDetailsImpl;
import com.example.tabelog.service.CategoryService;
import com.example.tabelog.service.FavoriteService;
import com.example.tabelog.service.RestaurantService;
import com.example.tabelog.service.ReviewService;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {
	private final RestaurantService restaurantService;
	private final CategoryService categoryService;
	private final ReviewRepository reviewRepository;
	private final ReviewService reviewService;
	private final FavoriteRepository favoriteRepository;
    private final FavoriteService favoriteService;
	
	public RestaurantController(RestaurantService restaurantService, CategoryService categoryService, ReviewRepository reviewRepository, ReviewService reviewService, FavoriteRepository favoriteRepository, FavoriteService favoriteService, CategoryRestaurantRepository categoryRestaurantRepository) {
		this.restaurantService = restaurantService;
	    this.categoryService = categoryService;
		this.reviewRepository = reviewRepository;
		this.reviewService = reviewService;
		this.favoriteRepository = favoriteRepository;
        this.favoriteService = favoriteService;
	}
	
	@GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword,
    		            @RequestParam(name = "area", required = false) String area,
                        @RequestParam(name = "categoryId", required = false) Integer categoryId,
                        @RequestParam(name = "price", required = false) Integer price,
                        @RequestParam(name = "order", required = false) String order,
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        Model model) {
		
		Page<Restaurant> restaurantPage;
		
		// キーワードで検索
		if (keyword != null && !keyword.isEmpty()) {
			// 価格の昇順
			if (order != null && order.equals("lowestPriceAsc")) {
				restaurantPage = restaurantService.findRestaurantsByNameLikeOrAddressLikeOrCategoryNameLikeOrderByLowestPriceAsc(keyword, keyword, keyword, pageable);
			// 評価の降順
			} else if (order != null && order.equals("ratingDesc")) {
                restaurantPage = restaurantService.findRestaurantsByNameLikeOrAddressLikeOrCategoryNameLikeOrderByAverageScoreDesc(keyword, keyword, keyword, pageable);                
            // 予約数の降順
			} else if (order != null && order.equals("popularDesc")) {
                restaurantPage = restaurantService.findRestaurantsByNameLikeOrAddressLikeOrCategoryNameLikeOrderByReservationCountDesc(keyword, keyword, keyword, pageable);                
            // 作成日時の降順でソート（デフォルト）
			} else  {
				restaurantPage = restaurantService.findRestaurantsByNameLikeOrAddressLikeOrCategoryNameLikeOrderByCreatedAtDesc(keyword, keyword, keyword, pageable);
		    }
		// カテゴリで検索
		} else if (categoryId != null) {
			// 価格の昇順
            if (order != null && order.equals("lowestPriceAsc")) {
                restaurantPage = restaurantService.findRestaurantsByCategoryIdOrderByLowestPriceAsc(categoryId, pageable);
            // 評価の降順
            } else if (order != null && order.equals("ratingDesc")) {
                restaurantPage = restaurantService.findRestaurantsByCategoryIdOrderByAverageScoreDesc(categoryId, pageable);                
            // 予約数の降順
            } else if (order != null && order.equals("popularDesc")) {
                restaurantPage = restaurantService.findRestaurantsByCategoryIdOrderByReservationCountDesc(categoryId, pageable);                
            // 作成日時の降順
            } else {
                restaurantPage = restaurantService.findRestaurantsByCategoryIdOrderByCreatedAtDesc(categoryId, pageable);
            }
        // 低価格で検索
        } else if (price != null) {
        	// 価格の昇順
            if (order != null && order.equals("lowestPriceAsc")) {
                restaurantPage = restaurantService.findRestaurantsByLowestPriceLessThanEqualOrderByLowestPriceAsc(price, pageable);
            // 評価の降順
            } else if (order != null && order.equals("ratingDesc")) {
                restaurantPage = restaurantService.findRestaurantsByLowestPriceLessThanEqualOrderByAverageScoreDesc(price, pageable);                
             // 予約数の降順
            } else if (order != null && order.equals("popularDesc")) {
                restaurantPage = restaurantService.findRestaurantsByLowestPriceLessThanEqualOrderByReservationCountDesc(categoryId, pageable);                
            // 作成日時の降順
            } else {
                restaurantPage = restaurantService.findRestaurantsByLowestPriceLessThanEqualOrderByCreatedAtDesc(price, pageable);
            }
        // とにかく低価格順
        } else {
        	// 価格の昇順
            if (order != null && order.equals("lowestPriceAsc")) {
                restaurantPage = restaurantService.findAllRestaurantsByOrderByLowestPriceAsc(pageable);
            // 評価の降順
            } else if (order != null && order.equals("ratingDesc")) {
                restaurantPage = restaurantService.findAllRestaurantsByOrderByAverageScoreDesc(pageable);                
            // 予約数で降順
            } else if (order != null && order.equals("popularDesc")) {
                restaurantPage = restaurantService.findAllRestaurantsByOrderByReservationCountDesc(pageable);                
            // とにかく作成順
            } else {
                restaurantPage = restaurantService.findAllRestaurantsByOrderByCreatedAtDesc(pageable);
            }
        }

	       List<Category> categories = categoryService.findAllCategories();
	       model.addAttribute("restaurantPage", restaurantPage);
	       model.addAttribute("categories", categories);
	       model.addAttribute("keyword", keyword);
	       model.addAttribute("categoryId", categoryId);
	       model.addAttribute("price", price);
	       model.addAttribute("order", order);                              
        
        return "restaurants/index";
    }
	
	@GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id,
    		           @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    		           RedirectAttributes redirectAttributes,
    		           Model model) {
		
		Optional<Restaurant> optionalRestaurant  = restaurantService.findRestaurantById(id);

        if (optionalRestaurant.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "店舗が存在しません。");

            return "redirect:/restaurants";
        }
		
        Restaurant restaurant = optionalRestaurant.get();
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
