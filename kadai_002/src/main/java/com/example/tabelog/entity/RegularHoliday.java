package com.example.tabelog.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "regular_holidays")
@Data
public class RegularHoliday {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "day")
	private String day;

	@Column(name = "day_index")
	private Integer dayIndex;

	@OneToMany(mappedBy = "regularHoliday", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<RegularHolidayRestaurant> regularHolidaysRestaurants;
}
