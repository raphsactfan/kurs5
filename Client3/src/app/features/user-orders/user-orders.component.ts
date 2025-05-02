import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserOrderService, Order } from '../../core/services/user-order.service';

@Component({
  selector: 'app-user-orders',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-orders.component.html',
  styleUrl: './user-orders.component.css'
})
export class UserOrdersComponent implements OnInit {
  allOrders: Order[] = [];
  userOrders: Order[] = [];
  filteredOrders: Order[] = [];
  login = localStorage.getItem('login');
  searchDate: string = '';
  sortDirection: 'asc' | 'desc' = 'desc';

  constructor(private orderService: UserOrderService, private router: Router) {}

  ngOnInit(): void {
    this.orderService.getAll().subscribe(data => {
      this.allOrders = data.filter(o => o.login === this.login);
      this.applyFilters();
    });
  }

  applyFilters() {
    this.filteredOrders = this.allOrders.filter(order => {
      const matchDate = this.searchDate ? order.dateTime.startsWith(this.searchDate) : true;
      return matchDate;
    });

    this.filteredOrders.sort((a, b) => {
      const timeA = new Date(a.dateTime).getTime();
      const timeB = new Date(b.dateTime).getTime();
      return this.sortDirection === 'asc' ? timeA - timeB : timeB - timeA;
    });
  }

  toggleSort() {
    this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    this.applyFilters();
  }

  printReceipt(orderId: number) {
    this.orderService.downloadReceipt(orderId).subscribe({
      next: blob => {
        const file = new Blob([blob], { type: 'application/pdf' });
        const link = document.createElement('a');
        link.href = URL.createObjectURL(file);
        link.download = `order_${orderId}.pdf`;
        link.click();
        URL.revokeObjectURL(link.href);
      },
      error: err => {
        console.error('Ошибка при загрузке чека:', err);
        alert('Не удалось получить чек.');
      }
    });
  }

  goToMain() {
    this.router.navigate(['/main']);
  }
}
