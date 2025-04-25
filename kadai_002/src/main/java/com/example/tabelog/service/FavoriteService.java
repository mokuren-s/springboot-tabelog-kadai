package com.example.tabelog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tabelog.entity.Favorite;
import com.example.tabelog.entity.Restaurant;
import com.example.tabelog.entity.User;
import com.example.tabelog.repository.FavoriteRepository;

@Service
public class FavoriteService {
   private final FavoriteRepository favoriteRepository;        
   
   public FavoriteService(FavoriteRepository favoriteRepository) {        
       this.favoriteRepository = favoriteRepository;        
   }     
   
   @Transactional
   public void create(Restaurant restaurant, User user) {
       Favorite favorite = new Favorite();        
       
       favorite.setRestaurant(restaurant);                
       favorite.setUser(user);
                   
       favoriteRepository.save(favorite);
   }
   
   public boolean isFavorite(Restaurant restaurant, User user) {
       return favoriteRepository.findByRestaurantAndUser(restaurant, user) != null;
   }
}
