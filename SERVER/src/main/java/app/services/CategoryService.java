package app.services;

import app.dao.CategoryRepository;
import app.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        System.out.println("Категорий найдено: " + categories.size());
        return categories;
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(int id, Category updated) {
        Optional<Category> existing = categoryRepository.findById(id);
        if (existing.isPresent()) {
            Category category = existing.get();
            category.setName(updated.getName());
            return categoryRepository.save(category);
        } else {
            throw new RuntimeException("Категория не найдена: " + id);
        }
    }

    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }
}
