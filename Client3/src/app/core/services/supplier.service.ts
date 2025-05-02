import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Address {
  country: string;
  city: string;
  street: string;
}

export interface Supplier {
  id: number;
  name: string;
  representative: string;
  phone: string;
  address: Address;
}

@Injectable({
  providedIn: 'root'
})
export class SupplierService {
  private readonly apiUrl = 'http://localhost:8080/api/suppliers';

  constructor(private http: HttpClient) {}

  getAll(search = '', sortBy = ''): Observable<Supplier[]> {
    let params = new HttpParams();
    if (search) params = params.set('search', search);
    if (sortBy) params = params.set('sortBy', sortBy);
    return this.http.get<Supplier[]>(this.apiUrl, { params });
  }

  add(supplier: Partial<Supplier>): Observable<Supplier> {
    return this.http.post<Supplier>(this.apiUrl, supplier);
  }

  update(supplier: Supplier): Observable<any> {
    return this.http.put(`${this.apiUrl}/${supplier.id}`, supplier);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
