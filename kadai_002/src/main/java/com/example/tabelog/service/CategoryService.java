package com.example.tabelog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tabelog.entity.Category;
import com.example.tabelog.form.CategoryEditForm;
import com.example.tabelog.form.CategoryRegisterForm;
import com.example.tabelog.repository.CategoryRepository;

@Service
public class CategoryService {
	
    private final CategoryRepository categoryRepository;    
    
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;        
    }    

    @Transactional //  @Transactionalアノテーション： データベース操作が完全に成功するか、すべて失敗するかのどちらかに白黒はっきりさせられるため、データの整合性を保つことができる
    public void create(CategoryRegisterForm CategoryRegisterForm) {
        Category category = new Category();      
        
        category.setName(CategoryRegisterForm.getName());
        
        categoryRepository.save(category);
    }
    
    
    @Transactional
    public void update(CategoryEditForm categoryEditForm) {
        Category category = categoryRepository.getReferenceById(categoryEditForm.getId());
        
        category.setName(categoryEditForm.getName());                

                    
        categoryRepository.save(category);
    }     
}
