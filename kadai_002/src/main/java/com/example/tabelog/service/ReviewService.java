package com.example.tabelog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tabelog.entity.Restaurant;
import com.example.tabelog.entity.Review;
import com.example.tabelog.entity.User;
import com.example.tabelog.form.ReviewEditForm;
import com.example.tabelog.form.ReviewRegisterForm;
import com.example.tabelog.repository.ReviewRepository;

@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;        
	   
	   public ReviewService(ReviewRepository reviewRepository) {        
	       this.reviewRepository = reviewRepository;        
	   }     
	   
	   @Transactional
	   public void create(Restaurant restaurant, User user, ReviewRegisterForm reviewRegisterForm) {
	       Review review = new Review();        
	       
	       review.setRestaurant(restaurant);                
	       review.setUser(user);
	       review.setScore(reviewRegisterForm.getScore());
	       review.setContent(reviewRegisterForm.getContent());
	                   
	       reviewRepository.save(review);
	   }     
	   
	   @Transactional
	   public void update(ReviewEditForm reviewEditForm) {
	       Review review = reviewRepository.getReferenceById(reviewEditForm.getId());        
	       
	       review.setScore(reviewEditForm.getScore());                
	       review.setContent(reviewEditForm.getContent());
	                   
	       reviewRepository.save(review);
	   }    
	   
	   public boolean hasUserAlreadyReviewed(Restaurant restaurant, User user) {
	       return reviewRepository.findByRestaurantAndUser(restaurant, user) != null;
	   }
}
