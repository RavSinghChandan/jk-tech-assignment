import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private http: HttpClient) {}

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/authenticate`, credentials)
      .pipe(
        tap(response => {
          localStorage.setItem(environment.jwtTokenKey, response.token);
        })
      );
  }

  register(userData: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/register`, userData)
      .pipe(
        tap(response => {
          localStorage.setItem(environment.jwtTokenKey, response.token);
        })
      );
  }

  logout(): void {
    localStorage.removeItem(environment.jwtTokenKey);
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem(environment.jwtTokenKey);
  }

  getToken(): string | null {
    return localStorage.getItem(environment.jwtTokenKey);
  }
} 