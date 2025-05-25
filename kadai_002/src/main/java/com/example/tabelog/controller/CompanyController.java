package com.example.tabelog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.tabelog.entity.Company;
import com.example.tabelog.service.CompanyService;

@Controller
@RequestMapping("/company")
public class CompanyController {
   private final CompanyService companyService;

   public CompanyController(CompanyService companyService) {
       this.companyService = companyService;
   }

   @GetMapping
   public String index(Model model) {
       Company company = companyService.findFirstCompanyByOrderByIdDesc();

       model.addAttribute("company", company);

       return "company/index";
   }
}
