import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface OrderItem {
  productName: string;
  price: number;
  quantity: number;
}

export interface Order {
  orderId: number;
  login: string;
  address: string;
  items: OrderItem[];
  totalQuantity: number;
  totalAmount: number;
  dateTime: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserOrderService {
  private readonly apiUrl = 'http://localhost:8080/api/orders';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/all`);
  }

  downloadReceipt(orderId: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/receipt/${orderId}`, {
      responseType: 'blob'
    });
  }
}
