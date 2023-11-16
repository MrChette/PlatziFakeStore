package com.platzifakestore.microservices.products.serviceImpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.platzifakestore.microservices.products.entity.Category;
import com.platzifakestore.microservices.products.model.CategoryModel;
import com.platzifakestore.microservices.products.repository.CategoryRepository;
import com.platzifakestore.microservices.products.service.GenericService;


@Service("categoryServiceImpl")
public class CategoryServiceImpl implements GenericService<Category,CategoryModel,Long>{

	@Autowired
	@Qualifier("categoryRepository")
	private CategoryRepository categoryRepository;

	@Override
	public Category addEntity(CategoryModel model) {
		return categoryRepository.save(transform(model));
	}

	@Override
	public boolean removeEntity(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Category updateEntity(CategoryModel model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category findEntityById(Long id) {
		return categoryRepository.findById(id)
                .orElse(null);
	}

	@Override
	public CategoryModel findModelById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category transform(CategoryModel model) {
		ModelMapper mp = new ModelMapper();
		return mp.map(model, Category.class);
	}

	@Override
	public CategoryModel transformToModel(Category entity) {
		ModelMapper mp = new ModelMapper();
		return mp.map(entity, CategoryModel.class);
	}

	@Override
	public List<CategoryModel> listAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Category> saveAllEntities(List<Category> models) {
		List<Category> savedCategories = categoryRepository.saveAll(models);
		return savedCategories;
	}

	
	
}
