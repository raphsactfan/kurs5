import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {
  private readonly apiUrl = 'http://localhost:8080/api/statistics';

  constructor(private http: HttpClient) {}

  getCategoryStats(): Observable<{ [key: string]: number }> {
    return this.http.get<{ [key: string]: number }>(`${this.apiUrl}/categories`);
  }

  getSupplierStats(): Observable<{ [key: string]: number }> {
    return this.http.get<{ [key: string]: number }>(`${this.apiUrl}/suppliers`);
  }

  getTopUsers(): Observable<{ [key: string]: number }> {
    return this.http.get<{ [key: string]: number }>(`${this.apiUrl}/top-users`);
  }

  getDailySales(): Observable<{ [date: string]: number }> {
    return this.http.get<{ [date: string]: number }>(`${this.apiUrl}/daily-sales`);
  }

}
