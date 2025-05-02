import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface User {
  id: number;
  login: string;
  password: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private readonly apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  updateUser(user: User): Observable<string> {
    return this.http.put(`${this.apiUrl}/${user.id}`, user, { responseType: 'text' });
  }
}
