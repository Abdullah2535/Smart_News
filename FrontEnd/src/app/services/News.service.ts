import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserNews } from '../models/news.model';

@Injectable({
  providedIn: 'root',
})
export class NewsService {
  private apiUrl = 'http://localhost:8080/news/userP';

  constructor(private http: HttpClient) {}

  getPersonalizedFeed(): Observable<UserNews[]> {
    const token = localStorage.getItem('token');
    let headers = new HttpHeaders();

    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    // Sends the GET request with the user's token so Spring Boot knows who is asking
    return this.http.post<UserNews[]>(this.apiUrl, {}, { headers: headers });
  }
}
