import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CategoryService, Category } from '../../core/services/category.service';

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent implements OnInit {
  categories: Category[] = [];
  newCategoryName = '';
  editingCategory: Category | null = null;

  constructor(private categoryService: CategoryService, private router: Router) {}

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories() {
    this.categoryService.getAll().subscribe(data => {
      this.categories = data;
    });
  }

  addCategory() {
    const trimmedName = this.newCategoryName.trim();
    if (!trimmedName) {
      alert('Название категории не может быть пустым.');
      return;
    }

    this.categoryService.add(trimmedName).subscribe(() => {
      this.newCategoryName = '';
      this.loadCategories();
    });
  }

  editCategory(category: Category) {
    this.editingCategory = { ...category };
  }

  saveCategory() {
    if (this.editingCategory) {
      this.categoryService.update(this.editingCategory).subscribe(() => {
        this.editingCategory = null;
        this.loadCategories();
      });
    }
  }

  deleteCategory(id: number) {
    const confirmDelete = window.confirm('Вы уверены, что хотите удалить эту категорию?');
    if (!confirmDelete) return;

    this.categoryService.delete(id).subscribe(() => {
      this.loadCategories();
    });
  }

  goToMain() {
    this.router.navigate(['/main']);
  }
}
