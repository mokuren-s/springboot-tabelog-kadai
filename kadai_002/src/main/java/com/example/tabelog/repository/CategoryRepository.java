package com.example.tabelog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tabelog.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	// カテゴリで検索（カテゴリ名）
	Page<Category> findByNameLike(String categoryKeyword, Pageable pageable);   
	
	// カテゴリで検索（低価格順）
	// Page<Category> findByNameLikeOrderByLowestPriceAsc(String category, Pageable pageable);
	// カテゴリで検索（高価格順）
	// Page<Category> findByNameLikeOrderByHighestPriceDesc(String category, Pageable pageable);
	
	// テスト用（idを基準に降順で並べ替え、最初の1件を取得する）
	public Category findFirstByOrderByIdDesc();
	// 名前基準で、最初の1件を取得
	public Category findFirstByName(String name);

}
