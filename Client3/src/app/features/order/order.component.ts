import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { OrderService } from '../../core/services/order.service';

interface Product {
  id: number;
  name: string;
  price: number;
  quantity: number;
}

interface CartItem {
  product: Product;
  quantity: number;
}

interface Address {
  country: string;
  city: string;
  street: string;
}

declare let L: any;

import 'leaflet/dist/images/marker-icon.png';
import 'leaflet/dist/images/marker-icon-2x.png';
import 'leaflet/dist/images/marker-shadow.png';

delete L.Icon.Default.prototype._getIconUrl;

L.Icon.Default.mergeOptions({
  iconUrl: 'assets/marker-icon.png',
  iconRetinaUrl: 'assets/marker-icon-2x.png',
  shadowUrl: 'assets/marker-shadow.png'
});

@Component({
  selector: 'app-order',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './order.component.html',
  styleUrl: './order.component.css'
})
export class OrderComponent implements OnInit {
  cart: CartItem[] = [];
  address: Address = { country: '', city: '', street: '' };
  agreed = false;
  showModal = false;
  agreementText = '';

  map: any;
  marker: any;

  constructor(private orderService: OrderService, private router: Router) {}

  ngOnInit(): void {
    const userId = localStorage.getItem('id');
    const cartData = localStorage.getItem('cart_user_' + userId);
    if (cartData) {
      this.cart = JSON.parse(cartData);
    }

    this.orderService.loadAgreementText().subscribe(data => {
      this.agreementText = data;
    });

    setTimeout(() => this.initMap(), 100);
  }

  initMap(lat = 53.9, lon = 27.5667) {
    this.map = L.map('map').setView([lat, lon], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(this.map);

    this.marker = L.marker([lat, lon]).addTo(this.map);
  }

  updateMapLocation() {
    const fullAddress = `${this.address.country}, ${this.address.city}, ${this.address.street}`;
    this.orderService.geocodeAddress(fullAddress).subscribe(res => {
      if (res.length > 0) {
        const lat = parseFloat(res[0].lat);
        const lon = parseFloat(res[0].lon);
        this.map.setView([lat, lon], 17);
        this.marker.setLatLng([lat, lon]);
      } else {
        alert("Адрес не найден");
      }
    });
  }

  getTotal(): number {
    return this.cart.reduce((sum, item) => sum + item.quantity * item.product.price, 0);
  }

  showAgreement(event: Event) {
    event.preventDefault();
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
  }

  submitOrder() {
    if (!this.address.country || !this.address.city || !this.address.street) {
      alert('Пожалуйста, заполните все поля адреса.');
      return;
    }

    if (!confirm("Вы уверены, что хотите оформить заказ?")) {
      return;
    }

    const userId = localStorage.getItem('id');
    if (!userId) {
      alert('Пользователь не найден');
      return;
    }

    const order = {
      address: this.address,
      user: { id: Number(userId) },
      orderProducts: this.cart.map(item => ({
        product: { id: item.product.id },
        quantity: item.quantity
      }))
    };

    this.orderService.createOrder(order).subscribe({
      next: () => {
        localStorage.removeItem('cart_user_' + userId);
        alert('Заказ успешно оформлен');
        this.router.navigate(['/main']);
      },
      error: err => {
        console.error('Ошибка оформления:', err);
        alert('Ошибка при оформлении: ' + (err.error?.message || err.message || 'Неизвестная ошибка'));
      }
    });
  }

  goBack() {
    this.router.navigate(['/cart']);
  }
}
