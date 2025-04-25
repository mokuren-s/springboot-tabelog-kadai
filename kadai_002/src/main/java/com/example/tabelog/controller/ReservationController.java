package com.example.tabelog.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.tabelog.entity.Reservation;
import com.example.tabelog.entity.Restaurant;
import com.example.tabelog.entity.User;
import com.example.tabelog.form.ReservationInputForm;
import com.example.tabelog.form.ReservationRegisterForm;
import com.example.tabelog.repository.ReservationRepository;
import com.example.tabelog.repository.RestaurantRepository;
import com.example.tabelog.security.UserDetailsImpl;
import com.example.tabelog.service.ReservationService;

@Controller
public class ReservationController {
	private final ReservationRepository reservationRepository;
	private final RestaurantRepository restaurantRepository;
	private final ReservationService reservationService;
    
    public ReservationController(ReservationRepository reservationRepository,
    		                     RestaurantRepository restaurantRepository,
    		                     ReservationService reservationService) {        
        this.reservationRepository = reservationRepository;
        this.restaurantRepository = restaurantRepository;
        this.reservationService = reservationService;
    }    

    @GetMapping("/reservations")
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
        User user = userDetailsImpl.getUser();
        Page<Reservation> reservationPage = reservationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        
        model.addAttribute("reservationPage", reservationPage);         
        
        return "reservations/index";
    }
    
    @GetMapping("/restaurants/{id}/reservations/input")
    public String input(@PathVariable(name = "id") Integer id,
                        @ModelAttribute @Validated ReservationInputForm reservationInputForm,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model) {
    	
    	Restaurant restaurant = restaurantRepository.getReferenceById(id);
    	Integer numberOfPeople = reservationInputForm.getNumberOfPeople();   
        Integer seatingCapacity = restaurant.getSeatingCapacity();
    
        if (numberOfPeople != null) {
            if (!reservationService.isWithinCapacity(numberOfPeople, seatingCapacity)) {
                FieldError fieldError = new FieldError(bindingResult.getObjectName(), "numberOfPeople", "予約人数が定員を超えています。");
                bindingResult.addError(fieldError);                
            }            
        }         
        
        if (bindingResult.hasErrors()) {            
            model.addAttribute("restaurant", restaurant);            
            model.addAttribute("errorMessage", "予約内容に不備があります。"); 
            return "restaurants/show";
        }
        
        redirectAttributes.addFlashAttribute("reservationInputForm", reservationInputForm);           
        
        return "redirect:/restaurants/{id}/reservations/confirm";
    }
    
    @GetMapping("/restaurants/{id}/reservations/confirm")
    public String confirm(@PathVariable(name = "id") Integer id,
                          @ModelAttribute ReservationInputForm reservationInputForm,
                          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,                          
                          Model model) {
    	
    	Restaurant restaurant = restaurantRepository.getReferenceById(id);
    	User user = userDetailsImpl.getUser();
    	
    	// 予約日時を所得する
    	LocalDateTime reservedDatetime = reservationInputForm.getReservatedDatetime();
    	
    	ReservationRegisterForm reservationRegisterForm = new ReservationRegisterForm(restaurant.getId(), user.getId(), reservedDatetime.toString(), reservationInputForm.getNumberOfPeople());
        
        model.addAttribute("restaurant", restaurant);  
        model.addAttribute("reservationRegisterForm", reservationRegisterForm);       
        
        return "reservations/confirm";
    }
    
    @PostMapping("/restaurants/{id}/reservations/create")
    public String create(@ModelAttribute ReservationRegisterForm reservationRegisterForm) {                
        reservationService.create(reservationRegisterForm);        
        
        return "redirect:/reservations?reserved";
    }
}
