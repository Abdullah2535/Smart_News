import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
// 1. IMPORT YOUR MODEL
import { PreferencesResponse } from '../models/category.model';
import { UserNews } from '../models/news.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  private apiUrl = `${environment.apiUrl}`;
  private apiUrlGet = `${this.apiUrl}/preferences`;
  private apiUrlSave = `${this.apiUrl}/news/userP`;

  constructor(private http: HttpClient) {}

  getPreferences(): Observable<PreferencesResponse> {
    // 1. Grab the token from localStorage (Check your login component to see what you named this key!)
    const token = localStorage.getItem('token');

    // 2. Attach it to the Authorization header
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    // 3. Send the request with the headers
    return this.http.get<PreferencesResponse>(this.apiUrlGet, { headers: headers });
  }

  saveUserPreferences(sentimentIds: number[], categoryIds: number[]): Observable<UserNews[]> {
    const token = localStorage.getItem('token');

    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    const payload = {
      sentimentIds: sentimentIds,
      categoryIds: categoryIds,
    };

    return this.http.post<UserNews[]>(this.apiUrlSave, payload, { headers: headers });
  }
}
