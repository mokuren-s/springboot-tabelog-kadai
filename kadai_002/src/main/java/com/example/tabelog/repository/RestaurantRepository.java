package com.example.tabelog.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tabelog.entity.CategoryRestaurant;
import com.example.tabelog.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
	// 店舗名で検索
	Page<Restaurant> findByNameLike(String namekeyword, Pageable pageable);
	// 店舗名または住所で検索（新着順）
	Page<Restaurant> findByNameLikeOrAddressLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword, Pageable pageable);
	// 店舗名または住所で検索（低価格順）
	Page<Restaurant> findByNameLikeOrAddressLikeOrderByLowestPriceAsc(String nameKeyword, String addressKeyword, Pageable pageable);
	// 店舗名または住所で検索（高価格順）
	Page<Restaurant> findByNameLikeOrAddressLikeOrderByHighestPriceDesc(String nameKeyword, String addressKeyword, Pageable pageable);
	// エリアで検索（新着順）
	Page<Restaurant> findByAddressLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword, Pageable pageable);
	// エリアで検索（低価格順）
	Page<Restaurant> findAddressLikeOrderByLowestPriceAsc(String nameKeyword, String addressKeyword, Pageable pageable);
	// エリアで検索（高価格順）
	Page<Restaurant> findByAddressLikeOrderByHighestPriceDesc(String nameKeyword, String addressKeyword, Pageable pageable);
	// カテゴリで検索（新着順）
	Page<CategoryRestaurant> findByCategoryOrderByCreatedAtDesc(Integer category, Pageable pageable);
	// カテゴリで検索（低価格順）
	Page<CategoryRestaurant> findByCategoryOrderByLowestPriceAsc(Integer category, Pageable pageable);
	// カテゴリで検索（高価格順）
	Page<CategoryRestaurant> findByCategoryOrderByHighestPriceDesc(Integer category, Pageable pageable);
	// 上限価格で検索（新着順）
	Page<Restaurant> findByHighestPriceLessThanEqualOrderByCreatedAtDesc(Integer highestPrice, Pageable pageable);
	// 上限価格で検索（高価格順）
	Page<Restaurant> findByHighestPriceLessThanEqualOrderByHighestPriceDesc(Integer highestPrice, Pageable pageable);
	

	
	// とにかく新着順
	Page<Restaurant> findAllByOrderByCreatedAtDesc(Pageable pageable);
    // とにかく低価格順
	Page<Restaurant> findAllByOrderByLowestPriceAsc(Pageable pageable);
	// とにかく高価格順
	Page<Restaurant> findAllByOrderByHighestPriceDesc(Pageable pageable);
	// リストで新着10件
	List<Restaurant> findTop10ByOrderByCreatedAtDesc();
}
