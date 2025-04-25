package com.example.tabelog.form;

import java.sql.Time;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestaurantEditForm {
	@NotNull
    private Integer id;
	
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
    private String postalCode;
    
    @NotBlank(message = "住所を入力してください。")
    private String address;
    
    @NotBlank(message = "開店時間を入力してください。")
    private Time openingTime;
    
    @NotBlank(message = "閉店時間を入力してください。")
    private Time closingTime;
    
    @NotBlank(message = "定休日を入力してください。")
    private String regularHoliday;
    
    @NotNull(message = "座席数を入力してください。")
    @Min(value = 1, message = "座席数は1人以上に設定してください。")
    private Integer seatingCapacity;   
    
    @NotNull(message = "カテゴリを選んでください")
    private Integer category;
}
