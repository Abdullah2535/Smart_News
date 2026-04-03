import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserNews } from '../models/news.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class NewsService {
  private apiUrl = `${environment.apiUrl}`;
  constructor(private http: HttpClient) {}

  getPersonalizedFeed(): Observable<UserNews[]> {
    // Sends the GET request with the user's token so Spring Boot knows who is asking
    return this.http.get<UserNews[]>(`${this.apiUrl}/news/myFeed`);
  }
  // Add this new method to handle custom searches!
searchNews(keyword: string, limit: number): Observable<UserNews[]> {
  const searchUrl = `${this.apiUrl}/news/search?query=${encodeURIComponent(keyword)}&limit=${limit}`;
  // The interceptor will automatically catch this and add the Bearer token!
  return this.http.get<UserNews[]>(searchUrl); 
}
}
