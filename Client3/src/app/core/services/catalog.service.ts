import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Category {
  id: number;
  name: string;
}

export interface Supplier {
  id: number;
  name: string;
}

export interface Product {
  id: number;
  name: string;
  price: number;
  quantity: number;
  category: Category;
  supplier: Supplier;
}

@Injectable({
  providedIn: 'root'
})
export class CatalogService {
  private readonly apiUrl = 'http://localhost:8080/api/products';

  constructor(private http: HttpClient) {}

  getProducts(search: string = ''): Observable<Product[]> {
    let params = new HttpParams();
    if (search) params = params.set('search', search);
    return this.http.get<Product[]>(this.apiUrl, { params });
  }
}
