package com.example.tabelog.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TermEditForm {
   @NotBlank(message = "内容を入力してください。")
   private String content;
}
