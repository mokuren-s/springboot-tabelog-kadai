package com.example.tabelog.form;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationInputForm {
	@NotBlank(message = "予約日と予約時間を選択してください。")
	private String fromReservatedDatetime;
	
	@NotNull(message = "予約人数を選択してください。")
	private Integer numberOfPeople;
	
	// 予約日時を取得する
    public LocalDateTime getReservatedDatetime() {
        String reservedDatetime = getFromReservatedDatetime();
        return LocalDateTime.parse(reservedDatetime);
    }
}
