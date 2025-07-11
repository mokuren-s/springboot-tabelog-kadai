package com.example.tabelog.form;

import java.time.LocalTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RestaurantRegisterForm {
	@NotBlank(message = "店舗名を入力してください。")
    private String name;
    
    private MultipartFile imageFile;
    
    @NotBlank(message = "店舗の説明を入力してください。")
    private String description;
    
    @NotNull(message = "最低価格を入力してください。")
    @Min(value = 1, message = "価格は1円以上に設定してください。")
    private Integer lowestPrice;
    
    @NotNull(message = "最高価格を入力してください。")
    @Max(value = 100000, message = "価格は10万円以下に設定してください。")
    private Integer highestPrice;
    
    @NotBlank(message = "郵便番号を入力してください。")
    @Pattern(regexp = "^[0-9]{7}$", message = "郵便番号は7桁の半角数字で入力してください。")
    private String postalCode;
    
    @NotBlank(message = "住所を入力してください。")
    private String address;
    
    @NotBlank(message = "開店時間を入力してください。")
    private LocalTime openingTime;
    
    @NotBlank(message = "閉店時間を入力してください。")
    private LocalTime closingTime;
    
    @NotBlank(message = "定休日を入力してください。")
    private String regularHoliday;
    
    @NotNull(message = "座席数を入力してください。")
    @Min(value = 1, message = "座席数は1人以上に設定してください。")
    private Integer seatingCapacity;   
    
    @NotNull(message = "カテゴリを選んでください")
    private Integer category;
    
    private List<Integer> categoryIds;
    private List<Integer> regularHolidayIds;
}