package com.example.tabelog.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.tabelog.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
	public Restaurant findRestaurantById(Integer id);
	public Restaurant findFirstByOrderByIdDesc();
	public Page<Restaurant> findByNameLike(String keyword, Pageable pageable);
	public Page<Restaurant> findAllByOrderByCreatedAtDesc(Pageable pageable);
	public Page<Restaurant> findAllByOrderByLowestPriceAsc(Pageable pageable);

	
	// 店舗名・住所またはカテゴリ名で検索して新着順に並べ替え
	@Query("SELECT DISTINCT r FROM Restaurant r " +
			"LEFT JOIN r.categoriesRestaurants cr " +
			"WHERE r.name LIKE %:name% " +
			"OR r.address LIKE %:address% " +
			"OR cr.category.name LIKE %:categoryName% " +
			"ORDER BY r.createdAt DESC")
	public Page<Restaurant> findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByCreatedAtDesc(
			@Param("name") String nameKeyword,
			@Param("address") String addressKeyword,
			@Param("categoryName") String categoryNameKeyword,
			Pageable pageable);
	
	// 店舗名・住所またはカテゴリ名で検索して価格の安い順に並べ替え
	@Query("SELECT DISTINCT r FROM Restaurant r " +
			"LEFT JOIN r.categoriesRestaurants cr " +
			"WHERE r.name LIKE %:name% " +
			"OR r.address LIKE %:address% " +
			"OR cr.category.name LIKE %:categoryName% " +
			"ORDER BY r.lowestPrice ASC")
	public Page<Restaurant> findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByLowestPriceAsc(
			@Param("name") String nameKeyword,
			@Param("address") String addressKeyword,
			@Param("categoryName") String categoryNameKeyword,
			Pageable pageable);
	
	// 店舗名・住所またはカテゴリ名で検索して評価の高い順に並べ替え
	@Query("SELECT r FROM Restaurant r " +
			"LEFT JOIN r.categoriesRestaurants cr " +
			"LEFT JOIN r.reviews rev " +
			"WHERE r.name LIKE %:name% " +
			"OR r.address LIKE %:address% " +
			"OR cr.category.name LIKE %:categoryName% " +
			"GROUP BY r.id " +
			"ORDER BY AVG(rev.score) DESC")
	public Page<Restaurant> findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByAverageScoreDesc(
			@Param("name") String nameKeyword,
			@Param("address") String addressKeyword,
			@Param("categoryName") String categoryNameKeyword,
			Pageable pageable);
	
	// 店舗名・住所またはカテゴリ名で検索して予約数の多い順に並べ替え
	@Query("SELECT r FROM Restaurant r " +
			"LEFT JOIN r.categoriesRestaurants cr " +
			"LEFT JOIN r.reservations res " +
			"WHERE r.name LIKE %:name% " +
			"OR r.address LIKE %:address% " +
			"OR cr.category.name LIKE %:categoryName% " +
			"GROUP BY r.id " +
			"ORDER BY COUNT(DISTINCT res.id) DESC")
	public Page<Restaurant> findByNameLikeOrAddressLikeOrCategoryNameLikeOrderByReservationCountDesc(
			@Param("name") String nameKeyword,
			@Param("address") String addressKeyword,
			@Param("categoryName") String categoryNameKeyword,
			Pageable pageable);

	
	
	// カテゴリ指定で新着順
	@Query("SELECT r FROM Restaurant r " +
			"INNER JOIN r.categoriesRestaurants cr " +
			"WHERE cr.category.id = :categoryId " +
			"ORDER BY r.createdAt DESC")
	public Page<Restaurant> findByCategoryIdOrderByCreatedAtDesc(
			@Param("categoryId") Integer categoryId,
			Pageable pageable);
	
	// カテゴリ指定で低価格順
	@Query("SELECT r FROM Restaurant r " +
			"INNER JOIN r.categoriesRestaurants cr " +
			"WHERE cr.category.id = :categoryId " +
			"ORDER BY r.lowestPrice ASC")
	public Page<Restaurant> findByCategoryIdOrderByLowestPriceAsc(
			@Param("categoryId") Integer categoryId,
			Pageable pageable);
	
	// カテゴリ指定で平均評価の高い順
	@Query("SELECT r FROM Restaurant r " +
			"INNER JOIN r.categoriesRestaurants cr " +
			"LEFT JOIN r.reviews rev " +
			"WHERE cr.category.id = :categoryId " +
			"GROUP BY r.id " +
			"ORDER BY AVG(rev.score) DESC")
	public Page<Restaurant> findByCategoryIdOrderByAverageScoreDesc(
			@Param("categoryId") Integer categoryId,
			Pageable pageable);

	//　カテゴリ指定で予約数が多い順
	@Query("SELECT r FROM Restaurant r " +
			"INNER JOIN r.categoriesRestaurants cr " +
			"LEFT JOIN r.reservations res " +
			"WHERE cr.category.id = :categoryId " +
			"GROUP BY r.id " +
			"ORDER BY COUNT(res) DESC")
	public Page<Restaurant> findByCategoryIdOrderByReservationCountDesc(
			@Param("categoryId") Integer categoryId, Pageable pageable);

	
	
	// 低価格帯の新着順
	public Page<Restaurant> findByLowestPriceLessThanEqualOrderByCreatedAtDesc(Integer price, Pageable pageable);
	// 低価格帯の価格順
	public Page<Restaurant> findByLowestPriceLessThanEqualOrderByLowestPriceAsc(Integer price, Pageable pageable);

	// 低価格帯の平均評価の高い順
	@Query("SELECT r FROM Restaurant r " +
			"LEFT JOIN r.reviews rev " +
			"WHERE r.lowestPrice <= :price " +
			"GROUP BY r.id " +
			"ORDER BY AVG(rev.score) DESC")
	public Page<Restaurant> findByLowestPriceLessThanEqualOrderByAverageScoreDesc(
			@Param("price") Integer price, Pageable pageable);

	// 低価格帯の予約数が多い順
	@Query("SELECT r FROM Restaurant r " +
			"LEFT JOIN r.reservations res " +
			"WHERE r.lowestPrice <= :price " +
			"GROUP BY r.id " +
			"ORDER BY COUNT(res) DESC")
	public Page<Restaurant> findByLowestPriceLessThanEqualOrderByReservationCountDesc(
			@Param("price") Integer price,
			Pageable pageable);

	
	
	// 営業している店舗を検索
	@Query("SELECT rh.dayIndex FROM RegularHoliday rh " +
			"INNER JOIN rh.regularHolidaysRestaurants rhr " +
			"INNER JOIN rhr.restaurant r " +
			"WHERE r.id = :restaurantId")
	public List<Integer> findDayIndexesByRestaurantId(
			@Param("restaurantId") Integer restaurantId);


	// 全ての店舗を平均評価が高い順に並べる
	@Query("SELECT r FROM Restaurant r " +
			"LEFT JOIN r.reviews rev " +
			"GROUP BY r.id " +
			"ORDER BY AVG(rev.score) DESC")
	public Page<Restaurant> findAllByOrderByAverageScoreDesc(Pageable pageable);

	// 全ての店舗を予約数が多い順に並べる。
	@Query("SELECT r FROM Restaurant r " +
			"LEFT JOIN r.reservations res " +
			"GROUP BY r.id " +
			"ORDER BY COUNT(res) DESC ")
	public Page<Restaurant> findAllByOrderByReservationCountDesc(Pageable pageable);


}

/*
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
} */
