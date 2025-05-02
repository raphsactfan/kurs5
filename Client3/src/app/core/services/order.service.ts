import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

interface Address {
  country: string;
  city: string;
  street: string;
}

interface OrderProduct {
  product: { id: number };
  quantity: number;
}

interface OrderRequest {
  address: Address;
  user: { id: number };
  orderProducts: OrderProduct[];
}

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private readonly apiUrl = 'http://localhost:8080/api/orders';

  constructor(private http: HttpClient) {}

  createOrder(order: OrderRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/create`, order);
  }

  loadAgreementText(): Observable<string> {
    return this.http.get('assets/agreement.txt', { responseType: 'text' });
  }

  geocodeAddress(fullAddress: string): Observable<any[]> {
    const url = `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(fullAddress)}`;
    return this.http.get<any[]>(url);
  }
}
