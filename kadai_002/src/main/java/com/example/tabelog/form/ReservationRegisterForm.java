package com.example.tabelog.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationRegisterForm {
	private Integer restaurantId;
	
	private Integer userId;
	
	private String reservedDatetime;
	
	private Integer numberOfPeople;
	
}
