import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AdminOrderService, Order } from '../../core/services/admin-order.service';

@Component({
  selector: 'app-admin-orders',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-orders.component.html',
  styleUrl: './admin-orders.component.css'
})
export class AdminOrdersComponent implements OnInit {
  orders: Order[] = [];
  filteredOrders: Order[] = [];

  searchLogin = '';
  searchDate = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  constructor(private orderService: AdminOrderService, private router: Router) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders() {
    this.orderService.getAll().subscribe(data => {
      this.orders = data;
      this.applyFilters();
    });
  }

  deleteOrder(id: number) {
    if (confirm('Вы уверены, что хотите удалить заказ?')) {
      this.orderService.delete(id).subscribe(() => {
        this.loadOrders();
      });
    }
  }

  applyFilters() {
    this.filteredOrders = this.orders
      .filter(order =>
        (!this.searchLogin || order.login.toLowerCase().includes(this.searchLogin.toLowerCase())) &&
        (!this.searchDate || order.dateTime.startsWith(this.searchDate))
      )
      .sort((a, b) => {
        const dateA = new Date(a.dateTime).getTime();
        const dateB = new Date(b.dateTime).getTime();
        return this.sortDirection === 'asc' ? dateA - dateB : dateB - dateA;
      });
  }

  toggleSort() {
    this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    this.applyFilters();
  }

  goToMain() {
    this.router.navigate(['/main']);
  }

  downloadExcel() {
    window.open('http://localhost:8080/api/orders/export/excel', '_blank');
  }

}
