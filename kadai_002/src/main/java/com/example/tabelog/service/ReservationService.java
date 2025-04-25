package com.example.tabelog.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tabelog.entity.Reservation;
import com.example.tabelog.entity.Restaurant;
import com.example.tabelog.entity.User;
import com.example.tabelog.form.ReservationRegisterForm;
import com.example.tabelog.repository.ReservationRepository;
import com.example.tabelog.repository.RestaurantRepository;
import com.example.tabelog.repository.UserRepository;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;
	private final RestaurantRepository restaurantRepository;
	private final UserRepository userRepository;
	
	public ReservationService(ReservationRepository reservationRepository,
			                  RestaurantRepository restaurantRepository,
			                  UserRepository userRepository) {
		this.reservationRepository = reservationRepository;
		this.restaurantRepository = restaurantRepository;
		this.userRepository = userRepository;
	}
	
	@Transactional
    public void create(ReservationRegisterForm reservationRegisterForm) { 
        Reservation reservation = new Reservation();
        Restaurant restaurant = restaurantRepository.getReferenceById(reservationRegisterForm.getRestaurantId());
        User user = userRepository.getReferenceById(reservationRegisterForm.getUserId());
        LocalDateTime reservedDatetime = LocalDateTime.parse(reservationRegisterForm.getReservedDatetime());
    	
    	reservation.setRestaurant(restaurant);
    	reservation.setUser(user);
    	reservation.setReservedDatetime(reservedDatetime);
    	reservation.setNumberOfPeople(reservationRegisterForm.getNumberOfPeople());
    	
    	reservationRepository.save(reservation);
	}
	
	// 予約人数が定員以下かどうかをチェックする
    public boolean isWithinCapacity(Integer numberOfPeople, Integer capacity) {
        return numberOfPeople <= capacity;
    }
}
