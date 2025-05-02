package app.controllers;

import app.entities.Category;
import app.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAll() {
        return categoryService.getAllCategories();
    }

    @PostMapping
    public Category create(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @PutMapping("/{id}")
    public Category update(@PathVariable int id, @RequestBody Category updated) {
        return categoryService.updateCategory(id, updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        categoryService.deleteCategory(id);
    }
}
