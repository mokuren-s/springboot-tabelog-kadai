package com.example.tabelog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	// すべてのカテゴリをページングされた状態で取得する
	public Page<Category> findAllCategories(Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}

	// 指定されたキーワードをカテゴリ名に含むカテゴリを、ページングされた状態で取得する
	public Page<Category> findCategoriesByNameLike(String keyword, Pageable pageable) {
		return categoryRepository.findByNameLike("%" + keyword + "%", pageable);
	}

	// 指定したidを持つカテゴリを取得する
	public Optional<Category> findCategoryById(Integer id) {
		return categoryRepository.findById(id);
	}

	// カテゴリのレコード数を取得する
	public long countCategories() {
		return categoryRepository.count();
	}

	// idを基準に降順で並べ替え、最初の1件を取得する（降順なので、最大値となる）
	public Category findFirstCategoryByOrderByIdDesc() {
		return categoryRepository.findFirstByOrderByIdDesc();
	}
	
	// すべてのカテゴリをリスト形式で取得する
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }
    
	// 指定したカテゴリ名を持つ最初のカテゴリを取得する
    public Category findFirstCategoryByName(String name) {
        return categoryRepository.findFirstByName(name);
    }

	@Transactional
	public void createCategory(CategoryRegisterForm categoryRegisterForm) {
		Category category = new Category();

		category.setName(categoryRegisterForm.getName());

		categoryRepository.save(category);
	}

	@Transactional
	public void updateCategory(CategoryEditForm categoryEditForm, Category category) {
		category.setName(categoryEditForm.getName());

		categoryRepository.save(category);
	}

	@Transactional
	public void deleteCategory(Category category) {
		categoryRepository.delete(category);
	}
}

/*
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
*/
