package com.example.tabelog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.tabelog.entity.RegularHoliday;
import com.example.tabelog.entity.RegularHolidayRestaurant;
import com.example.tabelog.entity.Restaurant;

public interface RegularHolidayRestaurantRepository extends JpaRepository<RegularHolidayRestaurant, Integer> {
	   @Query("SELECT rhr.regularHoliday.id FROM RegularHolidayRestaurant rhr WHERE rhr.restaurant = :restaurant")
	   public List<Integer> findRegularHolidayIdsByRestaurant(@Param("restaurant") Restaurant restaurant);

	   public Optional<RegularHolidayRestaurant> findByRegularHolidayAndRestaurant(RegularHoliday regularHoliday, Restaurant restaurant);
	   public List<RegularHolidayRestaurant> findByRestaurant(Restaurant restaurant);
	}
