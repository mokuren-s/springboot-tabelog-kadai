package com.example.tabelog.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.tabelog.entity.Favorite;
import com.example.tabelog.entity.Restaurant;
import com.example.tabelog.entity.User;
import com.example.tabelog.repository.FavoriteRepository;
import com.example.tabelog.repository.RestaurantRepository;
import com.example.tabelog.security.UserDetailsImpl;
import com.example.tabelog.service.FavoriteService;

@Controller
public class FavoriteController {
   private final FavoriteRepository favoriteRepository;
   private final RestaurantRepository restaurantRepository;
   private final FavoriteService favoriteService;
   
   public FavoriteController(FavoriteRepository favoriteRepository, RestaurantRepository restaurantRepository, FavoriteService favoriteService) {        
       this.favoriteRepository = favoriteRepository;
       this.restaurantRepository = restaurantRepository;
       this.favoriteService = favoriteService;
   }
   
   @GetMapping("/favorites")
   public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
		               @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable,
		               Model model) {
	   
       User user = userDetailsImpl.getUser();   
       Page<Favorite> favoritePage = favoriteRepository.findByUserOrderByCreatedAtDesc(user, pageable);       
                           
       model.addAttribute("favoritePage", favoritePage);                
       
       return "favorites/index";
   }
   
   @PostMapping("/restaurants/{restaurantId}/favorites/create")
   public String create(@PathVariable(name = "restaurantd") Integer restaurantId,
                        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,                                                  
                        RedirectAttributes redirectAttributes,
                        Model model)
   {    
	   Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
       User user = userDetailsImpl.getUser();        
       
       favoriteService.create(restaurant, user);
       redirectAttributes.addFlashAttribute("successMessage", "お気に入りに追加しました。");    
       
       return "redirect:/restaurants/{restaurantId}";
   }
   
   @PostMapping("/restaurants/{restaurantId}/favorites/{favoriteId}/delete")
   public String delete(@PathVariable(name = "favoriteId") Integer favoriteId,
		                RedirectAttributes redirectAttributes) {
	   
       favoriteRepository.deleteById(favoriteId);
               
       redirectAttributes.addFlashAttribute("successMessage", "お気に入りを解除しました。");
       
       return "redirect:/restaurants/{restaurantId}";
   }
}
