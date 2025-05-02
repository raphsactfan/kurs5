import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ReportItem {
  productName: string;
  quantity: number;
  total: number;
}

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private readonly apiUrl = 'http://localhost:8080/api/report';

  constructor(private http: HttpClient) {}

  getReportByDate(date: string): Observable<ReportItem[]> {
    return this.http.get<ReportItem[]>(`${this.apiUrl}?date=${date}`);
  }
}
