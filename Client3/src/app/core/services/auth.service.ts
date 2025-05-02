import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

export interface LoginRequest {
  login: string;
  password: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly apiUrl = 'http://localhost:8080/api/auth/login';

  constructor(private http: HttpClient) {}

  login(user: any): Observable<any> {
    return this.http.post<any>('http://localhost:8080/api/auth/login', user).pipe(
      tap(response => {
        if (response.status === 'ok') {
          localStorage.setItem('id', response.id);
          localStorage.setItem('login', response.login);
          localStorage.setItem('role', response.role);
        }
      })
    );
  }


  register(data: LoginRequest): Observable<string> {
    return this.http.post('http://localhost:8080/api/auth/register', data, { responseType: 'text' });
  }

}
