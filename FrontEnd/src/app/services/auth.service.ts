import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { RegisterPayload } from '../models/RegisterPayload';

export interface LoginResponse {
  token: string;
  // Add other expected user data here (e.g., username, role)
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient) {}

  login(credentials: any): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}auth/login`, credentials);
  }
  register(userData: RegisterPayload): Observable<any> {
    return this.http.post(`${this.apiUrl}users`, userData);
  }
}
