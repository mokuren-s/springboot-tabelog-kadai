package com.example.tabelog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tabelog.entity.Favorite;
import com.example.tabelog.entity.Restaurant;
import com.example.tabelog.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
	   public Page<Favorite> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
	   public Favorite findByRestaurantAndUser(Restaurant restaurant, User user);
}
