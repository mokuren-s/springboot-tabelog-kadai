package com.example.tabelog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.tabelog.entity.Category;
import com.example.tabelog.entity.CategoryRestaurant;
import com.example.tabelog.entity.Restaurant;

public interface CategoryRestaurantRepository extends JpaRepository <CategoryRestaurant, Integer> {
	
	// 特定の店舗に関連するカテゴリのIDを取得するカスタムクエリ
	@Query("SELECT cr.category.id FROM CategoryRestaurant cr WHERE cr.restaurant = :restaurant ORDER BY cr.id ASC")
	public List<Integer> findCategoryIdsByRestaurantOrderByIdAsc(@Param("restaurant") Restaurant restaurant);

	// 指定されたカテゴリと店舗に関連付けられたCategoryStoreエンティティを取得
    public Optional<CategoryRestaurant> findByCategoryAndRestaurant(Category category, Restaurant restaurant);
    
    // 特定の店舗に関連する全てのCategoryRestaurantエンティティを取得
	List<CategoryRestaurant> findByRestaurantOrderByIdAsc(Restaurant restaurant);
	
	// List<CategoryRestaurant> findByCategoryAndRestaurantOrderByHighestPriceDesc(Integer category, Pageable pageable);
	// List<CategoryRestaurant> findByCategoryAndRestaurantOrderByCreatedAtDesc(Integer category, Pageable pageable);
    
}
