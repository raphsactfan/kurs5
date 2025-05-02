import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { CatalogService, Product, Category } from '../../core/services/catalog.service';
import { CategoryService } from '../../core/services/category.service';

interface CartItem {
  product: Product;
  quantity: number;
}

@Component({
  selector: 'app-catalog',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './catalog.component.html',
  styleUrl: './catalog.component.css'
})
export class CatalogComponent implements OnInit {
  products: Product[] = [];
  filteredProducts: Product[] = [];
  categories: Category[] = [];

  cart: CartItem[] = [];
  quantities: { [productId: number]: number } = {};

  search = '';
  sortBy = '';
  sortOrder: 'asc' | 'desc' = 'asc';
  selectedCategoryId = 0;

  constructor(
    private catalogService: CatalogService,
    private categoryService: CategoryService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadProducts();
    this.loadCategories();
    this.loadCart();
  }

  loadProducts() {
    this.catalogService.getProducts(this.search).subscribe(data => {
      this.products = data;
      this.applyFilters();
    });
  }

  loadCategories() {
    this.categoryService.getAll().subscribe(data => {
      this.categories = data;
    });
  }

  applyFilters() {
    let result = [...this.products];

    if (this.selectedCategoryId !== 0) {
      result = result.filter(p => p.category?.id === this.selectedCategoryId);
    }

    if (this.sortBy) {
      result.sort((a, b) => {
        const aVal = this.sortBy === 'price' ? a.price : a.name.toLowerCase();
        const bVal = this.sortBy === 'price' ? b.price : b.name.toLowerCase();

        const compare = aVal < bVal ? -1 : aVal > bVal ? 1 : 0;
        return this.sortOrder === 'asc' ? compare : -compare;
      });
    }

    this.filteredProducts = result;
  }

  changeCategory(id: any) {
    this.selectedCategoryId = parseInt(id, 10);
    this.applyFilters();
  }

  updateQuantity(productId: number, quantity: number) {
    this.quantities[productId] = quantity;
  }

  addToCart(product: Product) {
    const qtyToAdd = this.quantities[product.id] || 1;
    if (qtyToAdd < 1) return;

    const existing = this.cart.find(item => item.product.id === product.id);
    const currentQty = existing ? existing.quantity : 0;
    const totalAfterAdd = currentQty + qtyToAdd;

    if (totalAfterAdd > product.quantity) {
      alert(`Нельзя добавить больше ${product.quantity} шт. этого товара`);
      return;
    }

    if (existing) {
      existing.quantity += qtyToAdd;
    } else {
      this.cart.push({ product, quantity: qtyToAdd });
    }

    this.saveCart();
    this.quantities[product.id] = 1;
  }

  saveCart() {
    const key = 'cart_user_' + localStorage.getItem('id');
    localStorage.setItem(key, JSON.stringify(this.cart));
  }

  loadCart() {
    const key = 'cart_user_' + localStorage.getItem('id');
    const data = localStorage.getItem(key);
    if (data) {
      this.cart = JSON.parse(data);
    }
  }

  goToCart() {
    this.router.navigate(['/cart']);
  }

  goToMain() {
    this.router.navigate(['/main']);
  }
}
