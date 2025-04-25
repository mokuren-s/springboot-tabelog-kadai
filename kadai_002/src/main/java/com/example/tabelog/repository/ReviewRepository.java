package com.example.tabelog.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tabelog.entity.Restaurant;
import com.example.tabelog.entity.Review;
import com.example.tabelog.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	   public List<Review> findTop2ByRestaurantOrderByCreatedAtDesc(Restaurant restaurant);
	   public Review findByRestaurantAndUser(Restaurant restaurant, User user);
	   public long countByRestaurant(Restaurant restaurant);
	   public Page<Review> findByRestaurantOrderByCreatedAtDesc(Restaurant restaurant, Pageable pageable);
	}
