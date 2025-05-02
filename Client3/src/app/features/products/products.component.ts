import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { ProductService, Product, Category, Supplier } from '../../core/services/product.service';
import { CategoryService } from '../../core/services/category.service';
import { SupplierService } from '../../core/services/supplier.service';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  suppliers: Supplier[] = [];

  search = '';
  sortBy = '';

  newProduct: Partial<Product> = {
    name: '',
    quantity: 0,
    price: 0,
    category: { id: 0, name: '' },
    supplier: { id: 0, name: '' }
  };

  editingProduct: Product | null = null;

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private supplierService: SupplierService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadProducts();
    this.loadCategories();
    this.loadSuppliers();
  }

  loadProducts() {
    this.productService.getAll(this.search, this.sortBy).subscribe(data => {
      this.products = data;
    });
  }

  loadCategories() {
    this.categoryService.getAll().subscribe(data => {
      this.categories = data;
    });
  }

  loadSuppliers() {
    this.supplierService.getAll().subscribe(data => {
      this.suppliers = data;
    });
  }

  addProduct() {
    const p = this.newProduct;

    if (!p.name?.trim() || p.quantity! <= 0 || p.price! <= 0
      || !p.supplier?.id || !p.category?.id) {
      alert('Все поля товара должны быть заполнены');
      return;
    }

    this.productService.add(p).subscribe({
      next: () => {
        this.newProduct = {
          name: '',
          quantity: 0,
          price: 0,
          category: { id: 0, name: '' },
          supplier: { id: 0, name: '' }
        };
        this.loadProducts();
      },
      error: err => {
        alert(err.error?.message || 'Ошибка при создании товара');
      }
    });
  }

  editProduct(product: Product) {
    this.editingProduct = {
      ...product,
      supplier: { ...product.supplier },
      category: { ...product.category }
    };
  }

  saveProduct() {
    if (!this.editingProduct) return;

    const p = this.editingProduct;

    if (!p.name?.trim() || p.quantity <= 0 || p.price <= 0
      || !p.supplier?.id || !p.category?.id) {
      alert('Все поля товара должны быть заполнены');
      return;
    }

    this.productService.update(p).subscribe({
      next: () => {
        this.editingProduct = null;
        this.loadProducts();
      },
      error: err => {
        alert(err.error?.message || 'Ошибка при обновлении товара');
      }
    });
  }

  deleteProduct(id: number) {
    if (confirm('Удалить товар?')) {
      this.productService.delete(id).subscribe(() => {
        this.loadProducts();
      });
    }
  }

  cancelEdit() {
    this.editingProduct = null;
  }

  goToMain() {
    this.router.navigate(['/main']);
  }

  downloadExcel() {
    window.open('http://localhost:8080/api/products/export/excel', '_blank');
  }

}
