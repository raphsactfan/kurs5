import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Address {
  id: number;
  country: string;
  city: string;
  street: string;
}

@Injectable({
  providedIn: 'root'
})
export class AddressService {
  private readonly apiUrl = 'http://localhost:8080/api/addresses';

  constructor(private http: HttpClient) {}

  getAll(search: string = '', sortBy: string = ''): Observable<Address[]> {
    let params = new HttpParams();
    if (search) params = params.set('search', search);
    if (sortBy) params = params.set('sortBy', sortBy);

    return this.http.get<Address[]>(this.apiUrl, { params });
  }
}
