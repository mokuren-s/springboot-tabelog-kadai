package com.example.tabelog.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tabelog.entity.Company;
import com.example.tabelog.form.CompanyEditForm;
import com.example.tabelog.repository.CompanyRepository;

@Service
public class CompanyService {

	    private final CompanyRepository companyRepository;
	    
	    public CompanyService(CompanyRepository companyRepository) {
	        this.companyRepository = companyRepository;
	    }    
	//  ユーザー情報更新
    @Transactional
    public void update(CompanyEditForm companyEditForm) {
        Optional<Company> optionalCompany = companyRepository.findById(companyEditForm.getId());
        
        if (optionalCompany.isPresent()) {
        	
            Company company = optionalCompany.get();

	        company.setName(companyEditForm.getName());
	        company.setPostalCode(companyEditForm.getPostalCode());
	        company.setAddress(companyEditForm.getAddress());
	        company.setRepresentative(companyEditForm.getRepresentative());
	        company.setCapital(companyEditForm.getCapital());
	        company.setBusiness(companyEditForm.getBusiness());
	        company.setNumberOfEmployees(companyEditForm.getNumberOfEmployees());
	        
	        companyRepository.save(company);
        
        } else {
            // ユーザーが存在しない場合の処理
            // 例：例外を投げる、エラーメッセージをログに出力するなど
            throw new RuntimeException("company not found with id: " + companyEditForm.getId());
        }
    }
}
