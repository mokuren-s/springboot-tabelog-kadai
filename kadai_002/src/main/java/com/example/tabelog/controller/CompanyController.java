package com.example.tabelog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.tabelog.entity.Company;
import com.example.tabelog.form.CompanyEditForm;
import com.example.tabelog.repository.CompanyRepository;
import com.example.tabelog.service.CompanyService;

@RequestMapping("/company")
@Controller
public class CompanyController {

	private final CompanyRepository companyRepository;
	private final CompanyService companyService;

    public CompanyController(CompanyRepository companyRepository, CompanyService companyService) {
        this.companyRepository = companyRepository;
        this.companyService = companyService;
    }

    @GetMapping("/index")
    public String index(Model model) {
        Company company = null;
        try {
            company = companyRepository.findById(1).orElse(null);
            if (company == null) {
                model.addAttribute("errorMessage", "会社情報が見つかりませんでした。");
                return "company/error";
            }
        } catch (Exception e) {
            e.printStackTrace(); // スタックトレースを表示
            model.addAttribute("errorMessage", "会社情報の取得に失敗しました。");
            return "company/error";
        }

        model.addAttribute("company", company);
        return "company/index";
    }
    
    @GetMapping("/edit")
    public String edit(Model model) {        
    	
    	Company company = companyRepository.findById(1).orElse(null);
    	
        CompanyEditForm companyEditForm = new CompanyEditForm(company.getId(),
        													  company.getName(),
        													  company.getPostalCode(),
        													  company.getAddress(),
        													  company.getRepresentative(),
        													  company.getCapital(), 
        													  company.getBusiness(),
        													  company.getNumberOfEmployees());
        
        return "company/edit";
    }    
    
    @PostMapping("/update")
    public String update(@ModelAttribute @Validated CompanyEditForm companyEditForm, 
    					 BindingResult bindingResult, 
    					 RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "company/edit";
        }
        companyService.update(companyEditForm);
        
        redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");
        
        return "redirect:/company/index";
    }
}
