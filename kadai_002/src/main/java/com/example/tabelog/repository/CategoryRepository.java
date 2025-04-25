package com.example.tabelog.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tabelog.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	List<Category> findByName(String name);
	// カテゴリで検索（カテゴリ名）
	Page<Category> findByNameLike(String categoryKeyword, Pageable pageable);   
	// カテゴリで検索（新着順）
	Page<Category> findByNameLikeOrderByCreatedAtDesc(String category, Pageable pageable);
	// カテゴリで検索（低価格順）
	// Page<Category> findByNameLikeOrderByLowestPriceAsc(String category, Pageable pageable);
	// カテゴリで検索（高価格順）
	// Page<Category> findByNameLikeOrderByHighestPriceDesc(String category, Pageable pageable);

}
