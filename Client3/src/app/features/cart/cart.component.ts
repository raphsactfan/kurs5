import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

interface Category {
  id: number;
  name: string;
}

interface Supplier {
  id: number;
  name: string;
}

interface Product {
  id: number;
  name: string;
  price: number;
  quantity: number;
  category: Category;
  supplier: Supplier;
}

interface CartItem {
  product: Product;
  quantity: number;
}

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent implements OnInit {
  cart: CartItem[] = [];

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart() {
    const key = 'cart_user_' + localStorage.getItem('id');
    const data = localStorage.getItem(key);
    if (data) {
      this.cart = JSON.parse(data);
    }
  }

  saveCart() {
    const key = 'cart_user_' + localStorage.getItem('id');
    localStorage.setItem(key, JSON.stringify(this.cart));
  }

  removeItem(index: number) {
    this.cart.splice(index, 1);
    this.saveCart();
  }

  getTotal(): number {
    return this.cart.reduce((sum, item) => sum + item.product.price * item.quantity, 0);
  }

  goToCatalog() {
    this.router.navigate(['/catalog']);
  }

  goToMain() {
    this.router.navigate(['/main']);
  }

  goToOrder() {
    this.router.navigate(['/order']);
  }
}

